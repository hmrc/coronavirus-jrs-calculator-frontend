/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import base.{CoreTestDataBuilder, SpecBase}
import models.PaymentFrequency.{FourWeekly, Monthly}
import models.{Amount, PartialPeriod, PartialPeriodBreakdown, PartialPeriodWithPaymentDate, PaymentDate, Period}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class NicCalculatorSpec extends SpecBase with ScalaCheckPropertyChecks with CoreTestDataBuilder {

  forAll(partialPeriodScenarios) { (frequency, grossPay, furloughPayment, period, paymentDate, expectedGrant) =>
    s"Calculate grant for a partial period with Payment Frequency: $frequency," +
      s"a PaymentDate: $paymentDate and a Gross Pay: ${grossPay.value}" in new NicCalculator {
      val expected = PartialPeriodBreakdown(grossPay, expectedGrant, PartialPeriodWithPaymentDate(period, paymentDate))

      calculatePartialPeriodNic(frequency, grossPay, furloughPayment, period, paymentDate) mustBe expected
    }
  }

  "For a partial period and variable pay calculate nic grant" in new NicCalculator {
    val period = PartialPeriod(
      Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 28)),
      Period(LocalDate.of(2020, 3, 20), LocalDate.of(2020, 3, 28)))
    val paymentDate: PaymentDate = PaymentDate(LocalDate.of(2020, 3, 28))

    val expected = PartialPeriodBreakdown(Amount(1124.23), Amount(39.30), PartialPeriodWithPaymentDate(period, paymentDate))

    calculatePartialPeriodNic(FourWeekly, Amount(1124.23), Amount(426.02), period, paymentDate) mustBe expected
  }

  "calculates Nic with additional payment and 0.0 top up" in new NicCalculator {
    val additionalPayment = Some(Amount(300))
    val topUp = None
    val nonFurlough = Amount(2200.0)
    val furlough = Amount(720.0)
    val pp = partialPeriod("2020,3,1" -> "2020,3,31", "2020,3,23" -> "2020, 3, 31")

    calculatePartialPeriodNic(Monthly, nonFurlough, furlough, pp, PaymentDate(LocalDate.of(2020, 3, 31)), additionalPayment, topUp).grant mustBe Amount(
      100.20)
  }

  "calculates Nic with top up and 0.0 additional payment" in new NicCalculator {
    val additionalPayment = None
    val topUp = Some(Amount(200.0))
    val nonFurlough = Amount(2200.0)
    val furlough = Amount(720.0)
    val pp = partialPeriod("2020,3,1" -> "2020,3,31", "2020,3,23" -> "2020, 3, 31")

    calculatePartialPeriodNic(Monthly, nonFurlough, furlough, pp, PaymentDate(LocalDate.of(2020, 3, 31)), additionalPayment, topUp).grant mustBe Amount(
      75.28)
  }

  "calculates Nic with additional payment plus top up" in new NicCalculator {
    val additionalPayment = Some(Amount(300))
    val topUp = Some(Amount(200))
    val nonFurlough = Amount(2200.0)
    val furlough = Amount(720.0)
    val pp = partialPeriod("2020,3,1" -> "2020,3,31", "2020,3,23" -> "2020, 3, 31")

    calculatePartialPeriodNic(Monthly, nonFurlough, furlough, pp, PaymentDate(LocalDate.of(2020, 3, 31)), additionalPayment, topUp).grant mustBe Amount(
      84.69)
  }

  private lazy val partialPeriodScenarios = Table(
    ("frequency", "grossPay", "furloughPayment", "period", "paymentDate", "expectedGrant"),
    (
      Monthly,
      Amount(1200.0),
      Amount(960.00),
      partialPeriod("2020,4,1" -> "2020,4, 30", "2020,4, 16" -> "2020,4,30"),
      PaymentDate(LocalDate.of(2020, 4, 30)),
      Amount(98.53)
    ),
    (
      Monthly,
      Amount(1016.13),
      Amount(1774.30),
      partialPeriod("2020, 3, 1" -> "2020, 3, 31", "2020, 3, 10" -> "2020, 3, 31"),
      PaymentDate(LocalDate.of(2020, 3, 31)),
      Amount(202.83)
    ),
    (
      Monthly,
      Amount(180.0),
      Amount(496.0),
      partialPeriod("2020, 3, 1" -> "2020, 3, 31", "2020, 3, 10" -> "2020, 3, 31"),
      PaymentDate(LocalDate.of(2020, 3, 31)),
      Amount(0.00)
    )
  )

}
