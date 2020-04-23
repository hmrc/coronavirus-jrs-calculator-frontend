/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import models.{CylbOperators, FullPeriod, PartialPeriod, PaymentFrequency, Periods}
import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}

trait PreviousYearPeriod extends PeriodHelper {

  def previousYearPayDate(paymentFrequency: PaymentFrequency, payDateThisYear: LocalDate): Seq[LocalDate] = paymentFrequency match {
    case Monthly => Seq(payDateThisYear.minusYears(1))
    case _       => calculateDatesForPreviousYear(paymentFrequency, payDateThisYear)
  }

  private val dividers: Map[PaymentFrequency, Int] = Map(
    Weekly      -> 7,
    FortNightly -> 14,
    FourWeekly  -> 28,
  )

  def operatorsEnhanced(paymentFrequency: PaymentFrequency, period: Periods): CylbOperators =
    (paymentFrequency, period) match {
      case (Monthly, FullPeriod(p))             => CylbOperators(periodDaysCount(p), 0, periodDaysCount(p))
      case (Monthly, PartialPeriod(o, p))       => CylbOperators(periodDaysCount(o), 0, periodDaysCount(p))
      case (Weekly, _: FullPeriod)              => CylbOperators(7, 2, 5)
      case (f @ Weekly, pp: PartialPeriod)      => handlePartial(f, pp)
      case (FortNightly, _: FullPeriod)         => CylbOperators(14, 2, 12)
      case (f @ FortNightly, pp: PartialPeriod) => handlePartial(f, pp)
      case (FourWeekly, _: FullPeriod)          => CylbOperators(28, 2, 26)
      case (f @ FourWeekly, pp: PartialPeriod)  => handlePartial(f, pp)
    }

  private val predicate: PartialPeriod => Boolean =
    pp => periodDaysCount(pp.partial) < (periodDaysCount(pp.original) - 1)

  private def handlePartial(frequency: PaymentFrequency, p: PartialPeriod): CylbOperators =
    if (predicate(p))
      CylbOperators(dividers(frequency), 0, periodDaysCount(p.partial))
    else
      CylbOperators(dividers(frequency), 1, periodDaysCount(p.partial) - 1)

  private def calculateDatesForPreviousYear(paymentFrequency: PaymentFrequency, payDateThisYear: LocalDate): Seq[LocalDate] = {
    val dateAfter = payDateThisYear.minusDays(364)

    val dateBefore = paymentFrequency match {
      case Weekly      => dateAfter.minusDays(7)
      case FortNightly => dateAfter.minusDays(14)
      case FourWeekly  => dateAfter.minusDays(28)
    }

    val dateBeforeAdjusted = if (dateBefore.isBefore(LocalDate.of(2019, 3, 1))) dateBefore.plusDays(1) else dateBefore

    Seq(dateBeforeAdjusted, dateAfter)
  }
}
