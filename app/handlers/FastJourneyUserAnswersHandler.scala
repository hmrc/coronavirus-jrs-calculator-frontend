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
import cats.data.Validated.{Invalid, Valid}
import cats.implicits._
import models.ClaimPeriodQuestion.{ClaimOnDifferentPeriod, ClaimOnSamePeriod}
import models.FurloughPeriodQuestion.{FurloughedOnDifferentPeriod, FurloughedOnSamePeriod}
import models.PayPeriodQuestion.{UseDifferentPayPeriod, UseSamePayPeriod}
import models.UserAnswers
import pages._
import play.api.libs.json.Json
import utils.UserAnswersHelper
import com.softwaremill.quicklens._
import models.UserAnswers.AnswerV

trait FastJourneyUserAnswersHandler extends DataExtractor with UserAnswersHelper {

//  def updateJourney(userAnswer: UserAnswers): Option[UserAnswersState] =
//    userAnswer.getV(ClaimPeriodQuestionPage) flatMap {
//      case ClaimOnSamePeriod      => processFurloughQuestion(UserAnswersState(userAnswer, userAnswer))
//      case ClaimOnDifferentPeriod => Some(UserAnswersState(userAnswer.copy(data = Json.obj()), userAnswer))
//    }

  def updateJourney(userAnswer: UserAnswers): AnswerV[UserAnswersState] =
    userAnswer.getV(ClaimPeriodQuestionPage) match {
      case Valid(ClaimOnSamePeriod)      => processFurloughQuestion(UserAnswersState(userAnswer, userAnswer))
      case Valid(ClaimOnDifferentPeriod) => UserAnswersState(userAnswer.copy(data = Json.obj()), userAnswer).validNec
    }

//  private[this] def processFurloughQuestion(answer: UserAnswersState): Option[UserAnswersState] =
//    answer.original.get(FurloughPeriodQuestionPage) match {
//      case Some(FurloughedOnSamePeriod)      => processPayQuestion(answer)
//      case Some(FurloughedOnDifferentPeriod) => (clearAllAnswers andThen keepClaimPeriod).run(answer)
//      case None                              => Some(answer)
//    }

  private[this] def processFurloughQuestion(answer: UserAnswersState): AnswerV[UserAnswersState] =
    answer.original.getV(FurloughPeriodQuestionPage) match {
      case Valid(FurloughedOnSamePeriod)      => processPayQuestion(answer)
      case Valid(FurloughedOnDifferentPeriod) => (clearAllAnswers andThen keepClaimPeriod).run(answer)
      case Invalid(errors)                    => Some(answer)
    }

  private[this] def processPayQuestion(answer: UserAnswersState): Option[UserAnswersState] =
    answer.original.get(PayPeriodQuestionPage) match {
      case Some(UseSamePayPeriod) =>
        (clearAllAnswers andThen keepClaimPeriod andThen keepFurloughPeriod andThen keepPayPeriodData).run(answer)
      case Some(UseDifferentPayPeriod) =>
        (clearAllAnswers andThen keepClaimPeriod andThen keepFurloughPeriod).run(answer)
      case None => Some(answer)
    }

  private val keepClaimPeriod: Kleisli[Option, UserAnswersState, UserAnswersState] = Kleisli(answersState =>
    for {
      start     <- extractClaimPeriodStart(answersState.original)
      end       <- extractClaimPeriodEnd(answersState.original)
      withStart <- answersState.updated.set(ClaimPeriodStartPage, start).toOption
      withEnd   <- withStart.set(ClaimPeriodEndPage, end).toOption
    } yield UserAnswersState(withEnd, answersState.original))

  private val keepFurloughPeriod: Kleisli[Option, UserAnswersState, UserAnswersState] = Kleisli(answersState =>
    for {
      furlough  <- extractFurloughWithinClaim(answersState.original)
      withStart <- answersState.updated.set(FurloughStartDatePage, furlough.start).toOption
      withEnd   <- withStart.set(FurloughEndDatePage, furlough.end).toOption
    } yield UserAnswersState(withEnd, answersState.original))

  private val keepPayPeriod: Kleisli[Option, UserAnswersState, UserAnswersState] = Kleisli(
    answersState =>
      addPayDates(answersState.updated, answersState.original.getList(PayDatePage).toList).toOption
        .map(payPeriods => UserAnswersState(payPeriods, answersState.original)))

  private val keepPayMethod: Kleisli[Option, UserAnswersState, UserAnswersState] = Kleisli(answersState =>
    for {
      method     <- extractPayMethod(answersState.original)
      withMethod <- answersState.updated.set(PayMethodPage, method).toOption
    } yield UserAnswersState(withMethod, answersState.original))

  private val keepPaymentFrequency: Kleisli[Option, UserAnswersState, UserAnswersState] = Kleisli(answersState =>
    for {
      frequency     <- extractPaymentFrequency(answersState.original)
      withFrequency <- answersState.updated.set(PaymentFrequencyPage, frequency).toOption
    } yield UserAnswersState(withFrequency, answersState.original))

  private val keepPayPeriodData = keepPayPeriod andThen keepPayMethod andThen keepPaymentFrequency

  private val clearAllAnswers: Kleisli[Option, UserAnswersState, UserAnswersState] = Kleisli(
    answersState => Option(answersState.modify(_.updated.data).setTo(Json.obj())))
}

final case class UserAnswersState(updated: UserAnswers, original: UserAnswers)
