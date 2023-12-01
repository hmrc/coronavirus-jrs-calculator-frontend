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
import controllers.actions._
import forms.AdditionalPaymentPeriodsFormProvider
import handlers.FurloughCalculationHandler
import models.UserAnswers
import navigation.Navigator
import pages.AdditionalPaymentPeriodsPage
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import services.UserAnswerPersistence
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import utils.LoggerUtil
import views.html.AdditionalPaymentPeriodsView

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AdditionalPaymentPeriodsController @Inject() (
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  navigator: Navigator,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: AdditionalPaymentPeriodsFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: AdditionalPaymentPeriodsView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController with I18nSupport with FurloughCalculationHandler with LoggerUtil {

  val form: Form[List[LocalDate]] = formProvider()

  val userAnswerPersistence = new UserAnswerPersistence(sessionRepository.set)

  def onPageLoad(): Action[AnyContent] =
    (identify andThen getData andThen requireData).async { implicit request =>
      handleCalculationFurloughV(request.userAnswers)
        .map { furlough =>
          furlough.periodBreakdowns match {
            case breakdown :: Nil =>
              import breakdown.paymentWithPeriod.periodWithPaymentDate.period._
              saveAndRedirect(request.userAnswers, List(period.end))
            case _ =>
              val preparedForm = request.userAnswers.getV(AdditionalPaymentPeriodsPage) match {
                case Invalid(e)           => form
                case Valid(selectedDates) => form.fill(selectedDates)
              }
              Future.successful(Ok(view(preparedForm, furlough.periodBreakdowns)))
          }
        }
        .fold(
          nel => {
            UserAnswers.logErrors(nel)(logger.logger)
            Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
          },
          identity
        )
    }

  def onSubmit(): Action[AnyContent] =
    (identify andThen getData andThen requireData).async { implicit request =>
      handleCalculationFurloughV(request.userAnswers)
        .map { furlough =>
          form
            .bindFromRequest()
            .fold(
              formWithErrors => Future.successful(BadRequest(view(formWithErrors, furlough.periodBreakdowns))),
              { dates =>
                val periods = dates.flatMap { date =>
                  furlough.periodBreakdowns
                    .map(_.paymentWithPeriod.periodWithPaymentDate.period.period.end)
                    .find(_ == date)
                }

                if (dates.length != periods.length) {
                  logger.warn("[AdditionalPaymentPeriodsController][onSubmit] Dates in furlough and input do not align")
                  Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
                } else
                  saveAndRedirect(request.userAnswers, periods)
              }
            )
        } fold (nel => {
        UserAnswers.logErrors(nel)(logger.logger)
        Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
      }, identity)
    }

  private def saveAndRedirect(userAnswers: UserAnswers, additionalPaymentPeriods: List[LocalDate]) =
    userAnswerPersistence
      .persistAnswer(userAnswers, AdditionalPaymentPeriodsPage, additionalPaymentPeriods, None)
      .map { updatedAnswers =>
        Redirect(navigator.nextPage(AdditionalPaymentPeriodsPage, updatedAnswers, None))
      }
}
