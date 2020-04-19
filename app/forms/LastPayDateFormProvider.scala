/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import java.time.LocalDate

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form

class LastPayDateFormProvider @Inject() extends Mappings {

  def apply(): Form[LocalDate] =
    Form(
      "value" -> localDate(
        invalidKey = "lastPayDate.error.invalid",
        allRequiredKey = "lastPayDate.error.required.all",
        twoRequiredKey = "lastPayDate.error.required.two",
        requiredKey = "lastPayDate.error.required"
      )
    )
}
