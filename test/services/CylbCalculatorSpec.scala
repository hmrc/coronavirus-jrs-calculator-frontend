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

import java.time.LocalDate

import base.{CoreTestDataBuilder, SpecBase}
import models.PaymentFrequency.{FortNightly, FourWeekly, Weekly}
import models.{Amount, AveragePaymentWithFullPeriod, AveragePaymentWithPartialPeriod, CylbPaymentWithFullPeriod, CylbPaymentWithPartialPeriod, FullPeriodWithPaymentDate, LastYearPayment, NonFurloughPay, CylbPeriod, PartialPeriodWithPaymentDate, TwoPeriodCylb}

class CylbCalculatorSpec extends SpecBase with CoreTestDataBuilder {

  "calculate cylb amounts for weekly" in new CylbCalculator {
    val cylbs = Seq(
      LastYearPayment(LocalDate.of(2019, 3, 2), Amount(700.00)),
      LastYearPayment(LocalDate.of(2019, 3, 9), Amount(350.00))
    )

    val payPeriod: FullPeriodWithPaymentDate = fullPeriodWithPaymentDate("2020,3,1", "2020,3,7", "2020, 3, 7")

    val averagePayment: AveragePaymentWithFullPeriod =
      averagePaymentWithFullPeriod(350.00, payPeriod, 10000.0, period("2019-08-14", "2020-02-29"))

    val nonFurloughPay: NonFurloughPay = NonFurloughPay(None, None)

    val cylbBreakdown: TwoPeriodCylb =
      TwoPeriodCylb(
        Amount(450.0),
        Amount(700.0),
        7,
        2,
        Amount(200.00),
        LocalDate.of(2019, 3, 2),
        Amount(350.0),
        7,
        5,
        Amount(250.0),
        LocalDate.of(2019, 3, 9)
      )

    val expected: CylbPaymentWithFullPeriod = cylbPaymentWithFullPeriod(450.00, payPeriod, averagePayment, cylbBreakdown)

    calculateCylb(averagePayment, nonFurloughPay, Weekly, cylbs, payPeriod) mustBe expected
  }

  "calculate cylb amounts for fortnightly" in new CylbCalculator {
    val cylbs = Seq(
      LastYearPayment(LocalDate.of(2019, 3, 2), Amount(1400.00)),
      LastYearPayment(LocalDate.of(2019, 3, 16), Amount(1050.00))
    )

    val payPeriod: FullPeriodWithPaymentDate = fullPeriodWithPaymentDate("2020,3,1", "2020,3,14", "2020, 3, 14")

    val averagePayment: AveragePaymentWithFullPeriod =
      averagePaymentWithFullPeriod(700.00, payPeriod, 10000.0, period("2019-08-14", "2020-02-29"))

    val nonFurloughPay = NonFurloughPay(None, None)

    val cylbBreakdown: TwoPeriodCylb =
      TwoPeriodCylb(
        Amount(1100.0),
        Amount(1400.0),
        14,
        2,
        Amount(200.00),
        LocalDate.of(2019, 3, 2),
        Amount(1050.0),
        14,
        12,
        Amount(900.0),
        LocalDate.of(2019, 3, 16)
      )

    val expected: CylbPaymentWithFullPeriod = cylbPaymentWithFullPeriod(1100.00, payPeriod, averagePayment, cylbBreakdown)

    calculateCylb(averagePayment, nonFurloughPay, FortNightly, cylbs, payPeriod) mustBe expected
  }

  "calculate cylb amounts for fourweekly" in new CylbCalculator {
    val cylbs = Seq(
      LastYearPayment(LocalDate.of(2019, 3, 2), Amount(2800.00)),
      LastYearPayment(LocalDate.of(2019, 3, 30), Amount(1680.00))
    )

    val payPeriod: FullPeriodWithPaymentDate = fullPeriodWithPaymentDate("2020,3,1", "2020,3,28", "2020, 3, 28")

    val averagePayment: AveragePaymentWithFullPeriod =
      averagePaymentWithFullPeriod(1400.00, payPeriod, 10000.0, period("2019-08-14", "2020-02-29"))

    val nonFurloughPay = NonFurloughPay(None, None)

    val cylbBreakdown: TwoPeriodCylb =
      TwoPeriodCylb(
        Amount(1760.0),
        Amount(2800.0),
        28,
        2,
        Amount(200.00),
        LocalDate.of(2019, 3, 2),
        Amount(1680.0),
        28,
        26,
        Amount(1560.0),
        LocalDate.of(2019, 3, 30)
      )

    val expected: CylbPaymentWithFullPeriod = cylbPaymentWithFullPeriod(1760.00, payPeriod, averagePayment, cylbBreakdown)

    calculateCylb(averagePayment, nonFurloughPay, FourWeekly, cylbs, payPeriod) mustBe expected
  }

  "calculate cylb amounts for partial period where only days from current required" in new CylbCalculator {
    val cylbs = Seq(
      LastYearPayment(LocalDate.of(2019, 3, 2), Amount(350.00)),
      LastYearPayment(LocalDate.of(2019, 3, 9), Amount(700.00))
    )

    val payPeriod: PartialPeriodWithPaymentDate = partialPeriodWithPaymentDate("2020,3,1", "2020,3,7", "2020,3,3", "2020,3,7", "2020, 3, 7")

    val averagePayment: AveragePaymentWithPartialPeriod =
      averagePaymentWithPartialPeriod(0.00, 350.00, payPeriod, 10000.0, period("2019-08-14", "2020-02-29"))

    val nonFurloughPay = NonFurloughPay(None, None)

    val cylbBreakdown = CylbPeriod(Amount(500.00), Amount(700.00), 7, 5, LocalDate.of(2019, 3, 9))

    val expected = cylbPaymentWithPartialPeriod(0.0, 500.00, payPeriod, averagePayment, cylbBreakdown)

    calculateCylb(averagePayment, nonFurloughPay, Weekly, cylbs, payPeriod) mustBe expected
  }

  "calculate cylb amounts for partial period where only days from previous required" in new CylbCalculator {
    val cylbs = Seq(
      LastYearPayment(LocalDate.of(2019, 3, 9), Amount(700.00)),
      LastYearPayment(LocalDate.of(2019, 3, 16), Amount(350.00))
    )

    val payPeriod: PartialPeriodWithPaymentDate =
      partialPeriodWithPaymentDate("2020,3,8", "2020,3,14", "2020,3,8", "2020,3,9", "2020, 3, 14")

    val averagePayment: AveragePaymentWithPartialPeriod =
      averagePaymentWithPartialPeriod(0.00, 100.00, payPeriod, 10000.0, period("2019-08-14", "2020-02-29"))

    val nonFurloughPay = NonFurloughPay(None, None)

    val cylbBreakdown: CylbPeriod = CylbPeriod(Amount(200.00), Amount(700.00), 7, 2, LocalDate.of(2019, 3, 9))

    val expected: CylbPaymentWithPartialPeriod = cylbPaymentWithPartialPeriod(0.0, 200.00, payPeriod, averagePayment, cylbBreakdown)

    calculateCylb(averagePayment, nonFurloughPay, Weekly, cylbs, payPeriod) mustBe expected
  }

}
