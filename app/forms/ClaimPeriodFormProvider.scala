/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import forms.mappings.Mappings
import javax.inject.Inject
import models.ClaimPeriodModel
import play.api.data.Form
import play.api.data.Forms._

class ClaimPeriodFormProvider @Inject() extends Mappings {

  def apply(): Form[ClaimPeriodModel] =
    Form(mapping(
      "startDateValue" -> localDate(
        invalidKey     = "claimPeriod.start.error.invalid",
        allRequiredKey = "claimPeriod.start.error.required.all",
        twoRequiredKey = "claimPeriod.start.error.required.two",
        requiredKey    = "claimPeriod.start.error.required"
      ),
      "endDateValue" -> localDate(
        invalidKey     = "claimPeriod.end.error.invalid",
        allRequiredKey = "claimPeriod.end.error.required.all",
        twoRequiredKey = "claimPeriod.end.error.required.two",
        requiredKey    = "claimPeriod.end.error.required"
      )
    )(ClaimPeriodModel.apply)(ClaimPeriodModel.unapply))
}
