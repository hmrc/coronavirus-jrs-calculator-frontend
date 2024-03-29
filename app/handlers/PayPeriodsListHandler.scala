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

import cats.data.Validated.{Invalid, Valid}
import cats.syntax.apply._
import models.UserAnswers.AnswerV
import models.{Period, Periods, UserAnswers}
import pages.PayDatePage
import utils.LoggerUtil

trait PayPeriodsListHandler extends DataExtractor with LoggerUtil {

  def extractPayPeriods(userAnswers: UserAnswers): Seq[Periods] =
    userAnswers.getList(PayDatePage) match {
      case Nil => Seq.empty
      case endDates =>
        extractFurloughWithinClaimV(userAnswers) match {
          case Valid(furlough) =>
            generatePeriodsWithFurlough(endDates, furlough)
          case Invalid(errors) =>
            UserAnswers.logWarnings(errors)(logger.logger)
            Seq.empty
        }
    }

  def extractClaimPeriod(userAnswers: UserAnswers): AnswerV[Period] =
    (
      extractClaimPeriodStartV(userAnswers),
      extractClaimPeriodEndV(userAnswers)
    ).mapN(Period.apply)

}
