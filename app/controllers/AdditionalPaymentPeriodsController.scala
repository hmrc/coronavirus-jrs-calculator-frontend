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

package controllers

import java.time.LocalDate

import controllers.actions.FeatureFlag.TopUpJourneyFlag
import controllers.actions._
import forms.AdditionalPaymentPeriodsFormProvider
import handlers.FurloughCalculationHandler
import javax.inject.Inject
import models.UserAnswers
import navigation.Navigator
import pages.AdditionalPaymentPeriodsPage
import play.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.AdditionalPaymentPeriodsView

import scala.concurrent.{ExecutionContext, Future}

class AdditionalPaymentPeriodsController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  navigator: Navigator,
  identify: IdentifierAction,
  feature: FeatureFlagActionProvider,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: AdditionalPaymentPeriodsFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: AdditionalPaymentPeriodsView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController with I18nSupport with FurloughCalculationHandler {

  val form = formProvider()

  def onPageLoad(): Action[AnyContent] = (identify andThen feature(TopUpJourneyFlag) andThen getData andThen requireData).async {
    implicit request =>
      handleCalculationFurlough(request.userAnswers)
        .map { furlough =>
          furlough.payPeriodBreakdowns match {
            case breakdown :: Nil =>
              saveAndRedirect(request.userAnswers, List(breakdown.periodWithPaymentDate.period.period.end))
            case _ =>
              val preparedForm = request.userAnswers.get(AdditionalPaymentPeriodsPage) match {
                case None                => form
                case Some(selectedDates) => form.fill(selectedDates)
              }
              Future.successful(Ok(view(preparedForm, furlough.payPeriodBreakdowns)))
          }
        }
        .getOrElse(
          Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
        )
  }

  def onSubmit(): Action[AnyContent] = (identify andThen feature(TopUpJourneyFlag) andThen getData andThen requireData).async {
    implicit request =>
      handleCalculationFurlough(request.userAnswers)
        .map { furlough =>
          form
            .bindFromRequest()
            .fold(
              formWithErrors => Future.successful(BadRequest(view(formWithErrors, furlough.payPeriodBreakdowns))), { dates =>
                val periods = dates.flatMap { date =>
                  furlough.payPeriodBreakdowns
                    .map(_.periodWithPaymentDate.period.period.end)
                    .find(_ == date)
                }

                if (dates.length != periods.length) {
                  Logger.warn("[AdditionalPaymentPeriodsController][onSubmit] Dates in furlough and input do not align")
                  Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
                } else {
                  saveAndRedirect(request.userAnswers, periods)
                }
              }
            )
        }
        .getOrElse(
          Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
        )
  }

  private def saveAndRedirect(userAnswers: UserAnswers, additionalPaymentPeriods: List[LocalDate]) =
    for {
      updatedAnswers <- Future.fromTry(userAnswers.set(AdditionalPaymentPeriodsPage, additionalPaymentPeriods))
      _              <- sessionRepository.set(updatedAnswers)
    } yield Redirect(navigator.nextPage(AdditionalPaymentPeriodsPage, updatedAnswers))
}