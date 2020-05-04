/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import models.Amount._
import models.Calculation.NicCalculationResult
import models.{Amount, CalculationResult, FullPeriod, FullPeriodBreakdown, FullPeriodWithPaymentDate, PartialPeriod, PartialPeriodBreakdown, PartialPeriodWithPaymentDate, PaymentDate, PaymentFrequency, PeriodBreakdown}
import services.Calculators._

trait NicCalculator extends FurloughCapCalculator with CommonCalculationService {

  def calculateNicGrant(frequency: PaymentFrequency, furloughBreakdown: Seq[PeriodBreakdown]): CalculationResult = {
    val nicBreakdowns = furloughBreakdown.map {
      case FullPeriodBreakdown(grant, periodWithPaymentDate) =>
        calculateFullPeriodNic(frequency, grant, periodWithPaymentDate.period, periodWithPaymentDate.paymentDate)
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
    val roundedTotalPay = Amount(total).down
    val threshold = FrequencyTaxYearThresholdMapping.findThreshold(frequency, taxYearAt(paymentDate), NiRate())
    val grossNi = greaterThanAllowance(roundedTotalPay, threshold, NiRate())
    val dailyNi = grossNi.value / periodDaysCount(period.original)
    val apportion = furloughPayment.value / (furloughPayment.value + topUp.defaulted.value)
    val grant = Amount((dailyNi * periodDaysCount(period.partial)) * apportion).halfUp

    PartialPeriodBreakdown(nonFurloughPay, grant, PartialPeriodWithPaymentDate(period, paymentDate))
  }

  protected def calculateFullPeriodNic(
    frequency: PaymentFrequency,
    furloughPayment: Amount,
    period: FullPeriod,
    paymentDate: PaymentDate,
    additionalPayment: Option[Amount] = None,
    topUp: Option[Amount] = None): FullPeriodBreakdown = { //TODO remove defaulted

    val total = furloughPayment.value + additionalPayment.defaulted.value + topUp.defaulted.value
    val roundedTotalPay = Amount(total).down
    val threshold = thresholdFinder(frequency, paymentDate, NiRate())
    val apportion = furloughPayment.value / (furloughPayment.value + topUp.defaulted.value)
    val grossNi = greaterThanAllowance(roundedTotalPay, threshold, NiRate())

    val grant = Amount(grossNi.value * apportion).halfUp

    FullPeriodBreakdown(grant, FullPeriodWithPaymentDate(period, paymentDate))
  }

}
