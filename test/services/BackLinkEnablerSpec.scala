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
import models.{BackJourneyDisabled, BackJourneyEnabled}
import utils.CoreTestData

class BackLinkEnablerSpec extends SpecBase with CoreTestData {

  "enable back link on claim question page if claim start and claim end exists" in new BackLinkEnabler {
    val answers = emptyUserAnswers.withClaimPeriodStart("2020,1,1").withClaimPeriodEnd("2020,2,1")

    backLinkStatus(answers) mustBe BackJourneyEnabled
  }
}
