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

package controllers

import cats.data.Validated.{Invalid, Valid}
import config.featureSwitch.{FeatureSwitching, WriteConfirmationTestCasesToFile}
import config.{CalculatorVersionConfiguration, FrontendAppConfig}
import controllers.actions._
import handlers.{ConfirmationControllerRequestHandler, ErrorHandler}
import models.{FurloughGrantRate, UserAnswers}
import navigation.Navigator
import play.api.i18n.MessagesApi
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{AuditService, EmployeeTypeService}
import utils.ConfirmationTestCasesUtil.writeConfirmationTestCasesToFile
import utils.PagerDutyHelper.PagerDutyKeys._
import utils.{PagerDutyHelper, YearMonthHelper}
import viewmodels.{ConfirmationDataResultWithoutNicAndPension, PhaseOneConfirmationDataResult, PhaseTwoConfirmationDataResult}
import views.html._

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class ConfirmationController @Inject() (
  override val messagesApi: MessagesApi,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  val controllerComponents: MessagesControllerComponents,
  employeeTypeService: EmployeeTypeService,
  viewWithDetailedBreakdowns: ConfirmationViewWithDetailedBreakdowns,
  phaseTwoView: PhaseTwoConfirmationView,
  extensionView: JrsExtensionConfirmationView,
  auditService: AuditService,
  val navigator: Navigator
)(implicit val errorHandler: ErrorHandler, ec: ExecutionContext, appConfig: FrontendAppConfig)
    extends BaseController with ConfirmationControllerRequestHandler with CalculatorVersionConfiguration with YearMonthHelper
    with FeatureSwitching {

  //scalastyle:off
  def onPageLoad: Action[AnyContent] =
    (identify andThen getData andThen requireData) { implicit request =>
      if (isEnabled(WriteConfirmationTestCasesToFile))
        writeConfirmationTestCasesToFile(request.userAnswers, loadResultData(request.userAnswers))

      loadResultData(request.userAnswers) match {
        case Valid(data: PhaseOneConfirmationDataResult) =>
          auditService.sendCalculationPerformed(request.userAnswers, data.confirmationViewBreakdown)
          Ok(viewWithDetailedBreakdowns(data.confirmationViewBreakdown, data.metaData.claimPeriod, calculatorVersionConf))
        case Valid(data: PhaseTwoConfirmationDataResult) =>
          auditService.sendCalculationPerformed(request.userAnswers, data.confirmationViewBreakdown)
          Ok(phaseTwoView(data.confirmationViewBreakdown, data.metaData.claimPeriod, calculatorVersionConf))
        case Valid(data: ConfirmationDataResultWithoutNicAndPension) =>
          auditService.sendCalculationPerformed(request.userAnswers, data.confirmationViewBreakdown)
          Ok(
            extensionView(
              data.confirmationViewBreakdown,
              data.metaData.claimPeriod,
              calculatorVersionConf,
              employeeTypeService.isType5NewStarter(),
              FurloughGrantRate.rateForYearMonth(data.metaData.claimPeriod.start.getYearMonth)
            )
          )
        case Invalid(e) =>
          auditService.sendCalculationFailed(request.userAnswers)
          PagerDutyHelper.alert(CALCULATION_FAILED)
          UserAnswers.logErrors(e)(logger.logger)
          Redirect(routes.ErrorController.somethingWentWrong())
      }
    }

}
