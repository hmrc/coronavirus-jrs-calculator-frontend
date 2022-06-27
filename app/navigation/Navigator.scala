/*
 * Copyright 2022 HM Revenue & Customs
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

import cats.data.Validated.{Invalid, Valid}
import config.SchemeConfiguration
import config.featureSwitch._
import controllers.routes
import handlers.LastYearPayControllerRequestHandler
import models.EmployeeRTISubmission.{No, Yes}
import models.EmployeeStarted.{After1Feb2019, OnOrBefore1Feb2019}
import models.PartTimeQuestion.{PartTimeNo, PartTimeYes}
import models.PayMethod.{Regular, Variable}
import models.{UserAnswers, _}
import pages._
import play.api.mvc.Call
import services.PartialPayExtractor
import utils.LocalDateHelpers._
import utils.{LocalDateHelpers, LoggerUtil}

import java.time.LocalDate
import javax.inject.{Inject, Singleton}

@Singleton
class Navigator @Inject()
    extends LastYearPayControllerRequestHandler with LocalDateHelpers with PartialPayExtractor with SchemeConfiguration
    with FeatureSwitching with LoggerUtil {

  private[this] val normalRoutes: Page => UserAnswers => Call = {
    case ClaimPeriodStartPage =>
      _ =>
        routes.ClaimPeriodEndController.onPageLoad()
    case ClaimPeriodEndPage =>
      _ =>
        routes.FurloughStartDateController.onPageLoad()
    case FurloughStatusPage =>
      furloughOngoingRoutes
    case FurloughStartDatePage =>
      furloughOngoingRoutes
    case FurloughEndDatePage =>
      _ =>
        routes.PaymentFrequencyController.onPageLoad()
    case PaymentFrequencyPage =>
      _ =>
        routes.PayMethodController.onPageLoad()
    case PayMethodPage =>
      payMethodRoutes
    case FurloughInLastTaxYearPage =>
      furloughInLastTaxYearRoutes
    case RegularLengthEmployedPage =>
      regularLengthEmployedRoutes
    case PayPeriodsListPage =>
      payPeriodsListRoute
    case RegularPayAmountPage =>
      regularPayAmountRoute
    case EmployeeStartedPage =>
      variableLengthEmployedRoutes
    case PartialPayBeforeFurloughPage =>
      partialPayBeforeFurloughRoutes
    case PartialPayAfterFurloughPage =>
      _ =>
        routes.TopUpStatusController.onPageLoad()
    case AnnualPayAmountPage =>
      annualPayAmountRoutes
    case LastPayDatePage =>
      lastPayDateRoutes
    case NicCategoryPage =>
      _ =>
        routes.PensionContributionController.onPageLoad()
    case PensionStatusPage =>
      _ =>
        routes.ConfirmationController.onPageLoad()
    case TopUpStatusPage =>
      topUpStatusRoutes
    case TopUpPeriodsPage =>
      _ =>
        routes.TopUpAmountController.onPageLoad(1)
    case AdditionalPaymentStatusPage =>
      additionalPaymentStatusRoutes
    case AdditionalPaymentPeriodsPage =>
      _ =>
        routes.AdditionalPaymentAmountController.onPageLoad(1)

    case FurloughPeriodQuestionPage =>
      furloughPeriodQuestionRoutes

    case ClaimPeriodQuestionPage =>
      claimPeriodQuestionRoutes
    case EmployeeStartDatePage     => employeeStartDateRoutes
    case EmployeeRTISubmissionPage => employeeRTISubmissionRoutes
    case PayPeriodQuestionPage =>
      payPeriodQuestionRoutes
    case PartTimeQuestionPage => partTimeQuestionRoute
    case PartTimePeriodsPage =>
      _ =>
        routes.PartTimeNormalHoursController.onPageLoad(1)
    case PreviousFurloughPeriodsPage =>
      firstFurloughedRoutes
    case FirstFurloughDatePage =>
      handlePayDateRoutes
    case OnPayrollBefore30thOct2020Page =>
      onPayrollBefore30thOct2020Routes
    case StatutoryLeavePayPage =>
      statutoryLeavePayRoutes
    case NumberOfStatLeaveDaysPage =>
      numberOfStatLeaveDaysRoutes
    case HasEmployeeBeenOnStatutoryLeavePage =>
      hasBeenOnStatutoryLeaveRoutes
    case _ =>
      _ =>
        routes.RootPageController.onPageLoad()
  }

  private[this] def regularPayAmountRoute: UserAnswers => Call = { userAnswer =>
    if (isPhaseTwoOnwards(userAnswer)) {
      routes.PartTimeQuestionController.onPageLoad()
    } else {
      routes.TopUpStatusController.onPageLoad()
    }
  }

  private[this] def partTimeQuestionRoute: UserAnswers => Call =
    userAnswer =>
      userAnswer.getV(PartTimeQuestionPage) match {
        case Valid(PartTimeYes) => routes.PartTimePeriodsController.onPageLoad()
        case Valid(PartTimeNo)  => skipNicAndPensionFromAug2020(userAnswer)
        case _                  => routes.PartTimePeriodsController.onPageLoad()
    }

  private[this] val payDateRoutes: (Int, UserAnswers) => Call = { (previousIdx, userAnswers) =>
    (for {
      claimEndDate <- userAnswers.getV(ClaimPeriodEndPage).toOption
      lastPayDate  <- userAnswers.getList(PayDatePage).lastOption
    } yield {
      val endDate = userAnswers
        .getV(FurloughEndDatePage)
        .fold(nel => {
          UserAnswers.logWarnings(nel)(logger.logger)
          claimEndDate
        }, { furloughEndDate =>
          earliestOf(claimEndDate, furloughEndDate)
        })

      if (lastPayDate.isAfter(endDate.minusDays(1))) {
        routes.PayPeriodsListController.onPageLoad()
      } else {
        routes.PayDateController.onPageLoad(previousIdx + 1)
      }
    }).getOrElse(routes.ErrorController.internalServerError())
  }

  private[this] def hasStartDateWithinFirstLookbackPeriod(userAnswers: UserAnswers): Boolean =
    (userAnswers.getV(EmployeeStartedPage), userAnswers.getV(EmployeeStartDatePage), getLastYearPeriodsForFirstPeriod(userAnswers)) match {
      case (Valid(OnOrBefore1Feb2019), _, _) => false
      case (Valid(After1Feb2019), Valid(startDate), Valid(lookbackPeriods)) if startDate.isEqualOrBefore(lookbackPeriods.head.start) =>
        false
      case (Valid(After1Feb2019), Valid(startDate), Valid(lookbackPeriods)) =>
        lookbackPeriods match {
          case Nil                => throw new RuntimeException("Lookback periods list was empty")
          case first :: Nil       => startDate.isEqualOrBefore(first.end)
          case _ :: second :: Nil => startDate.isEqualOrBefore(second.end)
        }
      case (_, _, _) => throw new RuntimeException("No lookback periods could be determined")
    }

  private[this] val lastYearPayRoutes: (Int, UserAnswers) => Call = { (previousIdx, userAnswers) =>
    getLastYearPeriods(userAnswers).fold(
      nel => {
        UserAnswers.logErrors(nel)(logger.logger)
        routes.ErrorController.somethingWentWrong()
      }, { periods =>
        periods.lift.apply(previousIdx) match {
          case Some(_) => routes.LastYearPayController.onPageLoad(previousIdx + 1)
          case None    => routes.AnnualPayAmountController.onPageLoad()
        }
      }
    )
  }

  private[this] val topUpAmountRoutes: (Int, UserAnswers) => Call = { (previousIdx, userAnswers) =>
    userAnswers
      .getV(TopUpPeriodsPage)
      .map { topUpPeriods =>
        if (topUpPeriods.isDefinedAt(previousIdx)) {
          routes.TopUpAmountController.onPageLoad(previousIdx + 1)
        } else {
          routes.AdditionalPaymentStatusController.onPageLoad()
        }
      }
      .getOrElse(routes.TopUpPeriodsController.onPageLoad())
  }

  private[this] val partTimeHoursRoutes: (Int, UserAnswers) => Call = { (previousIdx, userAnswers) =>
    userAnswers.getV(PartTimePeriodsPage) match {
      case Valid(periods) if periods.isDefinedAt(previousIdx) => routes.PartTimeNormalHoursController.onPageLoad(previousIdx + 1)
      case Valid(_)                                           => skipNicAndPensionFromAug2020(userAnswers)
      case _                                                  => routes.PartTimePeriodsController.onPageLoad()
    }
  }

  private def skipNicAndPensionFromAug2020(userAnswers: UserAnswers): Call =
    userAnswers.getV(ClaimPeriodStartPage) match {
      case Valid(date) if date.isEqualOrAfter(aug1st2020) => routes.ConfirmationController.onPageLoad()
      case Valid(_)                                       => routes.NicCategoryController.onPageLoad()
      case _                                              => routes.ClaimPeriodStartController.onPageLoad()
    }

  private[this] val partTimeNormalHoursRoutes: (Int, UserAnswers) => Call = { (previousIdx, userAnswers) =>
    userAnswers
      .getV(PartTimePeriodsPage)
      .map { _ =>
        routes.PartTimeHoursController.onPageLoad(previousIdx)
      }
      .getOrElse(routes.PartTimePeriodsController.onPageLoad())
  }

  private[this] val additionalPaymentAmountRoutes: (Int, UserAnswers) => Call = (previousIdx, userAnswers) =>
    userAnswers.getV(AdditionalPaymentPeriodsPage) match {
      case Valid(additionalPaymentPeriods) if additionalPaymentPeriods.isDefinedAt(previousIdx) =>
        routes.AdditionalPaymentAmountController.onPageLoad(previousIdx + 1)
      case Valid(_) => skipNicAndPensionFromAug2020(userAnswers)
      case _        => routes.AdditionalPaymentPeriodsController.onPageLoad()
  }

  private val idxRoutes: Page => (Int, UserAnswers) => Call = {
    case PayDatePage                 => payDateRoutes
    case LastYearPayPage             => lastYearPayRoutes
    case TopUpAmountPage             => topUpAmountRoutes
    case PartTimeHoursPage           => partTimeHoursRoutes
    case PartTimeNormalHoursPage     => partTimeNormalHoursRoutes
    case AdditionalPaymentAmountPage => additionalPaymentAmountRoutes
    case _ =>
      (_, _) =>
        routes.RootPageController.onPageLoad()
  }

  def routeFor(page: Page): Call =
    page match {
      case FurloughStartDatePage => routes.FurloughStartDateController.onPageLoad()
      case TopUpPeriodsPage      => routes.TopUpPeriodsController.onPageLoad()
      case PartTimePeriodsPage   => routes.PartTimePeriodsController.onPageLoad()
      case p =>
        logger.warn(s"can't find the route for the page: $p")
        routes.ErrorController.internalServerError()
    }

  private[this] def partialPayBeforeFurloughRoutes: UserAnswers => Call = { userAnswers =>
    if (hasPartialPayAfter(userAnswers)) {
      routes.PartialPayAfterFurloughController.onPageLoad()
    } else {
      nextPage(PartialPayAfterFurloughPage, userAnswers)
    }
  }

  def nextPage(page: Page, userAnswers: UserAnswers, idx: Option[Int] = None): Call =
    idx.fold(normalRoutes(page)(userAnswers))(idx => idxRoutes(page)(idx, userAnswers))

  private[this] def furloughOngoingRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.getV(FurloughStatusPage) match {
      case Valid(FurloughStatus.FurloughEnded)   => routes.FurloughEndDateController.onPageLoad()
      case Valid(FurloughStatus.FurloughOngoing) => routes.PaymentFrequencyController.onPageLoad()
      case Invalid(err) =>
        UserAnswers.logWarnings(err)(logger.logger)
        routes.FurloughOngoingController.onPageLoad()
    }
  }

  private[this] def nov1stClaimPeriodOnwardsRouting(userAnswers: UserAnswers, payDateRoutes: Call): Call =
    userAnswers.getV(EmployeeStartDatePage) match {
      case Valid(empStartDate) if empStartDate.isBefore(feb1st2019) => payDateRoutes
      case Valid(empStartDate) if empStartDate.isAfter(mar19th2020) =>
        routes.OnPayrollBefore30thOct2020Controller.onPageLoad()
      case Valid(empStartDate) if empStartDate.betweenLowerBoundInclusive(feb1st2019, feb1st2020) => payDateRoutes
      case Valid(empStartDate) if empStartDate.betweenInclusive(feb1st2020, mar19th2020) =>
        routes.EmployeeRTISubmissionController.onPageLoad()
      case Invalid(_) => routes.EmployeeStartDateController.onPageLoad()
    }

  private[this] def employeeStartDateRoutes: UserAnswers => Call = { userAnswers =>
    val payDateRoutes: Call = handlePayDateRoutes(userAnswers)
    (userAnswers.getV(ClaimPeriodStartPage), userAnswers.getV(EmployeeStartDatePage)) match {
      case (Valid(claimPeriodStart), _) if claimPeriodStart.isEqualOrAfter(nov1st2020) =>
        nov1stClaimPeriodOnwardsRouting(userAnswers, payDateRoutes)
      case (Valid(claimPeriodStart), Valid(empStartDate)) if empStartDate.betweenInclusive(feb1st2020, mar19th2020) =>
        routes.EmployeeRTISubmissionController.onPageLoad()
      case (Valid(_), _) => payDateRoutes
      case _             => routes.ClaimPeriodStartController.onPageLoad()
    }
  }

  private[navigation] def routeToEmployeeFirstFurloughed(userAnswers: UserAnswers): Call =
    (userAnswers.getV(FurloughStartDatePage), userAnswers.getV(OnPayrollBefore30thOct2020Page)) match {
      case (Valid(furloughStartDate), Valid(isOnPayrollBefore30thOct2020Page))
          if isOnPayrollBefore30thOct2020Page && furloughStartDate.isAfter(nov8th2020) =>
        routes.PreviousFurloughPeriodsController.onPageLoad()
      case (Valid(furloughStartDate), Valid(isOnPayrollBefore30thOct2020Page))
          if !isOnPayrollBefore30thOct2020Page && furloughStartDate.isAfter(may8th2021) =>
        routes.PreviousFurloughPeriodsController.onPageLoad()
      case _ =>
        handlePayDateRoutes(userAnswers)
    }

  //scalastyle:on

  private[this] val handlePayDateRoutes: UserAnswers => Call = { userAnswers =>
    (userAnswers.getList(PayDatePage).isEmpty, userAnswers.getV(LastPayDatePage)) match {
      case (true, _)           => routes.PayDateController.onPageLoad(1)
      case (false, Invalid(_)) => requireLastPayDateRoutes(userAnswers)
      case _                   => lastPayDateRoutes(userAnswers)
    }
  }

  private[this] def employeeRTISubmissionRoutes: UserAnswers => Call = { userAnswers =>
    (userAnswers.getV(EmployeeRTISubmissionPage), userAnswers.getV(ClaimPeriodStartPage)) match {
      case (Valid(Yes), _) =>
        handlePayDateRoutes(userAnswers)
      case (Valid(No), Valid(claimStartDate)) if claimStartDate.isBefore(nov1st2020) =>
        routes.CalculationUnsupportedController.startDateWithinLookbackUnsupported()
      case (Valid(No), _) =>
        routes.OnPayrollBefore30thOct2020Controller.onPageLoad()
      case (Valid(No), _) =>
        routeToEmployeeFirstFurloughed(userAnswers)
      case _ => routes.EmployeeRTISubmissionController.onPageLoad()
    }
  }

  private[this] def firstFurloughedRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.getV(PreviousFurloughPeriodsPage) match {
      case Valid(true)  => routes.FirstFurloughDateController.onPageLoad()
      case Valid(false) => handlePayDateRoutes(userAnswers)
      case Invalid(_)   => routes.PreviousFurloughPeriodsController.onPageLoad()
    }
  }

  private[this] def payMethodRoutes: UserAnswers => Call = { userAnswers =>
    (userAnswers.getV(PayMethodPage), userAnswers.getV(ClaimPeriodStartPage), userAnswers.getList(PayDatePage)) match {
      case (Valid(Regular), Valid(claimStartDate), _) if claimStartDate.isEqualOrAfter(extensionStartDate) =>
        routes.RegularLengthEmployedController.onPageLoad()
      case (Valid(Regular), Valid(claimStartDate), dates) if dates.isEmpty => routes.PayDateController.onPageLoad(1)
      case (Valid(Regular), Valid(claimStartDate), _)                      => routes.RegularPayAmountController.onPageLoad()
      case (Valid(Variable), Valid(claimStartDate), _) =>
        if (claimStartDate.isBefore(LocalDate.of(2020, 11, 1))) {
          routes.VariableLengthEmployedController.onPageLoad()
        } else {
          routes.FurloughInLastTaxYearController.onPageLoad()
        }
      case (Invalid(_), _, _) => routes.PayMethodController.onPageLoad()
    }
  }

  private[this] def furloughInLastTaxYearRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.getV(FurloughInLastTaxYearPage) match {
      case Valid(true)  => routes.CalculationUnsupportedController.multipleFurloughUnsupported()
      case Valid(false) => routes.VariableLengthEmployedController.onPageLoad()
      case Invalid(_)   => routes.FurloughInLastTaxYearController.onPageLoad()
    }
  }

  private[this] def regularLengthEmployedRoutes: UserAnswers => Call = { userAnswers =>
    (userAnswers.getV(RegularLengthEmployedPage), userAnswers.getV(ClaimPeriodStartPage), userAnswers.getList(PayDatePage)) match {
      case (Valid(RegularLengthEmployed.No), Valid(claimStartDate), _) if claimStartDate.isEqualOrAfter(extensionStartDate) =>
        routes.OnPayrollBefore30thOct2020Controller.onPageLoad()
      case (Valid(_), Valid(claimStartDate), dates) if claimStartDate.isEqualOrAfter(extensionStartDate) && dates.isEmpty =>
        routes.PayDateController.onPageLoad(1)
      case (Valid(_), Valid(claimStartDate), _) if claimStartDate.isEqualOrAfter(extensionStartDate) =>
        routes.RegularPayAmountController.onPageLoad()
      case (Valid(_), Valid(claimStartDate), _) =>
        //if claim is not on or after 1/11/2020, then users should not have seen RegularLengthEmployedPage
        //something must have gone wrong
        routes.RootPageController.onPageLoad()
      case _ => routes.RegularLengthEmployedController.onPageLoad()
    }
  }

  private[this] def regularPayOnPayrollBefore30thOct2020Routes(userAnswers: UserAnswers): Call =
    (userAnswers.getV(OnPayrollBefore30thOct2020Page), userAnswers.getV(ClaimPeriodStartPage), userAnswers.getList(PayDatePage)) match {
      case (Valid(_), Valid(claimStartDate), _) if claimStartDate.isBefore(extensionStartDate) =>
        //if claim is not on or after 1/11/2020, then users should not have seen RegularLengthEmployedPage
        //something must have gone wrong
        routes.RootPageController.onPageLoad()
      case (Valid(_), _, Seq()) =>
        routes.PayDateController.onPageLoad(1)
      case (Valid(_), _, _) =>
        routes.RegularPayAmountController.onPageLoad()
      case (Invalid(_), _, _) =>
        routes.OnPayrollBefore30thOct2020Controller.onPageLoad()
    }

  private[this] def variableLengthEmployedRoutes: UserAnswers => Call = { userAnswers =>
    (userAnswers.getV(EmployeeStartedPage), userAnswers.getList(PayDatePage)) match {
      case (Valid(EmployeeStarted.OnOrBefore1Feb2019), dates) if dates.isEmpty => routes.PayDateController.onPageLoad(1)
      case (Valid(EmployeeStarted.OnOrBefore1Feb2019), _)                      => routes.LastYearPayController.onPageLoad(1)
      case (Valid(EmployeeStarted.After1Feb2019), _)                           => routes.EmployeeStartDateController.onPageLoad()
      case _                                                                   => routes.VariableLengthEmployedController.onPageLoad()
    }
  }

  private[this] def topUpStatusRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.getV(TopUpStatusPage) match {
      case Valid(TopUpStatus.ToppedUp)    => routes.TopUpPeriodsController.onPageLoad()
      case Valid(TopUpStatus.NotToppedUp) => routes.AdditionalPaymentStatusController.onPageLoad()
      case _                              => routes.TopUpStatusController.onPageLoad()
    }
  }

  private[this] def additionalPaymentStatusRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.getV(AdditionalPaymentStatusPage) match {
      case Valid(AdditionalPaymentStatus.YesAdditionalPayments) => routes.AdditionalPaymentPeriodsController.onPageLoad()
      case Valid(AdditionalPaymentStatus.NoAdditionalPayments)  => skipNicAndPensionFromAug2020(userAnswers)
      case _                                                    => routes.AdditionalPaymentStatusController.onPageLoad()
    }
  }

  private[this] def payPeriodsListRoute: UserAnswers => Call = { userAnswers =>
    userAnswers.getV(PayPeriodsListPage) match {
      case Valid(PayPeriodsList.Yes) => requireLastPayDateRoutes(userAnswers)
      case Valid(PayPeriodsList.No)  => routes.PayDateController.onPageLoad(1)
      case Invalid(_)                => routes.PayDateController.onPageLoad(1)
    }
  }

  private[this] def employeeStartedAfter1Feb2019Routes(userAnswers: UserAnswers): Call =
    userAnswers.getV(EmployeeStartDatePage) match {
      case Valid(_) if hasStartDateWithinFirstLookbackPeriod(userAnswers) =>
        routes.CalculationUnsupportedController.startDateWithinLookbackUnsupported()
      case Valid(date) if date.isBefore(dynamicCylbCutoff(userAnswers)) =>
        routes.LastYearPayController.onPageLoad(1)
      case Valid(_) => routes.AnnualPayAmountController.onPageLoad()
      case Invalid(err) =>
        UserAnswers.logErrors(err)(logger.logger)
        routes.EmployeeStartDateController.onPageLoad()
    }

  private[this] def lastPayDateRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.getV(PayMethodPage) match {
      case Valid(Regular) => routes.RegularPayAmountController.onPageLoad()
      case Valid(Variable) =>
        userAnswers.getV(EmployeeStartedPage) match {
          case Valid(EmployeeStarted.OnOrBefore1Feb2019) =>
            routes.LastYearPayController.onPageLoad(1)
          case Valid(EmployeeStarted.After1Feb2019) =>
            employeeStartedAfter1Feb2019Routes(userAnswers)
          case Invalid(err) =>
            UserAnswers.logErrors(err)(logger.logger)
            routes.VariableLengthEmployedController.onPageLoad()
        }
      case Invalid(err) =>
        UserAnswers.logErrors(err)(logger.logger)
        routes.PayMethodController.onPageLoad()
    }
  }

  //scalastyle:on

  private def annualPayAmountRoutes: UserAnswers => Call = { userAnswers =>
    (isPhaseTwoOnwards(userAnswers), isMayExtensionOnwards(userAnswers)) match {
      case (true, true)  => routes.HasEmployeeBeenOnStatutoryLeaveController.onPageLoad()
      case (true, false) => routes.PartTimeQuestionController.onPageLoad()
      case _             => phaseOneAnnualPayAmountRoute(userAnswers)
    }
  }

  private def phaseOneAnnualPayAmountRoute(userAnswers: UserAnswers): Call =
    if (hasPartialPayBefore(userAnswers)) {
      routes.PartialPayBeforeFurloughController.onPageLoad()
    } else if (hasPartialPayAfter(userAnswers)) {
      routes.PartialPayAfterFurloughController.onPageLoad()
    } else {
      routes.TopUpStatusController.onPageLoad()
    }

  private def furloughPeriodQuestionRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.getV(FurloughPeriodQuestionPage) match {
      case Valid(FurloughPeriodQuestion.FurloughedOnSamePeriod)      => routes.PayPeriodQuestionController.onPageLoad()
      case Valid(FurloughPeriodQuestion.FurloughedOnDifferentPeriod) => routes.FurloughStartDateController.onPageLoad()
      case Invalid(e) =>
        UserAnswers.logErrors(e)(logger.logger)
        routes.FurloughPeriodQuestionController.onPageLoad()
    }
  }

  private def claimPeriodQuestionRoutes: UserAnswers => Call =
    userAnswers =>
      userAnswers
        .getV(ClaimPeriodQuestionPage)
        .fold(
          nel => {
            UserAnswers.logErrors(nel)(logger.logger)
            routes.ClaimPeriodQuestionController.onPageLoad()
          }, {
            case ClaimPeriodQuestion.ClaimOnSamePeriod      => routes.FurloughPeriodQuestionController.onPageLoad()
            case ClaimPeriodQuestion.ClaimOnDifferentPeriod => routes.ClaimPeriodStartController.onPageLoad()
          }
      )

  private def payPeriodQuestionRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.getV(PayPeriodQuestionPage) match {
      case Valid(PayPeriodQuestion.UseSamePayPeriod)      => routes.PayMethodController.onPageLoad()
      case Valid(PayPeriodQuestion.UseDifferentPayPeriod) => routes.PaymentFrequencyController.onPageLoad()
      case Invalid(_)                                     => routes.PayPeriodQuestionController.onPageLoad()
    }
  }

  private[navigation] def requireLastPayDateRoutes: UserAnswers => Call = { userAnswers =>
    val endDates = sortedEndDates(userAnswers.getList(PayDatePage))
    val period   = Period(endDates.head.plusDays(1), endDates.last)
    if (period.start.isBefore(LocalDate.of(2020, 4, 6))) {
      routes.LastPayDateController.onPageLoad()
    } else {
      lastPayDateRoutes(userAnswers)
    }
  }

  private[navigation] def onPayrollBefore30thOct2020Routes: UserAnswers => Call = { userAnswers =>
    userAnswers.getV(PayMethodPage) match {
      case Valid(Variable) => routeToEmployeeFirstFurloughed(userAnswers)
      case Valid(Regular)  => regularPayOnPayrollBefore30thOct2020Routes(userAnswers)
      case Invalid(_) =>
        logger.info(
          "[Navigator][onPayrollBefore30thOct2020Routes] - User tried to route from 'onPayrollBefore30thOct2020' page but did not have a valid answer for 'PayMethodPage'")
        routes.RootPageController.onPageLoad()
    }
  }

  private[navigation] def numberOfStatLeaveDaysRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.getV(NumberOfStatLeaveDaysPage) match {
      case Valid(_)   => routes.StatutoryLeavePayController.onPageLoad()
      case Invalid(_) => routes.NumberOfStatLeaveDaysController.onPageLoad()
    }
  }

  private[navigation] def hasBeenOnStatutoryLeaveRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.getV(HasEmployeeBeenOnStatutoryLeavePage) match {
      case Valid(false) => routes.PartTimeQuestionController.onPageLoad()
      case Valid(true)  => routes.NumberOfStatLeaveDaysController.onPageLoad()
      case Invalid(_)   => routes.HasEmployeeBeenOnStatutoryLeaveController.onPageLoad()
    }
  }

  private[navigation] def statutoryLeavePayRoutes: UserAnswers => Call = { userAnswers =>
    userAnswers.getV(StatutoryLeavePayPage) match {
      case Valid(_)   => routes.PartTimeQuestionController.onPageLoad()
      case Invalid(_) => routes.StatutoryLeavePayController.onPageLoad()
    }
  }

  private def isPhaseTwoOnwards: UserAnswers => Boolean =
    userAnswer => userAnswer.getV(ClaimPeriodStartPage).exists(!_.isBefore(phaseTwoStartDate))

  private def isMayExtensionOnwards: UserAnswers => Boolean =
    userAnswer => userAnswer.getV(ClaimPeriodStartPage).exists(!_.isBefore(may2021extensionStartDate))
}
