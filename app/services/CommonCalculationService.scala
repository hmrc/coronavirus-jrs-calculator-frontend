/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import models.{Amount, FullPeriod, FullPeriodBreakdown, FullPeriodWithPaymentDate, PaymentDate, PaymentFrequency}
import services.Calculators._
import utils.TaxYearFinder

trait CommonCalculationService extends TaxYearFinder {

  def fullPeriodCalculation(
    frequency: PaymentFrequency,
    furloughPayment: Amount,
    period: FullPeriod,
    paymentDate: PaymentDate,
    rate: Rate): FullPeriodBreakdown = {

    val threshold = thresholdFinder(frequency, paymentDate, rate)
    val roundedFurloughPayment = furloughPayment.down
    val grant = greaterThanAllowance(roundedFurloughPayment, threshold, rate)

    FullPeriodBreakdown(grant, FullPeriodWithPaymentDate(period, paymentDate))
  }

  protected def greaterThanAllowance(amount: Amount, threshold: BigDecimal, rate: Rate): Amount =
    if (amount.value < threshold) Amount(0.0)
    else Amount((amount.value - threshold) * rate.value).halfUp

  protected def thresholdFinder(frequency: PaymentFrequency, paymentDate: PaymentDate, rate: Rate): BigDecimal =
    FrequencyTaxYearThresholdMapping.findThreshold(frequency, taxYearAt(paymentDate), rate)

}
