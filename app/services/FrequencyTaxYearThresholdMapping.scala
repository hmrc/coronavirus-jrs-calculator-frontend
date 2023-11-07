/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package services

import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}
import models.{PaymentFrequency, TaxYear, TaxYearEnding2020, TaxYearEnding2021}

case class Threshold(value: BigDecimal, taxYear: TaxYear, frequency: PaymentFrequency) {
  def messagesKey: String = taxYear match {
    case TaxYearEnding2020 => "20"
    case TaxYearEnding2021 => "21"
  }
}

sealed trait Rate {
  val value: BigDecimal
}
case class NiRate(value: BigDecimal = 13.8 / 100)     extends Rate
case class PensionRate(value: BigDecimal = 3.0 / 100) extends Rate

object FrequencyTaxYearThresholdMapping {
  def thresholdFor(frequency: PaymentFrequency, taxYear: TaxYear, rate: Rate): Threshold =
    frequency match {
      case Weekly      => weeklyThreshold(taxYear, rate)
      case FortNightly => fortnightlyThreshold(taxYear, rate)
      case FourWeekly  => fourWeeklyThreshold(taxYear, rate)
      case Monthly     => monthlyThreshold(taxYear, rate)
    }

  def weeklyThreshold(taxYear: TaxYear, rate: Rate): Threshold =
    (taxYear, rate) match {
      case (TaxYearEnding2020, _: NiRate)      => Threshold(166.00, TaxYearEnding2020, Weekly)
      case (TaxYearEnding2021, _: NiRate)      => Threshold(169.00, TaxYearEnding2021, Weekly)
      case (TaxYearEnding2020, _: PensionRate) => Threshold(118.00, TaxYearEnding2020, Weekly)
      case (TaxYearEnding2021, _: PensionRate) => Threshold(120.00, TaxYearEnding2021, Weekly)
    }

  def fortnightlyThreshold(taxYear: TaxYear, rate: Rate): Threshold =
    (taxYear, rate) match {
      case (TaxYearEnding2020, _: NiRate)      => Threshold(332.00, TaxYearEnding2020, FortNightly)
      case (TaxYearEnding2021, _: NiRate)      => Threshold(338.00, TaxYearEnding2021, FortNightly)
      case (TaxYearEnding2020, _: PensionRate) => Threshold(236.00, TaxYearEnding2020, FortNightly)
      case (TaxYearEnding2021, _: PensionRate) => Threshold(240.00, TaxYearEnding2021, FortNightly)
    }

  def fourWeeklyThreshold(taxYear: TaxYear, rate: Rate): Threshold =
    (taxYear, rate) match {
      case (TaxYearEnding2020, _: NiRate)      => Threshold(664.00, TaxYearEnding2020, FourWeekly)
      case (TaxYearEnding2021, _: NiRate)      => Threshold(676.00, TaxYearEnding2021, FourWeekly)
      case (TaxYearEnding2020, _: PensionRate) => Threshold(472.00, TaxYearEnding2020, FourWeekly)
      case (TaxYearEnding2021, _: PensionRate) => Threshold(480.00, TaxYearEnding2021, FourWeekly)
    }

  def monthlyThreshold(taxYear: TaxYear, rate: Rate): Threshold =
    (taxYear, rate) match {
      case (TaxYearEnding2020, _: NiRate)      => Threshold(719.00, TaxYearEnding2020, Monthly)
      case (TaxYearEnding2021, _: NiRate)      => Threshold(732.00, TaxYearEnding2021, Monthly)
      case (TaxYearEnding2020, _: PensionRate) => Threshold(512.00, TaxYearEnding2020, Monthly)
      case (TaxYearEnding2021, _: PensionRate) => Threshold(520.00, TaxYearEnding2021, Monthly)
    }
}
