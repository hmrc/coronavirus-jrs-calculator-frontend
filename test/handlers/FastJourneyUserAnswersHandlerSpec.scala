/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package handlers

import java.time.LocalDate

import base.SpecBase
import models.ClaimPeriodQuestion.{ClaimOnDifferentPeriod, ClaimOnSamePeriod}
import models.FurloughPeriodQuestion.{FurloughedOnDifferentPeriod, FurloughedOnSamePeriod}
import models.PayPeriodQuestion.{UseDifferentPayPeriod, UseSamePayPeriod}
import pages.{ClaimPeriodEndPage, ClaimPeriodStartPage, FurloughEndDatePage, FurloughStartDatePage, PayDatePage}
import play.api.libs.json.{JsObject, Json}
import utils.CoreTestData

class FastJourneyUserAnswersHandlerSpec extends SpecBase with CoreTestData {

  "delete all data from the DB if answer is `No` to claim period question" in new FastJourneyUserAnswersHandler {
    val userAnswers = dummyUserAnswers.withClaimPeriodQuestion(ClaimOnDifferentPeriod)

    userAnswers.data.value.values.size must be > 1
    updateJourney(userAnswers).get.id mustBe userAnswers.id
    updateJourney(userAnswers).get.data mustBe Json.obj()
  }

  "delete nothing from the DB if answer is `Yes` to claim period question" in new FastJourneyUserAnswersHandler {
    val userAnswers = dummyUserAnswers.withClaimPeriodQuestion(ClaimOnSamePeriod)

    userAnswers.data.value.values.size must be > 1
    updateJourney(userAnswers).get.id mustBe userAnswers.id
    updateJourney(userAnswers).get.data mustBe userAnswers.data
  }

  "delete all from the DB if answer is `No` to furlough period question excluding claim period" in new FastJourneyUserAnswersHandler {
    val userAnswers = dummyUserAnswers
      .withClaimPeriodQuestion(ClaimOnSamePeriod)
        .withFurloughPeriodQuestion(FurloughedOnDifferentPeriod)

    val expectedUserAnswersData: JsObject = emptyUserAnswers
      .copy(id = userAnswers.id)
      .withClaimPeriodStart(userAnswers.get(ClaimPeriodStartPage).get.toString)
      .withClaimPeriodEnd(userAnswers.get(ClaimPeriodEndPage).get.toString).data

    userAnswers.data.value.values.size must be > 2
    updateJourney(userAnswers).get.id mustBe userAnswers.id
    updateJourney(userAnswers).get.data mustBe expectedUserAnswersData
  }

  "delete nothing from the DB if answer is `Yes` to furlough period question" in new FastJourneyUserAnswersHandler {
    val userAnswers = dummyUserAnswers
      .withClaimPeriodQuestion(ClaimOnSamePeriod)
        .withFurloughPeriodQuestion(FurloughedOnSamePeriod)

    userAnswers.data.value.values.size must be > 2
    updateJourney(userAnswers).get.id mustBe userAnswers.id
    updateJourney(userAnswers).get.data mustBe userAnswers.data
  }

  "delete all from the DB if answer is `Yes` to claim and furlough period questions " +
  "but `No` to pay period, keeping claim&furlough period dates" in new FastJourneyUserAnswersHandler {
    val userAnswers = dummyUserAnswers
        .withFurloughEndDate("2020-3-31")
      .withClaimPeriodQuestion(ClaimOnSamePeriod)
      .withFurloughPeriodQuestion(FurloughedOnSamePeriod)
      .withPayPeriodQuestion(UseDifferentPayPeriod)

    val expectedUserAnswersData: JsObject = emptyUserAnswers
      .copy(id = userAnswers.id)
      .withClaimPeriodStart(userAnswers.get(ClaimPeriodStartPage).get.toString)
      .withClaimPeriodEnd(userAnswers.get(ClaimPeriodEndPage).get.toString)
      .withFurloughStartDate(userAnswers.get(FurloughStartDatePage).get.toString)
      .withFurloughEndDate(userAnswers.get(FurloughEndDatePage).get.toString)
      .data

    userAnswers.data.value.values.size must be > 2
    updateJourney(userAnswers).get.id mustBe userAnswers.id
    updateJourney(userAnswers).get.data mustBe expectedUserAnswersData
  }

  "delete data from the DB if answer is `Yes` to pay period question excluding Claim,furlough and pay periods" in new FastJourneyUserAnswersHandler {
    val userAnswers = dummyUserAnswers
      .withFurloughEndDate("2020-3-31")
      .withClaimPeriodQuestion(ClaimOnSamePeriod)
      .withFurloughPeriodQuestion(FurloughedOnSamePeriod)
      .withPayPeriodQuestion(UseSamePayPeriod)

    val expectedUserAnswersData: JsObject = emptyUserAnswers.copy(data = Json.obj(
      ClaimPeriodStartPage.toString -> userAnswers.get(ClaimPeriodStartPage).fold("")(v => v.toString),
      ClaimPeriodEndPage.toString -> userAnswers.get(ClaimPeriodEndPage).fold("")(v => v.toString),
      FurloughStartDatePage.toString -> userAnswers.get(FurloughStartDatePage).fold("")(v => v.toString),
      FurloughEndDatePage.toString -> userAnswers.get(FurloughEndDatePage).fold("")(v => v.toString),
      PayDatePage.toString -> userAnswers.getList(PayDatePage)
    )).data

    userAnswers.data.value.values.size must be > 2
    updateJourney(userAnswers).get.id mustBe userAnswers.id
    updateJourney(userAnswers).get.data mustBe expectedUserAnswersData
  }
}
