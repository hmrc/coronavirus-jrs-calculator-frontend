/*
 * Copyright 2020 HM Revenue & Customs
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

package navigation

import controllers.routes
import javax.inject.Singleton
import models.AdditionalPaymentStatus.{NoAdditionalPayments, YesAdditionalPayments}
import models.EmployeeStarted.{After1Feb2019, OnOrBefore1Feb2019}
import models.FurloughPeriodQuestion.FurloughedOnDifferentPeriod
import models.FurloughStatus.{FurloughEnded, FurloughOngoing}
import models.PayMethod.{Regular, Variable}
import models.PayPeriodQuestion.UseSamePayPeriod
import models.TopUpStatus.ToppedUp
import models.requests.DataRequest
import pages._
import play.api.mvc.Call

@Singleton
class BackLinkImpl {

  def backLinkFor(currentPage: Page, idx: Option[Int] = None)(implicit request: DataRequest[_]): Call =
    currentPage match {
      case ClaimPeriodEndPage           => routes.ClaimPeriodStartController.onPageLoad()
      case FurloughStartDatePage        => furloughStartBackLink()
      case FurloughStatusPage           => routes.FurloughStartDateController.onPageLoad()
      case FurloughEndDatePage          => routes.FurloughOngoingController.onPageLoad()
      case PaymentFrequencyPage         => paymentFrequencyBackLink()
      case PayMethodPage                => payMethodBackLink()
      case EmployedStartedPage          => routes.PayMethodController.onPageLoad()
      case EmployeeStartDatePage        => routes.VariableLengthEmployedController.onPageLoad()
      case PayDatePage                  => payDateBackLink(idx)
      case LastPayDatePage              => lastPayDateBackLink()
      case LastYearPayPage              => lastYearPayBackLink(idx)
      case AnnualPayAmountPage          => annualPayAmountBackLink()
      case RegularPayAmountPage         => regularPayAmountBackLink()
      case PartialPayBeforeFurloughPage => routes.AnnualPayAmountController.onPageLoad()
      case PartialPayAfterFurloughPage  => partialPayAfterFurloughBackLink()
      case TopUpStatusPage              => topUpStatusBackLink()
      case TopUpPeriodsPage             => routes.TopUpStatusController.onPageLoad()
      case TopUpAmountPage              => topUpAmountBackLink(idx)
      case AdditionalPaymentStatusPage  => additionalPayStatusBackLink()
      case AdditionalPaymentPeriodsPage => routes.AdditionalPaymentStatusController.onPageLoad()
      case AdditionalPaymentAmountPage  => additionalPayAmountPageBackLink(idx)
      case NicCategoryPage              => nicCategoryPageBackLink()
      case PensionStatusPage            => routes.NicCategoryController.onPageLoad()
      case FurloughPeriodQuestionPage   => routes.ClaimPeriodQuestionController.onPageLoad()
      case PayPeriodQuestionPage        => routes.FurloughPeriodQuestionController.onPageLoad()
      case p                            => throw new RuntimeException(s"Back link not yet implemented for $p")
    }

  private def furloughStartBackLink()(implicit request: DataRequest[_]): Call =
    request.userAnswers.get(FurloughPeriodQuestionPage) match {
      case Some(FurloughedOnDifferentPeriod) => routes.FurloughPeriodQuestionController.onPageLoad()
      case _                                 => routes.ClaimPeriodEndController.onPageLoad()
    }

  private def paymentFrequencyBackLink()(implicit request: DataRequest[_]): Call =
    request.userAnswers.get(FurloughStatusPage) match {
      case Some(FurloughOngoing) => routes.FurloughOngoingController.onPageLoad()
      case Some(FurloughEnded)   => routes.FurloughEndDateController.onPageLoad()
      case None                  => routes.FurloughOngoingController.onPageLoad()
    }

  private def payMethodBackLink()(implicit request: DataRequest[_]): Call =
    request.userAnswers.get(PayPeriodQuestionPage) match {
      case Some(_) => routes.PayPeriodQuestionController.onPageLoad()
      case _                                 => routes.PaymentFrequencyController.onPageLoad()
    }


  private def payDateBackLink(idx: Option[Int])(implicit request: DataRequest[_]): Call =
    if (idx.getOrElse(1) == 1) {
      request.userAnswers.get(EmployedStartedPage) match {
        case Some(OnOrBefore1Feb2019) => routes.VariableLengthEmployedController.onPageLoad()
        case Some(After1Feb2019)      => routes.EmployeeStartDateController.onPageLoad()
        case None                     => routes.PayMethodController.onPageLoad()
      }
    } else {
      routes.PayDateController.onPageLoad(idx.getOrElse(1) - 1)
    }

  private def lastPayDateBackLink()(implicit request: DataRequest[_]): Call = {
    val lastIdx = request.userAnswers.getList(PayDatePage).length + 1
    routes.PayDateController.onPageLoad(lastIdx)
  }

  private def lastYearPayBackLink(idx: Option[Int])(implicit request: DataRequest[_]): Call =
    if (idx.getOrElse(1) == 1) {
      routes.LastPayDateController.onPageLoad()
    } else {
      routes.LastYearPayController.onPageLoad(idx.getOrElse(1) - 1)
    }

  private def annualPayAmountBackLink()(implicit request: DataRequest[_]): Call = {
    val lyp = request.userAnswers.getList(LastYearPayPage)
    if (lyp.nonEmpty) {
      val lastIdx = lyp.length + 1
      routes.LastYearPayController.onPageLoad(lastIdx)
    } else {
      request.userAnswers.get(PayPeriodQuestionPage) match {
        case Some(UseSamePayPeriod) => routes.PayMethodController.onPageLoad()
        case _ =>
          //TODO: This should consider PayFrequency and return correct  link as per Journey Flow doc
          val lastIdx = request.userAnswers.getList(PayDatePage).length + 1
          routes.PayDateController.onPageLoad(lastIdx)
      }
    }
  }

  private def regularPayAmountBackLink()(implicit request: DataRequest[_]): Call =
    routes.LastPayDateController.onPageLoad()

  private def partialPayAfterFurloughBackLink()(implicit request: DataRequest[_]): Call =
    request.userAnswers.get(PartialPayBeforeFurloughPage) match {
      case Some(_) => routes.PartialPayBeforeFurloughController.onPageLoad()
      case _       => routes.AnnualPayAmountController.onPageLoad()
    }

  private def topUpStatusBackLink()(implicit request: DataRequest[_]): Call =
    request.userAnswers.get(PartialPayAfterFurloughPage) match {
      case Some(_) => routes.PartialPayAfterFurloughController.onPageLoad()
      case _ =>
        request.userAnswers.get(PartialPayBeforeFurloughPage) match {
          case Some(_) => routes.PartialPayBeforeFurloughController.onPageLoad()
          case _ =>
            request.userAnswers.get(PayMethodPage) match {
              case Some(Regular)  => routes.RegularPayAmountController.onPageLoad()
              case Some(Variable) => routes.AnnualPayAmountController.onPageLoad()
              case None           => routes.PayMethodController.onPageLoad()
            }
        }
    }

  private def topUpAmountBackLink(idx: Option[Int])(implicit request: DataRequest[_]): Call =
    if (idx.getOrElse(1) == 1) {
      request.userAnswers.get(TopUpPeriodsPage) match {
        case Some(_) => routes.TopUpPeriodsController.onPageLoad()
        case _       => routes.TopUpStatusController.onPageLoad()
      }
    } else {
      routes.TopUpAmountController.onPageLoad(idx.getOrElse(1) - 1)
    }

  private def additionalPayStatusBackLink()(implicit request: DataRequest[_]): Call =
    request.userAnswers.get(TopUpStatusPage) match {
      case Some(ToppedUp) =>
        val lastIdx = request.userAnswers.getList(TopUpAmountPage).length + 1
        routes.TopUpAmountController.onPageLoad(lastIdx)

      case _ => routes.TopUpStatusController.onPageLoad()
    }

  private def additionalPayAmountPageBackLink(idx: Option[Int])(implicit request: DataRequest[_]): Call =
    if (idx.getOrElse(1) == 1) {
      request.userAnswers.get(AdditionalPaymentPeriodsPage) match {
        case Some(_) => routes.AdditionalPaymentPeriodsController.onPageLoad()
        case _       => routes.AdditionalPaymentStatusController.onPageLoad()
      }
      routes.AdditionalPaymentStatusController.onPageLoad()
    } else {
      routes.AdditionalPaymentAmountController.onPageLoad(idx.getOrElse(1) - 1)
    }

  private def nicCategoryPageBackLink()(implicit request: DataRequest[_]): Call =
    request.userAnswers.get(AdditionalPaymentStatusPage) match {
      case Some(YesAdditionalPayments) =>
        val lastIdx = request.userAnswers.getList(AdditionalPaymentAmountPage).length + 1
        routes.AdditionalPaymentAmountController.onPageLoad(lastIdx)

      case _ => routes.AdditionalPaymentStatusController.onPageLoad()
    }

}
