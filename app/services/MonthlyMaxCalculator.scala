/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.temporal.ChronoUnit

import models.{PayPeriod, RegularPayment}

class MonthlyMaxCalculator {

  def calculate(payPeriod: PayPeriod): Double = {
    val daysMonthOne: Long = ChronoUnit.DAYS.between(payPeriod.start, payPeriod.start.withDayOfMonth(payPeriod.start.getMonth.maxLength())) + 1
    val daysMonthTwo = ChronoUnit.DAYS.between(payPeriod.end.withDayOfMonth(1), payPeriod.end) + 1

    val result = (daysMonthOne * 80.65) + (daysMonthTwo * 83.34)

    BigDecimal(result).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  }

}
