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

package assets.messages

import utils.ValueFormatter

object PhaseTwoReferencePayBreakdownHelperMessages extends ValueFormatter {

  object AveragingMethod {
    val h4 = "Calculate the employee’s pay based on their furlough days"
    def p1(isType5: Boolean) =
      if (isType5)
        "Averaging method: take the employee’s total pay from 6 April 2020 (or the date the employment started, whichever is later), up to the day before the furlough started on or after 1 November 2020."
      else
        "Take the total pay from the employee’s start date (or 6 April 2019, if they started earlier than this date) to the day before the employee’s furlough start date (or 5 April 2020, whichever is earlier)."

    def numbered1(amount: BigDecimal, hasStatLeave: Boolean) =
      if (hasStatLeave)
        s"Start with ${currencyFormatter(amount)} (total pay minus the amount paid for periods of statutory leave)."
      else
        s"Start with ${currencyFormatter(amount)} (total pay)."

    def numbered2(days: Int, hasStatLeave: Boolean) =
      if (hasStatLeave)
        s"Divide by $days (days employed minus the number of days on statutory leave)."
      else
        s"Divide by $days (days employed)."

    def numbered3(furloughDays: BigDecimal) = s"Multiply by $furloughDays (furlough days)."
    def p2(amount: BigDecimal)              = s"Total pay based on furlough days = ${currencyFormatter(amount)}"
  }

  object PartTimeHours {
    val p1                                   = "Then:"
    def numbered1(amount: BigDecimal)        = s"Start with ${currencyFormatter(amount)} (total pay based on furlough days)."
    def numbered2(usualHours: BigDecimal)    = s"Divide by ${usualHours.formatted("%.2f")} (usual hours)."
    def numbered3(furloughHours: BigDecimal) = s"Multiply by ${furloughHours.formatted("%.2f")} (furlough hours)."
    def p2(amount: BigDecimal)               = s"Total pay based on hours worked in this pay period = ${currencyFormatter(amount)}"
  }

}
