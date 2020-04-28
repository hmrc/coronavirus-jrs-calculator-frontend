/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import base.SpecBase
import models.Amount

class CalculatorsSpec extends SpecBase {

  "Calculates 80% of a given amount" in new Calculators {
    eightyPercent(Amount(1000.0)) mustBe Amount(800.0)
  }

  "Round an amount HALF_UP" in new Calculators {
    Amount(1000.5111).halfUp mustBe Amount(1000.51)
    Amount(1000.4999).halfUp mustBe Amount(1000.50)
  }

}
