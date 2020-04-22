/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package handlers

import java.time.LocalDate

import base.{CoreDataBuilder, SpecBase}
import models.PayQuestion.{Regularly, Varies}
import models.{Amount, FullPeriod, MandatoryData, PaymentDate, PaymentWithPeriod, Period, PeriodWithPaymentDate, UserAnswers}
import pages.FurloughStartDatePage
import play.api.libs.json.Json
import utils.CoreTestData

class DataExtractorSpec extends SpecBase with CoreTestData with CoreDataBuilder {

  "Extract mandatory data in order to do the calculation" in new DataExtractor {
    val userAnswers = Json.parse(userAnswersJson()).as[UserAnswers]

    extract(userAnswers) must matchPattern {
      case Some(MandatoryData(_, _, _, _, _, _, _, _, _)) =>
    }
  }

  "Extract furlough period matching claim period if furlough question entered is no" in new DataExtractor {
    val userAnswers = Json.parse(userAnswersJson("no")).as[UserAnswers]
    val claimPeriod = Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 30))
    val expected = Period(userAnswers.get(FurloughStartDatePage).get, claimPeriod.end)

    extractFurloughPeriod(extract(userAnswers).get, userAnswers) mustBe Some(expected)
  }

  "Extract furlough period with end date matching the claim end date with user submitted start date" in new DataExtractor {
    val userAnswers = Json.parse(userAnswersJson("no", "2020-03-15")).as[UserAnswers]
    val claimPeriod = Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 30))
    val expected = Period(LocalDate.of(2020, 3, 15), claimPeriod.end)

    extractFurloughPeriod(extract(userAnswers).get, userAnswers) mustBe Some(expected)
  }

  "Extract Salary as an Amount() when payQuestion answer is Regularly" in new DataExtractor {
    val userAnswers = Json.parse(userAnswersJson()).as[UserAnswers]
    val expected = Amount(2000.0)

    extractGrossPay(userAnswers) mustBe Some(expected)
  }

  "Extract prior furlough period from user answers" in new DataExtractor {
    val userAnswers = Json.parse(userAnswersJson(employeeStartDate = "2020-12-1")).as[UserAnswers]
    val expected = Period(LocalDate.of(2020, 12, 1), LocalDate.of(2020, 2, 29))
  }

  "Extract variable gross pay when payQuestion answer is Varies" in new DataExtractor {
    val userAnswers = Json.parse(userAnswersJson(payQuestion = "varies", variableGrossPay = "2400.0")).as[UserAnswers]
    val expected = Amount(2400.0)

    extractGrossPay(userAnswers) mustBe Some(expected)
  }

  "Extract payments for employees that are paid a regular amount each time" in new DataExtractor {
    val userAnswers = Json.parse(userAnswersJson()).as[UserAnswers]
    val expected = Seq(
      PaymentWithPeriod(
        Amount(0.0),
        Amount(2000.0),
        PeriodWithPaymentDate(
          FullPeriod(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))),
          PaymentDate(LocalDate.of(2020, 3, 20))),
        Regularly
      ),
      PaymentWithPeriod(
        Amount(0.0),
        Amount(2000.0),
        PeriodWithPaymentDate(
          FullPeriod(Period(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30))),
          PaymentDate(LocalDate.of(2020, 4, 20))),
        Regularly
      )
    )

    val furloughPeriod: Period = Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 30))

    extractPayments(userAnswers, furloughPeriod) mustBe Some(expected)
  }

  "Extract payments for employees that are paid a variable amount each time" in new DataExtractor {
    val userAnswers =
      Json.parse(userAnswersJson(payQuestion = "varies", variableGrossPay = "2400.00", employeeStartDate = "2019-12-01")).as[UserAnswers]
    val expected = Seq(
      PaymentWithPeriod(
        Amount(0.00),
        Amount(817.47),
        PeriodWithPaymentDate(
          FullPeriod(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))),
          PaymentDate(LocalDate.of(2020, 3, 20))),
        Varies
      ),
      PaymentWithPeriod(
        Amount(0.00),
        Amount(791.10),
        PeriodWithPaymentDate(
          FullPeriod(Period(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30))),
          PaymentDate(LocalDate.of(2020, 4, 20))),
        Varies
      )
    )

    val furloughPeriod: Period = Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 30))

    extractPayments(userAnswers, furloughPeriod) mustBe Some(expected)
  }

  "Calculates cylbs when variable length is No but the employee start date is before 6/4/2019" in new DataExtractor {
    val userAnswers = Json.parse(jsonCylb).as[UserAnswers]

    val expected =
      List(
        paymentWithPeriod(
          150,
          1786.84,
          partialPeriodWithPaymentDate("2020-03-01", "2020-03-31", "2020-03-15", "2020-03-31", "2020-03-30"),
          Varies),
        paymentWithPeriod(0.0, 1729.20, fullPeriodWithPaymentDate("2020-04-01", "2020-04-30", "2020-04-30"), Varies)
      )
    extractPayments(userAnswers, extractFurloughPeriod(extract(userAnswers).get, userAnswers).get) mustBe Some(expected)
  }

  "Calculates cylbs when variable length is Yes but the employee start date is None" in new DataExtractor {
    val userAnswers = Json.parse(jsonCylbWithoutEmployeeStartDate).as[UserAnswers]

    val expected =
      List(
        paymentWithPeriod(
          100.0,
          3385.71,
          partialPeriodWithPaymentDate("2020-3-1", "2020-3-28", "2020-3-2", "2020-3-28", "2020-03-28"),
          Varies)
      )

    extractPayments(userAnswers, extractFurloughPeriod(extract(userAnswers).get, userAnswers).get) mustBe Some(expected)
  }

}
