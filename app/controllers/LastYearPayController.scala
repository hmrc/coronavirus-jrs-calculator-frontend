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
import forms.LastYearPayFormProvider
import handlers.LastYearPayControllerRequestHandler
import models.{Amount, LastYearPayment, Period, UserAnswers}
import navigation.Navigator
import org.slf4j.{Logger, LoggerFactory}
import pages.{ClaimPeriodStartPage, LastYearPayPage}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import repositories.SessionRepository
import services.UserAnswerPersistence
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.LastYearPayView

import java.time.Month
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class LastYearPayController @Inject() (
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  navigator: Navigator,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: LastYearPayFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: LastYearPayView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController with I18nSupport with LastYearPayControllerRequestHandler {

  implicit val logger: Logger = LoggerFactory.getLogger(getClass)

  val form: Form[Amount]              = formProvider()
  protected val userAnswerPersistence = new UserAnswerPersistence(sessionRepository.set)

  def onPageLoad(idx: Int): Action[AnyContent] =
    (identify andThen getData andThen requireData).async { implicit request =>
      getLastYearPeriods(request.userAnswers).fold(
        nel => {
          UserAnswers.logErrors(nel)
          Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
        },
        periods =>
          withValidPeriod(periods, idx) { period =>
            val preparedForm = request.userAnswers.getV(LastYearPayPage) match {
              case Invalid(e)   => form
              case Valid(value) => form.fill(value.amount)
            }

            request.userAnswers.getV(ClaimPeriodStartPage) match {
              case Invalid(e) => Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
              case Valid(startDate) =>
                val isStartDateOfClaimInFebruaryAndYear2021: Boolean = startDate.getMonth == Month.FEBRUARY && startDate.getYear == 2021
                Future.successful(Ok(view(preparedForm, idx, period, isStartDateOfClaimInFebruaryAndYear2021)))
            }

          }
      )
    }

  def withValidPeriod(periods: Seq[Period], idx: Int)(f: Period => Future[Result]): Future[Result] =
    periods.lift(idx - 1) match {
      case Some(period) => f(period)
      case None         => Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
    }

  def onSubmit(idx: Int): Action[AnyContent] =
    (identify andThen getData andThen requireData).async { implicit request =>
      getLastYearPeriods(request.userAnswers).fold(
        nel => {
          UserAnswers.logErrors(nel)
          Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
        },
        periods =>
          withValidPeriod(periods, idx) { period =>
            request.userAnswers.getV(ClaimPeriodStartPage) match {
              case Invalid(e) => Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
              case Valid(startDate) =>
                val isStartDateOfClaimInFebruaryAndYear2021: Boolean = startDate.getMonth == Month.FEBRUARY && startDate.getYear == 2021
                form
                  .bindFromRequest()
                  .fold(
                    formWithErrors =>
                      Future.successful(BadRequest(view(formWithErrors, idx, period, isStartDateOfClaimInFebruaryAndYear2021))),
                    value =>
                      userAnswerPersistence
                        .persistAnswer(request.userAnswers, LastYearPayPage, LastYearPayment(period.end, value), Some(idx))
                        .map { updatedAnswers =>
                          Redirect(navigator.nextPage(LastYearPayPage, updatedAnswers, Some(idx)))
                        }
                  )
            }
          }
      )
    }
}
