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

import cats.data.Validated.Valid
import cats.syntax.apply._
import models.UserAnswers.AnswerV
import models.{Period, PeriodWithPaymentDate, UserAnswers}
import pages._
import services.PreviousYearPeriod

import java.time.LocalDate

trait LastYearPayControllerRequestHandler extends DataExtractor with PreviousYearPeriod {

  def getLastYearPeriodsForFirstPeriod(userAnswers: UserAnswers): AnswerV[Seq[Period]] =
    (
      userAnswers.getV(PaymentFrequencyPage),
      extractFurloughWithinClaimV(userAnswers)
    ).mapN { (frequency, furlough) =>
      val endDates = userAnswers.getList(PayDatePage)
      val periods  = generatePeriodsWithFurlough(endDates, furlough)
      periods.headOption.map(p => previousYearPeriod(frequency, p)).getOrElse(Seq.empty)
    }

  def getLastYearPeriods(userAnswers: UserAnswers): AnswerV[Seq[Period]] =
    (
      userAnswers.getV(PaymentFrequencyPage),
      extractFurloughWithinClaimV(userAnswers)
    ).mapN { (frequency, furlough) =>
      val endDates              = userAnswers.getList(PayDatePage)
      val periods               = generatePeriodsWithFurlough(endDates, furlough)
      val periodsWithDuplicates = periods.flatMap(p => previousYearPeriod(frequency, p))
      periodsWithDuplicates.distinct
    }

  def dynamicCylbCutoff(userAnswers: UserAnswers): LocalDate = {
    val date: AnswerV[LocalDate] = (
      userAnswers.getV(PaymentFrequencyPage),
      getPeriodsWithPaymentDateV(userAnswers)
    ).mapN { (frequency, periodsWithPayDates) =>
      cylbCutoff(frequency, periodsWithPayDates)
    }

    (userAnswers.getV(ClaimPeriodStartPage), date) match {
      case (Valid(claimStart), Valid(cutoff)) if !claimStart.isBefore(LocalDate.of(2020, 7, 1)) => cutoff
      case _                                                                                    => LocalDate.of(2019, 4, 6)
    }
  }

  private def getPeriodsWithPaymentDateV(userAnswers: UserAnswers): AnswerV[Seq[PeriodWithPaymentDate]] =
    (
      userAnswers.getV(PaymentFrequencyPage),
      extractFurloughWithinClaimV(userAnswers)
    ).mapN { (frequency, furloughPeriod) =>
      val payDates   = userAnswers.getList(PayDatePage)
      val periods    = generatePeriodsWithFurlough(payDates, furloughPeriod)
      val lastPayDay = determineLastPayDay(userAnswers, periods)

      assignPayDates(frequency, periods, lastPayDay)
    }

}
