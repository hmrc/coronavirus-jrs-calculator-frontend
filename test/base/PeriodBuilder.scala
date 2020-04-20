/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package base

import java.time.LocalDate

import models.{PartialPeriod, Period}

trait PeriodBuilder {

  def period(start: String, end: String) = {
    val s = periodBuilder(start)
    val e = periodBuilder(end)

    Period(LocalDate.of(s(0), s(1), s(2)), LocalDate.of(e(0), e(1), e(2)))
  }

  def partialPeriod(original: (String, String), partial: (String, String)) =
    PartialPeriod(period(original._1, original._2), period(partial._1, partial._2))

  private val periodBuilder: String => Array[Int] =
    date => date.replace(" ", "").split(",").map(_.toInt)
}
