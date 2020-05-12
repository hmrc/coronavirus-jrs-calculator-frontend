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

import java.util.UUID

import base.CoreTestDataBuilder
import models.Amount._
import models.EmployeeStarted.{After1Feb2019, OnOrBefore1Feb2019}
import models.FurloughStatus.{FurloughEnded, FurloughOngoing}
import models.FurloughTopUpStatus.{NotToppedUp, ToppedUp}
import models.NicCategory.{Nonpayable, Payable}
import models.PayMethod.{Regular, Variable}
import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}
import models.PensionStatus.{DoesContribute, DoesNotContribute}
import models.{CylbPayment, FurloughPartialPay, PaymentFrequency, Salary, UserAnswers, VariableGrossPay}
import pages._
import play.api.libs.json.Json

import scala.annotation.tailrec

trait CoreTestData {

  val coreDataBuilder = new CoreTestDataBuilder {}
  import coreDataBuilder._

  val userAnswersId = "id"
  def dummyUserAnswers = userAnswersJson()
  def emptyUserAnswers = UserAnswers(userAnswersId, Json.obj())

  def userAnswersJson(): UserAnswers =
    emptyUserAnswers
      .withFurloughStartDate("2020-03-01")
      .withLastPayDate("2020-04-20")
      .withOngoingFurlough
      .withRegularPayMethod
      .withSalary(2000.0)
      .withPaymentFrequency(Monthly)
      .withPension
      .withNi
      .withClaimPeriodStart("2020-03-01")
      .withClaimPeriodEnd("2020-04-30")
      .withPayDate(List("2020-02-29", "2020-03-31", "2020-04-30"))

  lazy val answersWithPartialPeriod: UserAnswers =
    emptyUserAnswers
      .withFurloughStartDate("2020-03-10")
      .withOngoingFurlough
      .withPaymentFrequency(Monthly)
      .withSalary(3500)
      .withRegularPayMethod
      .withClaimPeriodStart("2020-03-01")
      .withClaimPeriodEnd("2020-03-31")
      .withPension
      .withNi
      .withLastPayDate("2020-03-31")
      .withPayDate(List("2020-02-29", "2020-03-31"))

  lazy val variableMonthlyPartial: UserAnswers =
    emptyUserAnswers.withEndedFurlough.withEmployeeStartedAfter1Feb2019
      .withFurloughNotToppedUp()
      .withFurloughStartDate("2020-03-10")
      .withFurloughEndDate("2020-04-20")
      .withEmployeeStartDate("2019-12-01")
      .withPaymentFrequency(Monthly)
      .withClaimPeriodStart("2020-03-01")
      .withVariablePayMethod
      .withVariableGrossPay(10000.0)
      .withPartialPayBeforeFurlough(1000.0)
      .withPartialPayAfterFurlough(800.0)
      .withLastPayDate("2020-04-20")
      .withClaimPeriodStart("2020-03-10")
      .withClaimPeriodEnd("2020-04-30")
      .withNi
      .withPension
      .withPayDate(List("2020-02-29", "2020-03-31", "2020-04-30"))

  lazy val variableAveragePartial: UserAnswers =
    emptyUserAnswers.withOngoingFurlough
      .withVariableGrossPay(12960.0)
      .withEmployeeStartedAfter1Feb2019
      .withEmployeeStartDate("2019-08-01")
      .withPaymentFrequency(Monthly)
      .withClaimPeriodStart("2020-03-01")
      .withClaimPeriodEnd("2020-03-31")
      .withFurloughNotToppedUp
      .withLastPayDate("2020-03-31")
      .withPartialPayBeforeFurlough(280.0)
      .withFurloughStartDate("2020-03-05")
      .withVariablePayMethod
      .withNi
      .withPension
      .withPayDate(List("2020-02-29", "2020-03-31"))

  def variableWeekly(lastPayDate: String = "2020-03-21"): UserAnswers =
    emptyUserAnswers.withEndedFurlough.withVariablePayMethod
      .withVariableGrossPay(10000.0)
      .withEmployeeStartedAfter1Feb2019
      .withEmployeeStartDate("2019-12-01")
      .withFurloughStartDate("2020-03-10")
      .withFurloughEndDate("2020-03-21")
      .withPaymentFrequency(Weekly)
      .withClaimPeriodStart("2020-03-01")
      .withClaimPeriodEnd("2020-03-21")
      .withFurloughNotToppedUp()
      .withNi
      .withPension
      .withLastPayDate(lastPayDate)
      .withPayDate(List("2020-02-29", "2020-03-07", "2020-03-14", "2020-03-21"))

  lazy val variableFortnightly: UserAnswers =
    emptyUserAnswers.withEndedFurlough.withVariablePayMethod
      .withPaymentFrequency(FortNightly)
      .withVariableGrossPay(10000.0)
      .withEmployeeStartedAfter1Feb2019
      .withEmployeeStartDate("2019-12-01")
      .withFurloughStartDate("2020-03-10")
      .withFurloughEndDate("2020-03-21")
      .withClaimPeriodStart("2020-03-01")
      .withClaimPeriodEnd("2020-03-21")
      .withLastPayDate("2020-03-28")
      .withFurloughNotToppedUp
      .withNi
      .withPension
      .withPayDate(List("2020-02-29", "2020-03-14", "2020-03-28"))

  lazy val variableFourweekly: UserAnswers =
    emptyUserAnswers.withEndedFurlough
      .withVariablePayMethod()
      .withVariableGrossPay(10000.0)
      .withEmployeeStartedAfter1Feb2019
      .withPaymentFrequency(FourWeekly)
      .withEmployeeStartDate("2019-12-01")
      .withFurloughEndDate("2020-04-26")
      .withClaimPeriodStart("2020-03-01")
      .withClaimPeriodEnd("2020-03-21")
      .withLastPayDate("2020-04-25")
      .withFurloughStartDate("2020-03-10")
      .withNi
      .withPension
      .withFurloughNotToppedUp
      .withPayDate(List("2020-02-29", "2020-03-28", "2020-04-25"))

  lazy val manyPeriods =
    template("""
               |    "data" : {
               |        "furloughStatus" : "ended",
               |        "variableGrossPay" : {
               |            "amount" : 31970
               |        },
               |        "employeeStarted" : "onOrBefore1Feb2019",
               |        "furloughEndDate" : "2020-03-31",
               |        "paymentFrequency" : "weekly",
               |        "claimPeriodStart" : "2020-03-01",
               |        "furloughTopUpStatus" : "notToppedUp",
               |        "lastPayDate" : "2020-03-31",
               |        "PartialPayBeforeFurlough" : {
               |            "value" : 200
               |        },
               |        "furloughStartDate" : "2020-03-01",
               |        "payMethod" : "variable",
               |        "pensionStatus" : "doesContribute",
               |        "claimPeriodEnd" : "2020-03-31",
               |        "nicCategory" : "payable"
               |    }""".stripMargin)

  implicit class UserAnswerBuilder(userAnswers: UserAnswers) {

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
  }

  private def template(data: String): UserAnswers =
    Json.parse(s"""{
                  |    "_id" : "session-${UUID.randomUUID().toString}",
                  |    $data,
                  |  "lastUpdated": {
                  |    "$$date": 1586873457650
                  |  }
                  |}""".stripMargin).as[UserAnswers]
}
