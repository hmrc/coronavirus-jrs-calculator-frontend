/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import models.{FurloughPayment, Monthly, PayPeriod}
import org.scalatest.{MustMatchers, WordSpec}

class NicCalculatorServiceSpec extends WordSpec with MustMatchers {

  "For PaymentFrequency Monthly with Furlough Pay < £719 in TaxYear 19/20" in new NicCalculatorService {
    val startDate = LocalDate.of(2020, 3, 1)
    val endDate = LocalDate.of(2020, 3, 31)
    val furloughPayment = FurloughPayment(700.00, PayPeriod(startDate, endDate))
    val furloughPaymentOver719 = FurloughPayment(1000.00, PayPeriod(startDate, endDate))

    calculateNic(Monthly, furloughPayment) mustBe 0
    calculateNic(Monthly, furloughPaymentOver719) mustBe 38.77
  }

}
