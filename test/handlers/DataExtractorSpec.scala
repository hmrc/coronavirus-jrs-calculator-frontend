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

/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package handlers

import java.time.LocalDate

import base.{CoreTestDataBuilder, SpecBase}
import models.{Amount, TopUpPayment, UserAnswers}
import pages.TopUpAmountPage
import play.api.libs.json.Json
import utils.CoreTestData

class DataExtractorSpec extends SpecBase with CoreTestData with CoreTestDataBuilder {

  "Extract prior furlough period from user answers" when {

    "employee start date is present" in new DataExtractor {
      val userAnswers = Json.parse(userAnswersJson(employeeStartDate = "2020-12-01")).as[UserAnswers]
      val expected = period("2020, 12, 1", "2020, 2, 29")

      extractPriorFurloughPeriod(userAnswers) mustBe Some(expected)
    }

    "employee start date is not present" in new DataExtractor {
      val userAnswers = Json.parse(userAnswersJson()).as[UserAnswers]
      val expected = period("2019, 4, 6", "2020, 2, 29")

      extractPriorFurloughPeriod(userAnswers) mustBe Some(expected)
    }

    "extract top up payments" in new DataExtractor {
      val payments = TopUpPayment(LocalDate.of(2020, 3, 1), Amount(100.0))
      val userAnswers = UserAnswers("123").set(TopUpAmountPage, payments).success.get

      extractTopUpPayment(userAnswers) mustBe Some(payments)
    }
  }
}
