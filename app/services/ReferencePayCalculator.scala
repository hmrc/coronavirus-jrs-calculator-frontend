/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import models.PayQuestion.Varies
import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}
import models.{Amount, CylbPayment, FullPeriod, NonFurloughPay, PartialPeriod, PaymentFrequency, PaymentWithPeriod, Period, PeriodWithPaymentDate, Periods, VariableLengthEmployed}
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

  def addCylbToCalculation(
    nonFurloughPay: NonFurloughPay,
    frequency: PaymentFrequency,
    cylbs: Seq[CylbPayment],
    periods: Seq[PeriodWithPaymentDate],
    avg: Seq[PaymentWithPeriod]): Seq[PaymentWithPeriod] = {

    val cylb = calculateCylb(nonFurloughPay, frequency, cylbs, periods)

    if (cylb.isEmpty) avg else greaterGrossPay(cylb, avg)
  }

  protected def cylbCalculationPredicate(variableLength: VariableLengthEmployed, employeeStartDate: LocalDate): Boolean =
    variableLength == VariableLengthEmployed.Yes || employeeStartDate.isBefore(LocalDate.of(2019, 4, 6))

  private def calculateAveragePay(
    nonFurloughPay: NonFurloughPay,
    priorFurloughPeriod: Period,
    afterFurloughPayPeriod: PeriodWithPaymentDate,
    amount: Amount): PaymentWithPeriod = {

    val daily = periodDaysCount(afterFurloughPayPeriod.period.period) * averageDailyCalculator(priorFurloughPeriod, amount)

    val nfp = determineNonFurloughPay(afterFurloughPayPeriod.period, nonFurloughPay)

    PaymentWithPeriod(nfp, Amount(daily), afterFurloughPayPeriod, Varies)
  }

  protected def greaterGrossPay(cylb: Seq[PaymentWithPeriod], avg: Seq[PaymentWithPeriod]): Seq[PaymentWithPeriod] =
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
    frequency match {
      case Monthly => monthlyCylb(nonFurloughPay, cylbs, periods)
      case _       => nonMonthlyCylb(nonFurloughPay, frequency, cylbs, periods)
    }

  private def nonMonthlyCylb(
    nonFurloughPay: NonFurloughPay,
    frequency: PaymentFrequency,
    cylbs: Seq[CylbPayment],
    periods: Seq[PeriodWithPaymentDate]) =
    for {
      period        <- periods.zip(cylbs.sliding(2, 1).toList)
      firstPayment  <- period._2.headOption
      secondPayment <- period._2.tail.headOption
      nfp = determineNonFurloughPay(period._1.period, nonFurloughPay)
    } yield buildPayWithPeriod(frequency, period, nfp, firstPayment, secondPayment)

  private def buildPayWithPeriod(
    frequency: PaymentFrequency,
    period: (PeriodWithPaymentDate, Seq[CylbPayment]),
    nfp: Amount,
    head: CylbPayment,
    taildHead: CylbPayment) = {
    val divisor = cylbDivisor(frequency)
    val multiplier = cylbMultiplier(frequency)
    val amount =
      roundAmountWithMode(totalFromBothWeeks(head, taildHead, divisor, multiplier), RoundingMode.HALF_UP)
    PaymentWithPeriod(nfp, amount, period._1, Varies)
  }

  private def totalFromBothWeeks(head: CylbPayment, taildHead: CylbPayment, divisor: Int, multiplier: Int): Amount =
    Amount(((head.amount.value / divisor) * 2) + ((taildHead.amount.value / divisor) * multiplier))

  private def monthlyCylb(nonFurloughPay: NonFurloughPay, cylbs: Seq[CylbPayment], periods: Seq[PeriodWithPaymentDate]) =
    for {
      period <- periods
      cylb   <- cylbs
      nfp = determineNonFurloughPay(period.period, nonFurloughPay)
    } yield PaymentWithPeriod(nfp, cylb.amount, period, Varies)

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

  private def cylbMultiplier(frequency: PaymentFrequency): Int = frequency match {
    case Weekly      => 5
    case FortNightly => 12
    case FourWeekly  => 26
    case _ => {
      Logger.warn(s"Couldn't find multiplier for $frequency")
      0
    }
  }

  private def determineNonFurloughPay(period: Periods, nonFurloughPay: NonFurloughPay): Amount =
    period match {
      case FullPeriod(_) => Amount(0.00)
      case pp @ PartialPeriod(_, _) =>
        if (isFurloughStart(pp)) nonFurloughPay.preAmount else nonFurloughPay.postAmount
    }
}
