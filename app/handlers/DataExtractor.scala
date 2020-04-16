/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package handlers

import java.time.LocalDate

import models.FurloughDates.StartedInClaim
import models.FurloughQuestion.{No, Yes}
import models.{ClaimPeriodModel, FurloughDates, FurloughPeriod, FurloughQuestion, NicCategory, PayPeriod, PayQuestion, PaymentFrequency, PensionStatus, UserAnswers}
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
    for {
      data <- extract(userAnswers)
      furloughDates = userAnswers.get(FurloughDatesPage)
      furloughStart = userAnswers.get(FurloughStartDatePage)
    } yield {
      data.furloughQuestion match {
        case Yes => FurloughPeriod(data.claimPeriod.start, data.claimPeriod.end)
        case No =>
          processFurloughDates(data, furloughDates, furloughStart)
            .getOrElse(FurloughPeriod(data.claimPeriod.start, data.claimPeriod.end)) //TODO fix this
      }
    }

  private def processFurloughDates(
    data: MandatoryData,
    furloughDates: Option[FurloughDates],
    furloughStart: Option[LocalDate]): Option[FurloughPeriod] =
    for {
      start <- furloughStart
    } yield {
      furloughDates match {
        case Some(StartedInClaim) => FurloughPeriod(start, data.claimPeriod.end)
      }
    }
}
