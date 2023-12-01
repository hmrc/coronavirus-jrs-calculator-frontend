/*
 * Copyright 2023 HM Revenue & Customs
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
import com.softwaremill.quicklens._
import models.ClaimPeriodQuestion.{ClaimOnDifferentPeriod, ClaimOnSamePeriod}
import models.FurloughPeriodQuestion.{FurloughedOnDifferentPeriod, FurloughedOnSamePeriod}
import models.PayPeriodQuestion.{UseDifferentPayPeriod, UseSamePayPeriod}
import models.UserAnswers.AnswerV
import models.{GenericValidationError, UserAnswers}
import pages._
import play.api.libs.json.{JsError, Json}
import utils.{LoggerUtil, UserAnswersHelper}

trait FastJourneyUserAnswersHandler extends DataExtractor with UserAnswersHelper with LoggerUtil {

  def claimQuestion(userAnswer: UserAnswers): AnswerV[UserAnswersState] =
    userAnswer.getV(ClaimPeriodQuestionPage) map {
      case ClaimOnSamePeriod      => UserAnswersState(userAnswer, userAnswer)
      case ClaimOnDifferentPeriod => UserAnswersState(userAnswer.copy(data = Json.obj()), userAnswer)
    }

  def updateJourney(userAnswer: UserAnswers): AnswerV[UserAnswersState] =
    userAnswer.getV(ClaimPeriodQuestionPage) match {
      case Valid(ClaimOnSamePeriod)      => processFurloughQuestion(UserAnswersState(userAnswer, userAnswer))
      case Valid(ClaimOnDifferentPeriod) => UserAnswersState(userAnswer.copy(data = Json.obj()), userAnswer).validNec
      case inv @ Invalid(err) =>
        UserAnswers.logWarnings(err)(logger.logger)
        inv
    }

  def furloughQuestionV(answer: UserAnswers): AnswerV[UserAnswersState] =
    answer.getV(FurloughPeriodQuestionPage) match {
      case Valid(FurloughedOnSamePeriod) => Valid(UserAnswersState(answer, answer))
      case Valid(FurloughedOnDifferentPeriod) =>
        (clearAllAnswers andThen keepClaimPeriod)
          .run(UserAnswersState(answer, answer))
          .toValidNec(
            GenericValidationError(
              s"Unable to clear answers while keeping pay period for $answer",
              JsError(),
              answer.data
            )
          )

      case inv @ Invalid(err) =>
        UserAnswers.logWarnings(err)(logger.logger)
        inv
    }

  private[this] def processFurloughQuestion(answer: UserAnswersState): AnswerV[UserAnswersState] =
    answer.original.getV(FurloughPeriodQuestionPage) match {
      case Valid(FurloughedOnSamePeriod) => processPayQuestionV(answer)
      case Valid(FurloughedOnDifferentPeriod) =>
        (clearAllAnswers andThen keepClaimPeriod)
          .run(answer)
          .toValidNec(
            GenericValidationError(
              s"Unable to clear answers while keeping pay period for ${answer.original}",
              JsError(),
              answer.original.data
            )
          )

      case Invalid(err) =>
        UserAnswers.logWarnings(err)(logger.logger)
        Valid(answer)
    }

  def payQuestion(answer: UserAnswers): AnswerV[UserAnswersState] =
    answer.getV(PayPeriodQuestionPage) match {
      case Valid(UseSamePayPeriod) =>
        (clearAllAnswers andThen keepClaimPeriod andThen keepFurloughPeriod andThen keepFurloughStatus andThen keepPayPeriodData)
          .run(UserAnswersState(answer, answer))
          .toValidNec(
            GenericValidationError(
              s"""Unable to clear answers while keeping pay period for $answer""",
              JsError(),
              answer.data
            )
          )

      case Valid(UseDifferentPayPeriod) =>
        (clearAllAnswers andThen keepClaimPeriod andThen keepFurloughPeriod andThen keepFurloughStatus)
          .run(UserAnswersState(answer, answer))
          .toValidNec(
            GenericValidationError(
              s"Failed to run Kleisi composition for UseDifferentPayPeriod for $answer",
              JsError(),
              answer.data
            )
          )
      case inv @ Invalid(err) =>
        UserAnswers.logWarnings(err)(logger.logger)
        inv
    }

  private[this] def processPayQuestionV(answer: UserAnswersState): AnswerV[UserAnswersState] =
    answer.original.getV(PayPeriodQuestionPage) match {
      case Valid(UseSamePayPeriod) =>
        (clearAllAnswers andThen keepClaimPeriod andThen keepFurloughPeriod andThen keepFurloughStatus andThen keepPayPeriodData)
          .run(answer)
          .toValidNec(
            GenericValidationError(
              s"Unable to clear answers using same pay period ${answer.original}",
              JsError(),
              answer.original.data
            )
          )

      case Valid(UseDifferentPayPeriod) =>
        (clearAllAnswers andThen keepClaimPeriod andThen keepFurloughPeriod andThen keepFurloughStatus)
          .run(answer)
          .toValidNec(
            GenericValidationError(
              s"Unable to clear answers using different pay period ${answer.original}",
              JsError(),
              answer.original.data
            )
          )

      case Invalid(err) =>
        UserAnswers.logWarnings(err)(logger.logger)
        Valid(answer)
    }

  private val keepClaimPeriod: Kleisli[Option, UserAnswersState, UserAnswersState] = Kleisli(answersState =>
    for {
      start     <- extractClaimPeriodStartV(answersState.original).toOption
      end       <- extractClaimPeriodEndV(answersState.original).toOption
      withStart <- answersState.updated.set(ClaimPeriodStartPage, start).toOption
      withEnd   <- withStart.set(ClaimPeriodEndPage, end).toOption
    } yield UserAnswersState(withEnd, answersState.original)
  )

  private val keepFurloughPeriod: Kleisli[Option, UserAnswersState, UserAnswersState] = Kleisli(answersState =>
    for {
      furlough  <- extractFurloughPeriodDatesV(answersState.original).toOption
      withStart <- answersState.updated.set(FurloughStartDatePage, furlough.start).toOption
      withEnd   <- withStart.set(FurloughEndDatePage, furlough.end).toOption
    } yield UserAnswersState(withEnd, answersState.original)
  )

  private val keepPayPeriod: Kleisli[Option, UserAnswersState, UserAnswersState] = Kleisli(answersState =>
    addPayDates(answersState.updated, answersState.original.getList(PayDatePage).toList).toOption
      .map(payPeriods => UserAnswersState(payPeriods, answersState.original))
  )

  private val keepPaymentFrequency: Kleisli[Option, UserAnswersState, UserAnswersState] = Kleisli(answersState =>
    for {
      frequency     <- extractPaymentFrequencyV(answersState.original).toOption
      withFrequency <- answersState.updated.set(PaymentFrequencyPage, frequency).toOption
    } yield UserAnswersState(withFrequency, answersState.original)
  )

  private val keepLastPayDate: Kleisli[Option, UserAnswersState, UserAnswersState] = Kleisli(answersState =>
    extractLastPayDateV(answersState.original).toOption match {
      case Some(date) =>
        for {
          withLastPayDate <- answersState.updated.set(LastPayDatePage, date).toOption
        } yield UserAnswersState(withLastPayDate, answersState.original)
      case None => Some(UserAnswersState(answersState.updated, answersState.original))
    }
  )

  private val keepFurloughStatus: Kleisli[Option, UserAnswersState, UserAnswersState] = Kleisli(answersState =>
    for {
      status     <- extractFurloughStatusV(answersState.original).toOption
      withStatus <- answersState.updated.set(FurloughStatusPage, status).toOption
    } yield UserAnswersState(withStatus, answersState.original)
  )

  private val keepPayPeriodData = keepPaymentFrequency andThen keepPayPeriod andThen keepLastPayDate

  private val clearAllAnswers: Kleisli[Option, UserAnswersState, UserAnswersState] =
    Kleisli(answersState => Option(answersState.modify(_.updated.data).setTo(Json.obj())))
}

final case class UserAnswersState(updated: UserAnswers, original: UserAnswers)
