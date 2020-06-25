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

package forms

import java.time.LocalDate

import config.FrontendAppConfig
import forms.mappings.Mappings
import javax.inject.Inject
import models.Period
import play.api.data.Form
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationResult}
import utils.ImplicitDateFormatter
import views.ViewUtils

class ClaimPeriodEndFormProvider @Inject()(appConfig: FrontendAppConfig) extends Mappings with ImplicitDateFormatter {

  def apply(claimStart: LocalDate): Form[LocalDate] =
    Form("endDate" -> localDate(invalidKey = "claimPeriodEnd.error.invalid").verifying(validEndDate(claimStart)))

  private def validEndDate(claimStart: LocalDate): Constraint[LocalDate] = Constraint { claimEndDate =>
    (
      isBeforeStart(claimStart, claimEndDate),
      isDifferentCalendarMonth(claimStart, claimEndDate),
      isClaimLessThan7Days(claimStart, claimEndDate),
      isMoreThan14daysInFuture(claimStart, claimEndDate),
      isAfterPolicyEnd(claimEndDate),
    ) match {
      case (r @ Invalid(_), _, _, _, _) => r
      case (_, r @ Invalid(_), _, _, _) => r
      case (_, _, r @ Invalid(_), _, _) => r
      case (_, _, _, r @ Invalid(_), _) => r
      case (_, _, _, _, r @ Invalid(_)) => r
      case _                            => Valid
    }
  }

  val isMoreThan14daysInFuture: (LocalDate, LocalDate) => ValidationResult = (start, end) => {
    if (start.isBefore(appConfig.phaseTwoStartDate)) {
      Valid
    } else if (start.isAfter(appConfig.phaseTwoStartDate) && end.isAfter(LocalDate.now().plusDays(14))) {
      Invalid("claimPeriodEnd.cannot.be.after.14days")
    } else {
      Valid
    }

  }

  val isDifferentCalendarMonth: (LocalDate, LocalDate) => ValidationResult = (start, end) =>
    if (start.isBefore(appConfig.phaseTwoStartDate) && !end.isBefore(appConfig.phaseTwoStartDate)) {
      Invalid("claimPeriodEnd.cannot.be.after.july")
    } else if (start.getMonthValue >= 7 && start.getMonthValue != end.getMonthValue)
      Invalid("claimPeriodEnd.cannot.be.of.same.month")
    else Valid

  private val isBeforeStart: (LocalDate, LocalDate) => ValidationResult = (start, end) =>
    if (end.isBefore(start)) Invalid("claimPeriodEnd.cannot.be.before.claimStart") else Valid

  private val isAfterPolicyEnd: LocalDate => ValidationResult = end => {
    val schemaEndDate = appConfig.schemeEndDate
    if (end.isAfter(schemaEndDate)) {
      Invalid("claimPeriodEnd.cannot.be.after.policyEnd", ViewUtils.dateToString(schemaEndDate))
    } else Valid
  }

  private val isClaimLessThan7Days: (LocalDate, LocalDate) => ValidationResult = (start, end) =>
    if (start.isAfter(appConfig.phaseTwoStartDate.minusDays(1))) {
      if (end.getDayOfMonth != end.getMonth.maxLength() && Period(start, end).countDays < 7) {
        Invalid("claimPeriodEnd.cannot.be.lessThan.7days")
      } else {
        Valid
      }
    } else {
      Valid
  }
}
