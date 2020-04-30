/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import cats.data.NonEmptyList
import models.PayQuestion.Varies
import models.{Amount, CylbPayment, FullPeriodWithPaymentDate, NonFurloughPay, PartialPeriodWithPaymentDate, PaymentFrequency, PaymentWithFullPeriod, PaymentWithPartialPeriod, PaymentWithPeriod, Period, PeriodWithPaymentDate}
import services.Calculators._
import NonFurloughPay._

trait ReferencePayCalculator extends PreviousYearPeriod with CylbCalculator {

  def calculateVariablePay(
    nonFurloughPay: NonFurloughPay,
    priorFurloughPeriod: Period,
    furloughPayPeriods: Seq[PeriodWithPaymentDate],
    amount: Amount,
    cylbs: Seq[CylbPayment],
    frequency: PaymentFrequency): Seq[PaymentWithPeriod] = {
    val avg: Seq[PaymentWithPeriod] =
      furloughPayPeriods.map(period => calculateAveragePay(nonFurloughPay, priorFurloughPeriod, period, amount))

    NonEmptyList
      .fromList(cylbs.toList)
      .fold(avg)(_ =>
        takeGreaterGrossPay(calculateCylb(nonFurloughPay, frequency, cylbs, furloughPayPeriods, determineNonFurloughPay), avg))
  }

  private def calculateAveragePay(
    nonFurloughPay: NonFurloughPay,
    priorFurloughPeriod: Period,
    afterFurloughPayPeriod: PeriodWithPaymentDate,
    amount: Amount): PaymentWithPeriod =
    afterFurloughPayPeriod match {
      case fp: FullPeriodWithPaymentDate =>
        val daily = periodDaysCount(fp.period.period) * averageDailyCalculator(priorFurloughPeriod, amount).value
        PaymentWithFullPeriod(Amount(daily), fp, Varies)
      case pp: PartialPeriodWithPaymentDate =>
        val nfp = determineNonFurloughPay(afterFurloughPayPeriod.period, nonFurloughPay)
        val daily = periodDaysCount(pp.period.partial) * averageDailyCalculator(priorFurloughPeriod, amount).value

        PaymentWithPartialPeriod(nfp, Amount(daily), pp, Varies)
    }

  protected def takeGreaterGrossPay(cylb: Seq[PaymentWithPeriod], avg: Seq[PaymentWithPeriod]): Seq[PaymentWithPeriod] =
    cylb.zip(avg) map {
      case (cylbPayment, avgPayment) =>
        if (cylbPayment.furloughPayment.value > avgPayment.furloughPayment.value)
          cylbPayment
        else avgPayment
    }

  protected def averageDailyCalculator(period: Period, amount: Amount): Amount =
    Amount(amount.value / periodDaysCount(period)).halfUp
}
