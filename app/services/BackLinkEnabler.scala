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

import cats.data.Validated.Valid
import cats.data.{NonEmptyChain, NonEmptyList}
import cats.implicits._
import handlers.{DataExtractor, PayPeriodsListHandler}
import models.UserAnswers.AnswerV
import models.{AnswerValidation, BackJourneyDisabled, BackJourneyEnabled, BackJourneyStatus, Periods, UserAnswers}
import play.api.libs.json.JsError

trait BackLinkEnabler extends DataExtractor with PayPeriodsListHandler {

  def backLinkStatus(userAnswers: UserAnswers): BackJourneyStatus =
    (claimQuestionBackLinkStatus(userAnswers), furloughQuestionBackLinkStatus(userAnswers), payPeriodQuestionBackLinkStatus(userAnswers))
      .mapN((_, _, _) => BackJourneyEnabled)
      .getOrElse(BackJourneyDisabled)

  private def claimQuestionBackLinkStatus(userAnswers: UserAnswers): AnswerV[BackJourneyStatus] =
    (extractClaimPeriodStartV(userAnswers), extractClaimPeriodEndV(userAnswers))
      .mapN((_, _) => BackJourneyEnabled)

  private def furloughQuestionBackLinkStatus(userAnswers: UserAnswers): AnswerV[BackJourneyStatus] =
    (extractFurloughPeriodV(userAnswers), extractFurloughStatusV(userAnswers))
      .mapN((_, _) => BackJourneyEnabled)

  private def payPeriodQuestionBackLinkStatus(userAnswers: UserAnswers): AnswerV[BackJourneyStatus] =
    (extractFurloughPeriodV(userAnswers), hasReusablePayPeriods(userAnswers))
      .mapN((_, _) => BackJourneyEnabled)

  private def hasReusablePayPeriods(userAnswers: UserAnswers): AnswerV[Seq[Periods]] = {
    val periods = extractPayPeriods(userAnswers)
    if (periods.isEmpty)
      NonEmptyChain
        .fromNonEmptyList(NonEmptyList.fromListUnsafe(List(AnswerValidation(JsError("re-usable periods unavailable")))))
        .invalid
    else Valid(periods)
  }

}
