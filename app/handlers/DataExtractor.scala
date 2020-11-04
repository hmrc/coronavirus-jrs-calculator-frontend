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

import cats.data.Validated.{Invalid, Valid}
import models.UserAnswers.AnswerV
import models._
import pages._
import services.{FurloughPeriodExtractor, PeriodHelper}
import cats.syntax.apply._
import cats.syntax.validated._
import cats.syntax.semigroupk._

trait DataExtractor extends FurloughPeriodExtractor with PeriodHelper {

  def extractPriorFurloughPeriodV(userAnswers: UserAnswers): AnswerV[Period] = {
    val default = LocalDate.of(2019, 4, 6)

    (
      userAnswers.getV(FurloughStartDatePage),
      userAnswers.getV(EmployeeStartDatePage) <+> default.validNec[AnswerValidation],
      userAnswers.getV(ClaimPeriodStartPage)
    ).mapN { (furloughStart, employeeStartDate, claimStart) =>
      endDateOrTaxYearEnd(Period(employeeStartDate, furloughStart.minusDays(1)), claimStart)
    }
  }

  def extractNonFurloughV(userAnswers: UserAnswers): AnswerV[NonFurloughPay] = {
    val preFurloughPay = userAnswers.getV(PartialPayBeforeFurloughPage).toOption
    val postFurloughPay = userAnswers.getV(PartialPayAfterFurloughPage).toOption

    NonFurloughPay(
      preFurloughPay.map(v => Amount(v.value)),
      postFurloughPay.map(v => Amount(v.value))
    ).validNec
  }

  def extractBranchingQuestionsV(userAnswers: UserAnswers): AnswerV[BranchingQuestions] =
    extractPayMethodV(userAnswers).map {
      BranchingQuestions(
        _,
        userAnswers.getV(EmployeeStartedPage).toOption,
        userAnswers.getV(EmployeeStartDatePage).toOption
      )
    }

  def extractPayMethodV(userAnswers: UserAnswers): AnswerV[PayMethod] =
    userAnswers.getV(PayMethodPage)

  def extractSalaryV(userAnswers: UserAnswers): AnswerV[Amount] =
    userAnswers.getV(RegularPayAmountPage).map(v => Amount(v.amount))

  def extractAnnualPayAmountV(userAnswers: UserAnswers): AnswerV[Amount] =
    userAnswers.getV(AnnualPayAmountPage).map(v => Amount(v.amount))

  def extractCylbPayments(userAnswers: UserAnswers): Seq[LastYearPayment] =
    userAnswers.getList(LastYearPayPage)

  def extractNicCategoryV(userAnswers: UserAnswers): AnswerV[NicCategory] =
    userAnswers.getV(NicCategoryPage)

  def extractPensionStatusV(userAnswers: UserAnswers): AnswerV[PensionStatus] =
    userAnswers.getV(PensionStatusPage)

  def extractClaimPeriodStartV(userAnswers: UserAnswers): AnswerV[LocalDate] =
    userAnswers.getV(ClaimPeriodStartPage)

  def extractClaimPeriodEndV(userAnswers: UserAnswers): AnswerV[LocalDate] =
    userAnswers.getV(ClaimPeriodEndPage)

  def extractPaymentFrequencyV(userAnswers: UserAnswers): AnswerV[PaymentFrequency] =
    userAnswers.getV(PaymentFrequencyPage)

  def extractTopUpPayment(userAnswers: UserAnswers): Seq[TopUpPayment] =
    userAnswers.getList(TopUpAmountPage)

  def extractAdditionalPayment(userAnswers: UserAnswers): Seq[AdditionalPayment] =
    userAnswers.getList(AdditionalPaymentAmountPage)

  def extractLastPayDateV(userAnswers: UserAnswers): AnswerV[LocalDate] =
    userAnswers.getV(LastPayDatePage)

  def extractFurloughStatusV(userAnswers: UserAnswers): AnswerV[FurloughStatus] =
    userAnswers.getV(FurloughStatusPage)

  def extractReferencePayDataV(userAnswers: UserAnswers): AnswerV[ReferencePayData] =
    (
      extractFurloughWithinClaimV(userAnswers),
      extractPaymentFrequencyV(userAnswers)
    ).mapN { (furloughPeriod, frequency) =>
      val payDates = userAnswers.getList(PayDatePage)
      val periods = generatePeriodsWithFurlough(payDates, furloughPeriod)
      val lastPayDay = determineLastPayDay(userAnswers, periods)
      val assigned = assignPayDates(frequency, periods, lastPayDay)
      ReferencePayData(furloughPeriod, assigned, frequency)
    }

  def extractPhaseTwoReferencePayDataV(userAnswers: UserAnswers): AnswerV[PhaseTwoReferencePayData] =
    (
      extractFurloughWithinClaimV(userAnswers),
      extractPaymentFrequencyV(userAnswers)
    ).mapN { (furloughPeriod, frequency) =>
      val payDates = userAnswers.getList(PayDatePage)
      val actuals = userAnswers.getList(PartTimeHoursPage)
      val usuals: Seq[UsualHours] = userAnswers.getList(PartTimeNormalHoursPage)
      val periods = generatePeriodsWithFurlough(payDates, furloughPeriod)
      val lastPayDay = determineLastPayDay(userAnswers, periods)
      val assigned = assignPayDates(frequency, periods, lastPayDay)
      val phaseTwo: Seq[PhaseTwoPeriod] = assignPartTimeHours(assigned, actuals, usuals)

      PhaseTwoReferencePayData(furloughPeriod, phaseTwo, frequency)
    }

  def determineLastPayDay(userAnswers: UserAnswers, periods: Seq[Periods]): LocalDate =
    extractLastPayDateV(userAnswers) match {
      case Valid(date) => date
      case Invalid(_)  => periods.last.period.end
    }
}
