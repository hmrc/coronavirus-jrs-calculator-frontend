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

package controllers

import cats.data.Validated.{Invalid, Valid}
import controllers.actions._
import forms.FurloughPartialPayFormProvider
import models.FurloughPartialPay
import navigation.Navigator
import pages._
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import services.PartialPayExtractor
import views.html.VariableLengthPartialPayView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PartialPayBeforeFurloughController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  val navigator: Navigator,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: FurloughPartialPayFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: VariableLengthPartialPayView
)(implicit ec: ExecutionContext)
    extends BaseController with I18nSupport with PartialPayExtractor {

  val form: Form[FurloughPartialPay] = formProvider()

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    getPartialPeriods(request.userAnswers)
      .find(isFurloughStart)
      .map(getBeforeFurloughPeriodRemainder)
      .fold(
        Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
      ) { beforeFurlough =>
        val preparedForm = request.userAnswers.getV(PartialPayBeforeFurloughPage) match {
          case Invalid(_)   => form
          case Valid(value) => form.fill(value)
        }

        Future.successful(
          Ok(
            view(
              preparedForm,
              beforeFurlough.start,
              beforeFurlough.end,
              routes.PartialPayBeforeFurloughController.onSubmit()
            )))
      }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    getPartialPeriods(request.userAnswers)
      .find(isFurloughStart)
      .map(getBeforeFurloughPeriodRemainder)
      .fold(
        Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
      ) { partialPeriodBefore =>
        form
          .bindFromRequest()
          .fold(
            formWithErrors =>
              Future.successful(
                BadRequest(
                  view(
                    formWithErrors,
                    partialPeriodBefore.start,
                    partialPeriodBefore.end,
                    routes.PartialPayBeforeFurloughController.onSubmit()
                  ))), { value =>
              for {
                updatedAnswers <- Future.fromTry(request.userAnswers.set(PartialPayBeforeFurloughPage, value))
                _              <- sessionRepository.set(updatedAnswers)
              } yield {
                Redirect(navigator.nextPage(PartialPayBeforeFurloughPage, updatedAnswers))
              }
            }
          )
      }
  }
}
