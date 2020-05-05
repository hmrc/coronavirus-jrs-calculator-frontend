/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form
import play.api.data.Forms.set
import models.TopupPeriods

class TopupPeriodsFormProvider @Inject() extends Mappings {

  def apply(): Form[Set[TopupPeriods]] =
    Form(
      "value" -> set(enumerable[TopupPeriods]("topupPeriods.error.required")).verifying(nonEmptySet("topupPeriods.error.required"))
    )
}
