/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import java.time.LocalDate

import play.api.libs.json.{Format, Json}
import utils.ValueClassFormat

case class Payment(amount: Amount)

object Payment {
  implicit val defaultFormat: Format[Payment] = Json.format[Payment]
}

case class PaymentDate(value: LocalDate)

object PaymentDate {
  implicit val defaultFormat: Format[PaymentDate] =
    ValueClassFormat.format(dateString => PaymentDate.apply(LocalDate.parse(dateString)))(_.value)
}

case class PaymentWithPeriod(amount: Amount, period: Period)

case class PeriodBreakdown(payment: Payment, periodWithPaymentDate: PeriodWithPaymentDate, furloughCap: Amount)

object PeriodBreakdown {
  implicit val defaultFormat: Format[PeriodBreakdown] = Json.format[PeriodBreakdown]
}
