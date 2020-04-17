/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import base.SpecBase
import models.Calculation.FurloughCalculationResult
import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}
import models.{Amount, CalculationResult, Payment, PaymentDate, PaymentWithPeriod, Period, PeriodBreakdown, PeriodWithPaymentDate, Salary}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class FurloughCalculatorSpec extends SpecBase with ScalaCheckPropertyChecks {

  forAll(fullPeriodScenarios) { (frequency, payment, cap, expectedFurlough) =>
    s"Full Period: For payment frequency $frequency and payment ${payment.amount.value} should return $expectedFurlough" in new FurloughCalculator {
      val expected =
        PeriodBreakdown(expectedFurlough, PeriodWithPaymentDate(payment.period, PaymentDate(payment.period.end)), cap)
      calculateFullPeriod(frequency, payment, PeriodWithPaymentDate(payment.period, PaymentDate(payment.period.end))) mustBe expected
    }
  }

  forAll(partialPeriodScenarios) { (payment, cap, expectedFurlough) =>
    s"Partial Period: For payment with a payment ${payment.amount.value} which has been adjusted for a partial period " +
      s"should return $expectedFurlough" in new FurloughCalculator {
      val expected =
        PeriodBreakdown(expectedFurlough, PeriodWithPaymentDate(payment.period, PaymentDate(payment.period.end)), cap)
      calculatePartialPeriod(payment, PeriodWithPaymentDate(payment.period, PaymentDate(payment.period.end))) mustBe expected
    }
  }

  "return a CalculationResult with a total and a list of furlough payments for a given list regular payment" in new FurloughCalculator {
    val periodOne =
      PeriodWithPaymentDate(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31)), PaymentDate(LocalDate.of(2020, 3, 31)))
    val periodTwo =
      PeriodWithPaymentDate(Period(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30)), PaymentDate(LocalDate.of(2020, 4, 30)))
    val paymentOne: PaymentWithPeriod = PaymentWithPeriod(Amount(2000.00), periodOne.period)
    val paymentTwo: PaymentWithPeriod = PaymentWithPeriod(Amount(2000.00), periodTwo.period)
    val payments: List[PaymentWithPeriod] = List(paymentOne, paymentTwo)

    val furloughPeriod = Period(periodOne.period.start, periodTwo.period.end)

    val taxYearDate = LocalDate.of(2020, 4, 20)
    val periodTwoWithNewPaymentDate = periodTwo.copy(paymentDate = PaymentDate(taxYearDate))

    val expected =
      CalculationResult(
        FurloughCalculationResult,
        3200.00,
        List(
          PeriodBreakdown(Payment(Amount(1600.0)), periodOne, Amount(2500.00)),
          PeriodBreakdown(Payment(Amount(1600.0)), periodTwoWithNewPaymentDate, Amount(2500.00))
        )
      )

    calculateFurlough(Monthly, payments, furloughPeriod, taxYearDate) mustBe expected
  }

  "return a pay period with new start/end dates if a given furlough period begins/ends in the pay period" in new FurloughCalculator {
    val furloughPeriodOne = Period(LocalDate.of(2020, 3, 15), LocalDate.of(2020, 5, 30))
    val payPeriodOne = Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))
    val expectedOne = Period(LocalDate.of(2020, 3, 15), LocalDate.of(2020, 3, 31))
    val furloughPeriodTwo = Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 25))
    val payPeriodTwo = Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))
    val expectedTwo = Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 25))
    val furloughPeriodThr = Period(LocalDate.of(2020, 3, 5), LocalDate.of(2020, 3, 28))
    val payPeriodThr = Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))
    val expectedThr = Period(LocalDate.of(2020, 3, 5), LocalDate.of(2020, 3, 28))

    payPeriodFromFurloughPeriod(furloughPeriodOne, payPeriodOne) mustBe expectedOne
    payPeriodFromFurloughPeriod(furloughPeriodTwo, payPeriodTwo) mustBe expectedTwo
    payPeriodFromFurloughPeriod(furloughPeriodThr, payPeriodThr) mustBe expectedThr
  }

  private lazy val fullPeriodScenarios = Table(
    ("paymentFrequency", "payment", "cap", "expectedFurlough"),
    (
      Monthly,
      PaymentWithPeriod(Amount(2000.00), Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))),
      Amount(2500.00),
      Payment(Amount(1600.00))),
    (
      Monthly,
      PaymentWithPeriod(Amount(5000.00), Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))),
      Amount(2500.00),
      Payment(Amount(2500.00))),
    (
      Monthly,
      PaymentWithPeriod(Amount(5000.00), Period(LocalDate.of(2020, 3, 15), LocalDate.of(2020, 4, 15))),
      Amount(2621.15),
      Payment(Amount(2621.15))),
    (
      Weekly,
      PaymentWithPeriod(Amount(500.00), Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 7))),
      Amount(576.92),
      Payment(Amount(400.00))),
    (
      Weekly,
      PaymentWithPeriod(Amount(1000.00), Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 7))),
      Amount(576.92),
      Payment(Amount(576.92))),
    (
      FortNightly,
      PaymentWithPeriod(Amount(2000.00), Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 14))),
      Amount(1153.84),
      Payment(Amount(1153.84))),
    (
      FortNightly,
      PaymentWithPeriod(Amount(1000.00), Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 14))),
      Amount(1153.84),
      Payment(Amount(800.00))),
    (
      FourWeekly,
      PaymentWithPeriod(Amount(5000.00), Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 28))),
      Amount(2307.68),
      Payment(Amount(2307.68))),
    (
      FourWeekly,
      PaymentWithPeriod(Amount(2000.00), Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 28))),
      Amount(2307.68),
      Payment(Amount(1600.00)))
  )

  private lazy val partialPeriodScenarios = Table(
    ("payment", "cap", "expectedFurlough"),
    (
      PaymentWithPeriod(Amount(1032.32), Period(LocalDate.of(2020, 3, 15), LocalDate.of(2020, 3, 31))),
      Amount(1371.05),
      Payment(Amount(825.86))),
    (
      PaymentWithPeriod(Amount(3000.00), Period(LocalDate.of(2020, 3, 15), LocalDate.of(2020, 3, 31))),
      Amount(1371.05),
      Payment(Amount(1371.05))),
    (
      PaymentWithPeriod(Amount(3000.00), Period(LocalDate.of(2020, 3, 15), LocalDate.of(2020, 3, 28))),
      Amount(1129.1),
      Payment(Amount(1129.10)))
  )
}
