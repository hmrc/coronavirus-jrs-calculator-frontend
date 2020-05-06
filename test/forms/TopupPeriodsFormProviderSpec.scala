/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import java.time.LocalDate

import forms.behaviours.CheckboxFieldBehaviours
import play.api.data.FormError

class TopupPeriodsFormProviderSpec extends CheckboxFieldBehaviours {

  val form = new TopupPeriodsFormProvider()()

  ".value" must {

    val fieldName = "value"
    val requiredKey = "topupPeriods.error.required"

    def datesBetween(fromDate: LocalDate, toDate: LocalDate) =
      fromDate.toEpochDay.until(toDate.toEpochDay).map(LocalDate.ofEpochDay)

    behave like checkboxField[LocalDate](
      form,
      fieldName,
      validValues = datesBetween(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 6, 30)),
      invalidError = FormError(s"$fieldName[0]", "error.invalid")
    )

    behave like mandatoryCheckboxField(
      form,
      fieldName,
      requiredKey
    )
  }
}
