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

package utils

import base.CoreTestDataBuilder
import models.EmployeeStarted.{After1Feb2019, OnOrBefore1Feb2019}
import models.FurloughStatus.{FurloughEnded, FurloughOngoing}
import models.FurloughTopUpStatus.{NotToppedUp, ToppedUp}
import models.NicCategory.{Nonpayable, Payable}
import models.PayMethod.{Regular, Variable}
import models.Amount._
import models.PensionStatus.{DoesContribute, DoesNotContribute}
import models.{CylbPayment, FurloughPartialPay, PaymentFrequency, Salary, UserAnswers, VariableGrossPay}
import pages.{ClaimPeriodEndPage, ClaimPeriodStartPage, EmployedStartedPage, EmployeeStartDatePage, FurloughEndDatePage, FurloughStartDatePage, FurloughStatusPage, FurloughTopUpStatusPage, LastPayDatePage, LastYearPayPage, NicCategoryPage, PartialPayAfterFurloughPage, PartialPayBeforeFurloughPage, PayDatePage, PayMethodPage, PaymentFrequencyPage, PensionStatusPage, SalaryQuestionPage, VariableGrossPayPage}

import scala.annotation.tailrec

trait UserAnswersBuilder extends CoreTestDataBuilder {

  implicit class UserAnswerBuilder(userAnswers: UserAnswers) {

    def withNi: UserAnswers =
      userAnswers.setValue(NicCategoryPage, Payable)

    def withNoNi: UserAnswers =
      userAnswers.setValue(NicCategoryPage, Nonpayable)

    def withNoPension: UserAnswers =
      userAnswers.setValue(PensionStatusPage, DoesNotContribute)

    def withPension: UserAnswers =
      userAnswers.setValue(PensionStatusPage, DoesContribute)

    def withEndedFurlough: UserAnswers =
      userAnswers.setValue(FurloughStatusPage, FurloughEnded)

    def withOngoingFurlough: UserAnswers =
      userAnswers.setValue(FurloughStatusPage, FurloughOngoing)

    def withFurloughStartDate(startDate: String): UserAnswers =
      userAnswers.setValue(FurloughStartDatePage, buildLocalDate(periodBuilder(startDate)))

    def withFurloughEndDate(startDate: String): UserAnswers =
      userAnswers.setValue(FurloughEndDatePage, buildLocalDate(periodBuilder(startDate)))

    def withEmployeeStartDate(startDate: String): UserAnswers =
      userAnswers.setValue(EmployeeStartDatePage, buildLocalDate(periodBuilder(startDate)))

    def withLastPayDate(date: String): UserAnswers =
      userAnswers.setValue(LastPayDatePage, buildLocalDate(periodBuilder(date)))

    def withClaimPeriodEnd(date: String): UserAnswers =
      userAnswers.setValue(ClaimPeriodEndPage, buildLocalDate(periodBuilder(date)))

    def withClaimPeriodStart(date: String): UserAnswers =
      userAnswers.setValue(ClaimPeriodStartPage, buildLocalDate(periodBuilder(date)))

    def withRegularPayMethod(): UserAnswers =
      userAnswers.setValue(PayMethodPage, Regular)

    def withVariablePayMethod(): UserAnswers =
      userAnswers.setValue(PayMethodPage, Variable)

    def withPaymentFrequency(frequency: PaymentFrequency): UserAnswers =
      userAnswers.setValue(PaymentFrequencyPage, frequency)

    def withSalary(salary: BigDecimal): UserAnswers =
      userAnswers.setValue(SalaryQuestionPage, Salary(salary))

    def withPartialPayBeforeFurlough(pay: BigDecimal): UserAnswers =
      userAnswers.setValue(PartialPayBeforeFurloughPage, FurloughPartialPay(pay))

    def withPartialPayAfterFurlough(pay: BigDecimal): UserAnswers =
      userAnswers.setValue(PartialPayAfterFurloughPage, FurloughPartialPay(pay))

    def withVariableGrossPay(gross: BigDecimal): UserAnswers =
      userAnswers.setValue(VariableGrossPayPage, VariableGrossPay(gross))

    def withEmployeeStartedOnOrBefore1Feb2019(): UserAnswers =
      userAnswers.setValue(EmployedStartedPage, OnOrBefore1Feb2019)

    def withEmployeeStartedAfter1Feb2019(): UserAnswers =
      userAnswers.setValue(EmployedStartedPage, After1Feb2019)

    def withFurloughToppedUp(): UserAnswers =
      userAnswers.setValue(FurloughTopUpStatusPage, ToppedUp)

    def withFurloughNotToppedUp(): UserAnswers =
      userAnswers.setValue(FurloughTopUpStatusPage, NotToppedUp)

    def withPayDate(dates: List[String]): UserAnswers = {
      val zipped: List[(String, Int)] = dates.zip(1 to dates.length)

      @tailrec
      def rec(userAnswers: UserAnswers, dates: List[(String, Int)]): UserAnswers =
        dates match {
          case Nil => userAnswers
          case (d, idx) :: tail =>
            rec(
              userAnswers
                .setListWithInvalidation(PayDatePage, buildLocalDate(periodBuilder(d)), idx)
                .get,
              tail)
        }

      rec(userAnswers, zipped)
    }

    def withLastYear(payments: List[(String, Int)]): UserAnswers = {
      val zipped: List[((String, Int), Int)] = payments.zip(1 to payments.length)

      @tailrec
      def rec(userAnswers: UserAnswers, payments: List[((String, Int), Int)]): UserAnswers =
        payments match {
          case Nil => userAnswers
          case ((d, amount), idx) :: tail =>
            rec(
              userAnswers
                .setListWithInvalidation(LastYearPayPage, CylbPayment(buildLocalDate(periodBuilder(d)), amount.toAmount), idx)
                .get,
              tail)
        }

      rec(userAnswers, zipped)
    }
  }
}
