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

package forms

import forms.mappings.Mappings
import javax.inject.Inject
import models.{Hours, Periods}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.{Constraint, Invalid, Valid}

class PartTimeNormalHoursFormProvider @Inject() extends Mappings {

  def apply(period: Periods): Form[Hours] =
    Form(
      mapping(
        "value" -> double(
          requiredKey = "partTimeNormalHours.error.required",
          nonNumericKey = "partTimeNormalHours.error.nonNumeric"
        ).verifying(greaterThan(0.0, "partTimeNormalHours.error.min"))
          .verifying(maximumValue(period))
      )(Hours.apply)(Hours.unapply))

  private def maximumValue(period: Periods): Constraint[Double] = Constraint { input =>
    if (input <= period.period.countHours) {
      Valid
    } else {
      Invalid("partTimeNormalHours.error.max")
    }
  }
}
