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
import models._
import play.api.test.Helpers._
import utils.{CreateRequestHelper, CustomMatchers, ITCoreTestData, IntegrationSpecBase}
import JanuaryConfirmationScenarios._
import DecemberConfirmationScenarios._
import NovemberConfirmationScenarios._

class ConfirmationControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers
  with BaseITConstants with ITCoreTestData {

  val november: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = {
    novemberFourWeeklyScenarios ++
      novemberMonthlyScenarios ++
      novemberTwoWeeklyScenarios ++
      novemberWeeklyScenarios ++
      novemberVariableFourWeeklyScenarios ++
      novemberVariableMonthlyScenarios ++
      novemberVariableTwoWeeklyScenarios ++
      novemberVariableWeeklyScenarios
  }

  val december: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = {
    decemberFourWeeklyScenarios ++
      decemberMonthlyScenarios ++
      decemberTwoWeeklyScenarios ++
      decemberWeeklyScenarios ++
      decemberVariableFourWeeklyScenarios ++
      decemberVariableMonthlyScenarios ++
      decemberVariableTwoWeeklyScenarios ++
      decemberVariableWeeklyScenarios
  }

  val january: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = {
      januaryFourWeeklyScenarios ++
      januaryMonthlyScenarios ++
      januaryTwoWeeklyScenarios ++
      januaryWeeklyScenarios ++
      januaryVariableFourWeeklyScenarios ++
      januaryVariableMonthlyScenarios ++
      januaryVariableTwoWeeklyScenarios ++
      januaryVariableWeeklyScenarios
  }

  val scenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = {
    november ++ december ++ january
  }

  "GET /confirmation" should {

    "show the page" when {

      scenarios.foreach {
        case (scenarioSummary, scenarios) =>

        scenarios.zipWithIndex.foreach {
          case ((scenario, outcome), index) =>

            s"the user has answered the questions relating to $scenarioSummary for scenario ${index + 1}" in {
              val userAnswers: UserAnswers = scenario

              setAnswers(userAnswers)

              val res = getRequestHeaders("/confirmation")("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

              whenReady(res) { result =>
                result should have(
                  httpStatus(OK),
                  titleOf("What you can claim for this employee - Job Retention Scheme calculator - GOV.UK"),
                  contentExists(s"${outcome.setScale(2).toString()}"),
                )
              }
            }
        }
      }

      "the user has answered the questions for regular journey" in {

        val userAnswers: UserAnswers = dummyUserAnswers

        setAnswers(userAnswers)

        val res = getRequestHeaders("/confirmation")("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

        whenReady(res) { result =>
          result should have(
            httpStatus(OK),
            titleOf("What you can claim for this employee - Job Retention Scheme calculator - GOV.UK"),
            contentExists(s"Total furlough grant for pay period = £")
          )
        }
      }
      "the user has answered the questions for dummyUserAnswersNoLastPayDate" in {

        val userAnswers: UserAnswers = dummyUserAnswersNoLastPayDate

        setAnswers(userAnswers)

        val res = getRequestHeaders("/confirmation")("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

        whenReady(res) { result =>
          result should have(
            httpStatus(OK),
            titleOf("What you can claim for this employee - Job Retention Scheme calculator - GOV.UK")
          )
        }
      }
      "the user has answered the questions for variableMonthlyPartial" in {

        val userAnswers: UserAnswers = variableMonthlyPartial

        setAnswers(userAnswers)

        val res = getRequestHeaders("/confirmation")("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

        whenReady(res) { result =>
          result should have(
            httpStatus(OK),
            titleOf("What you can claim for this employee - Job Retention Scheme calculator - GOV.UK")
          )
        }
      }
      "the user has answered the questions for phase 2" in {

        val userAnswers: UserAnswers = phaseTwoJourney()

        setAnswers(userAnswers)

        val res = getRequestHeaders("/confirmation")("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

        whenReady(res) { result =>
          result should have(
            httpStatus(OK),
            titleOf("What you can claim for this employee - Job Retention Scheme calculator - GOV.UK")
          )
        }
      }
    }


    "redirect to another page" when {

      "the user has not answered the questions" in {

        val userAnswers: UserAnswers = emptyUserAnswers

        setAnswers(userAnswers)

        val res = getRequest("/confirmation")("sessionId" -> userAnswers.id, "X-Session-ID" -> userAnswers.id)

        //TODO Should redirect to reset or start again page
        whenReady(res) { result =>
          result should have(
            httpStatus(SEE_OTHER),
            redirectLocation("/job-retention-scheme-calculator/error")
          )
        }
      }
    }
  }
}
