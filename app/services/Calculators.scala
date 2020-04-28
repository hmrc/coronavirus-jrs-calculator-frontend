/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import models.Amount
import utils.AmountRounding._

import scala.math.BigDecimal.RoundingMode

trait Calculators {

  def eightyPercent(amount: Amount): Amount = Amount(amount.value * 0.8)

  implicit class AmountRounding(amount: Amount) {
    def halfUp: Amount = roundAmountWithMode(amount, RoundingMode.HALF_UP)
  }
}
