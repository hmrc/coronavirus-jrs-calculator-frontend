/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import java.time.LocalDate

import javax.inject.Inject
import forms.mappings.Mappings
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._

class TopUpPeriodsFormProvider @Inject() extends Mappings {

  def apply(): Form[List[LocalDate]] =
    Form(
      "value" -> list(of(localDateFormat)).verifying("topupPeriods.error.required", _.nonEmpty)
    )
}
