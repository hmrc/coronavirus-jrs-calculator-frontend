/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import models.Calculation.FurloughCalculationResult
import models.{CalculationResult, FurloughPeriod, PayPeriodBreakdown, PayPeriodWithPayDay, PaymentDate, PaymentFrequency, Period, RegularPayment, Salary}
import utils.TaxYearFinder
import utils.AmountRounding._

import scala.math.BigDecimal.RoundingMode

trait FurloughCalculator extends FurloughCapCalculator with TaxYearFinder with PayPeriodGenerator {

  def calculateFurlough(
    paymentFrequency: PaymentFrequency,
    regularPayments: Seq[RegularPayment],
    furloughPeriod: FurloughPeriod,
    taxYearPayDate: LocalDate): CalculationResult = {
    val paymentDateBreakdowns = payPeriodBreakdownFromRegularPayment(paymentFrequency, regularPayments, furloughPeriod, taxYearPayDate)
    CalculationResult(FurloughCalculationResult, paymentDateBreakdowns.map(_.amount).sum, paymentDateBreakdowns)
  }

  protected def payPeriodBreakdownFromRegularPayment(
    paymentFrequency: PaymentFrequency,
    regularPayments: Seq[RegularPayment],
    furloughPeriod: FurloughPeriod,
    taxYearPayDate: LocalDate): Seq[PayPeriodBreakdown] =
    regularPayments.map { payment =>
      val paymentDate = periodContainsNewTaxYear(payment.payPeriod) match {
        case true  => PaymentDate(taxYearPayDate)
        case false => PaymentDate(payment.payPeriod.end)
      }

      val furloughPayPeriod = payPeriodFromFurloughPeriod(furloughPeriod, payment.payPeriod)

      val isPartialPeriod = periodDaysCount(furloughPayPeriod) != periodDaysCount(payment.payPeriod)

      if (isPartialPeriod) {
        val partialPayment = regularPaymentForFurloughPeriod(furloughPeriod, payment)
        PayPeriodBreakdown(calculatePartialPeriod(partialPayment), PayPeriodWithPayDay(partialPayment.payPeriod, paymentDate))
      } else {
        PayPeriodBreakdown(calculateFullPeriod(paymentFrequency, payment), PayPeriodWithPayDay(payment.payPeriod, paymentDate))
      }
    }

  protected def regularPaymentForFurloughPeriod(furloughPeriod: FurloughPeriod, payment: RegularPayment): RegularPayment = {
    val furloughPayPeriod: Period = payPeriodFromFurloughPeriod(furloughPeriod, payment.payPeriod)
    val daysInPayPeriod = periodDaysCount(payment.payPeriod)
    val daysInFurloughDayPeriod = periodDaysCount(furloughPayPeriod)
    val daily = roundWithMode(payment.salary.amount / daysInPayPeriod, RoundingMode.HALF_UP)
    val newSalary = if (daysInPayPeriod != daysInFurloughDayPeriod) {
      roundWithMode(daily * daysInFurloughDayPeriod, RoundingMode.HALF_UP)
    } else {
      payment.salary.amount
    }

    RegularPayment(Salary(newSalary), Period(furloughPayPeriod.start, furloughPayPeriod.end))
  }

  protected def payPeriodFromFurloughPeriod(furloughPeriod: FurloughPeriod, payPeriod: Period) = {
    val start =
      if (furloughPeriod.start.isAfter(payPeriod.start) && furloughPeriod.start.isBefore(payPeriod.end)) {
        furloughPeriod.start
      } else {
        payPeriod.start
      }

    val end =
      if (furloughPeriod.end.isAfter(payPeriod.start) && furloughPeriod.end.isBefore(payPeriod.end)) {
        furloughPeriod.end
      } else {
        payPeriod.end
      }

    Period(start, end)
  }

  protected def calculateFullPeriod(paymentFrequency: PaymentFrequency, regularPayment: RegularPayment): BigDecimal = {
    val eighty = roundWithMode(regularPayment.salary.amount * 0.8, RoundingMode.HALF_UP)
    val cap = furloughCap(paymentFrequency, regularPayment.payPeriod)

    if (eighty > cap) cap else eighty
  }

  protected def calculatePartialPeriod(regularPayment: RegularPayment): BigDecimal = {
    val eighty = roundWithMode(regularPayment.salary.amount * 0.8, RoundingMode.HALF_UP)
    val cap = partialFurloughCap(regularPayment.payPeriod)

    if (eighty > cap) cap else eighty
  }

}
