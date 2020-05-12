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
import models.NicCategory.Nonpayable
import models.PensionStatus.DoesNotContribute
import models.UserAnswers
import pages.{NicCategoryPage, PayDatePage, PensionStatusPage}
import play.api.libs.json.Json

import scala.annotation.tailrec

trait CoreTestData {

  val userAnswersId = "id"
  def dummyUserAnswers = userAnswersJson()
  def emptyUserAnswers = UserAnswers(userAnswersId, Json.obj())

  def userAnswersJson(
    furloughOngoing: String = "ongoing",
    furloughStartDate: String = "2020-03-01",
    furloughEndDate: String = "",
    payMethod: String = "regular",
    variableGrossPay: String = "",
    employeeStartDate: String = "",
    claimStartDate: String = "2020-03-01",
    nicCategory: String = "payable",
    pensionStatus: String = "doesContribute"): UserAnswers =
    template(s"""
                |    "data" : {
                |        "lastPayDate" : "2020-04-20",
                |        "furloughStatus" : "$furloughOngoing",
                |        "furloughStartDate" : "$furloughStartDate",
                |        "furloughEndDate" : "$furloughEndDate",
                |        "payMethod" : "$payMethod",
                |        "variableGrossPay": {
                |            "amount" : "$variableGrossPay"
                |        },
                |        "employeeStartDate": "$employeeStartDate",
                |        "pensionStatus" : "$pensionStatus",
                |        "claimPeriodEnd" : "2020-04-30",
                |        "paymentFrequency" : "monthly",
                |        "salary" : {
                |            "amount" : 2000.0
                |        },
                |        "nicCategory" : "$nicCategory",
                |        "claimPeriodStart" : "$claimStartDate"
                |    }""".stripMargin).withPayDate(
      List(
        "2020-02-29",
        "2020-03-31",
        "2020-04-30"
      ))

  lazy val answersWithPartialPeriod: UserAnswers =
    template("""
               |    "data" : {
               |        "lastPayDate" : "2020-03-31",
               |        "furloughStatus" : "ongoing",
               |        "furloughStartDate" : "2020-03-10",
               |        "payMethod" : "regular",
               |        "pensionStatus" : "doesContribute",
               |        "claimPeriodEnd" : "2020-03-31",
               |        "paymentFrequency" : "monthly",
               |        "salary" : {
               |            "amount" : 3500
               |        },
               |        "nicCategory" : "payable",
               |        "claimPeriodStart" : "2020-03-01"
               |    }""".stripMargin).withPayDate(List("2020-02-29", "2020-03-31"))

  val variableMonthlyPartial: UserAnswers =
    template("""
               |    "data" : {
               |        "furloughStatus" : "ended",
               |        "variableGrossPay" : {
               |            "amount" : 10000
               |        },
               |        "employeeStarted" : "after1Feb2019",
               |        "employeeStartDate" : "2019-12-01",
               |        "furloughEndDate" : "2020-04-20",
               |        "paymentFrequency" : "monthly",
               |        "claimPeriodStart" : "2020-03-01",
               |        "furloughTopUpStatus" : "notToppedUp",
               |        "PartialPayAfterFurlough" : {
               |            "value" : 800
               |        },
               |        "lastPayDate" : "2020-04-20",
               |        "PartialPayBeforeFurlough" : {
               |            "value" : 1000
               |        },
               |        "furloughStartDate" : "2020-03-10",
               |        "payMethod" : "variable",
               |        "pensionStatus" : "doesContribute",
               |        "claimPeriodEnd" : "2020-04-30",
               |        "nicCategory" : "payable",
               |        "payDate" : [
               |            "2020-02-29",
               |            "2020-03-31",
               |            "2020-04-30"
               |        ]
               |    }""".stripMargin)

  val variableAveragePartial: UserAnswers =
    template("""
               |    "data" : {
               |        "furloughStatus" : "ongoing",
               |        "variableGrossPay" : {
               |            "amount" : 12960
               |        },
               |        "employeeStarted" : "after1Feb2019",
               |        "employeeStartDate" : "2019-08-01",
               |        "paymentFrequency" : "monthly",
               |        "claimPeriodStart" : "2020-03-01",
               |        "furloughTopUpStatus" : "notToppedUp",
               |        "lastPayDate" : "2020-03-31",
               |        "PartialPayBeforeFurlough" : {
               |            "value" : 280
               |        },
               |        "furloughStartDate" : "2020-03-05",
               |        "payMethod" : "variable",
               |        "pensionStatus" : "doesContribute",
               |        "claimPeriodEnd" : "2020-03-31",
               |        "nicCategory" : "payable",
               |        "payDate" : [
               |            "2020-02-29",
               |            "2020-03-31"
               |        ]
               |    }""".stripMargin)

  def variableWeekly(lastPayDate: String = "2020-03-21"): UserAnswers =
    template(s"""
                |    "data" : {
                |        "furloughStatus" : "ended",
                |        "variableGrossPay" : {
                |            "amount" : 10000
                |        },
                |        "employeeStarted" : "after1Feb2019",
                |        "employeeStartDate" : "2019-12-01",
                |        "furloughEndDate" : "2020-03-21",
                |        "paymentFrequency" : "weekly",
                |        "claimPeriodStart" : "2020-03-01",
                |        "furloughTopUpStatus" : "notToppedUp",
                |        "lastPayDate" : "$lastPayDate",
                |        "furloughStartDate" : "2020-03-10",
                |        "payMethod" : "variable",
                |        "pensionStatus" : "doesContribute",
                |        "claimPeriodEnd" : "2020-03-21",
                |        "nicCategory" : "payable",
                |        "payDate" : [
                |            "2020-02-29",
                |            "2020-03-07",
                |            "2020-03-14",
                |            "2020-03-21"
                |        ]
                |    }""".stripMargin)

  val variableFortnightly: UserAnswers =
    template("""
               |    "data" : {
               |        "furloughStatus" : "ended",
               |        "variableGrossPay" : {
               |            "amount" : 10000
               |        },
               |        "employeeStarted" : "after1Feb2019",
               |        "employeeStartDate" : "2019-12-01",
               |        "furloughEndDate" : "2020-03-21",
               |        "paymentFrequency" : "fortnightly",
               |        "claimPeriodStart" : "2020-03-01",
               |        "furloughTopUpStatus" : "notToppedUp",
               |        "lastPayDate" : "2020-03-28",
               |        "furloughStartDate" : "2020-03-10",
               |        "payMethod" : "variable",
               |        "pensionStatus" : "doesContribute",
               |        "claimPeriodEnd" : "2020-03-21",
               |        "nicCategory" : "payable",
               |        "payDate" : [
               |            "2020-02-29",
               |            "2020-03-14",
               |            "2020-03-28"
               |        ]
               |    }""".stripMargin)

  val variableFourweekly: UserAnswers =
    template("""
               |    "data" : {
               |        "furloughStatus" : "ended",
               |        "variableGrossPay" : {
               |            "amount" : 10000
               |        },
               |        "employeeStarted" : "after1Feb2019",
               |        "employeeStartDate" : "2019-12-01",
               |        "furloughEndDate" : "2020-04-26",
               |        "paymentFrequency" : "fourweekly",
               |        "claimPeriodStart" : "2020-03-01",
               |        "furloughTopUpStatus" : "notToppedUp",
               |        "lastPayDate" : "2020-04-25",
               |        "furloughStartDate" : "2020-03-10",
               |        "payMethod" : "variable",
               |        "pensionStatus" : "doesContribute",
               |        "claimPeriodEnd" : "2020-03-21",
               |        "nicCategory" : "payable",
               |        "payDate" : [
               |            "2020-02-29",
               |            "2020-03-28",
               |            "2020-04-25"
               |        ]
               |    }""".stripMargin)

  val manyPeriods =
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
               |        "lastYearPay" : [
               |            {
               |                "date" : "2019-03-05",
               |                "amount" : "500"
               |            },
               |            {
               |                "date" : "2019-03-12",
               |                "amount" : "450"
               |            },
               |            {
               |                "date" : "2019-03-19",
               |                "amount" : "500"
               |            },
               |            {
               |                "date" : "2019-03-26",
               |                "amount" : "550"
               |            },
               |            {
               |                "date" : "2019-04-02",
               |                "amount" : "600"
               |            }
               |        ],
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

  val coreDataBuilder = new CoreTestDataBuilder {}
  import coreDataBuilder._

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

    def withNoNi: UserAnswers =
      userAnswers.setValue(NicCategoryPage, Nonpayable)

    def withNoPension: UserAnswers =
      userAnswers.setValue(PensionStatusPage, DoesNotContribute)
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
