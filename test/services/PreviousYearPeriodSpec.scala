/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import base.{CoreTestDataBuilder, SpecBase}
import models.CylbDuration
import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}

class PreviousYearPeriodSpec extends SpecBase with CoreTestDataBuilder {

  "return previous year date for a given date of this year" in new PreviousYearPeriod {
    fullPeriodWithPaymentDate("2020, 3, 1", "2020, 3, 7", "2020, 3, 7")

    previousYearPayDate(Weekly, fullPeriodWithPaymentDate("2020, 3, 1", "2020, 3, 7", "2020, 3, 7")) mustBe Seq(
      LocalDate.of(2019, 3, 2),
      LocalDate.of(2019, 3, 9))
    previousYearPayDate(Weekly, partialPeriodWithPaymentDate("2020, 3, 1", "2020, 3, 7", "2020, 3, 3", "2020, 3, 7", "2020, 3, 7")) mustBe Seq(
      LocalDate.of(2019, 3, 9))
    previousYearPayDate(Weekly, partialPeriodWithPaymentDate("2020, 3, 1", "2020, 3, 7", "2020, 3, 1", "2020, 3, 2", "2020, 3, 7")) mustBe Seq(
      LocalDate.of(2019, 3, 2))
    previousYearPayDate(Weekly, partialPeriodWithPaymentDate("2020, 3, 1", "2020, 3, 7", "2020, 3, 1", "2020, 3, 6", "2020, 3, 7")) mustBe Seq(
      LocalDate.of(2019, 3, 2),
      LocalDate.of(2019, 3, 9))

    previousYearPayDate(FortNightly, fullPeriodWithPaymentDate("2020, 3, 1", "2020, 3, 14", "2020, 3, 14")) mustBe Seq(
      LocalDate.of(2019, 3, 2),
      LocalDate.of(2019, 3, 16))

    previousYearPayDate(FourWeekly, fullPeriodWithPaymentDate("2020, 3, 1", "2020, 3, 28", "2020, 3, 28")) mustBe Seq(
      LocalDate.of(2019, 3, 2),
      LocalDate.of(2019, 3, 30))

    previousYearPayDate(FourWeekly, partialPeriodWithPaymentDate("2020, 3, 1", "2020, 3, 28", "2020, 3, 1", "2020, 3, 10", "2020, 3, 28")) mustBe Seq(
      LocalDate.of(2019, 3, 2),
      LocalDate.of(2019, 3, 30))

    previousYearPayDate(Monthly, fullPeriodWithPaymentDate("2020, 3, 1", "2020, 3, 31", "2020, 3, 31")) mustBe Seq(
      LocalDate.of(2019, 3, 31))
  }

  def extract(duration: CylbDuration): (Int, Int, Int) =
    (duration.fullPeriodLength, duration.equivalentPeriodDays, duration.previousPeriodDays)

  "Weekly tests" in {
    extract(CylbDuration(Weekly, fullPeriod("2020,3,1", "2020,3,7"))) mustBe Tuple3(7, 5, 2)

    extract(CylbDuration(Weekly, partialPeriod("2020,3,1" -> "2020,3,7", "2020,3,3" -> "2020,3,7"))) mustBe
      Tuple3(7, 5, 0)

    extract(CylbDuration(Weekly, partialPeriod("2020,3,1" -> "2020,3,7", "2020,3,4" -> "2020,3,7"))) mustBe
      Tuple3(7, 4, 0)

    extract(CylbDuration(Weekly, partialPeriod("2020,3,1" -> "2020,3,7", "2020,3,2" -> "2020,3,7"))) mustBe
      Tuple3(7, 5, 1)

    extract(CylbDuration(Weekly, partialPeriod("2020,3,1" -> "2020,3,7", "2020,3,1" -> "2020,3,6"))) mustBe
      Tuple3(7, 4, 2)

    extract(CylbDuration(Weekly, partialPeriod("2020,3,1" -> "2020,3,7", "2020,3,1" -> "2020,3,2"))) mustBe
      Tuple3(7, 0, 2)
  }

  "Fortnightly tests" in {
    extract(CylbDuration(FortNightly, fullPeriod("2020,3,1", "2020,3,14"))) mustBe
      Tuple3(14, 12, 2)

    extract(CylbDuration(FortNightly, partialPeriod("2020,3,1" -> "2020,3,14", "2020,3,3" -> "2020,3,14"))) mustBe
      Tuple3(14, 12, 0)

    extract(CylbDuration(FortNightly, partialPeriod("2020,3,1" -> "2020,3,14", "2020,3,5" -> "2020,3,14"))) mustBe
      Tuple3(14, 10, 0)

    extract(CylbDuration(FortNightly, partialPeriod("2020,3,1" -> "2020,3,14", "2020,3,2" -> "2020,3,14"))) mustBe
      Tuple3(14, 12, 1)
  }

  "Fourweekly tests" in {
    extract(CylbDuration(FourWeekly, fullPeriod("2020,3,1", "2020,3,28"))) mustBe
      Tuple3(28, 26, 2)

    extract(CylbDuration(FourWeekly, partialPeriod("2020,3,1" -> "2020,3,28", "2020,3,3" -> "2020,3,28"))) mustBe
      Tuple3(28, 26, 0)

    extract(CylbDuration(FourWeekly, partialPeriod("2020,3,1" -> "2020,3,28", "2020,3,9" -> "2020,3,28"))) mustBe
      Tuple3(28, 20, 0)

    extract(CylbDuration(FourWeekly, partialPeriod("2020,3,1" -> "2020,3,28", "2020,3, 2" -> "2020,3,28"))) mustBe
      Tuple3(28, 26, 1)
  }

  "Monthly tests" in {
    extract(CylbDuration(Monthly, fullPeriod("2020,3,1", "2020,3,31"))) mustBe
      Tuple3(31, 31, 0)
    extract(CylbDuration(Monthly, fullPeriod("2020,4,1", "2020,4,30"))) mustBe
      Tuple3(30, 30, 0)
    extract(CylbDuration(Monthly, partialPeriod("2020,3,1" -> "2020,3,31", "2020,3,3" -> "2020,3,31"))) mustBe
      Tuple3(31, 29, 0)
  }

}
