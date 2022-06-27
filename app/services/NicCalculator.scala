/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package services

import models.Amount._
import models.NicCategory.{Nonpayable, Payable}
import models.Period._
import models._
import services.Calculators._

trait NicCalculator extends FurloughCapCalculator with CommonCalculationService with Calculators {

  def calculateNicGrant(nicCategory: NicCategory,
                        frequency: PaymentFrequency,
                        furloughBreakdowns: Seq[FurloughBreakdown],
                        additionals: Seq[AdditionalPayment],
                        topUps: Seq[TopUpPayment]): NicCalculationResult = {
    val breakdowns = furloughBreakdowns.map {
      case fp: FullPeriodFurloughBreakdown =>
        calculateFullPeriodNic(
          nicCategory,
          frequency,
          fp.grant,
          fp.paymentWithPeriod,
          additionalPayments(additionals, fp.paymentWithPeriod.periodWithPaymentDate),
          topUpPayments(topUps, fp.paymentWithPeriod.periodWithPaymentDate)
        )
      case pp: PartialPeriodFurloughBreakdown =>
        calculatePartialPeriodNic(
          nicCategory,
          frequency,
          pp.grant,
          pp.paymentWithPeriod,
          additionalPayments(additionals, pp.paymentWithPeriod.periodWithPaymentDate),
          topUpPayments(topUps, pp.paymentWithPeriod.periodWithPaymentDate)
        )
    }
    NicCalculationResult(breakdowns.map(_.grant.value).sum, breakdowns)
  }

  def phaseTwoNic(furloughBreakdowns: Seq[PhaseTwoFurloughBreakdown],
                  frequency: PaymentFrequency,
                  nicCategory: NicCategory): PhaseTwoNicCalculationResult = {
    val breakdowns = furloughBreakdowns.map { furloughBreakdown =>
      val phaseTwoPeriod = furloughBreakdown.paymentWithPeriod.phaseTwoPeriod

      val threshold =
        thresholdFinder(frequency, phaseTwoPeriod.periodWithPaymentDate.paymentDate, NiRate())

      val thresholdBasedOnDays = phaseTwoPeriod.periodWithPaymentDate match {
        case _: FullPeriodWithPaymentDate => threshold
        case pp: PartialPeriodWithPaymentDate =>
          threshold.copy(value = partialPeriodDailyCalculation(Amount(threshold.value), pp.period).value)
      }

      val thresholdBasedOnHours = if (phaseTwoPeriod.isPartTime) {
        thresholdBasedOnDays.copy(
          value = partTimeHoursCalculation(Amount(thresholdBasedOnDays.value), phaseTwoPeriod.furloughed, phaseTwoPeriod.usual).value)
      } else {
        thresholdBasedOnDays
      }

      val roundedFurloughGrant = furloughBreakdown.grant.down

      val grant = nicCategory match {
        case Payable    => greaterThanAllowance(roundedFurloughGrant, thresholdBasedOnHours.value, NiRate())
        case Nonpayable => Amount(0.0)
      }

      PhaseTwoNicBreakdown(grant, furloughBreakdown.paymentWithPeriod, thresholdBasedOnHours, nicCategory)
    }

    PhaseTwoNicCalculationResult(breakdowns.map(_.grant.value).sum, breakdowns)
  }

  protected def calculatePartialPeriodNic(nicCategory: NicCategory,
                                          frequency: PaymentFrequency,
                                          furloughGrant: Amount,
                                          payment: PaymentWithPartialPeriod,
                                          additionalPayment: Option[Amount],
                                          topUp: Option[Amount]): PartialPeriodNicBreakdown = {

    import payment.periodWithPaymentDate._

    val total                 = Amount(payment.nonFurloughPay.value + sumValues(furloughGrant, additionalPayment, topUp))
    val calculationParameters = periodCalculation(total, frequency, paymentDate, furloughGrant, topUp)
    import calculationParameters._

    val dailyNi = grossNi.value / period.original.countDays
    val grant = nicCategory match {
      case Payable    => niGrant(Amount(dailyNi * period.partial.countDays), apportion)
      case Nonpayable => Amount(0.00)
    }

    val nicCap = nicGrantCap(furloughGrant, grant)

    PartialPeriodNicBreakdown(nicCap.cappedGrant, topUp.defaulted, additionalPayment.defaulted, payment, threshold, nicCap, nicCategory)
  }

  protected def calculateFullPeriodNic(nicCategory: NicCategory,
                                       frequency: PaymentFrequency,
                                       furloughGrant: Amount,
                                       payment: PaymentWithFullPeriod,
                                       additionalPayment: Option[Amount],
                                       topUp: Option[Amount]): FullPeriodNicBreakdown = {

    import payment.periodWithPaymentDate._

    val total                 = Amount(sumValues(furloughGrant, additionalPayment, topUp))
    val calculationParameters = periodCalculation(total, frequency, paymentDate, furloughGrant, topUp)
    import calculationParameters._

    val grant = nicCategory match {
      case Payable    => niGrant(grossNi, apportion)
      case Nonpayable => Amount(0.00)
    }

    val nicCap = nicGrantCap(furloughGrant, grant)

    FullPeriodNicBreakdown(nicCap.cappedGrant, topUp.defaulted, additionalPayment.defaulted, payment, threshold, nicCap, nicCategory)
  }

  private def periodCalculation(total: Amount,
                                frequency: PaymentFrequency,
                                paymentDate: PaymentDate,
                                furloughGrant: Amount,
                                topUpPayment: Option[Amount]): CalculationParameters = {
    val roundedTotalPay       = total.down
    val threshold: Threshold  = thresholdFinder(frequency, paymentDate, NiRate())
    val grossNi: Amount       = greaterThanAllowance(roundedTotalPay, threshold.value, NiRate())
    val apportion: BigDecimal = furloughGrant.value / (furloughGrant.value + topUpPayment.defaulted.value)

    CalculationParameters(roundedTotalPay, threshold, grossNi, apportion)
  }

  private def nicGrantCap(furloughGrant: Amount, nicGrant: Amount): NicCap = {
    val cap = Amount(furloughGrant.value * NiRate().value).halfUp

    NicCap(furloughGrant, nicGrant, cap)
  }

  private def topUpPayments(topUps: Seq[TopUpPayment], periodWithPaymentDate: PeriodWithPaymentDate): Option[Amount] =
    topUps.find(_.date == periodWithPaymentDate.period.period.end).map(_.amount)

  private def additionalPayments(additionals: Seq[AdditionalPayment], periodWithPaymentDate: PeriodWithPaymentDate): Option[Amount] =
    additionals.find(_.date == periodWithPaymentDate.period.period.end).map(_.amount)

  private def sumValues(furloughGrant: Amount, additional: Option[Amount], topUp: Option[Amount]): BigDecimal =
    furloughGrant.value + additional.defaulted.value + topUp.defaulted.value

  private def niGrant(grossNi: Amount, apportion: BigDecimal) = Amount(grossNi.value * apportion).halfUp

  case class CalculationParameters(total: Amount, threshold: Threshold, grossNi: Amount, apportion: BigDecimal)
}
