/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import models.Calculation.FurloughCalculationResult
import models.{Amount, CalculationResult, FullPeriod, PartialPeriod, PaymentDate, PaymentFrequency, PaymentWithPeriod, Period, PeriodBreakdown, PeriodWithPaymentDate}
import utils.AmountRounding._
import utils.TaxYearFinder

import scala.math.BigDecimal.RoundingMode

trait FurloughCalculator extends FurloughCapCalculator with TaxYearFinder with PeriodHelper {

  def calculateFurloughGrant(
    paymentFrequency: PaymentFrequency,
    regularPayments: Seq[PaymentWithPeriod],
    furloughPeriod: Period,
    taxYearPayDate: LocalDate): CalculationResult = {
    val paymentDateBreakdowns = payPeriodBreakdownFromRegularPayment(paymentFrequency, regularPayments, furloughPeriod, taxYearPayDate)
    CalculationResult(FurloughCalculationResult, paymentDateBreakdowns.map(_.grant.value).sum, paymentDateBreakdowns)
  }

  protected def payPeriodBreakdownFromRegularPayment(
    paymentFrequency: PaymentFrequency,
    paymentsWithPeriod: Seq[PaymentWithPeriod],
    furloughPeriod: Period,
    taxYearPayDate: LocalDate): Seq[PeriodBreakdown] =
    paymentsWithPeriod.map { payment =>
      payment.period match {
        case fp @ FullPeriod(p) if periodContainsNewTaxYear(p) =>
          calculateFullPeriod(paymentFrequency, payment, fp, PaymentDate(taxYearPayDate))
        case pp @ PartialPeriod(o, _) if periodContainsNewTaxYear(o) =>
          calculatePartialPeriod(paymentFrequency, payment, pp, PaymentDate(taxYearPayDate))
        case fp @ FullPeriod(p) =>
          calculateFullPeriod(paymentFrequency, payment, fp, PaymentDate(p.end))
        case pp @ PartialPeriod(o, _) =>
          calculatePartialPeriod(paymentFrequency, payment, pp, PaymentDate(o.end))
      }
    }

  protected def proRatePay(paymentWithPeriod: PaymentWithPeriod): Amount =
    paymentWithPeriod.period match {
      case FullPeriod(_) => paymentWithPeriod.furloughPayment
      case PartialPeriod(o, p) => {
        val proRatedPay =
          roundWithMode((paymentWithPeriod.furloughPayment.value / periodDaysCount(o)) * periodDaysCount(p), RoundingMode.HALF_UP)
        Amount(proRatedPay)
      }
    }

  protected def calculateFullPeriod(
    paymentFrequency: PaymentFrequency,
    payment: PaymentWithPeriod,
    period: FullPeriod,
    paymentDate: PaymentDate): PeriodBreakdown = {
    val payForPeriod = proRatePay(payment)
    val eighty = roundWithMode(payForPeriod.value * 0.8, RoundingMode.HALF_UP)
    val cap = furloughCap(paymentFrequency, period.period)

    val amount: BigDecimal = if (eighty > cap) cap else eighty

    PeriodBreakdown(Amount(0.0), Amount(amount), PeriodWithPaymentDate(period, paymentDate))
  }

  protected def calculatePartialPeriod(
    paymentFrequency: PaymentFrequency,
    payment: PaymentWithPeriod,
    period: PartialPeriod,
    paymentDate: PaymentDate): PeriodBreakdown = {
    val payForPeriod = proRatePay(payment)
    val eighty = roundWithMode(payForPeriod.value * 0.8, RoundingMode.HALF_UP)
    val cap = partialFurloughCap(period.partial)

    val amount: BigDecimal = if (eighty > cap) cap else eighty

    val nonFurloughPay = payment.furloughPayment.value - payForPeriod.value

    PeriodBreakdown(Amount(nonFurloughPay), Amount(amount), PeriodWithPaymentDate(period, paymentDate))
  }

}
