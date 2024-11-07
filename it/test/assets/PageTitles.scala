/*
 * Copyright 2024 HM Revenue & Customs
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

package assets

object PageTitles {
  def regularPayAmount(cutOffDate: String) = s"What was the employee paid in the last pay period ending on or before ${cutOffDate}?"

  val regularLengthEmployed = "Was this employee on your payroll on or before 19 March 2020?"
  val statutoryLeavePay = "How much was this employee paid for the periods of statutory leave?"
  val claimPeriodStartDate = "What’s the start date of this claim?"
  val firstFurloughDate = "When was this employee first furloughed?"
  val previousFurloughPeriods = "Has this employee been furloughed more than once since 1 November 2020? - Job Retention Scheme calculator - GOV.UK"
  val onPayrollBefore30thOct2020 = "Was this employee on your payroll on or before 30 October 2020?"

  def hasEmployeeBeenOnStatutoryLeave(boundaryStart: String, boundaryEnd: String) =
    s"Has this employee been on statutory leave for part of the period between $boundaryStart and $boundaryEnd"

  def numberOfStatLeaveDays(boundaryStart: Option[String], boundaryEnd: String): String = {
    s"How many days was this employee on statutory leave between ${boundaryStart.fold("the day their employment started")(identity)} and $boundaryEnd?"
  }

}