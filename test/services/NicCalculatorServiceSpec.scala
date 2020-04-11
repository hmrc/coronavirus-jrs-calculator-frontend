/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import models.{FurloughPayment, Monthly, PayPeriod}
import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class NicCalculatorServiceSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks {

  forAll(scenarios) { (frequency, payment, expected) =>
    s"For payment frequency $frequency with payment amount ${payment.amount} should return $expected" in new NicCalculatorService {
      calculateNic(frequency, payment) mustBe expected
    }
  }

  private lazy val scenarios = Table(
    ("paymentFrequency", "FurloughPayment", "expected"),
    (Monthly, FurloughPayment(700.00, PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))), 0.00),
    (Monthly, FurloughPayment(1000.00, PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))), 38.77)
  )

}
