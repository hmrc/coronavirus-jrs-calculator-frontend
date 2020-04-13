/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import base.SpecBase
import models.{PayPeriod, PaymentDate, RegularPayment}

class DailyAverageEarningCalculatorSpec extends SpecBase {

  "Calculates daily average earning for a given calendar month and salary" in new DailyAverageEarningCalculator {
    val payPeriod = PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31), PaymentDate(LocalDate.of(2020, 3, 31)))
    val payment = RegularPayment(Salary(2000), payPeriod)

    calculate(payment) mustBe 64.52
  }

//  "Calculates daily average earning for a given pay period within two different calendar months" in new DailyAverageEarningCalculator {
//    val payPeriod = PayPeriod(LocalDate.of(2020, 3, 15), LocalDate.of(2020, 4, 14), PaymentDate(LocalDate.of(2020, 3, 31)))
//    val payment = RegularPayment(Salary(2000), payPeriod)
//
//    //March 64.52
//    //April 66.66
//    //2000 / 31 => (64.52*16) 1032.32 +
//    //2000 / 30 => (66.66*14) 933.24 => mustBe 2K 1965.56
//
//    calculateMonthMax(payment) mustBe 1965.56
//  }
}
