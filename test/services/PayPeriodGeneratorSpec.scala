/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import base.SpecBase
import models.PayPeriod

class PayPeriodGeneratorSpec extends SpecBase {

  "For a given list of pay period end dates, should return a List[LocalDate] in ascending order" in new PayPeriodGenerator {
    val unsortedEndDates: List[LocalDate] = List(LocalDate.of(2020, 3, 20), LocalDate.of(2020, 3, 18), LocalDate.of(2020, 3, 19))
    sortedEndDates(unsortedEndDates) mustBe List(LocalDate.of(2020, 3, 18), LocalDate.of(2020, 3, 19), LocalDate.of(2020, 3, 20))
  }

  "Returns a Pay Period with the same start and end date if only one date is supplied" in new PayPeriodGenerator {
    //This is not a valid scenario, just testing for safety
    val endDates: List[LocalDate] = List(LocalDate.of(2020, 2, 20))

    val expected: List[PayPeriod] = List(PayPeriod(LocalDate.of(2020, 2, 20), LocalDate.of(2020, 2, 20)))

    generatePayPeriods(endDates) mustBe expected
  }

  "Returns a sorted List[PayPeriod] for a given List[LocalDate] that represents PayPeriod.end LocalDates" in new PayPeriodGenerator {
    val endDates: List[LocalDate] = List(LocalDate.of(2020, 4, 20), LocalDate.of(2020, 3, 20), LocalDate.of(2020, 2, 20))
    val endDatesTwo: List[LocalDate] = List(LocalDate.of(2020, 3, 20), LocalDate.of(2020, 2, 20))

    val expected: List[PayPeriod] =
      List(PayPeriod(LocalDate.of(2020, 2, 21), LocalDate.of(2020, 3, 20)), PayPeriod(LocalDate.of(2020, 3, 21), LocalDate.of(2020, 4, 20)))

    val expectedTwo: List[PayPeriod] = List(
      PayPeriod(LocalDate.of(2020, 2, 21), LocalDate.of(2020, 3, 20))
    )

    generatePayPeriods(endDates) mustBe expected
    generatePayPeriods(endDatesTwo) mustBe expectedTwo
  }

  "counts days in a given period" in new PayPeriodGenerator {
    val periodOne = PayPeriod(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30))
    val periodTwo = PayPeriod(LocalDate.of(2020, 4, 15), LocalDate.of(2020, 4, 30))
    val periodThree = PayPeriod(LocalDate.of(2020, 5, 1), LocalDate.of(2020, 5, 20))

    periodDaysCount(periodOne) mustBe 30
    periodDaysCount(periodTwo) mustBe 15
    periodDaysCount(periodThree) mustBe 20
  }

}
