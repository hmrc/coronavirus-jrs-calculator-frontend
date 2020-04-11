package models

import java.time.LocalDate

case class PayPeriod(start: LocalDate, end: LocalDate)

case class FurloughPayment(amount: Double, payPeriod: PayPeriod)
