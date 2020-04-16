/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package handlers

import java.time.LocalDate

import models.FurloughDates.{EndedInClaim, StartedAndEndedInClaim, StartedInClaim}
import models.FurloughQuestion.{No, Yes}
import models.{FurloughPeriod, FurloughQuestion, NicCategory, PayPeriod, PayQuestion, PaymentFrequency, PensionStatus, UserAnswers}
import pages._

case class MandatoryData(
  claimPeriod: PayPeriod,
  paymentFrequency: PaymentFrequency,
  nicCategory: NicCategory,
  pensionStatus: PensionStatus,
  payQuestion: PayQuestion,
  furloughQuestion: FurloughQuestion,
  payDates: Seq[LocalDate]) //TODO make it a NonEmptyList

trait DataExtractor {

  def extract(userAnswers: UserAnswers): Option[MandatoryData] =
    for {
      claimStart  <- userAnswers.get(ClaimPeriodStartPage)
      claimEnd    <- userAnswers.get(ClaimPeriodEndPage)
      frequency   <- userAnswers.get(PaymentFrequencyPage)
      nic         <- userAnswers.get(NicCategoryPage)
      pension     <- userAnswers.get(PensionAutoEnrolmentPage)
      payQuestion <- userAnswers.get(PayQuestionPage)
      furlough    <- userAnswers.get(FurloughQuestionPage)
      payDate = userAnswers.getList(PayDatePage)
    } yield MandatoryData(PayPeriod(claimStart, claimEnd), frequency, nic, pension, payQuestion, furlough, payDate)

  def extractFurloughPeriod(userAnswers: UserAnswers): Option[FurloughPeriod] =
    extract(userAnswers).flatMap { data =>
      data.furloughQuestion match {
        case Yes => Some(FurloughPeriod(data.claimPeriod.start, data.claimPeriod.end))
        case No  => processFurloughDates(userAnswers)
      }
    }

  private def processFurloughDates(userAnswers: UserAnswers): Option[FurloughPeriod] =
    userAnswers.get(FurloughDatesPage).flatMap { furloughDates =>
      furloughDates match {
        case StartedInClaim         => patchStartDate(userAnswers)
        case EndedInClaim           => patchEndDate(userAnswers)
        case StartedAndEndedInClaim => patchStartAndEndDate(userAnswers)
      }
    }

  private def patchStartDate(userAnswers: UserAnswers): Option[FurloughPeriod] =
    for {
      data  <- extract(userAnswers)
      start <- userAnswers.get(FurloughStartDatePage)
    } yield FurloughPeriod(start, data.claimPeriod.end)

  private def patchEndDate(userAnswers: UserAnswers): Option[FurloughPeriod] =
    for {
      data <- extract(userAnswers)
      end  <- userAnswers.get(FurloughEndDatePage)
    } yield FurloughPeriod(data.claimPeriod.start, end)

  private def patchStartAndEndDate(userAnswers: UserAnswers): Option[FurloughPeriod] =
    for {
      start <- userAnswers.get(FurloughStartDatePage)
      end   <- userAnswers.get(FurloughEndDatePage)
    } yield FurloughPeriod(start, end)
}
