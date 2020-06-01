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

import cats.data.Validated.{Invalid, Valid}
import controllers.actions._
import forms.ClaimPeriodStartFormProvider
import javax.inject.Inject
import models.UserAnswers
import navigation.Navigator
import pages.ClaimPeriodStartPage
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.ClaimPeriodStartView

import scala.concurrent.{ExecutionContext, Future}

class ClaimPeriodStartController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  navigator: Navigator,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: ClaimPeriodStartFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: ClaimPeriodStartView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController with I18nSupport {

  def form: Form[LocalDate] = formProvider()

  def onPageLoad(): Action[AnyContent] = (identify andThen getData) { implicit request =>
    val preparedForm = request.userAnswers
      .getOrElse(UserAnswers(request.internalId))
      .getV(ClaimPeriodStartPage) match {
      case Invalid(e)   => form
      case Valid(value) => form.fill(value)
    }

    Ok(view(preparedForm))
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData).async { implicit request =>
    form
      .bindFromRequest()
      .fold(
        formWithErrors => Future.successful(BadRequest(view(formWithErrors))),
        value =>
          for {
            updatedAnswers <- Future.fromTry(UserAnswers(request.internalId).set(ClaimPeriodStartPage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(ClaimPeriodStartPage, updatedAnswers))
      )
  }
}
