/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import models.NonFurloughPay.determineNonFurloughPay
import models.{Amount, FullPeriodWithPaymentDate, NonFurloughPay, PartialPeriodWithPaymentDate, PaymentWithFullPeriod, PaymentWithPartialPeriod, PaymentWithPeriod, Period, PeriodWithPaymentDate}
import services.Calculators._

trait AveragePayCalculator {

  def calculateAveragePay(
    nonFurloughPay: NonFurloughPay,
    priorFurloughPeriod: Period,
    periods: Seq[PeriodWithPaymentDate],
    grossPay: Amount): Seq[PaymentWithPeriod] =
    periods map {
      case fp: FullPeriodWithPaymentDate =>
        val daily = fp.period.period.countDays * averageDailyCalculator(priorFurloughPeriod, grossPay).value
        PaymentWithFullPeriod(Amount(daily), fp)
      case pp: PartialPeriodWithPaymentDate =>
        val nfp = determineNonFurloughPay(pp.period, nonFurloughPay)
        val daily = pp.period.partial.countDays * averageDailyCalculator(priorFurloughPeriod, grossPay).value

        PaymentWithPartialPeriod(nfp, Amount(daily), pp)
    }

  protected def averageDailyCalculator(period: Period, amount: Amount): Amount =
    Amount(amount.value / period.countDays).halfUp
}
