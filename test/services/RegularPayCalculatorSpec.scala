/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import base.{CoreTestDataBuilder, SpecBase}
import models.Amount
import models.PayMethod.Regular

class RegularPayCalculatorSpec extends SpecBase with CoreTestDataBuilder {

  "assign user entered salary to each pay period" in new RegularPayCalculator {
    val wage = Amount(1000.0)
    val periods = defaultJourneyCoreData.periods

    val expected = Seq(
      paymentWithFullPeriod(1000.0, fullPeriodWithPaymentDate("2020-03-01", "2020-03-31", "2020-03-31"), Regular)
    )

    calculateRegularPay(wage, periods) mustBe expected
  }

  "apportion the user entered salary for partial periods" in new RegularPayCalculator {
    val wage = Amount(2000.0)
    val partial = partialPeriodWithPaymentDate("2020, 4, 1", "2020, 4, 30", "2020, 4, 1", "2020, 4, 15", "2020, 4, 30")
    val periods = defaultJourneyCoreData.periods :+ partial

    val expected = Seq(
      paymentWithFullPeriod(2000.0, fullPeriodWithPaymentDate("2020,3,1", "2020,3,31", "2020, 3, 31"), Regular),
      paymentWithPartialPeriod(1000.0, 1000.0, partial, Regular)
    )

    calculateRegularPay(wage, periods) mustBe expected
  }

}
