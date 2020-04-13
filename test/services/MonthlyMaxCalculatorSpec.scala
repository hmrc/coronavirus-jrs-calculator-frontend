/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import base.SpecBase
import models.{PayPeriod, PaymentDate}

class MonthlyMaxCalculatorSpec extends SpecBase {

  "Calculates monthly max where pay period spans March/April" in new MonthlyMaxCalculator {
    val payPeriod = PayPeriod(LocalDate.of(2020, 3, 15), LocalDate.of(2020, 4, 15), PaymentDate(LocalDate.of(2020, 3, 31)))

    //15th -> 31st = 16 Days * £80.65 = 1290.40
    //1st -> 15th = 15 Days * £83.34 = 1250.10
    //2540.50
    calculate(payPeriod) mustBe 2540.50
  }

}
