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

package services

import base.SpecBase
import models.{BackJourneyDisabled, BackJourneyEnabled, UserAnswers}
import utils.CoreTestData

class BackLinkEnablerSpec extends SpecBase with CoreTestData {

  "enable back link on claim question page if claim start and claim end exists" in new BackLinkEnabler {
    val answers = emptyUserAnswers.withClaimPeriodStart("2020,1,1").withClaimPeriodEnd("2020,2,1")

    claimQuestionBackLinkStatus(answers) mustBe BackJourneyEnabled
    claimQuestionBackLinkStatus(emptyUserAnswers) mustBe BackJourneyDisabled
  }

  "enable back link on furlough question page if claim start and claim end exists" in new BackLinkEnabler {
    val answersWithClaim = emptyUserAnswers.withClaimPeriodStart("2020,1,1").withClaimPeriodEnd("2020,2,1")
    val answers = answersWithClaim.withFurloughStartDate("2020,1,1").withFurloughEndDate("2020,2,1")

    furloughQuestionBackLinkStatus(answers) mustBe BackJourneyEnabled
    furloughQuestionBackLinkStatus(answersWithClaim) mustBe BackJourneyDisabled
  }

  "enable back link on pay period question page if furlough start, furlough end and pay periods exists" in new BackLinkEnabler {
    val answers = emptyUserAnswers
      .withFurloughStartDate("2020,1,1")
      .withFurloughEndDate("2020,2,1")
      .withClaimPeriodStart("2020,1,1")
      .withClaimPeriodEnd("2020,2,1")
      .withPayDate(List("2020,2,1"))

    payPeriodQuestionBackLinkStatus(answers) mustBe BackJourneyEnabled
    payPeriodQuestionBackLinkStatus(emptyUserAnswers) mustBe BackJourneyDisabled
  }
}
