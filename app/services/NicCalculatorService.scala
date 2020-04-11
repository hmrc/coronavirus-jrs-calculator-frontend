/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.LocalDate

import models.{FurloughPayment, PaymentFrequency}

trait NicCalculatorService {

  def calculateNic(paymentFrequency: PaymentFrequency, furloughPayment: FurloughPayment): Double = {
    val cappedFurloughPayment = if(furloughPayment.amount > 2500.00) 2500.00 else furloughPayment.amount
    if(cappedFurloughPayment < threshold(furloughPayment)) 0
    else BigDecimal(((cappedFurloughPayment - threshold(furloughPayment)) * 0.138)).setScale(2, BigDecimal.RoundingMode.DOWN).toDouble

  }

  private def threshold(payment: FurloughPayment): Double =
    if(payment.payPeriod.end.isBefore(LocalDate.of(2020, 4, 5))) 719.00 else 732.00

}
