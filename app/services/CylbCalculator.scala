/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import models.NonFurloughPay.determineNonFurloughPay
import models.{Amount, CylbOperators, CylbPayment, FullPeriodWithPaymentDate, NonFurloughPay, PartialPeriodWithPaymentDate, PaymentFrequency, PaymentWithFullPeriod, PaymentWithPartialPeriod, PaymentWithPeriod, PeriodWithPaymentDate}

trait CylbCalculator extends PreviousYearPeriod {

  def calculateCylb(
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
    val furlough: Amount = previousYearFurlough(datesRequired, cylbs, cylbOps)

    period match {
      case fp: FullPeriodWithPaymentDate    => PaymentWithFullPeriod(furlough, fp)
      case pp: PartialPeriodWithPaymentDate => PaymentWithPartialPeriod(nfp, furlough, pp)
    }
  }

  private def previousYearFurlough(datesRequired: Seq[LocalDate], cylbs: Seq[CylbPayment], ops: CylbOperators): Amount = {
    val amounts: Seq[Amount] = datesRequired.flatMap(date => cylbs.find(_.date == date)).map(_.amount)

    amounts match {
      case x :: Nil => previousOrCurrent(x, ops)
      case x :: y :: Nil =>
        Amount(((x.value / ops.fullPeriodLength) * ops.daysFromPrevious) + ((y.value / ops.fullPeriodLength) * ops.daysFromCurrent))
    }
  }

  private def previousOrCurrent(amount: Amount, ops: CylbOperators) =
    if (ops.daysFromCurrent == 0)
      Amount((amount.value / ops.fullPeriodLength) * ops.daysFromPrevious)
    else Amount((amount.value / ops.fullPeriodLength) * ops.daysFromCurrent)

}
