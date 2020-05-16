/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package services

import base.{CoreTestDataBuilder, SpecBase}
import models.PaymentFrequency.Monthly
import models.PensionStatus.DoesContribute
import models.{Amount, FullPeriodPensionBreakdown}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class PensionCalculatorSpec extends SpecBase with ScalaCheckPropertyChecks with CoreTestDataBuilder {

  forAll(fullPeriodScenarios) { (frequency, furloughPayment, payment, expectedGrant) =>
    s"Calculate pension grant for a full period with Payment Frequency: $frequency, " +
      s"a Payment Date: ${payment.periodWithPaymentDate.paymentDate} and a Furlough Grant: ${furloughPayment.value}" in new PensionCalculator {
      val expected = FullPeriodPensionBreakdown(expectedGrant, payment)

      calculateFullPeriodPension(DoesContribute, frequency, furloughPayment, payment) mustBe expected
    }
  }

  private lazy val fullPeriodScenarios = Table(
    ("frequency", "furloughPayment", "payment", "expectedGrant"),
    (
      Monthly,
      Amount(1600.00),
      paymentWithFullPeriod(2000.00, fullPeriodWithPaymentDate("2020-03-01", "2020-03-31", "2020-03-31")),
      Amount(32.64)
    ),
    (
      Monthly,
      Amount(600.00),
      paymentWithFullPeriod(750.00, fullPeriodWithPaymentDate("2020-03-01", "2020-03-31", "2020-03-31")),
      Amount(2.64)
    )
  )

}
