/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package navigation

import javax.inject.{Inject, Singleton}

import play.api.mvc.Call
import controllers.routes
import pages._
import models._

@Singleton
class Navigator @Inject()() {

  private val normalRoutes: Page => UserAnswers => Call = {
    case ClaimPeriodPage =>
      _ =>
        routes.PayQuestionController.onPageLoad(NormalMode)
    case PayQuestionPage =>
      _ =>
        routes.PaymentFrequencyController.onPageLoad(NormalMode)
    case PaymentFrequencyPage =>
      _ =>
        routes.SalaryQuestionController.onPageLoad(NormalMode)
    case SalaryQuestionPage =>
      _ =>
        routes.PayDateController.onPageLoad(1)
    case PayDatePage =>
      _ =>
        routes.ReviewPayDatesController.onPageLoad(NormalMode)
    case _ =>
      _ =>
        routes.RootPageController.onPageLoad()
  }

  private val checkRouteMap: Page => UserAnswers => Call = {
    case _ =>
      _ =>
        routes.CheckYourAnswersController.onPageLoad()
  }

  private val idxRoutes: Page => (Int, UserAnswers) => Call = {
    case PayDatePage =>
      (idx, _) =>
        routes.PayDateController.onPageLoad(idx)
  }

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, idx: Option[Int] = None): Call = mode match {
    case NormalMode =>
      idx.fold(normalRoutes(page)(userAnswers))(idx => idxRoutes(page)(idx, userAnswers))
    case CheckMode =>
      checkRouteMap(page)(userAnswers)
  }
}
