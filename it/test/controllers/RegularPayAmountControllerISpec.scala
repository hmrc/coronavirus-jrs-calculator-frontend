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

import assets.{BaseITConstants, PageTitles}
import controllers.scenarios.AprilConfirmationScenarios.emptyUserAnswers
import models.{RegularLengthEmployed, Salary, UserAnswers}
import pages.{ClaimPeriodStartPage, OnPayrollBefore30thOct2020Page, RegularLengthEmployedPage, RegularPayAmountPage}
import play.api.http.Status._
import play.api.libs.json.Json
import utils.LocalDateHelpers.{mar19th2020, mar2nd2021, oct30th2020}
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}
import views.ViewUtils._

import java.time.LocalDate

class RegularPayAmountControllerISpec extends IntegrationSpecBase with CustomMatchers
 with CreateRequestHelper with BaseITConstants {

  "employee is type 1" when {

    val userAnswers: UserAnswers = emptyUserAnswers
      .set(RegularLengthEmployedPage, RegularLengthEmployed.Yes)
      .success
      .value

    getTests(mar19th2020, userAnswers)
  }

  "employee is type 2a" when {

    val userAnswers: UserAnswers = emptyUserAnswers
      .set(RegularLengthEmployedPage, RegularLengthEmployed.No)
      .success
      .value
      .set(OnPayrollBefore30thOct2020Page, true)
      .success
      .value

    getTests(oct30th2020, userAnswers)
  }

  "employee is type 2b" when {

    val userAnswers: UserAnswers = emptyUserAnswers
      .set(RegularLengthEmployedPage, RegularLengthEmployed.No)
      .success
      .value
      .set(OnPayrollBefore30thOct2020Page, false)
      .success
      .value

    getTests(mar2nd2021, userAnswers)
  }

  "POST /regular-pay-amount" when {

    "enters a valid answer" when {

      "redirect to PartTimeQuestion page" in {

        val ans = Salary(BigDecimal(123.45))

        val userAnswers: UserAnswers = emptyUserAnswers
          .set(ClaimPeriodStartPage, phaseTwoStartDate.plusDays(1))
          .success
          .value
          .set(RegularLengthEmployedPage, RegularLengthEmployed.Yes)
          .success
          .value
          .set(RegularPayAmountPage, ans)
          .success
          .value

        setAnswers(userAnswers)

        val res = postRequest("/regular-pay-amount",
          Json.obj("value" -> ans.amount.toString())
        )("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

        whenReady(res) { result =>
          result must have(
            httpStatus(SEE_OTHER),
            redirectLocation(controllers.routes.PartTimeQuestionController.onPageLoad().url)
          )
        }
      }
    }

    "enters an invalid answer" when {

      "return BadRequest" in {

        val ans = Salary(BigDecimal(123.45))

        val userAnswers: UserAnswers = emptyUserAnswers
          .set(ClaimPeriodStartPage, phaseTwoStartDate.plusDays(1))
          .success
          .value
          .set(RegularLengthEmployedPage, RegularLengthEmployed.Yes)
          .success
          .value
          .set(RegularPayAmountPage, ans)
          .success
          .value

        setAnswers(userAnswers)

        val res = postRequest("/regular-pay-amount",
          Json.obj("value" -> "NOPE")
        )("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

        whenReady(res) { result =>
          result must have(
            httpStatus(BAD_REQUEST)
          )
        }
      }
    }
  }

  def getTests(cutOffDate: LocalDate, userAnswers: UserAnswers): Unit =
    "GET /regular-pay-amount" in {

      setAnswers(userAnswers)
      val res = getRequest("/regular-pay-amount")("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

      whenReady(res) { result =>
        result must have(
          httpStatus(OK),
          titleOf(PageTitles.regularPayAmount(dateToString(cutOffDate)))
        )
      }
    }
}
