/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package handlers

import java.time.LocalDate

import base.SpecBase
import models.{FurloughPeriod, PayPeriod, UserAnswers}
import play.api.libs.json.Json
import utils.CoreTestData

class DataExtractorSpec extends SpecBase with CoreTestData {

  "Extract mandatory data in order to do the calculation" in new DataExtractor {
    val userAnswers = Json.parse(userAnswersJson()).as[UserAnswers]

    extract(userAnswers) must matchPattern {
      case Some(MandatoryData(_, _, _, _, _, _, _)) =>
    }
  }

  "Extract furlough period matching claim period if furlough question entered is yes" in new DataExtractor {
    val userAnswers = Json.parse(userAnswersJson("yes")).as[UserAnswers]
    val claimPeriod = PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 30))
    val expected = FurloughPeriod(claimPeriod.start, claimPeriod.end)

    extractFurloughPeriod(userAnswers) mustBe Some(expected)
  }

  "Extract furlough period with end date matching the claim end date with user submitted start date" in new DataExtractor {
    val userAnswers = Json.parse(userAnswersJson("no", "startedInClaim", "2020-03-15")).as[UserAnswers]
    val claimPeriod = PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 30))
    val expected = FurloughPeriod(LocalDate.of(2020, 3, 15), claimPeriod.end)

    extractFurloughPeriod(userAnswers) mustBe Some(expected)
  }

  "Extract furlough period with start date matching the claim start date with user submitted end date" in new DataExtractor {
    val userAnswers = Json.parse(userAnswersJson("no", "endedInClaim", furloughEndDate = "2020-04-15")).as[UserAnswers]
    val claimPeriod = PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 30))
    val expected = FurloughPeriod(claimPeriod.start, LocalDate.of(2020, 4, 15))

    extractFurloughPeriod(userAnswers) mustBe Some(expected)
  }

  "Extract furlough period with user submitted start and end date" in new DataExtractor {
    val userAnswers = Json.parse(userAnswersJson("no", "startedAndEndedInClaim", "2020-03-15", "2020-04-15")).as[UserAnswers]
    val claimPeriod = PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 30))
    val expected = FurloughPeriod(LocalDate.of(2020, 3, 15), LocalDate.of(2020, 4, 15))

    extractFurloughPeriod(userAnswers) mustBe Some(expected)
  }

}
