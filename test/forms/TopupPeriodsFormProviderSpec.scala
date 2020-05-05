/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import forms.behaviours.CheckboxFieldBehaviours
import models.TopupPeriods
import play.api.data.FormError

class TopupPeriodsFormProviderSpec extends CheckboxFieldBehaviours {

  val form = new TopupPeriodsFormProvider()()

  ".value" must {

    val fieldName = "value"
    val requiredKey = "topupPeriods.error.required"

    behave like checkboxField[TopupPeriods](
      form,
      fieldName,
      validValues = TopupPeriods.values,
      invalidError = FormError(s"$fieldName[0]", "error.invalid")
    )

    behave like mandatoryCheckboxField(
      form,
      fieldName,
      requiredKey
    )
  }
}
