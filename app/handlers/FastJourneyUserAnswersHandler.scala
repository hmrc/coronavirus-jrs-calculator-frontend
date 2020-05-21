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

import cats.data.Kleisli
import cats.implicits._
import models.ClaimPeriodQuestion.{ClaimOnDifferentPeriod, ClaimOnSamePeriod}
import models.FurloughPeriodQuestion.{FurloughedOnDifferentPeriod, FurloughedOnSamePeriod}
import models.PayPeriodQuestion.{UseDifferentPayPeriod, UseSamePayPeriod}
import models.UserAnswers
import pages._
import play.api.libs.json.Json
import utils.UserAnswersHelper

trait FastJourneyUserAnswersHandler extends DataExtractor with UserAnswersHelper {

  def updateJourney(userAnswer: UserAnswers): Option[UserAnswersState] =
    userAnswer.get(ClaimPeriodQuestionPage) flatMap {
      case ClaimOnSamePeriod      => processFurloughQuestion(UserAnswersState(userAnswer, userAnswer))
      case ClaimOnDifferentPeriod => Some(UserAnswersState(userAnswer.copy(data = Json.obj()), userAnswer))
    }

  private def processFurloughQuestion(answer: UserAnswersState): Option[UserAnswersState] =
    answer.original.get(FurloughPeriodQuestionPage) match {
      case Some(FurloughedOnSamePeriod)      => processPayQuestion(answer)
      case Some(FurloughedOnDifferentPeriod) => keepClaimPeriod(answer)
      case None => Some(answer)
    }

  private def processPayQuestion(answer: UserAnswersState): Option[UserAnswersState] =
    answer.original.get(PayPeriodQuestionPage) match {
      case Some(UseSamePayPeriod) =>
        (keepClaimPeriod andThen keepFurloughPeriod andThen keepPayPeriod).run(answer)
      case Some(UseDifferentPayPeriod) =>
        (keepClaimPeriod andThen keepFurloughPeriod).run(answer)
      case None => Some(answer)
    }



  private val keepClaimPeriod: Kleisli[Option, UserAnswersState, UserAnswersState] = Kleisli(current =>
    for {
      newAnswers <- Option(current.original.copy(data = Json.obj()))
      start      <- extractClaimPeriodStart(current.original)
      end        <- extractClaimPeriodEnd(current.original)
      withStart  <- newAnswers.set(ClaimPeriodStartPage, start).toOption
      withEnd    <- withStart.set(ClaimPeriodEndPage, end).toOption
    } yield UserAnswersState(withEnd, current.original)
  )

  private val keepFurloughPeriod: Kleisli[Option, UserAnswersState, UserAnswersState] = Kleisli(current =>
    for {
      furlough   <- extractFurloughWithinClaim(current.original)
      withStart  <- current.updated.set(FurloughStartDatePage, furlough.start).toOption
      withEnd    <- withStart.set(FurloughEndDatePage, furlough.end).toOption
    } yield UserAnswersState(withEnd, current.original))

  private val keepPayPeriod: Kleisli[Option, UserAnswersState, UserAnswersState] = Kleisli(current =>
    addPayDates(current.updated, current.original.getList(PayDatePage).toList).toOption
      .map(payPeriods => UserAnswersState(payPeriods, current.original))
  )
}

case class UserAnswersState(updated: UserAnswers, original: UserAnswers)
