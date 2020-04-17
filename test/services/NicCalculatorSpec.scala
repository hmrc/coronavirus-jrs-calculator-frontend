/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import base.SpecBase
import models.PaymentFrequency.Monthly
import models.{Amount, Payment, PaymentDate, Period, PeriodBreakdown, PeriodWithPaymentDate}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class NicCalculatorSpec extends SpecBase with ScalaCheckPropertyChecks {

  forAll(fullPeriodScenarios) { (frequency, furloughPayment, periodWithPaymentDate, expectedGrant) =>
    s"Calculate NIC grant for a full period with Payment Frequency: $frequency, " +
      s"a Payment Date: ${periodWithPaymentDate.paymentDate} and a Furlough Grant: ${furloughPayment.amount}" in new NicCalculator {
      val expected = PeriodBreakdown(expectedGrant, periodWithPaymentDate, Amount(0.0))

      calculateFullPeriod(frequency, furloughPayment, periodWithPaymentDate) mustBe expected
    }
  }

  val fullPeriodScenarios = Table(
    ("frequency", "furloughPayment", "periodWithPaymentDate", "expectedGrant"),
    (
      Monthly,
      Payment(Amount(1600.00)),
      PeriodWithPaymentDate(Period(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31)), PaymentDate(LocalDate.of(2020, 3, 31))),
      Payment(Amount(220.80)))
  )

}
