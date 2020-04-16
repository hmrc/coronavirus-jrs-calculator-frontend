/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}
import models.{PaymentFrequency, TaxYear, TaxYearEnding2020, TaxYearEnding2021}

case class FrequencyTaxYearKey(paymentFrequency: PaymentFrequency, taxYear: TaxYear, rate: Rate)
case class Threshold(lower: BigDecimal)

sealed trait Rate {
  val value: BigDecimal
}
case class NiRate(value: BigDecimal = 13.8 / 100) extends Rate
case class PensionRate(value: BigDecimal = 3.0 / 100) extends Rate

object FrequencyTaxYearThresholdMapping {
  //TODO: This map could be loaded from application.conf
  val mappings: Map[FrequencyTaxYearKey, Threshold] = Map(
    FrequencyTaxYearKey(Monthly, TaxYearEnding2020, NiRate())          -> Threshold(719.00),
    FrequencyTaxYearKey(Monthly, TaxYearEnding2021, NiRate())          -> Threshold(732.00),
    FrequencyTaxYearKey(FourWeekly, TaxYearEnding2020, NiRate())       -> Threshold(664.00),
    FrequencyTaxYearKey(FourWeekly, TaxYearEnding2021, NiRate())       -> Threshold(676.00),
    FrequencyTaxYearKey(FortNightly, TaxYearEnding2020, NiRate())      -> Threshold(332.00),
    FrequencyTaxYearKey(FortNightly, TaxYearEnding2021, NiRate())      -> Threshold(338.00),
    FrequencyTaxYearKey(Weekly, TaxYearEnding2020, NiRate())           -> Threshold(166.00),
    FrequencyTaxYearKey(Weekly, TaxYearEnding2021, NiRate())           -> Threshold(169.00),
    FrequencyTaxYearKey(Monthly, TaxYearEnding2020, PensionRate())     -> Threshold(511.00),
    FrequencyTaxYearKey(Monthly, TaxYearEnding2021, PensionRate())     -> Threshold(520.00),
    FrequencyTaxYearKey(FourWeekly, TaxYearEnding2020, PensionRate())  -> Threshold(472.00),
    FrequencyTaxYearKey(FourWeekly, TaxYearEnding2021, PensionRate())  -> Threshold(480.00),
    FrequencyTaxYearKey(FortNightly, TaxYearEnding2020, PensionRate()) -> Threshold(236.00),
    FrequencyTaxYearKey(FortNightly, TaxYearEnding2021, PensionRate()) -> Threshold(240.00),
    FrequencyTaxYearKey(Weekly, TaxYearEnding2020, PensionRate())      -> Threshold(118.00),
    FrequencyTaxYearKey(Weekly, TaxYearEnding2021, PensionRate())      -> Threshold(120.00)
  )
}
case class FurloughCap(value: BigDecimal)

object FurloughCapMapping {
  val mappings = Map[PaymentFrequency, FurloughCap](
    Monthly     -> FurloughCap(2500.00),
    Weekly      -> FurloughCap(576.92),
    FortNightly -> FurloughCap(1153.84),
    FourWeekly  -> FurloughCap(2307.68)
  )
}
