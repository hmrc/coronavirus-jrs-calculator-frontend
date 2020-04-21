/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import base.{CoreDataBuilder, SpecBase}
import models.PayQuestion.Varies
import models.PaymentFrequency.{FortNightly, FourWeekly, Weekly}
import models.{Amount, CylbPayment, FullPeriod, NonFurloughPay, PartialPeriod, PaymentDate, PaymentWithPeriod, Period, PeriodWithPaymentDate, VariableLengthEmployed}

class ReferencePayCalculatorSpec extends SpecBase with CoreDataBuilder {

  "calculates reference gross pay for an employee on variable pays" in new ReferencePayCalculator {
    val employeeStartDate = LocalDate.of(2019, 12, 1)
    val furloughStartDate = LocalDate.of(2020, 3, 1)
    val nonFurloughPay = NonFurloughPay(None, Some(Amount(1000.00)))
    val grossPay = Amount(2400.00)
    val priorFurloughPeriod = Period(employeeStartDate, furloughStartDate.minusDays(1))
    val afterFurloughPeriod =
      PeriodWithPaymentDate(FullPeriod(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31))), PaymentDate(LocalDate.of(2020, 3, 31)))
    val afterFurloughPeriodTwo =
      PeriodWithPaymentDate(FullPeriod(Period(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30))), PaymentDate(LocalDate.of(2020, 4, 30)))
    val afterFurloughPartial = PeriodWithPaymentDate(
      PartialPeriod(
        Period(LocalDate.of(2020, 5, 1), LocalDate.of(2020, 5, 31)),
        Period(LocalDate.of(2020, 5, 1), LocalDate.of(2020, 5, 15))),
      PaymentDate(LocalDate.of(2020, 5, 31))
    )
    val payPeriods = Seq(afterFurloughPeriod, afterFurloughPeriodTwo, afterFurloughPartial)

    val expected = Seq(
      PaymentWithPeriod(Amount(0.0), Amount(817.47), afterFurloughPeriod, Varies),
      PaymentWithPeriod(Amount(0.0), Amount(791.10), afterFurloughPeriodTwo, Varies),
      PaymentWithPeriod(Amount(1000.0), Amount(817.47), afterFurloughPartial, Varies),
    )

    calculateVariablePay(nonFurloughPay, priorFurloughPeriod, payPeriods, grossPay) mustBe expected
  }

  "calculate daily average gross earning for a given pay period" in new ReferencePayCalculator {
    val employeeStartDate = LocalDate.of(2019, 12, 1)
    val furloughStartDate = LocalDate.of(2020, 3, 1)
    val periodBeforeFurlough = Period(employeeStartDate, furloughStartDate.minusDays(1))

    averageDailyCalculator(periodBeforeFurlough, Amount(2400.0)) mustBe 26.37
  }

  "calculate cylb amounts for weekly" in new ReferencePayCalculator {
    val cylbs = Seq(
      CylbPayment(paymentDate("2019, 3, 2"), Amount(700.00)),
      CylbPayment(paymentDate("2019, 3, 9"), Amount(350.00)),
      CylbPayment(paymentDate("2019, 3, 16"), Amount(140.00))
    )

    val periods = Seq(
      fullPeriodWithPaymentDate("2020,3,1", "2020,3,7", "2020, 3, 7"),
      fullPeriodWithPaymentDate("2020,3,8", "2020,3,14", "2020, 3, 14")
    )

    val nonFurloughPay = NonFurloughPay(None, None)

    val expected = Seq(
      paymentWithPeriod(0.0, 450.00, fullPeriodWithPaymentDate("2020,3,1", "2020,3,7", "2020, 3, 7"), Varies),
      paymentWithPeriod(0.0, 200.00, fullPeriodWithPaymentDate("2020,3,8", "2020,3,14", "2020, 3, 14"), Varies)
    )

    calculateCylb(nonFurloughPay, Weekly, cylbs, periods) mustBe expected
  }

  "calculate cylb amounts for fortnightly" in new ReferencePayCalculator {
    val cylbs = Seq(
      CylbPayment(paymentDate("2019, 3, 2"), Amount(1400.00)),
      CylbPayment(paymentDate("2019, 3, 16"), Amount(700.00)),
      CylbPayment(paymentDate("2019, 3, 30"), Amount(280.00))
    )

    val periods = Seq(
      fullPeriodWithPaymentDate("2020,3,1", "2020,3,14", "2020, 3, 14"),
      fullPeriodWithPaymentDate("2020,3,15", "2020,3,28", "2020, 3, 28")
    )

    val nonFurloughPay = NonFurloughPay(None, None)

    val expected = Seq(
      paymentWithPeriod(0.0, 450.00, fullPeriodWithPaymentDate("2020,3,1", "2020,3,14", "2020, 3, 14"), Varies),
      paymentWithPeriod(0.0, 200.00, fullPeriodWithPaymentDate("2020,3,15", "2020,3,28", "2020, 3, 28"), Varies)
    )

    calculateCylb(nonFurloughPay, FortNightly, cylbs, periods) mustBe expected
  }

  "calculate cylb amounts for fourweekly" in new ReferencePayCalculator {
    val cylbs = Seq(
      CylbPayment(paymentDate("2019, 3, 2"), Amount(2800.00)),
      CylbPayment(paymentDate("2019, 3, 30"), Amount(1400.00)),
      CylbPayment(paymentDate("2019, 4, 27"), Amount(560.00))
    )

    val periods = Seq(
      fullPeriodWithPaymentDate("2020,3,1", "2020,3,28", "2020, 3, 28"),
      fullPeriodWithPaymentDate("2020,3,29", "2020,4,26", "2020, 4, 26")
    )

    val nonFurloughPay = NonFurloughPay(None, None)

    val expected = Seq(
      paymentWithPeriod(0.0, 450.00, fullPeriodWithPaymentDate("2020,3,1", "2020,3,28", "2020, 3, 28"), Varies),
      paymentWithPeriod(0.0, 200.00, fullPeriodWithPaymentDate("2020,3,29", "2020,4,26", "2020, 4, 26"), Varies)
    )

    calculateCylb(nonFurloughPay, FourWeekly, cylbs, periods) mustBe expected
  }

  "compare cylb and avg gross pay amount taking the greater" in new ReferencePayCalculator {
    val cylb = Seq(
      paymentWithPeriod(0.0, 500.00, fullPeriodWithPaymentDate("2020,3,1", "2020,3,28", "2020, 3, 28"), Varies),
      paymentWithPeriod(0.0, 200.00, fullPeriodWithPaymentDate("2020,3,29", "2020,4,26", "2020, 4, 26"), Varies)
    )

    val avg = Seq(
      paymentWithPeriod(0.0, 450.0, fullPeriodWithPaymentDate("2020,3,1", "2020,3,28", "2020, 3, 28"), Varies),
      paymentWithPeriod(0.0, 450.0, fullPeriodWithPaymentDate("2020,3,29", "2020,4,26", "2020, 4, 26"), Varies)
    )

    val expected = Seq(
      paymentWithPeriod(0.0, 500.00, fullPeriodWithPaymentDate("2020,3,1", "2020,3,28", "2020, 3, 28"), Varies),
      paymentWithPeriod(0.0, 450.00, fullPeriodWithPaymentDate("2020,3,29", "2020,4,26", "2020, 4, 26"), Varies)
    )

    greaterGrossPay(cylb, avg) mustBe expected
  }

  "calculate avg and cylb and return the greater" in new ReferencePayCalculator {
    val nonFurloughPay = NonFurloughPay(None, None)

    val cylbs = Seq(
      CylbPayment(paymentDate("2020,3,2"), Amount(1400.0)), // (1400 / 28) * 2 => 600
      CylbPayment(paymentDate("2020,3,30"), Amount(2800.0)), // (700 / 28) * 5
      CylbPayment(paymentDate("2020,4,27"), Amount(700.0))
    ) // (700 / 28) * 5 => 325

    val paymentDates = Seq(
      fullPeriodWithPaymentDate("2020,3,1", "2020,3,28", "2020, 3, 28"),
      fullPeriodWithPaymentDate("2020,3,29", "2020,4,26", "2020, 4, 26"))

    val avg = Seq(
      paymentWithPeriod(0.0, 500.0, fullPeriodWithPaymentDate("2020,3,1", "2020,3,28", "2020, 3, 28"), Varies),
      paymentWithPeriod(0.0, 500.0, fullPeriodWithPaymentDate("2020,3,29", "2020,4,26", "2020, 4, 26"), Varies)
    )

    val expected: Seq[PaymentWithPeriod] = Seq(
      paymentWithPeriod(0.0, 600.0, fullPeriodWithPaymentDate("2020,3,1", "2020,3,28", "2020, 3, 28"), Varies),
      paymentWithPeriod(0.0, 500.0, fullPeriodWithPaymentDate("2020,3,29", "2020,4,26", "2020, 4, 26"), Varies)
    )

    addCylbToCalculation(nonFurloughPay, FourWeekly, cylbs, paymentDates, avg) mustBe expected
  }

  "calculates only avg if cylb is empty" in new ReferencePayCalculator {
    val nonFurloughPay = NonFurloughPay(None, None)

    val paymentDates = Seq(
      fullPeriodWithPaymentDate("2020,3,1", "2020,3,28", "2020, 3, 28"),
      fullPeriodWithPaymentDate("2020,3,29", "2020,4,26", "2020, 4, 26"))

    val avg = Seq(
      paymentWithPeriod(0.0, 500.0, fullPeriodWithPaymentDate("2020,3,1", "2020,3,28", "2020, 3, 28"), Varies),
      paymentWithPeriod(0.0, 500.0, fullPeriodWithPaymentDate("2020,3,29", "2020,4,26", "2020, 4, 26"), Varies)
    )

    addCylbToCalculation(nonFurloughPay, FourWeekly, Seq.empty, paymentDates, avg) mustBe avg
  }
}
