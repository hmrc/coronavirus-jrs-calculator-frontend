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
import java.time.temporal.ChronoUnit

import config.FrontendAppConfig
import forms.mappings.Mappings
import javax.inject.Inject
import models.Period
import play.api.data.Form
import play.api.data.validation.{Constraint, Invalid, Valid}
import utils.ImplicitDateFormatter
import views.ViewUtils

class ClaimPeriodEndFormProvider @Inject()(appConfig: FrontendAppConfig) extends Mappings with ImplicitDateFormatter {

  def apply(claimStart: LocalDate): Form[LocalDate] =
    Form("endDate" -> localDate(invalidKey = "claimPeriodEnd.error.invalid").verifying(validEndDate(claimStart)))

  private def validEndDate(claimStart: LocalDate): Constraint[LocalDate] = Constraint { claimEndDate =>
    if (claimEndDate.isBefore(claimStart)) {
      Invalid("claimPeriodEnd.cannot.be.before.claimStart")
    } else if (claimEndDate.isAfter(appConfig.schemeEndDate)) {
      Invalid("claimPeriodEnd.cannot.be.after.policyEnd", ViewUtils.dateToString(appConfig.schemeEndDate))
    } else if (claimStart.isAfter(appConfig.phaseTwoStartDate.minusDays(1)) && Period(claimStart, claimEndDate).countDays < 7) {
      Invalid("claimPeriodEnd.cannot.be.lessThan.7days")
    } else {
      Valid
    }
  }
}
