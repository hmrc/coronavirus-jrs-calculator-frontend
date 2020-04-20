/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import base.{PeriodBuilder, SpecBase}
import models.PayQuestion.Varies
import models.{Amount, FullPeriod, NonFurloughPay, PartialPeriod, PaymentDate, PaymentWithPeriod, Period, PeriodWithPaymentDate}

import scala.collection.immutable

class ReferencePayCalculatorSpec extends SpecBase with PeriodBuilder {

  "calculates reference gross pay for an employee on variable pays" in new ReferencePayCalculator {
    val employeeStartDate = LocalDate.of(2019, 12, 1)
    val furloughStartDate = LocalDate.of(2020, 3, 1)
    val nonFurloughPay = NonFurloughPay(None, Some(Amount(1000.00)))
    val grossPay = Amount(2400.00)
    val priorFurloughPeriod = Period(employeeStartDate, furloughStartDate.minusDays(1))
    val afterFurloughPeriod =
      PeriodWithPaymentDate(FullPeriod(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))), PaymentDate(LocalDate.of(2020, 3, 31)))
    val afterFurloughPeriodTwo =
      PeriodWithPaymentDate(FullPeriod(Period(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30))), PaymentDate(LocalDate.of(2020, 4, 30)))
    val afterFurloughPartial = PeriodWithPaymentDate(
      PartialPeriod(
        Period(LocalDate.of(2020, 5, 1), LocalDate.of(2020, 5, 31)),
        Period(LocalDate.of(2020, 5, 1), LocalDate.of(2020, 5, 15))),
      PaymentDate(LocalDate.of(2020, 5, 31))
    )
    val payPeriods = Seq(afterFurloughPeriod, afterFurloughPeriodTwo, afterFurloughPartial)

    val expected = Seq(
      PaymentWithPeriod(Amount(0.0), Amount(817.47), afterFurloughPeriod, Varies),
      PaymentWithPeriod(Amount(0.0), Amount(791.10), afterFurloughPeriodTwo, Varies),
      PaymentWithPeriod(Amount(1000.0), Amount(395.55), afterFurloughPartial, Varies),
    )

    calculateVariablePay(nonFurloughPay, priorFurloughPeriod, payPeriods, grossPay) mustBe expected
  }

  "calculate daily average gross earning for a given pay period" in new ReferencePayCalculator {
    val employeeStartDate = LocalDate.of(2019, 12, 1)
    val furloughStartDate = LocalDate.of(2020, 3, 1)
    val periodBeforeFurlough = Period(employeeStartDate, furloughStartDate.minusDays(1))

    averageDailyCalculator(periodBeforeFurlough, Amount(2400.0)) mustBe 26.37
  }

  "calculate previous year wage" in new ReferencePayCalculator {
    val dateOne = PaymentDate(LocalDate.of(2020, 3, 2))
    val datetwo = PaymentDate(LocalDate.of(2020, 3, 9))
    val payForDateOne = Amount(700.00)
    val payForDateTwo = Amount(350.00)
    val input: Seq[(PaymentDate, Amount)] = List(dateOne -> payForDateOne, datetwo -> payForDateTwo)

    val expected: Seq[(PaymentDate, Double)] = List(dateOne -> 200.0, datetwo -> 250.0)

    payDateToDailyEarning(input) mustBe expected
  }
}
