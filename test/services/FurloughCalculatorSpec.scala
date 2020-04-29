/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import base.{CoreDataBuilder, SpecBase}
import models.Calculation.FurloughCalculationResult
import models.PayQuestion.Regularly
import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}
import models.{Amount, CalculationResult, FullPeriod, FullPeriodBreakdown, FullPeriodWithPaymentDate, PartialPeriod, PartialPeriodBreakdown, PartialPeriodWithPaymentDate, PaymentDate, PaymentWithPeriod, Period}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class FurloughCalculatorSpec extends SpecBase with ScalaCheckPropertyChecks with CoreDataBuilder {

  forAll(fullPeriodScenarios) { (frequency, payment, expectedFurlough) =>
    val fullPeriod: FullPeriod = payment.period.asInstanceOf[FullPeriod]

    s"Full Period: For payment frequency $frequency and payment ${payment.furloughPayment.value} return $expectedFurlough" in new FurloughCalculator {
      calculateFullPeriod(frequency, payment) mustBe expectedFurlough
    }
  }

  forAll(partialPeriodScenarios) { (payment, expectedFurlough) =>
    s"Partial Period: For gross payment: ${payment.furloughPayment.value} " +
      s"should return $expectedFurlough" in new FurloughCalculator {
      val period = payment.period.period.asInstanceOf[PartialPeriod]
      val expected =
        PartialPeriodBreakdown(payment.nonFurloughPay, expectedFurlough, payment.period.asInstanceOf[PartialPeriodWithPaymentDate])
      calculatePartialPeriod(payment) mustBe expected
    }
  }

  "return a CalculationResult with a total and a list of furlough payments for a given list regular payment" in new FurloughCalculator {
    val periodOne =
      FullPeriodWithPaymentDate(
        FullPeriod(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))),
        PaymentDate(LocalDate.of(2020, 3, 31)))
    val periodTwo =
      FullPeriodWithPaymentDate(
        FullPeriod(Period(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30))),
        PaymentDate(LocalDate.of(2020, 4, 30)))
    val paymentOne: PaymentWithPeriod = paymentWithFullPeriod(2000.00, periodOne, Regularly)
    val paymentTwo: PaymentWithPeriod = paymentWithFullPeriod(2000.00, periodTwo, Regularly)
    val payments: List[PaymentWithPeriod] = List(paymentOne, paymentTwo)

    val expected =
      CalculationResult(
        FurloughCalculationResult,
        3200.00,
        List(
          FullPeriodBreakdown(Amount(1600.00), periodOne),
          FullPeriodBreakdown(Amount(1600.00), periodTwo)
        )
      )

    calculateFurloughGrant(Monthly, payments) mustBe expected
  }

  private lazy val fullPeriodScenarios = Table(
    ("paymentFrequency", "payment", "expectedFurlough"),
    (
      Monthly,
      paymentWithFullPeriod(
        2000,
        FullPeriodWithPaymentDate(
          FullPeriod(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))),
          PaymentDate(LocalDate.of(2020, 3, 31))),
        Regularly
      ),
      Amount(1600.00)),
    (
      Monthly,
      paymentWithFullPeriod(
        5000.00,
        FullPeriodWithPaymentDate(
          FullPeriod(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))),
          PaymentDate(LocalDate.of(2020, 3, 31))),
        Regularly
      ),
      Amount(2500.00)),
    (
      Monthly,
      paymentWithFullPeriod(
        5000.00,
        FullPeriodWithPaymentDate(
          FullPeriod(Period(LocalDate.of(2020, 3, 15), LocalDate.of(2020, 4, 15))),
          PaymentDate(LocalDate.of(2020, 4, 30))),
        Regularly
      ),
      Amount(2621.15)),
    (
      Weekly,
      paymentWithFullPeriod(
        500.00,
        FullPeriodWithPaymentDate(
          FullPeriod(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 7))),
          PaymentDate(LocalDate.of(2020, 3, 21))),
        Regularly
      ),
      Amount(400.00)),
    (
      Weekly,
      paymentWithFullPeriod(
        1000.00,
        FullPeriodWithPaymentDate(
          FullPeriod(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 7))),
          PaymentDate(LocalDate.of(2020, 3, 21))),
        Regularly
      ),
      Amount(576.92)),
    (
      FortNightly,
      paymentWithFullPeriod(
        2000.00,
        FullPeriodWithPaymentDate(
          FullPeriod(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 14))),
          PaymentDate(LocalDate.of(2020, 3, 28))),
        Regularly
      ),
      Amount(1153.84)),
    (
      FortNightly,
      paymentWithFullPeriod(
        1000.00,
        FullPeriodWithPaymentDate(
          FullPeriod(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 14))),
          PaymentDate(LocalDate.of(2020, 3, 28))),
        Regularly
      ),
      Amount(800.00)),
    (
      FourWeekly,
      paymentWithFullPeriod(
        5000.00,
        FullPeriodWithPaymentDate(
          FullPeriod(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 28))),
          PaymentDate(LocalDate.of(2020, 4, 15))),
        Regularly
      ),
      Amount(2307.68)),
    (
      FourWeekly,
      paymentWithFullPeriod(
        2000.00,
        FullPeriodWithPaymentDate(
          FullPeriod(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 28))),
          PaymentDate(LocalDate.of(2020, 4, 15))),
        Regularly
      ),
      Amount(1600.00))
  )

  private lazy val partialPeriodScenarios = Table(
    ("payment", "expectedFurlough"),
    (
      paymentWithPartialPeriod(
        677.42,
        1500.00,
        PartialPeriodWithPaymentDate(
          PartialPeriod(
            Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31)),
            Period(LocalDate.of(2020, 3, 15), LocalDate.of(2020, 3, 31))),
          PaymentDate(LocalDate.of(2020, 3, 31))
        ),
        Regularly
      ),
      Amount(658.06)),
    (
      paymentWithPartialPeriod(
        1580.65,
        3500.00,
        PartialPeriodWithPaymentDate(
          PartialPeriod(
            Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31)),
            Period(LocalDate.of(2020, 3, 15), LocalDate.of(2020, 3, 31))),
          PaymentDate(LocalDate.of(2020, 3, 31))
        ),
        Regularly
      ),
      Amount(1371.05)),
    (
      paymentWithPartialPeriod(
        1096.77,
        2000.00,
        PartialPeriodWithPaymentDate(
          PartialPeriod(
            Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31)),
            Period(LocalDate.of(2020, 3, 15), LocalDate.of(2020, 3, 28))),
          PaymentDate(LocalDate.of(2020, 3, 31))
        ),
        Regularly
      ),
      Amount(722.58))
  )
}
