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
import forms.ClaimPeriodEndFormProvider
import navigation.Navigator
import pages.{ClaimPeriodEndPage, ClaimPeriodStartPage}
import play.api.data.Form
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import services.UserAnswerPersistence
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.ClaimPeriodEndView

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ClaimPeriodEndController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  navigator: Navigator,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: ClaimPeriodEndFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: ClaimPeriodEndView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController with I18nSupport {

  def form(claimStart: LocalDate)(implicit messages: Messages): Form[LocalDate] = formProvider(claimStart)
  protected val userAnswerPersistence                                           = new UserAnswerPersistence(sessionRepository.set)

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    val maybeClaimStart = request.userAnswers.getV(ClaimPeriodStartPage)
    val maybeClaimEnd   = request.userAnswers.getV(ClaimPeriodEndPage)

    (maybeClaimStart, maybeClaimEnd) match {
      case (Valid(claimStart), Valid(end)) => Ok(view(form(claimStart).fill(end)))
      case (Valid(claimStart), Invalid(_)) => Ok(view(form(claimStart)))
      case (Invalid(_), _)                 => Redirect(routes.ClaimPeriodStartController.onPageLoad())
    }
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    request.userAnswers.getV(ClaimPeriodStartPage) match {
      case Valid(claimStart) =>
        form(claimStart)
          .bindFromRequest()
          .fold(
            formWithErrors => Future.successful(BadRequest(view(formWithErrors))),
            value =>
              userAnswerPersistence
                .persistAnswer(request.userAnswers, ClaimPeriodEndPage, value, None)
                .map { updatedAnswers =>
                  Redirect(navigator.nextPage(ClaimPeriodEndPage, updatedAnswers, None))
              }
          )
      case Invalid(_) => Future.successful(Redirect(routes.ClaimPeriodStartController.onPageLoad()))
    }
  }
}
