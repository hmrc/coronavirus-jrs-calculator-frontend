/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import cats.data.NonEmptyList
import models.PayQuestion.Varies
import models.{Amount, CylbOperators, CylbPayment, FullPeriod, FullPeriodWithPaymentDate, NonFurloughPay, PartialPeriod, PartialPeriodWithPaymentDate, PaymentFrequency, PaymentWithFullPeriod, PaymentWithPartialPeriod, PaymentWithPeriod, Period, PeriodWithPaymentDate, Periods}
import services.Calculators._
import utils.AmountRounding._

import scala.math.BigDecimal.RoundingMode._

trait ReferencePayCalculator extends PreviousYearPeriod {

  def calculateVariablePay(
    nonFurloughPay: NonFurloughPay,
    priorFurloughPeriod: Period,
    afterFurloughPayPeriod: Seq[PeriodWithPaymentDate],
    amount: Amount,
    cylbs: Seq[CylbPayment],
    frequency: PaymentFrequency): Seq[PaymentWithPeriod] = {
    val avg: Seq[PaymentWithPeriod] =
      afterFurloughPayPeriod.map(period => calculateAveragePay(nonFurloughPay, priorFurloughPeriod, period, amount))

    NonEmptyList
      .fromList(cylbs.toList)
      .fold(avg)(_ => takeGreaterGrossPay(calculateCylb(nonFurloughPay, frequency, cylbs, afterFurloughPayPeriod), avg))
  }

  private def calculateAveragePay(
    nonFurloughPay: NonFurloughPay,
    priorFurloughPeriod: Period,
    afterFurloughPayPeriod: PeriodWithPaymentDate,
    amount: Amount): PaymentWithPeriod =
    afterFurloughPayPeriod match {
      case fp: FullPeriodWithPaymentDate =>
        val daily = periodDaysCount(fp.period.period) * averageDailyCalculator(priorFurloughPeriod, amount)
        PaymentWithFullPeriod(Amount(daily), fp, Varies)
      case pp: PartialPeriodWithPaymentDate =>
        val nfp = determineNonFurloughPay(afterFurloughPayPeriod.period, nonFurloughPay)
        val daily = periodDaysCount(pp.period.partial) * averageDailyCalculator(priorFurloughPeriod, amount)

        PaymentWithPartialPeriod(nfp, Amount(daily), pp, Varies)
    }

  protected def takeGreaterGrossPay(cylb: Seq[PaymentWithPeriod], avg: Seq[PaymentWithPeriod]): Seq[PaymentWithPeriod] =
    cylb.zip(avg) map {
      case (cylbPayment, avgPayment) =>
        if (cylbPayment.furloughPayment.value > avgPayment.furloughPayment.value)
          cylbPayment
        else avgPayment
    }

  protected def calculateCylb(
    nonFurloughPay: NonFurloughPay,
    frequency: PaymentFrequency,
    cylbs: Seq[CylbPayment],
    periods: Seq[PeriodWithPaymentDate]): Seq[PaymentWithPeriod] =
    for {
      period: PeriodWithPaymentDate <- periods
      datesRequired = previousYearPayDate(frequency, period)
      nfp = determineNonFurloughPay(period.period, nonFurloughPay)
    } yield cylbsAmount(frequency, period, datesRequired, nfp, cylbs)

  private def cylbsAmount(
    frequency: PaymentFrequency,
    period: PeriodWithPaymentDate,
    datesRequired: Seq[LocalDate],
    nfp: Amount,
    cylbs: Seq[CylbPayment]): PaymentWithPeriod = {
    val cylbOps = operators(frequency, period.period)
    val furlough: Amount = previousYearFurlough(cylbOps, previousPayments(datesRequired, cylbs).toList)

    period match {
      case fp: FullPeriodWithPaymentDate    => PaymentWithFullPeriod(furlough, fp, Varies)
      case pp: PartialPeriodWithPaymentDate => PaymentWithPartialPeriod(nfp, furlough, pp, Varies)
    }
  }

  private def previousYearFurlough(cylbOps: CylbOperators, previous: List[Amount]): Amount =
    (for {
      oneOrTwo <- NonEmptyList.fromList(previous)
      two      <- oneOrTwo.tail.headOption
    } yield two)
      .fold(totalOneToOne(previous.head, cylbOps))(two => totalTwoToOne(previous.head, two, cylbOps))
      .halfUp

  private def previousPayments(datesRequired: Seq[LocalDate], cylbs: Seq[CylbPayment]): Seq[Amount] =
    for {
      date     <- datesRequired
      previous <- cylbs.find(_.date == date)
    } yield previous.amount

  private def totalTwoToOne(payOne: Amount, payTwo: Amount, operator: CylbOperators): Amount = {
    import operator._
    Amount(((payOne.value / divider) * daysFromPrevious) + ((payTwo.value / divider) * daysFromCurrent))
  }

  private def totalOneToOne(pay: Amount, operator: CylbOperators): Amount =
    operator match {
      case CylbOperators(div, 0, multiplier) => Amount((pay.value / div) * multiplier)
      case CylbOperators(div, multiplier, 0) => Amount((pay.value / div) * multiplier)
    }

  protected def averageDailyCalculator(period: Period, amount: Amount): BigDecimal =
    roundWithMode(amount.value / periodDaysCount(period), HALF_UP)

  private def determineNonFurloughPay(period: Periods, nonFurloughPay: NonFurloughPay): Amount =
    period match {
      case _: FullPeriod => Amount(0.00)
      case pp: PartialPeriod =>
        val pre = if (isFurloughStart(pp)) nonFurloughPay.preAmount else Amount(0.00)
        val post = if (isFurloughEnd(pp)) nonFurloughPay.postAmount else Amount(0.00)
        Amount(pre.value + post.value)
    }
}
