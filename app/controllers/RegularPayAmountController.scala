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

import cats.data.Validated.{Invalid, Valid}
import controllers.actions._
import forms.RegularPayAmountFormProvider
import javax.inject.Inject
import models.{Salary, UserAnswers}
import navigation.Navigator
import org.slf4j
import org.slf4j.LoggerFactory
import pages.RegularPayAmountPage
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.RegularPayAmountView

import scala.concurrent.{ExecutionContext, Future}

class RegularPayAmountController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  navigator: Navigator,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: RegularPayAmountFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: RegularPayAmountView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController with I18nSupport {

  implicit val logger: slf4j.Logger = LoggerFactory.getLogger(getClass)

  val form: Form[Salary] = formProvider()

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    val maybeSalary = request.userAnswers.getV(RegularPayAmountPage)

    maybeSalary match {
      case Valid(sq) => Ok(view(form.fill(sq)))
      case Invalid(e) =>
        UserAnswers.logWarnings(e)
        Ok(view(form))
    }
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form
      .bindFromRequest()
      .fold(
        formWithErrors => {
          Future.successful(BadRequest(view(formWithErrors)))
        },
        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(RegularPayAmountPage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(RegularPayAmountPage, updatedAnswers))
      )
  }
}
