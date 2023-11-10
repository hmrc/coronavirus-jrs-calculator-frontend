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

import controllers.actions._
import forms.FurloughOngoingFormProvider
import handlers.ErrorHandler
import models.FurloughStatus
import navigation.Navigator
import pages.{ClaimPeriodStartPage, FurloughStatusPage}
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import services.UserAnswerPersistence
import views.html.FurloughOngoingView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class FurloughOngoingController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  val navigator: Navigator,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: FurloughOngoingFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: FurloughOngoingView
)(implicit ec: ExecutionContext, errorHandler: ErrorHandler)
    extends BaseController {

  val form: Form[FurloughStatus]      = formProvider()
  protected val userAnswerPersistence = new UserAnswerPersistence(sessionRepository.set)

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    val maybeFurlough = request.userAnswers.getV(FurloughStatusPage)
    getRequiredAnswerV(ClaimPeriodStartPage) { claimStartDate =>
      Future.successful(Ok(view(maybeFurlough.map(form.fill).getOrElse(form), claimStartDate)))
    }
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    getRequiredAnswerV(ClaimPeriodStartPage) { claimStartDate =>
      form
        .bindFromRequest()
        .fold(
          formWithErrors => Future.successful(BadRequest(view(formWithErrors, claimStartDate))),
          value =>
            userAnswerPersistence
              .persistAnswer(request.userAnswers, FurloughStatusPage, value, None)
              .map { updatedAnswers =>
                Redirect(navigator.nextPage(FurloughStatusPage, updatedAnswers, None))
            }
        )
    }
  }
}
