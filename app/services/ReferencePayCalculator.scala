/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import models.PayQuestion.Varies
import models.PaymentFrequency.Monthly
import models.{Amount, CylbPayment, FullPeriod, NonFurloughPay, PartialPeriod, PaymentDate, PaymentFrequency, PaymentWithPeriod, Period, PeriodWithPaymentDate, Periods}
import utils.AmountRounding._

import scala.math.BigDecimal.RoundingMode._

trait ReferencePayCalculator extends PeriodHelper {

  def calculateVariablePay(
    nonFurloughPay: NonFurloughPay,
    priorFurloughPeriod: Period,
    afterFurloughPayPeriod: Seq[PeriodWithPaymentDate],
    amount: Amount): Seq[PaymentWithPeriod] =
    afterFurloughPayPeriod.map(period => calculateReferencePay(nonFurloughPay, priorFurloughPeriod, period, amount))

  private def calculateReferencePay(
    nonFurloughPay: NonFurloughPay,
    priorFurloughPeriod: Period,
    afterFurloughPayPeriod: PeriodWithPaymentDate,
    amount: Amount): PaymentWithPeriod = {

    val period = afterFurloughPayPeriod.period match {
      case FullPeriod(p)       => p
      case PartialPeriod(_, p) => p
    }

    val daily = periodDaysCount(period) * averageDailyCalculator(priorFurloughPeriod, amount)

    val nfp = determineNonFurloughPay(afterFurloughPayPeriod.period, nonFurloughPay)

    PaymentWithPeriod(nfp, Amount(daily), afterFurloughPayPeriod, Varies)
  }

  protected def payDateToDailyEarning(datesWithAmount: Seq[(PaymentDate, Amount)]): Seq[(PaymentDate, BigDecimal)] = {
    val indexed: Seq[((PaymentDate, Amount), Int)] = datesWithAmount.zipWithIndex

    val res: Seq[(PaymentDate, BigDecimal)] = indexed.map {
      case (k, v) if v % 2 == 0 => k._1 -> (k._2.value / 7) * 2
      case (k, _) => k._1 -> (k._2.value / 7) * 5
    }
    res
  }

  protected def calculateCylb(nonFurloughPay: NonFurloughPay,
                              frequency: PaymentFrequency,
                              cylbs: Seq[CylbPayment],
                              periods: Seq[PeriodWithPaymentDate]): Seq[PaymentWithPeriod] = {
    frequency match {
      case Monthly => for {
        period <- periods
        cylb   <- cylbs
        nfp     = determineNonFurloughPay(period.period, nonFurloughPay)
      } yield PaymentWithPeriod(nfp, cylb.amount, period, Varies)
      case _ => for {
        period <- periods
        cylb   <- cylbs.sliding(2, 1)
        nfp     = determineNonFurloughPay(period.period, nonFurloughPay)
      } yield {
        val amount = Amount(cylb.map(_.amount.value).sum)
        PaymentWithPeriod(nfp, amount, period, Varies)
      }
    }
  }

  protected def averageDailyCalculator(period: Period, amount: Amount): BigDecimal =
    roundWithMode(amount.value / periodDaysCount(period), HALF_UP)

  private def determineNonFurloughPay(period: Periods, nonFurloughPay: NonFurloughPay): Amount =
    period match {
      case FullPeriod(_)            => Amount(0.00)
      case pp @ PartialPeriod(_, _) =>
      if (isFurloughStart(pp)) nonFurloughPay.pre.fold(Amount(0.0))(v => v) else nonFurloughPay.post.fold(Amount(0.0))(v => v)
  }
}
