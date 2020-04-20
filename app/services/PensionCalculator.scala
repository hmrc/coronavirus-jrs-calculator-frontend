/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import models.Calculation.PensionCalculationResult
import models.{Amount, CalculationResult, FullPeriod, PartialPeriod, PaymentDate, PaymentFrequency, PeriodBreakdown, PeriodWithPaymentDate}
import utils.AmountRounding._

import scala.math.BigDecimal.RoundingMode

trait PensionCalculator extends FurloughCapCalculator with CommonCalculationService {

  def calculatePensionGrant(frequency: PaymentFrequency, furloughBreakdown: Seq[PeriodBreakdown]): CalculationResult = {
    val pensionBreakdowns = furloughBreakdown.map { breakdown =>
      breakdown.periodWithPaymentDate.period match {
        case fp @ FullPeriod(_) =>
          calculateFullPeriodPension(frequency, breakdown.nonFurloughPay, breakdown.grant, fp, breakdown.periodWithPaymentDate.paymentDate)
        case pp @ PartialPeriod(_, _) =>
          calculatePartialPeriodPension(
            frequency,
            breakdown.nonFurloughPay,
            breakdown.grant,
            pp,
            breakdown.periodWithPaymentDate.paymentDate)
      }
    }

    CalculationResult(PensionCalculationResult, pensionBreakdowns.map(_.grant.value).sum, pensionBreakdowns)
  }

  protected def calculateFullPeriodPension(
    frequency: PaymentFrequency,
    grossPay: Amount,
    furloughPayment: Amount,
    period: FullPeriod,
    paymentDate: PaymentDate): PeriodBreakdown =
    fullPeriodCalculation(frequency, grossPay, furloughPayment, period, paymentDate, PensionRate())

  private def calculatePartialPeriodPension(
    frequency: PaymentFrequency,
    grossPay: Amount,
    furloughPayment: Amount,
    period: PartialPeriod,
    paymentDate: PaymentDate): PeriodBreakdown = {
    val fullPeriodDays = periodDaysCount(period.original)
    val furloughDays = periodDaysCount(period.partial)
    val threshold = FrequencyTaxYearThresholdMapping.findThreshold(frequency, taxYearAt(paymentDate), PensionRate())

    val allowance = roundWithMode((threshold / fullPeriodDays) * furloughDays, RoundingMode.HALF_UP)
    val roundedFurloughPayment = furloughPayment.value.setScale(0, RoundingMode.DOWN)
    val grant = greaterThanAllowance(roundedFurloughPayment, allowance, PensionRate())

    PeriodBreakdown(grossPay, Amount(grant), PeriodWithPaymentDate(period, paymentDate))
  }

}
