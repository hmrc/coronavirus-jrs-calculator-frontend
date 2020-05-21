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

import models.ClaimPeriodQuestion.{ClaimOnDifferentPeriod, ClaimOnSamePeriod}
import models.FurloughPeriodQuestion.{FurloughedOnDifferentPeriod, FurloughedOnSamePeriod}
import models.PayPeriodQuestion.{UseDifferentPayPeriod, UseSamePayPeriod}
import models.UserAnswers
import pages.{ClaimPeriodEndPage, ClaimPeriodQuestionPage, ClaimPeriodStartPage, FurloughEndDatePage, FurloughPeriodQuestionPage, FurloughStartDatePage, PayPeriodQuestionPage}
import play.api.libs.json.Json

trait FastJourneyUserAnswersHandler {

  def updateJourney(userAnswer: UserAnswers): Option[UserAnswers] =
    userAnswer.get(ClaimPeriodQuestionPage) map {
      case ClaimOnSamePeriod      => updateWithFurloughQuestion(userAnswer).fold(userAnswer)(updated => updated)
      case ClaimOnDifferentPeriod => userAnswer.copy(data = Json.obj())
    }


  private def updateWithFurloughQuestion(answer: UserAnswers): Option[UserAnswers] =
    answer.get(FurloughPeriodQuestionPage) map {
      case FurloughedOnSamePeriod => updateWithPayQuestion(answer).fold(answer)(updated => updated)
      case FurloughedOnDifferentPeriod =>
        answer.copy(data = Json.obj(
          ClaimPeriodStartPage.toString -> answer.get(ClaimPeriodStartPage).fold("")(v => v.toString),
          ClaimPeriodEndPage.toString -> answer.get(ClaimPeriodEndPage).fold("")(v => v.toString),
      ))
    }

  private def updateWithPayQuestion(answer: UserAnswers): Option[UserAnswers] =
    answer.get(PayPeriodQuestionPage) map {
      case UseSamePayPeriod => answer
      case UseDifferentPayPeriod =>
        answer.copy(data = Json.obj(
          ClaimPeriodStartPage.toString -> answer.get(ClaimPeriodStartPage).fold("")(v => v.toString),
          ClaimPeriodEndPage.toString -> answer.get(ClaimPeriodEndPage).fold("")(v => v.toString),
          FurloughStartDatePage.toString -> answer.get(FurloughStartDatePage).fold("")(v => v.toString),
          FurloughEndDatePage.toString -> answer.get(FurloughEndDatePage).fold("")(v => v.toString)
      ))
    }
}
