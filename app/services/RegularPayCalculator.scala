/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import models.PayMethod.Regular
import models.{Amount, FullPeriodWithPaymentDate, PartialPeriodWithPaymentDate, PaymentWithFullPeriod, PaymentWithPartialPeriod, PaymentWithPeriod, PeriodWithPaymentDate, RegularPayData}

trait RegularPayCalculator extends Calculators {

  def calculateRegularPay(wage: Amount, periods: Seq[PeriodWithPaymentDate]): Seq[PaymentWithPeriod] =
    periods.map {
      case fp: FullPeriodWithPaymentDate => PaymentWithFullPeriod(wage, fp, Regular)
      case pp: PartialPeriodWithPaymentDate =>
        val furloughAmount = partialPeriodDailyCalculation(wage, pp.period)
        val nonFurlough = Amount(wage.value - furloughAmount.value)
        PaymentWithPartialPeriod(nonFurlough, furloughAmount, pp, Regular)
    }

}
