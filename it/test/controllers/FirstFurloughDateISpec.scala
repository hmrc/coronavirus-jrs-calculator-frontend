/*
 * Copyright 2024 HM Revenue & Customs
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

package controllers

import assets.BaseITConstants
import assets.PageTitles.firstFurloughDate
import models.UserAnswers
import play.api.http.Status._
import play.api.libs.json.Json
import utils.{CreateRequestHelper, CustomMatchers, ITCoreTestData, IntegrationSpecBase}

class FirstFurloughDateISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants with ITCoreTestData {

  "GET /first-furlough-date" when {

    "render the first furlough date page" in {

      val userAnswers: UserAnswers = variablePayNewStarterEmployeeJourney

      setAnswers(userAnswers)

      val res = getRequestHeaders("/first-furlough-date")("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

      whenReady(res) { result =>
        result must have(
          httpStatus(OK),
          titleOf(firstFurloughDate)
        )
      }
    }
  }

  "POST /first-furlough-date" when {

    "valid values supplied" must {

      "redirect to onward route" in {

        val userAnswers: UserAnswers = variablePayNewStarterEmployeeJourney

        setAnswers(userAnswers)

        val res = postRequestHeader(
          path = "/first-furlough-date",
          formJson = Json.obj(
            "firstFurloughDate.day" -> "11",
            "firstFurloughDate.month" -> "11",
            "firstFurloughDate.year" -> "2020"
          )
        )("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

        whenReady(res) { result =>
          result must have(
            httpStatus(SEE_OTHER),
            redirectLocation(controllers.routes.PayDateController.onPageLoad(1).url)
          )
        }
      }
    }
  }
}

