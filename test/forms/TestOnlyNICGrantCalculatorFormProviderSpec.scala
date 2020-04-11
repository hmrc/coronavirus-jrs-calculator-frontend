/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import java.time.{LocalDate, ZoneOffset}

import base.SpecBase
import forms.behaviours.DateBehaviours
import play.api.data.FormError

class TestOnlyNICGrantCalculatorFormProviderSpec extends SpecBase {

  val form = new TestOnlyNICGrantCalculatorFormProvider(frontendAppConfig)
  val dateBehaviours = new DateBehaviours

  import dateBehaviours._

  ".value" should {}
}
