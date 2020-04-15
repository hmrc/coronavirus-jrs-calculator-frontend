/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import models.Calculation.FurloughCalculationResult
import models.{CalculationResult, FurloughPeriod, PayPeriod, PayPeriodBreakdown, PayPeriodWithPayDay, PaymentDate, PaymentFrequency, RegularPayment, Salary}
import utils.TaxYearFinder

import scala.math.BigDecimal.RoundingMode

trait FurloughCalculator extends FurloughCapCalculator with TaxYearFinder with PayPeriodGenerator {

  def calculateFurlough(
    paymentFrequency: PaymentFrequency,
    regularPayments: Seq[RegularPayment],
    taxYearPayDate: LocalDate): CalculationResult = {
    val paymentDateBreakdowns = payPeriodBreakdownFromRegularPayment(paymentFrequency, regularPayments, taxYearPayDate)
    CalculationResult(FurloughCalculationResult, paymentDateBreakdowns.map(_.amount).sum, paymentDateBreakdowns)
  }

  protected def payPeriodBreakdownFromRegularPayment(
    paymentFrequency: PaymentFrequency,
    regularPayments: Seq[RegularPayment],
    taxYearPayDate: LocalDate): Seq[PayPeriodBreakdown] =
    regularPayments.map { payment =>
      val paymentDate = containsNewTaxYear(payment.payPeriod) match {
        case true  => PaymentDate(taxYearPayDate)
        case false => PaymentDate(payment.payPeriod.end)
      }
      PayPeriodBreakdown(calculate(paymentFrequency, payment), PayPeriodWithPayDay(payment.payPeriod, paymentDate))
    }

  protected def regularPaymentForFurloughPeriod(furloughPeriod: FurloughPeriod, payments: RegularPayment): RegularPayment = {
    val furloughPayPeriod: PayPeriod = payPeriodFromFurloughPeriod(furloughPeriod, payments.payPeriod)
    val daysInPayPeriod = periodDaysCount(payments.payPeriod)
    val daysInFurloughDayPeriod = periodDaysCount(furloughPayPeriod)
    val daily = helper(payments.salary.amount / daysInPayPeriod, RoundingMode.HALF_UP)
    val newSalary = if (daysInPayPeriod != daysInFurloughDayPeriod) daily * daysInFurloughDayPeriod else payments.salary.amount

    RegularPayment(Salary(newSalary), PayPeriod(furloughPayPeriod.start, furloughPayPeriod.end))
  }

  protected def calculateNewPeriod(furloughPeriod: FurloughPeriod, payments: List[RegularPayment]): Double =
    payments.map(p => regularPaymentForFurloughPeriod(furloughPeriod, p)).map(_.salary.amount).sum

  //To be tested
  protected def payPeriodFromFurloughPeriod(furloughPeriod: FurloughPeriod, payPeriod: PayPeriod) = {
    val start =
      if (furloughPeriod.start.isAfter(payPeriod.start) && furloughPeriod.start.isBefore(payPeriod.end))
        furloughPeriod.start
      else payPeriod.start

    val end =
      if (furloughPeriod.end.isAfter(payPeriod.start) && furloughPeriod.end.isBefore(payPeriod.end))
        furloughPeriod.end
      else payPeriod.end

    PayPeriod(start, end)
  }

  protected def calculate(paymentFrequency: PaymentFrequency, regularPayment: RegularPayment): Double = {
    val eighty = helper(regularPayment.salary.amount * 0.8, RoundingMode.HALF_UP)
    val cap = furloughCap(paymentFrequency, regularPayment.payPeriod)

    if (eighty > cap) cap else eighty
  }

}
