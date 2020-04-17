/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.Period

import models.{Amount, PartialPeriod, Payment, PaymentFrequency, PeriodBreakdown, PeriodWithPaymentDate}
import play.api.Logger
import utils.AmountRounding.roundWithMode
import utils.TaxYearFinder

import scala.math.BigDecimal.RoundingMode

trait NicCalculator extends TaxYearFinder with FurloughCapCalculator {

  def calculateFullPeriod(frequency: PaymentFrequency, furloughPayment: Payment, periodWithPaymentDate: PeriodWithPaymentDate): PeriodBreakdown = {
    val frequencyTaxYearKey = FrequencyTaxYearKey(frequency, taxYearAt(periodWithPaymentDate.paymentDate), NiRate())

    val roundedFurloughPayment = furloughPayment.amount.value.setScale(0, RoundingMode.DOWN)
    val cap = furloughCap(frequency, periodWithPaymentDate.period).setScale(0, RoundingMode.DOWN)

    val grant = FrequencyTaxYearThresholdMapping.mappings
      .get(frequencyTaxYearKey)
      .fold {
        Logger.warn(s"Unable to find a threshold for $frequencyTaxYearKey")
        BigDecimal(0).setScale(2)
      } { threshold =>
        val cappedFurloughPayment = cap.min(roundedFurloughPayment)

        if(cappedFurloughPayment < threshold.lower) {
          BigDecimal(0).setScale(2)
        } else {
          roundWithMode((cappedFurloughPayment - threshold.lower) * frequencyTaxYearKey.rate.value, RoundingMode.HALF_UP)
        }
      }

    PeriodBreakdown(Payment(Amount(grant)), periodWithPaymentDate, Amount(cap))
  }

  def calculatPartialPeriod(period: PartialPeriod) = ???

}
