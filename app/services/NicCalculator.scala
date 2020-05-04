/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import models.Calculation.NicCalculationResult
import models.{Amount, CalculationResult, FullPeriodBreakdown, PartialPeriod, PartialPeriodBreakdown, PartialPeriodWithPaymentDate, PaymentDate, PaymentFrequency, PeriodBreakdown}
import utils.AmountRounding.roundWithMode
import models.Amount._

import scala.math.BigDecimal.RoundingMode

trait NicCalculator extends FurloughCapCalculator with CommonCalculationService {

  def calculateNicGrant(frequency: PaymentFrequency, furloughBreakdown: Seq[PeriodBreakdown]): CalculationResult = {
    val nicBreakdowns = furloughBreakdown.map {
      case FullPeriodBreakdown(grant, periodWithPaymentDate) =>
        fullPeriodCalculation(frequency, grant, periodWithPaymentDate.period, periodWithPaymentDate.paymentDate, NiRate())
      case PartialPeriodBreakdown(nonFurloughPay, grant, periodWithPaymentDate) =>
        calculatePartialPeriodNic(frequency, nonFurloughPay, grant, periodWithPaymentDate.period, periodWithPaymentDate.paymentDate)
    }
    CalculationResult(NicCalculationResult, nicBreakdowns.map(_.grant.value).sum, nicBreakdowns)
  }

  protected def calculatePartialPeriodNic(
    frequency: PaymentFrequency,
    nonFurloughPay: Amount,
    furloughPayment: Amount,
    period: PartialPeriod,
    paymentDate: PaymentDate,
    additionalPayment: Option[Amount] = None,
    topUp: Option[Amount] = None): PartialPeriodBreakdown = { //TODO remove defaulted None

    val total = nonFurloughPay.value + furloughPayment.value + additionalPayment.defaulted.value + topUp.defaulted.value
    val roundedTotalPay = total.setScale(0, RoundingMode.DOWN)
    val threshold = FrequencyTaxYearThresholdMapping.findThreshold(frequency, taxYearAt(paymentDate), NiRate())
    val grossNi = greaterThanAllowance(roundedTotalPay, threshold, NiRate())
    val dailyNi = grossNi / periodDaysCount(period.original)
    val apportion = furloughPayment.value / (furloughPayment.value + topUp.defaulted.value)
    val grant = roundWithMode((dailyNi * periodDaysCount(period.partial)) * apportion, RoundingMode.HALF_UP)

    PartialPeriodBreakdown(nonFurloughPay, Amount(grant), PartialPeriodWithPaymentDate(period, paymentDate))
  }

}
