/*
 * Copyright 2023 HM Revenue & Customs
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

import models.PaymentFrequency.{Monthly, _}
import models.{CylbDuration, PaymentFrequency, Period, PeriodWithPaymentDate, Periods}
import uk.gov.hmrc.http.InternalServerException

import java.time.LocalDate

trait PreviousYearPeriod {

  def previousYearPeriod(frequency: PaymentFrequency, period: Periods): Seq[Period] = {
    val cylbDuration = CylbDuration(frequency, period)

    (cylbDuration.previousPeriodDays, cylbDuration.equivalentPeriodDays) match {
      case (0, _) if frequency != Monthly =>
        lastYearPeriods(frequency, period.period).tail
      case (_, 0) =>
        lastYearPeriods(frequency, period.period).take(1)
      case _ =>
        lastYearPeriods(frequency, period.period)
    }
  }

  def cylbCutoff(frequency: PaymentFrequency, periods: Seq[PeriodWithPaymentDate]): LocalDate = {

    val periodWithPaymentDate: PeriodWithPaymentDate = periods.headOption match {
      case None                        => throw new InternalServerException("[PreviousYearPeriod][cylbCutoff] no PeriodWithPaymentDate found")
      case Some(periodWithPaymentDate) => periodWithPaymentDate
    }

    val cylbDuration = CylbDuration(frequency, periodWithPaymentDate.period)

    (cylbDuration.previousPeriodDays, cylbDuration.equivalentPeriodDays) match {
      case (0, _) =>
        lastYearPeriods(frequency, periodWithPaymentDate.period.period).last.start.plusDays(1)
      case _ =>
        lastYearPeriods(frequency, periodWithPaymentDate.period.period).head.start.plusDays(1)
    }
  }

  private def lastYearPeriods(frequency: PaymentFrequency, period: Period): Seq[Period] =
    frequency match {
      case Monthly => Seq(period.substractYears(period.yearsBetweenPolicyStartAndPeriodEnd + 1))
      case _ =>
        val equivalent = period.substract52Weeks(period.yearsBetweenPolicyStartAndPeriodEnd + 1)
        val previous   = equivalent.substractDays(paymentFrequencyDays(frequency))
        Seq(previous, equivalent)
    }
}
