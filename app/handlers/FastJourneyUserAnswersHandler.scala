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

import java.time.LocalDate

import models.ClaimPeriodQuestion.{ClaimOnDifferentPeriod, ClaimOnSamePeriod}
import models.FurloughPeriodQuestion.{FurloughedOnDifferentPeriod, FurloughedOnSamePeriod}
import models.PayPeriodQuestion.{UseDifferentPayPeriod, UseSamePayPeriod}
import models.UserAnswers
import pages._
import play.api.libs.json.Json

import scala.annotation.tailrec
import scala.util.Try

trait FastJourneyUserAnswersHandler {

  def updateJourney(userAnswer: UserAnswers): Option[UserAnswers] =
    userAnswer.get(ClaimPeriodQuestionPage) flatMap {
      case ClaimOnSamePeriod      => updateWithFurloughQuestion(AnswerState(userAnswer, userAnswer)).map(_.updated)
      case ClaimOnDifferentPeriod => Some(userAnswer.copy(data = Json.obj()))
    }


  private def updateWithFurloughQuestion(answer: AnswerState): Option[AnswerState] =
    answer.original.get(FurloughPeriodQuestionPage) match {
      case Some(FurloughedOnSamePeriod)      => updateWithPayQuestion(answer)
      case Some(FurloughedOnDifferentPeriod) => keepClaimPeriod(Try(answer)).toOption
      case None => Some(answer)
    }

  private def updateWithPayQuestion(answer: AnswerState): Option[AnswerState] =
    answer.original.get(PayPeriodQuestionPage) match {
      case Some(UseSamePayPeriod) =>
        (keepClaimPeriod andThen keepFurloughPeriod andThen keepPayPeriod)(Try(answer)).toOption
      case Some(UseDifferentPayPeriod) =>
        (keepClaimPeriod andThen keepFurloughPeriod)(Try(answer)).toOption
      case None => Some(answer)
    }

  val keepClaimPeriod: Try[AnswerState] => Try[AnswerState] = currentAnswersState =>
    for {
      current    <- currentAnswersState
      newAnswers <- currentAnswersState.map(_.original.copy(data = Json.obj()))
      start      <- Try(current.original.get(ClaimPeriodStartPage).get)
      end        <- Try(current.original.get(ClaimPeriodEndPage).get)
      withStart  <- newAnswers.set(ClaimPeriodStartPage, start)
      withEnd    <- withStart.set(ClaimPeriodEndPage, end)
    } yield AnswerState(withEnd, current.original)

  val keepFurloughPeriod: Try[AnswerState] => Try[AnswerState] = currentAnswersState =>
    for {
      current    <- currentAnswersState
      newAnswers <- currentAnswersState.map(_.updated)
      start      <- Try(current.original.get(FurloughStartDatePage).get)
      end        <- Try(current.original.get(FurloughEndDatePage).get)
      withStart  <- newAnswers.set(FurloughStartDatePage, start)
      withEnd    <- withStart.set(FurloughEndDatePage, end)
    } yield AnswerState(withEnd, current.original)

  val keepPayPeriod: Try[AnswerState] => Try[AnswerState] = currentAnswersState =>
    for {
      current    <- currentAnswersState
      newAnswers <- currentAnswersState.map(_.updated)
      payPeriod  <- Try(withListOfValues(newAnswers, current.original.getList(PayDatePage).toList))
    } yield AnswerState(payPeriod, current.original)

  private def withListOfValues(userAnswers: UserAnswers, answers: List[LocalDate]): UserAnswers = {
    val zipped: List[(LocalDate, Int)] = answers.zip(1 to answers.length)

    @tailrec
    def rec(userAnswers: UserAnswers, payments: List[(LocalDate, Int)]): UserAnswers =
      payments match {
        case Nil => userAnswers
        case (d: LocalDate, idx) :: tail =>
          rec(userAnswers.setListWithInvalidation(PayDatePage, d, idx).get, tail)
      }
    rec(userAnswers, zipped)
  }
}

case class AnswerState(updated: UserAnswers, original: UserAnswers)
