/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package viewmodels

import models.{CalculationResult, ClaimPeriodModel, FurloughPeriod, NicCategory, PaymentFrequency}

case class ConfirmationViewBreakdown(furlough: CalculationResult, nic: CalculationResult, pension: CalculationResult)

case class ConfirmationMetadata(
  claimPeriod: ClaimPeriodModel,
  furloughPeriod: FurloughPeriod,
  frequency: PaymentFrequency,
  nic: NicCategory,
  pension: Boolean)
