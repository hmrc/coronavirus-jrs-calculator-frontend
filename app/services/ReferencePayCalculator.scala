/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import models.PayQuestion.Varies
import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}
import models.{Amount, CylbPayment, FullPeriod, NonFurloughPay, PartialPeriod, PaymentDate, PaymentFrequency, PaymentWithPeriod, Period, PeriodWithPaymentDate, Periods}
import play.api.Logger
import utils.AmountRounding._

import scala.math.BigDecimal.RoundingMode
import scala.math.BigDecimal.RoundingMode._

trait ReferencePayCalculator extends PeriodHelper {

  def calculateVariablePay(
    nonFurloughPay: NonFurloughPay,
    priorFurloughPeriod: Period,
    afterFurloughPayPeriod: Seq[PeriodWithPaymentDate],
    amount: Amount): Seq[PaymentWithPeriod] =
    afterFurloughPayPeriod.map(period => calculateAveragePay(nonFurloughPay, priorFurloughPeriod, period, amount))

  private def calculateAveragePay(
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

  protected def calculateCylb(
    nonFurloughPay: NonFurloughPay,
    frequency: PaymentFrequency,
    cylbs: Seq[CylbPayment],
    periods: Seq[PeriodWithPaymentDate]): Seq[PaymentWithPeriod] =
    frequency match {
      case Monthly =>
        for {
          period <- periods
          cylb   <- cylbs
          nfp = determineNonFurloughPay(period.period, nonFurloughPay)
        } yield PaymentWithPeriod(nfp, cylb.amount, period, Varies)
      case _ =>
        for {
          period <- periods.zip(cylbs.sliding(2, 1).toList)
          nfp = determineNonFurloughPay(period._1.period, nonFurloughPay)
        } yield {
          val divisor = cylbDivisor(frequency)
          val amount =
            Amount(
              roundWithMode(
                ((period._2.head.amount.value / divisor) * 2) + ((period._2.tail.head.amount.value / divisor) * 5),
                RoundingMode.HALF_UP))
          PaymentWithPeriod(nfp, amount, period._1, Varies)
        }
    }

  protected def averageDailyCalculator(period: Period, amount: Amount): BigDecimal =
    roundWithMode(amount.value / periodDaysCount(period), HALF_UP)

  private def cylbDivisor(frequency: PaymentFrequency): Int = frequency match {
    case Weekly      => 7
    case FortNightly => 14
    case FourWeekly  => 28
    case _ => {
      Logger.warn(s"Couldn't find divisor for $frequency")
      0
    }
  }

  private def determineNonFurloughPay(period: Periods, nonFurloughPay: NonFurloughPay): Amount =
    period match {
      case FullPeriod(_) => Amount(0.00)
      case pp @ PartialPeriod(_, _) =>
        if (isFurloughStart(pp)) nonFurloughPay.pre.fold(Amount(0.0))(v => v) else nonFurloughPay.post.fold(Amount(0.0))(v => v)
    }
}
