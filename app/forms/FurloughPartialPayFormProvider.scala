/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import forms.mappings.Mappings
import models.FurloughPartialPay
import play.api.data.Form
import play.api.data.Forms.mapping

class FurloughPartialPayFormProvider extends Mappings {

  def apply(): Form[FurloughPartialPay] = Form(
    mapping(
      "value" -> bigDecimal(
        requiredKey = "FurloughPartialPay.error.required",
        nonNumericKey = "FurloughPartialPay.error.invalid"
      ).verifying(minimumValue(BigDecimal(0.0), "amount.error.negative"))
        .verifying(maxTwoDecimals())
    )(FurloughPartialPay.apply)(FurloughPartialPay.unapply)
  )
}
