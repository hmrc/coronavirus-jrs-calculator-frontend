/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate
import java.time.temporal.ChronoUnit

import models.PayPeriod

trait PayPeriodGenerator {

  def generatePayPeriods(endDates: Seq[LocalDate]): Seq[PayPeriod] = {
    def generate(acc: Seq[PayPeriod], list: Seq[LocalDate]): Seq[PayPeriod] = list match {
      case Nil      => acc
      case h :: Nil => acc
      case h :: t   => generate(acc ++ Seq(PayPeriod(h.plusDays(1), t.head)), t)
    }

    if (endDates.length == 1) {
      endDates.map(date => PayPeriod(date, date))
    } else {
      generate(Seq(), sortedEndDates(endDates))
    }
  }

  def periodDaysCount(payPeriod: PayPeriod): Int = {
    val count = if (payPeriod.start.getDayOfMonth != 1) {
      ChronoUnit.DAYS.between(payPeriod.start, payPeriod.end)
    } else {
      ChronoUnit.DAYS.between(payPeriod.start, payPeriod.end) + 1
    }

    count.toInt
  }

  def periodContainsNewTaxYear(period: PayPeriod): Boolean =
    dateExistsInPayPeriod(LocalDate.of(period.start.getYear, 4, 6), period)

  def dateExistsInPayPeriod(date: LocalDate, period: PayPeriod): Boolean =
    (date.isAfter(period.start) || date.isEqual(period.start)) &&
      (date.isBefore(period.end) || date.isEqual(period.end))

  protected def sortedEndDates(in: Seq[LocalDate]): Seq[LocalDate] = in.sortWith((x, y) => x.isBefore(y))

}
