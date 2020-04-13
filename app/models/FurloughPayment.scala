/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import java.time.LocalDate

import play.api.libs.json.{Format, Json}
import services.Salary
import utils.ValueClassFormat

sealed trait TaxYear
case object TaxYearEnding2020 extends TaxYear
case object TaxYearEnding2021 extends TaxYear

case class PaymentDate(value: LocalDate)

object PaymentDate {
  implicit val defaultFormat: Format[PaymentDate] =
    ValueClassFormat.format(dateString => PaymentDate.apply(LocalDate.parse(dateString)))(_.value)
}

case class PayPeriod(start: LocalDate, end: LocalDate, paymentDate: PaymentDate)
case class RegularPayment(salary: services.Salary, payPeriod: PayPeriod)

case class FurloughPayment(amount: Double, paymentDate: PaymentDate)

object FurloughPayment {
  implicit val defaultFormat: Format[FurloughPayment] = Json.format
}

case class PaymentDateBreakdown(amount: Double, paymentDate: PaymentDate)

object PaymentDateBreakdown {
  implicit val defaultFormat: Format[PaymentDateBreakdown] = Json.format
}

case class CalculationResult(total: Double, paymentDateBreakdowns: Seq[PaymentDateBreakdown])

object CalculationResult {
  implicit val defaultFormat: Format[CalculationResult] = Json.format[CalculationResult]
}
