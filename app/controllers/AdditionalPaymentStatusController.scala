/*
 * Copyright 2021 HM Revenue & Customs
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
import forms.AdditionalPaymentStatusFormProvider
import handlers.FurloughCalculationHandler
import javax.inject.Inject
import models.AdditionalPaymentStatus
import navigation.Navigator
import pages.AdditionalPaymentStatusPage
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import services.UserAnswerPersistence
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.AdditionalPaymentStatusView

import scala.concurrent.{ExecutionContext, Future}

class AdditionalPaymentStatusController @Inject()(override val messagesApi: MessagesApi,
                                                  sessionRepository: SessionRepository,
                                                  navigator: Navigator,
                                                  identify: IdentifierAction,
                                                  getData: DataRetrievalAction,
                                                  requireData: DataRequiredAction,
                                                  formProvider: AdditionalPaymentStatusFormProvider,
                                                  val controllerComponents: MessagesControllerComponents,
                                                  view: AdditionalPaymentStatusView)(implicit ec: ExecutionContext)
    extends FrontendBaseController with I18nSupport with FurloughCalculationHandler {

  val form: Form[AdditionalPaymentStatus] = formProvider()
  val userAnswerPersistence               = new UserAnswerPersistence(sessionRepository.set)

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    val preparedForm = request.userAnswers.getV(AdditionalPaymentStatusPage) match {
      case Invalid(e)   => form
      case Valid(value) => form.fill(value)
    }

    Ok(view(preparedForm))
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form
      .bindFromRequest()
      .fold(
        formWithErrors => Future.successful(BadRequest(view(formWithErrors))),
        value =>
          userAnswerPersistence
            .persistAnswer(request.userAnswers, AdditionalPaymentStatusPage, value, None)(AdditionalPaymentStatus.writes)
            .map { updatedAnswers =>
              Redirect(navigator.nextPage(AdditionalPaymentStatusPage, updatedAnswers, None))
          }
      )
  }
}
