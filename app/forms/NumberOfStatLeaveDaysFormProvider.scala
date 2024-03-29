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

import java.time.LocalDate
import java.time.Duration

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form
import play.api.i18n.Messages
import views.ViewUtils.dateToString

class NumberOfStatLeaveDaysFormProvider @Inject() extends Mappings {

  def apply(boundaryStart: LocalDate, boundaryEnd: LocalDate)(implicit messages: Messages): Form[Int] =
    Form(
      "value" -> int(
        "numberOfStatLeaveDays.error.required",
        "numberOfStatLeaveDays.error.wholeNumber",
        "numberOfStatLeaveDays.error.nonNumeric",
        args = Seq(dateToString(boundaryStart), dateToString(boundaryEnd))
      ).verifying(firstError(
        maximumValueWithArgs[Int](
          maximum = daysBetween(boundaryStart, boundaryEnd),
          errorKey = "numberOfStatLeaveDays.error.maximum",
          args = Seq(daysBetween(boundaryStart, boundaryEnd), dateToString(boundaryStart), dateToString(boundaryEnd))
        ),
        minimumValue[Int](minimum = 1, errorKey = "numberOfStatLeaveDays.error.minimum")
      ))
    )

  private[forms] def daysBetween(boundaryStart: LocalDate, boundaryEnd: LocalDate): Int =
    Duration.between(boundaryStart.atStartOfDay(), boundaryEnd.atStartOfDay()).toDays.toInt

}
