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
import play.api.data.Form
import play.api.data.validation.{Constraint, Invalid, Valid}
import utils.ImplicitDateFormatter

class ClaimPeriodStartFormProvider @Inject()(appConfig: FrontendAppConfig) extends Mappings with ImplicitDateFormatter {

  def apply(): Form[LocalDate] =
    Form(
      "startDate" -> localDate(
        invalidKey = "claimPeriodStart.error.invalid",
        allRequiredKey = "claimPeriodStart.error.required.all",
        twoRequiredKey = "claimPeriodStart.error.required.two",
        requiredKey = "claimPeriodStart.error.required"
      ).verifying(validStartDate)
    )

  private def validStartDate: Constraint[LocalDate] = Constraint { claimStartDate =>
    if (!claimStartDate.isBefore(appConfig.schemeStartDate) && !claimStartDate.isAfter(appConfig.schemeEndDate)) {
      Valid
    } else {
      Invalid("claimPeriodStart.error.outofrange", dateToString(appConfig.schemeStartDate), dateToString(appConfig.schemeEndDate))
    }
  }
}
