/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import javax.inject.{Inject, Singleton}
import models.{FurloughPayment, PaymentFrequency}

trait NicCalculatorService {

  def calculateNic(paymentFrequency: PaymentFrequency, furloughPayment: FurloughPayment): Double =
    if(furloughPayment.amount < 719.00) 0
    else BigDecimal(((furloughPayment.amount - 719.00) * 0.138)).setScale(2, BigDecimal.RoundingMode.DOWN).toDouble

}
