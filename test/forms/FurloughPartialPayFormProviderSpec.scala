/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import forms.behaviours.BigDecimalFieldBehaviours
import play.api.data.FormError

class FurloughPartialPayFormProviderSpec extends BigDecimalFieldBehaviours {

  val form = new FurloughPartialPayFormProvider()()

  ".value" must {

    val fieldName = "value"
    val requiredKey = "FurloughPartialPay.error.required"
    val invalidKey = "FurloughPartialPay.error.invalid"

    "accept 0 as a valid input" in {
      val value = "0.0"

      val data = Map("value" -> value)

      val result = form.bind(data)

      result.errors shouldBe empty
      result.value.value.value shouldEqual BigDecimal(value)
    }

    behave like bigDecimalField(
      form,
      fieldName,
      error = FormError(fieldName, invalidKey)
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
