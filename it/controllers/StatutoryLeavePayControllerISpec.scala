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

package controllers

import assets.BaseITConstants
import assets.PageTitles.statutoryLeavePay
import config.featureSwitch.FeatureSwitching
import models.PayMethod.Variable
import models.PaymentFrequency.Monthly
import models.UserAnswers
import play.api.http.Status._
import play.api.libs.json.Json
import utils.{CreateRequestHelper, CustomMatchers, ITCoreTestData, IntegrationSpecBase}

class StatutoryLeavePayControllerISpec
    extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with ITCoreTestData with BaseITConstants
    with FeatureSwitching {

  "GET /amount-paid-for-statutory-leave" must {

    "render the page correct with the correct status" in {
      val userAnswers: UserAnswers =
        emptyUserAnswers
          .withClaimPeriodStart("2020, 11, 1")
          .withClaimPeriodEnd("2020, 11, 30")
          .withFurloughStartDate("2020, 11, 1")
          .withFurloughStatus()
          .withAnnualPayAmount(BigDecimal(50000))
          .withPayMethod(Variable)

      setAnswers(userAnswers)
      val res = getRequest("/amount-paid-for-statutory-leave")("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

      whenReady(res) { result =>
        result must have(
          httpStatus(OK),
          titleOf(statutoryLeavePay),
        )
      }
    }
  }

  "POST /amount-paid-for-statutory-leave" when {
    "a valid amount has been entered" must {
      "redirect to the Part Time question" in {

        val userAnswers: UserAnswers = emptyUserAnswers
          .withClaimPeriodStart("2020, 10, 31")
          .withClaimPeriodEnd("2020, 11, 30")
          .withFurloughStartDate("2020, 11, 1")
          .withFurloughStatus()
          .withPaymentFrequency(Monthly)
          .withAnnualPayAmount(BigDecimal(50000))
          .withPayMethod(Variable)

        setAnswers(userAnswers)

        val res = postRequestHeader(
          path = "/amount-paid-for-statutory-leave",
          formJson = Json.obj("value" -> "420.00")
        )("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

        whenReady(res) { result =>
          result must have(
            httpStatus(SEE_OTHER),
            redirectLocation(controllers.routes.PartTimeQuestionController.onPageLoad().url)
          )
        }
      }
    }

    "£0 has been entered" must {
      "show the page with errors" in {
        val userAnswers: UserAnswers = emptyUserAnswers
          .withClaimPeriodStart("2020, 10, 31")
          .withClaimPeriodEnd("2020, 11, 30")
          .withFurloughStartDate("2020, 11, 1")
          .withFurloughStatus()
          .withPaymentFrequency(Monthly)
          .withAnnualPayAmount(BigDecimal(50000))
          .withPayMethod(Variable)

        setAnswers(userAnswers)

        val res = postRequestHeader(
          path = "/amount-paid-for-statutory-leave",
          formJson = Json.obj("value" -> "0")
        )("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

        whenReady(res) { result =>
          result must have(
            httpStatus(BAD_REQUEST),
            titleOf(statutoryLeavePay),
            contentExists("The amount this employee was paid for the periods of statutory leave must be more than £0")
          )
        }
      }
    }

    "an invalid amount has been entered" when {
      "a negative number has been entered" must {
        "show the page with errors" in {
          val userAnswers: UserAnswers = emptyUserAnswers
            .withClaimPeriodStart("2020, 10, 31")
            .withClaimPeriodEnd("2020, 11, 30")
            .withFurloughStartDate("2020, 11, 1")
            .withFurloughStatus()
            .withPaymentFrequency(Monthly)
            .withAnnualPayAmount(BigDecimal(50000))
            .withPayMethod(Variable)

          setAnswers(userAnswers)

          val res = postRequestHeader(
            path = "/amount-paid-for-statutory-leave",
            formJson = Json.obj("value" -> "-1")
          )("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

          whenReady(res) { result =>
            result must have(
              httpStatus(BAD_REQUEST),
              contentExists("The amount this employee was paid for the periods of statutory leave must be more than £0")
            )
          }
        }
      }

      "a value that is EQUAL to the reference pay is entered" must {
        "show the page with errors" in {
          val userAnswers: UserAnswers = emptyUserAnswers
            .withClaimPeriodStart("2020, 10, 31")
            .withClaimPeriodEnd("2020, 11, 30")
            .withFurloughStartDate("2020, 11, 1")
            .withFurloughStatus()
            .withPaymentFrequency(Monthly)
            .withAnnualPayAmount(BigDecimal(50000.01))
            .withPayMethod(Variable)

          setAnswers(userAnswers)

          val res = postRequestHeader(
            path = "/amount-paid-for-statutory-leave",
            formJson = Json.obj("value" -> "50000.01")
          )("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

          whenReady(res) { result =>
            result must have(
              httpStatus(BAD_REQUEST),
              contentExists("The amount this employee was paid for the periods of statutory leave must be less than £50,000")
            )
          }
        }
      }

      "a value that is MORE THAN the reference pay is entered" must {
        "show the page with errors" in {
          val userAnswers: UserAnswers = emptyUserAnswers
            .withClaimPeriodStart("2020, 10, 31")
            .withClaimPeriodEnd("2020, 11, 30")
            .withFurloughStartDate("2020, 11, 1")
            .withFurloughStatus()
            .withPaymentFrequency(Monthly)
            .withAnnualPayAmount(BigDecimal(50000.01))
            .withPayMethod(Variable)

          setAnswers(userAnswers)

          val res = postRequestHeader(
            path = "/amount-paid-for-statutory-leave",
            formJson = Json.obj("value" -> "50000.02")
          )("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

          whenReady(res) { result =>
            result must have(
              httpStatus(BAD_REQUEST),
              contentExists("The amount this employee was paid for the periods of statutory leave must be less than £50,000.01")
            )
          }
        }
      }

      "an invalid value has been entered" must {
        "show the page with errors" in {
          val userAnswers: UserAnswers = emptyUserAnswers
            .withClaimPeriodStart("2020, 10, 31")
            .withClaimPeriodEnd("2020, 11, 30")
            .withFurloughStartDate("2020, 11, 1")
            .withFurloughStatus()
            .withPaymentFrequency(Monthly)
            .withAnnualPayAmount(BigDecimal(50000))
            .withPayMethod(Variable)

          setAnswers(userAnswers)

          val res = postRequestHeader(
            path = "/amount-paid-for-statutory-leave",
            formJson = Json.obj("value" -> "hello world")
          )("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

          whenReady(res) { result =>
            result must have(
              httpStatus(BAD_REQUEST),
              contentExists("Enter a valid amount")
            )
          }
        }
      }
    }
  }
}
