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

package models

import play.api.libs.json.{Format, JsResult, JsValue, Json}

import java.time.LocalDate
import java.time.temporal.ChronoUnit

final case class Period(start: LocalDate, end: LocalDate) {

  val policyStart                         = LocalDate.of(2020, 3, 1)
  val yearsBetweenPolicyStartAndPeriodEnd = ChronoUnit.YEARS.between(policyStart, end).toInt.abs

  def substractDays(days: Int): Period          = Period(start.minusDays(days), end.minusDays(days))
  def substractYears(years: Int): Period        = Period(start.minusYears(years), end.minusYears(years))
  def substract52Weeks(nTimes: Int = 1): Period = Period(start.minusDays(364 * nTimes), end.minusDays(364 * nTimes))
}

object Period {
  implicit val defaultFormat: Format[Period] = Json.format[Period]
  implicit class Counter(period: Period) {
    def countDays: Int =
      (ChronoUnit.DAYS.between(period.start, period.end) + 1).toInt

    def countHours: Int = countDays * 24
  }
}

sealed trait Periods {
  val period: Period
}

final case class FullPeriod(period: Period) extends Periods

object FullPeriod {
  implicit val defaultFormat: Format[FullPeriod] = Json.format[FullPeriod]
}
final case class PartialPeriod(original: Period, partial: Period) extends Periods {
  override val period          = original
  def isFurloughStart: Boolean = original.start.isBefore(partial.start)
  def isFurloughEnd: Boolean   = original.end.isAfter(partial.end)
}

object PartialPeriod {
  implicit val defaultFormat: Format[PartialPeriod] = Json.format[PartialPeriod]
}

object Periods {
  implicit val defaultFormat: Format[Periods] = new Format[Periods] {
    override def writes(o: Periods): JsValue =
      o match {
        case fp: FullPeriod    => Json.writes[FullPeriod].writes(fp)
        case pp: PartialPeriod => Json.writes[PartialPeriod].writes(pp)
      }

    override def reads(json: JsValue): JsResult[Periods] =
      if ((json \ "partial").isDefined)
        Json.reads[PartialPeriod].reads(json)
      else
        Json.reads[FullPeriod].reads(json)
  }
}

sealed trait PeriodWithPaymentDate {
  val period: Periods
  val paymentDate: PaymentDate
}
case class FullPeriodWithPaymentDate(period: FullPeriod, paymentDate: PaymentDate)       extends PeriodWithPaymentDate
case class PartialPeriodWithPaymentDate(period: PartialPeriod, paymentDate: PaymentDate) extends PeriodWithPaymentDate

case class PhaseTwoPeriod(periodWithPaymentDate: PeriodWithPaymentDate, actualHours: Option[Hours], usualHours: Option[Hours]) {
  def isPartTime: Boolean = actualHours.isDefined && usualHours.isDefined
  def actual: BigDecimal  = actualHours.defaulted.value
  def usual: BigDecimal   = usualHours.defaulted.value
  def furloughed: BigDecimal = {
    val furloughed = usual - actual
    if (furloughed < 0)
      0.0
    else
      furloughed
  }
  def isFullTime: Boolean = isPartTime && furloughed == 0
}
