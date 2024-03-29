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
import models.UserAnswers.AnswerV
import models.{FurloughCalculationResult, UserAnswers}
import services.{FurloughCalculator, ReferencePayCalculator}

trait FurloughCalculationHandler
    extends FurloughCalculator with ReferencePayCalculator with JourneyBuilder with LastYearPayControllerRequestHandler {

  def handleCalculationFurloughV(userAnswers: UserAnswers): AnswerV[FurloughCalculationResult] =
    extractBranchingQuestionsV(userAnswers) match {
      case Valid(questions) =>
        journeyDataV(define(questions, dynamicCylbCutoff(userAnswers)), userAnswers).map { data =>
          val payments = calculateReferencePay(data)
          calculateFurloughGrant(data.frequency, payments)
        }

      case inv @ Invalid(e) => inv
    }
}
