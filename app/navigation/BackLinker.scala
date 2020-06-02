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
import models.AdditionalPaymentStatus.YesAdditionalPayments
import models.EmployeeStarted.{After1Feb2019, OnOrBefore1Feb2019}
import models.FurloughStatus.FurloughEnded
import models.PayMethod.{Regular, Variable}
import models.TopUpStatus.ToppedUp
import models.requests.DataRequest
import pages._
import pages.info.InfoPage
import play.api.mvc.{Call, Request}

object BackLinker {

  def getFor(currentPage: Page, idx: Option[Int] = None)(implicit request: Request[_]): String =
    request match {
      case dataRequest: DataRequest[_] =>
        currentPage match {
          case _: InfoPage => "#"
          case other       => getBackLink(other, idx)(dataRequest).url
        }
      case _ => "#"
    }

  private def getBackLink(page: Page, idx: Option[Int] = None)(implicit dataRequest: DataRequest[_]): Call =
    page match {
      case ClaimPeriodEndPage           => routes.ClaimPeriodStartController.onPageLoad()
      case FurloughStartDatePage        => routes.ClaimPeriodEndController.onPageLoad()
      case FurloughStatusPage           => routes.FurloughStartDateController.onPageLoad()
      case FurloughEndDatePage          => routes.FurloughOngoingController.onPageLoad()
      case PaymentFrequencyPage         => paymentFrequencyBackLink()
      case PayMethodPage                => routes.PaymentFrequencyController.onPageLoad()
      case EmployeeStartedPage          => routes.PayMethodController.onPageLoad()
      case EmployeeStartDatePage        => routes.VariableLengthEmployedController.onPageLoad()
      case PayDatePage                  => payDateBackLink(idx)
      case LastPayDatePage              => lastPayDateBackLink()
      case LastYearPayPage              => lastYearPayBackLink(idx)
      case AnnualPayAmountPage          => annualPayAmountBackLink()
      case RegularPayAmountPage         => routes.LastPayDateController.onPageLoad()
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

  private def paymentFrequencyBackLink()(implicit request: DataRequest[_]): Call =
    request.userAnswers.get(FurloughStatusPage) match {
      case Some(FurloughEnded) => routes.FurloughEndDateController.onPageLoad()
      case _                   => routes.FurloughOngoingController.onPageLoad()
    }

  private def payDateBackLink(idx: Option[Int])(implicit request: DataRequest[_]): Call =
    if (idx.getOrElse(1) == 1) {
      request.userAnswers.get(EmployeeStartedPage) match {
        case Some(OnOrBefore1Feb2019) => routes.VariableLengthEmployedController.onPageLoad()
        case Some(After1Feb2019)      => routes.EmployeeStartDateController.onPageLoad()
        case None                     => routes.PayMethodController.onPageLoad()
      }
    } else {
      routes.PayDateController.onPageLoad(idx.getOrElse(1) - 1)
    }

  private def lastPayDateBackLink()(implicit request: DataRequest[_]): Call = {
    val lastIdx = request.userAnswers.getList(PayDatePage).length
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
      routes.LastYearPayController.onPageLoad(lyp.length)
    } else {
      routes.LastPayDateController.onPageLoad()
    }
  }

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
        val lastIdx = request.userAnswers.getList(TopUpAmountPage).length
        routes.TopUpAmountController.onPageLoad(lastIdx)

      case _ => routes.TopUpStatusController.onPageLoad()
    }

  private def additionalPayAmountPageBackLink(idx: Option[Int])(implicit request: DataRequest[_]): Call =
    if (idx.getOrElse(1) == 1) {
      request.userAnswers.get(AdditionalPaymentPeriodsPage) match {
        case Some(_) => routes.AdditionalPaymentPeriodsController.onPageLoad()
        case _       => routes.AdditionalPaymentStatusController.onPageLoad()
      }
    } else {
      routes.AdditionalPaymentAmountController.onPageLoad(idx.getOrElse(1) - 1)
    }

  private def nicCategoryPageBackLink()(implicit request: DataRequest[_]): Call =
    request.userAnswers.get(AdditionalPaymentStatusPage) match {
      case Some(YesAdditionalPayments) =>
        val lastIdx = request.userAnswers.getList(AdditionalPaymentAmountPage).length
        routes.AdditionalPaymentAmountController.onPageLoad(lastIdx)

      case _ => routes.AdditionalPaymentStatusController.onPageLoad()
    }
}