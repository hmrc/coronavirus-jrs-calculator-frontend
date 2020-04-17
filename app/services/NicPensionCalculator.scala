/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import models.Calculation.{NicCalculationResult, PensionCalculationResult}
import models.{Amount, CalculationResult, Payment, PaymentFrequency, PeriodBreakdown}
import play.api.Logger
import utils.TaxYearFinder
import utils.AmountRounding._

import scala.math.BigDecimal.RoundingMode

trait NicPensionCalculator extends TaxYearFinder with FurloughCapCalculator {

  def calculateGrant(paymentFrequency: PaymentFrequency, furloughPayment: Seq[PeriodBreakdown], rate: Rate): CalculationResult = {
    val paymentDateBreakdowns: Seq[PeriodBreakdown] =
      furloughPayment.map(payment => payment.copy(payment = Payment(Amount(calculate(paymentFrequency, payment, rate)))))

    rate match {
      case NiRate(_) =>
        CalculationResult(NicCalculationResult, paymentDateBreakdowns.map(_.payment.amount.value).sum, paymentDateBreakdowns)
      case PensionRate(_) =>
        CalculationResult(PensionCalculationResult, paymentDateBreakdowns.map(_.payment.amount.value).sum, paymentDateBreakdowns)
    }
  }

  protected def calculate(paymentFrequency: PaymentFrequency, furloughPayment: PeriodBreakdown, rate: Rate): BigDecimal = {
    val frequencyTaxYearKey = FrequencyTaxYearKey(paymentFrequency, taxYearAt(furloughPayment.periodWithPaymentDate.paymentDate), rate)

    FrequencyTaxYearThresholdMapping.mappings
      .get(frequencyTaxYearKey)
      .fold {
        Logger.warn(s"Unable to find a threshold for $frequencyTaxYearKey")
        BigDecimal(0).setScale(2)
      } { threshold =>
        val cap = furloughPayment.furloughCap.value.setScale(0, RoundingMode.DOWN) //Remove the pennies
        val cappedFurloughPayment = cap.min(furloughPayment.payment.amount.value)

        if (cappedFurloughPayment < threshold.lower) {
          BigDecimal(0).setScale(2)
        } else {
          roundWithMode((cappedFurloughPayment - threshold.lower) * frequencyTaxYearKey.rate.value, RoundingMode.HALF_UP)
        }
      }
  }
}
