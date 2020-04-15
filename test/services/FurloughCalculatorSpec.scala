/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import base.SpecBase
import models.Calculation.FurloughCalculationResult
import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}
import models.{CalculationResult, FurloughPeriod, PayPeriod, PayPeriodBreakdown, PayPeriodWithPayDay, PaymentDate, RegularPayment, Salary}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class FurloughCalculatorSpec extends SpecBase with ScalaCheckPropertyChecks {

  forAll(scenarios) { (frequency, salary, expected) =>
    s"For payment frequency $frequency and salary $salary should return $expected" in new FurloughCalculator {
      calculate(frequency, salary) mustBe expected
    }
  }

  "return a CalculationResult with a total and a list of furlough payments for a given list regular payment" in new FurloughCalculator {
    val periodOne =
      PayPeriodWithPayDay(PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31)), PaymentDate(LocalDate.of(2020, 3, 31)))
    val periodTwo =
      PayPeriodWithPayDay(PayPeriod(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30)), PaymentDate(LocalDate.of(2020, 4, 30)))
    val paymentOne: RegularPayment = RegularPayment(Salary(2000.00), periodOne.payPeriod)
    val paymentTwo: RegularPayment = RegularPayment(Salary(2000.00), periodTwo.payPeriod)
    val payments: List[RegularPayment] = List(paymentOne, paymentTwo)

    val taxYearDate = LocalDate.of(2020, 4, 20)
    val periodTwoWithNewPaymentDate = periodTwo.copy(paymentDate = PaymentDate(taxYearDate))

    val expected =
      CalculationResult(
        FurloughCalculationResult,
        3200.00,
        List(PayPeriodBreakdown(1600.0, periodOne), PayPeriodBreakdown(1600.0, periodTwoWithNewPaymentDate)))

    calculateFurlough(Monthly, payments, taxYearDate) mustBe expected
  }

  "calcualtes furlough for a furlough period lesser than pay periods" in new FurloughCalculator {
    //claim 1/3 to 30/5
    //payperiods 1/3 to 30/4
    //furlough period 15/3 to 30/4 (16 dyas in march + 30 April)

    val periodOne =
      PayPeriodWithPayDay(PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31)), PaymentDate(LocalDate.of(2020, 3, 31)))
    val periodTwo =
      PayPeriodWithPayDay(PayPeriod(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30)), PaymentDate(LocalDate.of(2020, 4, 30)))
    val paymentOne: RegularPayment = RegularPayment(Salary(2000.00), periodOne.payPeriod)
    val paymentTwo: RegularPayment = RegularPayment(Salary(2000.00), periodTwo.payPeriod)
    val payments: List[RegularPayment] = List(paymentOne, paymentTwo)
    val furloughPeriod = FurloughPeriod(LocalDate.of(2020, 3, 15), LocalDate.of(2020, 4, 30))

    calculateNewPeriod(furloughPeriod, payments) mustBe (64.52 * 16) + 2000.0
  }

  private lazy val scenarios = Table(
    ("paymentFrequency", "RegularPayment", "expected"),
    (Monthly, RegularPayment(Salary(2000.00), PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))), 1600.00),
    (Monthly, RegularPayment(Salary(5000.00), PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))), 2500.00),
    (Monthly, RegularPayment(Salary(5000.00), PayPeriod(LocalDate.of(2020, 3, 15), LocalDate.of(2020, 4, 15))), 2540.50),
    (Weekly, RegularPayment(Salary(500.00), PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 7))), 400.00),
    (Weekly, RegularPayment(Salary(1000.00), PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 7))), 576.92),
    (FortNightly, RegularPayment(Salary(2000.00), PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 14))), 1153.84),
    (FortNightly, RegularPayment(Salary(1000.00), PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 14))), 800.00),
    (FourWeekly, RegularPayment(Salary(5000.00), PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 28))), 2307.68),
    (FourWeekly, RegularPayment(Salary(2000.00), PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 28))), 1600.00)
  )
}
