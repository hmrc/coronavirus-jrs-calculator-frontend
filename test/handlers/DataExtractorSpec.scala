/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package handlers

import java.time.LocalDate

import base.SpecBase
import models.{Amount, FullPeriod, PaymentWithPeriod, Period, Salary, UserAnswers}
import pages.FurloughStartDatePage
import play.api.libs.json.Json
import utils.CoreTestData

class DataExtractorSpec extends SpecBase with CoreTestData {

  "Extract mandatory data in order to do the calculation" in new DataExtractor {
    val userAnswers = Json.parse(userAnswersJson()).as[UserAnswers]

    extract(userAnswers) must matchPattern {
      case Some(MandatoryData(_, _, _, _, _, _, _, _)) =>
    }
  }

  "Extract furlough period matching claim period if furlough question entered is no" in new DataExtractor {
    val userAnswers = Json.parse(userAnswersJson("no")).as[UserAnswers]
    val claimPeriod = Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 30))
    val expected = Period(userAnswers.get(FurloughStartDatePage).get, claimPeriod.end)

    extractFurloughPeriod(userAnswers) mustBe Some(expected)
  }

  "Extract furlough period with end date matching the claim end date with user submitted start date" in new DataExtractor {
    val userAnswers = Json.parse(userAnswersJson("no", "2020-03-15")).as[UserAnswers]
    val claimPeriod = Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 30))
    val expected = Period(LocalDate.of(2020, 3, 15), claimPeriod.end)

    extractFurloughPeriod(userAnswers) mustBe Some(expected)
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
      PaymentWithPeriod(Amount(0.0), Amount(2000.0), FullPeriod(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31)))),
      PaymentWithPeriod(Amount(0.0), Amount(2000.0), FullPeriod(Period(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30))))
    )

    val furloughPeriod: Period = Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 30))

    extractPayments(userAnswers, furloughPeriod) mustBe Some(expected)
  }

  "Extract payments for employees that are paid a variable amount each time" in new DataExtractor {
    val userAnswers =
      Json.parse(userAnswersJson(payQuestion = "varies", variableGrossPay = "2400.00", employeeStartDate = "2019-12-01")).as[UserAnswers]
    val expected = Seq(
      PaymentWithPeriod(Amount(817.47), FullPeriod(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31)))),
      PaymentWithPeriod(Amount(791.10), FullPeriod(Period(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30))))
    )

    val furloughPeriod: Period = Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 30))

    extractPayments(userAnswers, furloughPeriod) mustBe Some(expected)
  }

}
