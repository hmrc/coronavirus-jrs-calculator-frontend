/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import base.SpecBase

class CommonCalculationServiceSpec extends SpecBase {

  "Returns zero for an amount lesser than threshold" in new CommonCalculationService {
    greaterThanAllowance(100.0, 101.0, NiRate()) mustBe 0.0
    greaterThanAllowance(99.0, 100.0, PensionRate()) mustBe 0.0
  }

  "Returns an ((amount - threshold) * rate) rounded half_up if greater than threshold" in new CommonCalculationService {
    greaterThanAllowance(1000.0, 100.0, NiRate()) mustBe 124.20
    greaterThanAllowance(1000.0, 100.0, PensionRate()) mustBe 27.0
  }

}
