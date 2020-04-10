/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import java.time.LocalDate

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form

class ClaimPeriodFormProvider @Inject() extends Mappings {

  def apply(): Form[LocalDate] =
    Form(
      "value" -> localDate(
        invalidKey     = "claimPeriod.error.invalid",
        allRequiredKey = "claimPeriod.error.required.all",
        twoRequiredKey = "claimPeriod.error.required.two",
        requiredKey    = "claimPeriod.error.required"
      )
    )
}
