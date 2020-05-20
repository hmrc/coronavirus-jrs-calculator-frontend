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

import base.SpecBase
import models.ClaimPeriodQuestion.{ClaimOnDifferentPeriod, ClaimOnSamePeriod}
import models.FurloughPeriodQuestion.FurloughedOnDifferentPeriod
import pages.{ClaimPeriodEndPage, ClaimPeriodStartPage}
import play.api.libs.json.{JsObject, Json}
import utils.CoreTestData

class FastJourneyUserAnswersHandlerSpec extends SpecBase with CoreTestData {

  "delete all data from the DB if answer is `No` to claim period question" in new FastJourneyUserAnswersHandler {
    val userAnswer = dummyUserAnswers.withClaimPeriodQuestion(ClaimOnDifferentPeriod)

    userAnswer.data.value.values.size must be > 1
    updateJourney(userAnswer).get.id mustBe userAnswer.id
    updateJourney(userAnswer).get.data mustBe Json.obj()
  }

  "delete nothing from the DB if answer is `Yes` to claim period question" in new FastJourneyUserAnswersHandler {
    val userAnswer = dummyUserAnswers.withClaimPeriodQuestion(ClaimOnSamePeriod)

    userAnswer.data.value.values.size must be > 1
    updateJourney(userAnswer).get.id mustBe userAnswer.id
    updateJourney(userAnswer).get.data mustBe userAnswer.data
  }

  "delete all from the DB if answer is `No` to furlough period question excluding claim period" in new FastJourneyUserAnswersHandler {
    val userAnswer = dummyUserAnswers
      .withClaimPeriodQuestion(ClaimOnSamePeriod)
        .withFurloughPeriodQuestion(FurloughedOnDifferentPeriod)

    val expectedUserAnswersData: JsObject = emptyUserAnswers
      .copy(id = userAnswer.id)
      .withClaimPeriodStart(userAnswer.get(ClaimPeriodStartPage).get.toString)
      .withClaimPeriodEnd(userAnswer.get(ClaimPeriodEndPage).get.toString).data

    userAnswer.data.value.values.size must be > 2
    updateJourney(userAnswer).get.id mustBe userAnswer.id
    updateJourney(userAnswer).get.data mustBe expectedUserAnswersData
  }
}
