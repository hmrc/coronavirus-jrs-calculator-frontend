/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.temporal.ChronoUnit

import models.RegularPayment

class DailyAverageEarningCalculator {

  def calculate(regularPayment: RegularPayment): Double = {
    val daysCount: Long = ChronoUnit.DAYS.between(regularPayment.payPeriod.start, regularPayment.payPeriod.end) + 1

    BigDecimal(regularPayment.salary.value / daysCount).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  }

  def calculateMonthMax(regularPayment: RegularPayment): Double = {
    //March 64.52
    //April 66.66
    //2000 / 31 => (64.52*16) 1032.32 +
    //2000 / 30 => (66.66*14) 933.24 => mustBe 2K 1965.56


    val daysCount: Long = ChronoUnit.DAYS.between(regularPayment.payPeriod.start, regularPayment.payPeriod.end) + 1

    BigDecimal(regularPayment.salary.value / daysCount).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  }

}
