/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package handlers

import models.Calculation.{NicCalculationResult, PensionCalculationResult}
import models.NicCategory.{Nonpayable, Payable}
import models.PensionStatus.{DoesContribute, DoesNotContribute}
import models.{AdditionalPayment, Amount, CalculationResult, FullPeriodBreakdown, NicCategory, PartialPeriodBreakdown, PaymentFrequency, PensionStatus, Period, TopUpPayment, UserAnswers}
import services._
import viewmodels.{ConfirmationDataResult, ConfirmationMetadata, ConfirmationViewBreakdown}

trait ConfirmationControllerRequestHandler
    extends FurloughCalculator with NicCalculator with PensionCalculator with JourneyBuilder with ReferencePayCalculator {

  def loadResultData(userAnswers: UserAnswers): Option[ConfirmationDataResult] =
    for {
      breakdown <- breakdown(userAnswers)
      metadata  <- meta(userAnswers)
    } yield ConfirmationDataResult(metadata, breakdown)

  private def breakdown(userAnswers: UserAnswers): Option[ConfirmationViewBreakdown] =
    for {
      questions <- extractBranchingQuestions(userAnswers)
      data      <- journeyData(define(questions), userAnswers)
      payments = calculateReferencePay(data)
      furlough = calculateFurloughGrant(data.frequency, payments)
      niAnswer <- extractNicCategory(userAnswers)
      ni = calculateNi(furlough, niAnswer, data.frequency, Seq.empty, Seq.empty)
      pensionAnswer <- extractPensionStatus(userAnswers)
      pension = calculatePension(furlough, pensionAnswer, data.frequency)
    } yield ConfirmationViewBreakdown(furlough, ni, pension)

  private def meta(userAnswers: UserAnswers): Option[ConfirmationMetadata] =
    for {
      furloughPeriod <- extractFurloughPeriod(userAnswers)
      claimStart     <- extractClaimPeriodStart(userAnswers)
      claimEnd       <- extractClaimPeriodEnd(userAnswers)
      frequency      <- extractPaymentFrequency(userAnswers)
      nicCategory    <- extractNicCategory(userAnswers)
      pensionStatus  <- extractPensionStatus(userAnswers)
    } yield ConfirmationMetadata(Period(claimStart, claimEnd), furloughPeriod, frequency, nicCategory, pensionStatus)

  private def calculateNi(
    furloughResult: CalculationResult,
    nic: NicCategory,
    frequency: PaymentFrequency,
    additionals: Seq[AdditionalPayment],
    topUps: Seq[TopUpPayment]): CalculationResult =
    nic match {
      case Payable => calculateNicGrant(frequency, furloughResult.payPeriodBreakdowns, additionals, topUps)
      case Nonpayable =>
        CalculationResult(
          NicCalculationResult,
          0.0,
          furloughResult.payPeriodBreakdowns.map {
            case FullPeriodBreakdown(_, withPaymentDate)       => FullPeriodBreakdown(Amount(0.0), withPaymentDate)
            case PartialPeriodBreakdown(_, _, withPaymentDate) => PartialPeriodBreakdown(Amount(0.0), Amount(0.0), withPaymentDate)
          }
        )
    }

  private def calculatePension(
    furloughResult: CalculationResult,
    payStatus: PensionStatus,
    frequency: PaymentFrequency): CalculationResult =
    payStatus match {
      case DoesContribute => calculatePensionGrant(frequency, furloughResult.payPeriodBreakdowns)
      case DoesNotContribute =>
        CalculationResult(
          PensionCalculationResult,
          0.0,
          furloughResult.payPeriodBreakdowns.map {
            case FullPeriodBreakdown(_, withPaymentDate)       => FullPeriodBreakdown(Amount(0.0), withPaymentDate)
            case PartialPeriodBreakdown(_, _, withPaymentDate) => PartialPeriodBreakdown(Amount(0.0), Amount(0.0), withPaymentDate)
          }
        )
    }
}
