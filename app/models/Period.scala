/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import java.time.LocalDate
import java.time.temporal.ChronoUnit

import play.api.libs.json.{Format, JsResult, JsValue, Json}

final case class Period(start: LocalDate, end: LocalDate)

object Period {
  implicit val defaultFormat: Format[Period] = Json.format

  implicit class DaysCounter(period: Period) {
    def countDays: Int =
      (ChronoUnit.DAYS.between(period.start, period.end) + 1).toInt
  }
}

sealed trait Periods {
  val period: Period
}
final case class FullPeriod(period: Period) extends Periods
final case class PartialPeriod(original: Period, partial: Period) extends Periods {
  override val period = original
  def isFurloughStart: Boolean = original.start.isBefore(partial.start)
  def isFurloughEnd: Boolean = original.end.isAfter(partial.end)
}

object FullPeriod {
  implicit val defaultFormat: Format[FullPeriod] = Json.format
}

object PartialPeriod {
  implicit val defaultFormat: Format[PartialPeriod] = Json.format
}

sealed trait PeriodWithPaymentDate {
  val period: Periods
  val paymentDate: PaymentDate
}
case class FullPeriodWithPaymentDate(period: FullPeriod, paymentDate: PaymentDate) extends PeriodWithPaymentDate
case class PartialPeriodWithPaymentDate(period: PartialPeriod, paymentDate: PaymentDate) extends PeriodWithPaymentDate

object PeriodWithPaymentDate {

  implicit val defaultFormat: Format[PeriodWithPaymentDate] = new Format[PeriodWithPaymentDate] {
    override def writes(o: PeriodWithPaymentDate): JsValue = o match {
      case fp: FullPeriodWithPaymentDate    => Json.writes[FullPeriodWithPaymentDate].writes(fp)
      case pp: PartialPeriodWithPaymentDate => Json.writes[PartialPeriodWithPaymentDate].writes(pp)
    }

    override def reads(json: JsValue): JsResult[PeriodWithPaymentDate] =
      if ((json \ "nonFurloughPay").isDefined) Json.reads[PartialPeriodWithPaymentDate].reads(json)
      else Json.reads[FullPeriodWithPaymentDate].reads(json)
  }
}

object FullPeriodWithPaymentDate {
  implicit val defaultFormat: Format[FullPeriodWithPaymentDate] = Json.format[FullPeriodWithPaymentDate]
}

object PartialPeriodWithPaymentDate {
  implicit val defaultFormat: Format[PartialPeriodWithPaymentDate] = Json.format[PartialPeriodWithPaymentDate]
}
