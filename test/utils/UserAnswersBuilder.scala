/*
 * Copyright 2021 HM Revenue & Customs
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

package utils

import base.CoreTestDataBuilder
import models.Amount._
import models.EmployeeStarted.{After1Feb2019, OnOrBefore1Feb2019}
import models.FurloughStatus.FurloughOngoing
import models.NicCategory.Payable
import models.PayMethod.Regular
import models.PensionStatus.DoesContribute
import models.TopUpStatus.ToppedUp
import models._
import pages.{PartTimeQuestionPage, QuestionPage, TopUpPeriodsPage, _}
import play.api.libs.json.Writes
import queries.Settable

import java.time.LocalDate
import scala.annotation.tailrec

trait UserAnswersBuilder extends CoreTestDataBuilder {

  implicit class UserAnswerBuilder(userAnswers: UserAnswers) {

    def withNiCategory(category: NicCategory = Payable): UserAnswers =
      userAnswers.setValue(NicCategoryPage, category)

    def withPensionStatus(status: PensionStatus = DoesContribute): UserAnswers =
      userAnswers.setValue(PensionStatusPage, status)

    def withFurloughStatus(status: FurloughStatus = FurloughOngoing): UserAnswers =
      userAnswers.setValue(FurloughStatusPage, status)

    def withFurloughStartDate(startDate: LocalDate): UserAnswers =
      userAnswers.setValue(FurloughStartDatePage, startDate)
    def withFurloughStartDate(startDate: String): UserAnswers = withFurloughStartDate(startDate.toLocalDate)

    def withFurloughEndDate(endDate: LocalDate): UserAnswers =
      userAnswers.setValue(FurloughEndDatePage, endDate)
    def withFurloughEndDate(endDate: String): UserAnswers = withFurloughEndDate(endDate.toLocalDate)

    def withFurloughInLastTaxYear(answer: Boolean): UserAnswers =
      userAnswers.setValue(FurloughInLastTaxYearPage, answer)

    def withEmployeeStartDate(startDate: LocalDate): UserAnswers =
      userAnswers.setValue(EmployeeStartDatePage, startDate)
    def withEmployeeStartDate(startDate: String): UserAnswers = withEmployeeStartDate(startDate.toLocalDate)

    def withFirstFurloughDate(date: LocalDate): UserAnswers =
      userAnswers.setValue(FirstFurloughDatePage, date)
    def withFirstFurloughDate(date: String): UserAnswers = withFirstFurloughDate(date.toLocalDate)

    def withLastPayDate(date: LocalDate): UserAnswers =
      userAnswers.setValue(LastPayDatePage, date)
    def withLastPayDate(date: String): UserAnswers = withLastPayDate(date.toLocalDate)

    def withClaimPeriodEnd(date: LocalDate): UserAnswers =
      userAnswers.setValue(ClaimPeriodEndPage, date)
    def withClaimPeriodEnd(date: String): UserAnswers = withClaimPeriodEnd(date.toLocalDate)

    def withClaimPeriodStart(date: LocalDate): UserAnswers =
      userAnswers.setValue(ClaimPeriodStartPage, date)
    def withClaimPeriodStart(date: String): UserAnswers = withClaimPeriodStart(date.toLocalDate)

    def withPayMethod(method: PayMethod = Regular): UserAnswers =
      userAnswers.setValue(PayMethodPage, method)

    def withRegularLengthEmployed(res: RegularLengthEmployed = RegularLengthEmployed.Yes): UserAnswers =
      userAnswers.setValue(RegularLengthEmployedPage, res)

    def withOnPayrollBefore30thOct2020(res: Boolean = true): UserAnswers =
      userAnswers.setValue(OnPayrollBefore30thOct2020Page, res)

    def withPaymentFrequency(frequency: PaymentFrequency): UserAnswers =
      userAnswers.setValue(PaymentFrequencyPage, frequency)

    def withVariableLengthEmployed(answer: EmployeeStarted): UserAnswers =
      userAnswers.setValue(EmployeeStartedPage, answer)

    def withRegularPayAmount(salary: BigDecimal): UserAnswers =
      userAnswers.setValue(RegularPayAmountPage, Salary(salary))

    def withPartialPayBeforeFurlough(pay: BigDecimal): UserAnswers =
      userAnswers.setValue(PartialPayBeforeFurloughPage, FurloughPartialPay(pay))

    def withPartialPayAfterFurlough(pay: BigDecimal): UserAnswers =
      userAnswers.setValue(PartialPayAfterFurloughPage, FurloughPartialPay(pay))

    def withAnnualPayAmount(gross: BigDecimal): UserAnswers =
      userAnswers.setValue(AnnualPayAmountPage, AnnualPayAmount(gross))

    def withRtiSubmission(value: EmployeeRTISubmission): UserAnswers =
      userAnswers.setValue(EmployeeRTISubmissionPage, value)

    def withEmployeeStartedOnOrBefore1Feb2019(): UserAnswers =
      userAnswers.setValue(EmployeeStartedPage, OnOrBefore1Feb2019)

    def withEmployeeStartedAfter1Feb2019(): UserAnswers =
      userAnswers.setValue(EmployeeStartedPage, After1Feb2019)

    def withToppedUpStatus(status: TopUpStatus = ToppedUp): UserAnswers =
      userAnswers.setValue(TopUpStatusPage, status)

    def withAdditionalPaymentPeriods(dates: List[String]): UserAnswers =
      userAnswers.setValue(AdditionalPaymentPeriodsPage, dates.map(_.toLocalDate))

    def withAdditionalPaymentStatus(status: AdditionalPaymentStatus): UserAnswers =
      userAnswers.setValue(AdditionalPaymentStatusPage, status)

    def withPartTimePeriods(periods: List[Periods]): UserAnswers =
      userAnswers.setValue(PartTimePeriodsPage, periods)

    def withPartTimeQuestion(question: PartTimeQuestion): UserAnswers =
      userAnswers.setValue(PartTimeQuestionPage, question)

    def withAdditionalPaymentAmount(payment: AdditionalPayment, idx: Option[Int]): UserAnswers =
      userAnswers.setValue(AdditionalPaymentAmountPage, payment, idx)

    def withTopUpPeriods(periods: List[TopUpPeriod]): UserAnswers =
      userAnswers.setValue(TopUpPeriodsPage, periods)

    def withTopUpAmount(payment: TopUpPayment, idx: Option[Int]): UserAnswers =
      userAnswers.setValue(TopUpAmountPage, payment, idx)

    def withClaimPeriodQuestion(claim: ClaimPeriodQuestion): UserAnswers =
      userAnswers.setValue(ClaimPeriodQuestionPage, claim)

    def withFurloughPeriodQuestion(furlough: FurloughPeriodQuestion): UserAnswers =
      userAnswers.setValue(FurloughPeriodQuestionPage, furlough)

    def withPayPeriodQuestion(pay: PayPeriodQuestion): UserAnswers =
      userAnswers.setValue(PayPeriodQuestionPage, pay)

    def withPayPeriodsList(answer: PayPeriodsList = PayPeriodsList.Yes) =
      userAnswers.setValue(PayPeriodsListPage, answer)

    def withPreviousFurloughedPeriodsAnswer(answer: Boolean): UserAnswers =
      userAnswers.setValue(PreviousFurloughPeriodsPage, answer)

    def withPayDate(dates: List[String]): UserAnswers =
      withListOfValues[String](dates, PayDatePage)

    def withLastYear(payments: List[(String, Int)]): UserAnswers =
      withListOfValues[(String, Int)](payments, LastYearPayPage)

    def withListOfValues[A](payments: List[A], page: QuestionPage[_]): UserAnswers = {
      val zipped: List[(A, Int)] = payments.zip(1 to payments.length)

      @tailrec
      def rec(userAnswers: UserAnswers, payments: List[(A, Int)]): UserAnswers =
        (payments, page) match {
          case (Nil, _) => userAnswers
          case (((d: String, amount: Int), idx) :: tail, LastYearPayPage) =>
            rec(userAnswers.setValue(LastYearPayPage, LastYearPayment(d.toLocalDate, amount.toAmount), Some(idx)), tail)
          case ((d: String, idx) :: tail, PayDatePage) =>
            rec(userAnswers.setValue(PayDatePage, d.toLocalDate, Some(idx)), tail)
          case (_, _) => userAnswers
        }
      rec(userAnswers, zipped)
    }

    def withStatutoryLeaveDays(days: Int): UserAnswers            = userAnswers.setValue(NumberOfStatLeaveDaysPage, days)
    def withStatutoryLeaveAmount(amount: BigDecimal): UserAnswers = userAnswers.setValue(StatutoryLeavePayPage, Amount(amount))
  }

  private implicit class UserAnswersHelper(val userAnswers: UserAnswers) {
    def setValue[A](page: Settable[A], value: A, idx: Option[Int] = None)(implicit writes: Writes[A]): UserAnswers =
      userAnswers.set(page, value, idx).success.value
  }
}
