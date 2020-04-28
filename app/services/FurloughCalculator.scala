/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import models.Calculation.FurloughCalculationResult
import models.PayQuestion.{Regularly, Varies}
import models.{Amount, CalculationResult, FullPeriod, PartialPeriod, PaymentDate, PaymentFrequency, PaymentWithPeriod, PeriodBreakdown, PeriodWithPaymentDate}
import utils.TaxYearFinder

import scala.math.BigDecimal.RoundingMode

trait FurloughCalculator extends FurloughCapCalculator with TaxYearFinder with PeriodHelper with Calculators {

  def calculateFurloughGrant(paymentFrequency: PaymentFrequency, payments: Seq[PaymentWithPeriod]): CalculationResult = {
    val paymentDateBreakdowns = payPeriodBreakdownFromRegularPayment(paymentFrequency, payments)
    CalculationResult(FurloughCalculationResult, paymentDateBreakdowns.map(_.grant.value).sum, paymentDateBreakdowns)
  }

  protected def payPeriodBreakdownFromRegularPayment(
    paymentFrequency: PaymentFrequency,
    paymentsWithPeriod: Seq[PaymentWithPeriod]): Seq[PeriodBreakdown] =
    paymentsWithPeriod.map { payment =>
      payment.period.period match {
        case fp: FullPeriod =>
          calculateFullPeriod(paymentFrequency, payment, fp, payment.period.paymentDate)
        case pp: PartialPeriod =>
          calculatePartialPeriod(payment, pp, payment.period.paymentDate)
      }
    }

  protected def proRatePay(paymentWithPeriod: PaymentWithPeriod): Amount =
    (paymentWithPeriod.period.period, paymentWithPeriod.payQuestion) match {
      case (PartialPeriod(o, p), Regularly) =>
        Amount((paymentWithPeriod.furloughPayment.value / periodDaysCount(o)) * periodDaysCount(p)).halfUp
      case _ => paymentWithPeriod.furloughPayment
    }

  protected def calculateFullPeriod(
    paymentFrequency: PaymentFrequency,
    payment: PaymentWithPeriod,
    period: FullPeriod,
    paymentDate: PaymentDate): PeriodBreakdown = {
    val payForPeriod = proRatePay(payment)
    val eighty = eightyPercent(payForPeriod).halfUp.value
    val cap = furloughCap(paymentFrequency, period.period)

    val amount: BigDecimal = if (eighty > cap) cap else eighty

    PeriodBreakdown(Amount(0.0), Amount(amount), PeriodWithPaymentDate(period, paymentDate))
  }

  protected def calculatePartialPeriod(payment: PaymentWithPeriod, period: PartialPeriod, paymentDate: PaymentDate): PeriodBreakdown = {
    val payForPeriod = proRatePay(payment)
    val eighty = eightyPercent(payForPeriod).halfUp.value
    val cap = partialFurloughCap(period.partial)

    val amount: BigDecimal = if (eighty > cap) cap else eighty

    val fullPeriodDays = periodDaysCount(period.original)
    val furloughDays = periodDaysCount(period.partial)
    val preFurloughDays = fullPeriodDays - furloughDays
    val nonFurloughPay = payment.payQuestion match {
      case Regularly => Amount((payment.furloughPayment.value / fullPeriodDays) * preFurloughDays).halfUp
      case Varies    => payment.nonFurloughPay
    }

    PeriodBreakdown(nonFurloughPay, Amount(amount), PeriodWithPaymentDate(period, paymentDate))
  }

}
