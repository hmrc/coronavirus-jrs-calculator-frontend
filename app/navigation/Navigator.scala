/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package navigation

import java.time.LocalDate

import config.FrontendAppConfig
import javax.inject.{Inject, Singleton}
import play.api.mvc.Call
import controllers.routes
import handlers.LastYearPayControllerRequestHandler
import models.PayQuestion.{Regularly, Varies}
import pages.{PayDatePage, _}
import models.{UserAnswers, _}

@Singleton
class Navigator @Inject()(appConfig: FrontendAppConfig) extends LastYearPayControllerRequestHandler {

  val apr7th2019 = LocalDate.of(2019, 4, 7)

  private val normalRoutes: Page => UserAnswers => Call = {
    case ClaimPeriodStartPage =>
      _ =>
        routes.ClaimPeriodEndController.onPageLoad(NormalMode)

    case ClaimPeriodEndPage =>
      _ =>
        routes.FurloughStartDateController.onPageLoad(NormalMode)
    case FurloughQuestionPage =>
      furloughQuestionRoutes
    case FurloughStartDatePage =>
      furloughQuestionRoutes
    case FurloughEndDatePage =>
      _ =>
        routes.PaymentFrequencyController.onPageLoad(NormalMode)
    case PaymentFrequencyPage =>
      _ =>
        routes.PayQuestionController.onPageLoad(NormalMode)
    case PayQuestionPage =>
      payQuestionRoutes
    case SalaryQuestionPage =>
      _ =>
        routes.PayDateController.onPageLoad(1)
    case VariableLengthEmployedPage =>
      variableLengthEmployedRoutes
    case EmployeeStartDatePage =>
      employeeStartDateRoutes
    case PartialPayBeforeFurloughPage =>
      _ =>
        routes.PartialPayAfterFurloughController.onPageLoad()
    case PartialPayAfterFurloughPage =>
      partialPayAfterFurloughRoutes
    case VariableGrossPayPage =>
      _ =>
        routes.PayDateController.onPageLoad(1)
    case LastPayDatePage =>
      lastPayDateRoutes
    case NicCategoryPage =>
      _ =>
        routes.PensionAutoEnrolmentController.onPageLoad(NormalMode)
    case PensionAutoEnrolmentPage =>
      _ =>
        routes.FurloughCalculationsController.onPageLoad(NormalMode)
    case FurloughCalculationsPage =>
      furloughCalculationsRoutes
    case _ =>
      _ =>
        routes.RootPageController.onPageLoad()
  }

  private val checkRouteMap: Page => UserAnswers => Call = {
    case _ =>
      _ =>
        routes.CheckYourAnswersController.onPageLoad()
  }

  private val payDateRoutes: (Int, UserAnswers) => Call = { (previousIdx, userAnswers) =>
    (for {
      claimEndDate <- userAnswers.get(ClaimPeriodEndPage)
      lastPayDate  <- userAnswers.getList(PayDatePage).lastOption
    } yield {
      if (lastPayDate.isAfter(claimEndDate.minusDays(1))) {
        routes.LastPayDateController.onPageLoad(NormalMode)
      } else {
        routes.PayDateController.onPageLoad(previousIdx + 1)
      }
    }).getOrElse(routes.ErrorController.internalServerError())
  }

  private val lastYearPayRoutes: (Int, UserAnswers) => Call = { (previousIdx, userAnswers) =>
    getPayDates(userAnswers).fold(
      routes.ErrorController.somethingWentWrong()
    ) { payDates =>
      payDates.lift.apply(previousIdx) match {
        case Some(_) => routes.LastYearPayController.onPageLoad(previousIdx + 1)
        case None    => routes.NicCategoryController.onPageLoad(NormalMode)
      }
    }
  }

  private val idxRoutes: Page => (Int, UserAnswers) => Call = {
    case PayDatePage     => payDateRoutes
    case LastYearPayPage => lastYearPayRoutes
  }

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, idx: Option[Int] = None): Call = mode match {
    case NormalMode =>
      idx.fold(normalRoutes(page)(userAnswers))(idx => idxRoutes(page)(idx, userAnswers))
    case CheckMode =>
      checkRouteMap(page)(userAnswers)
  }

  private def partialPayAfterFurloughRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.get(VariableLengthEmployedPage) match {
      case Some(VariableLengthEmployed.Yes) => routes.LastYearPayController.onPageLoad(1)
      case Some(VariableLengthEmployed.No) =>
        userAnswers.get(EmployeeStartDatePage) match {
          case Some(date) if date.isBefore(apr7th2019) => routes.LastYearPayController.onPageLoad(1)
          case _                                       => routes.NicCategoryController.onPageLoad(NormalMode)
        }
      case None => routes.NicCategoryController.onPageLoad(NormalMode)
    }
  }

  private def furloughQuestionRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.get(FurloughQuestionPage) match {
      case Some(FurloughQuestion.Yes) => routes.FurloughEndDateController.onPageLoad(NormalMode)
      case Some(FurloughQuestion.No)  => routes.PaymentFrequencyController.onPageLoad(NormalMode)
      case None                       => routes.FurloughQuestionController.onPageLoad(NormalMode)
    }
  }

  private def payQuestionRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.get(PayQuestionPage) match {
      case Some(Regularly) => routes.SalaryQuestionController.onPageLoad(NormalMode)
      case Some(Varies) if appConfig.variableJourneyEnabled =>
        routes.VariableLengthEmployedController.onPageLoad(NormalMode)
      case Some(Varies) => routes.ComingSoonController.onPageLoad()
      case None         => routes.PayQuestionController.onPageLoad(NormalMode)
    }
  }

  private def variableLengthEmployedRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.get(VariableLengthEmployedPage) match {
      case Some(VariableLengthEmployed.Yes) if appConfig.variableJourneyEnabled =>
        routes.VariableGrossPayController.onPageLoad(NormalMode)
      case Some(VariableLengthEmployed.Yes) => routes.ComingSoonController.onPageLoad(false)
      case Some(VariableLengthEmployed.No)  => routes.EmployeeStartDateController.onPageLoad(NormalMode)
      case _                                => routes.VariableLengthEmployedController.onPageLoad(NormalMode)
    }
  }

  private def furloughCalculationsRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.get(FurloughCalculationsPage) match {
      case Some(FurloughCalculations.Yes) => routes.ComingSoonController.onPageLoad(true)
      case Some(FurloughCalculations.No)  => routes.ConfirmationController.onPageLoad()
      case _                              => routes.FurloughCalculationsController.onPageLoad(NormalMode)
    }
  }

  private def employeeStartDateRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.get(EmployeeStartDatePage) match {
      case Some(date) if shouldShowVariableGrossPayPage(date) =>
        routes.VariableGrossPayController.onPageLoad(NormalMode)
      case Some(date) if date.isBefore(apr7th2019) => routes.ComingSoonController.onPageLoad(false)
      case _                                       => routes.EmployeeStartDateController.onPageLoad(NormalMode)
    }
  }

  private def lastPayDateRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.get(PayQuestionPage) match {
      case Some(Regularly) => routes.NicCategoryController.onPageLoad(NormalMode)
      case Some(Varies)    => routes.PartialPayBeforeFurloughController.onPageLoad()
      case None            => routes.PayQuestionController.onPageLoad(NormalMode)
    }
  }

  private def shouldShowVariableGrossPayPage(date: LocalDate) =
    date.isAfter(apr7th2019) || (date.isBefore(apr7th2019) && appConfig.variableJourneyEnabled)
}
