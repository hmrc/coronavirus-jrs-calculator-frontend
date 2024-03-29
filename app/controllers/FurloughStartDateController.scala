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
import forms.FurloughStartDateFormProvider
import handlers.ErrorHandler
import navigation.Navigator
import pages.{ClaimPeriodEndPage, ClaimPeriodStartPage, FurloughStartDatePage}
import play.api.data.Form
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import services.UserAnswerPersistence
import views.html.FurloughStartDateView

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class FurloughStartDateController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  val navigator: Navigator,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: FurloughStartDateFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: FurloughStartDateView
)(implicit ec: ExecutionContext, errorHandler: ErrorHandler)
    extends BaseController with I18nSupport {

  def form(claimEndDate: LocalDate)(implicit messages: Messages): Form[LocalDate] = formProvider(claimEndDate)
  protected val userAnswerPersistence                                             = new UserAnswerPersistence(sessionRepository.set)

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    getRequiredAnswersV(ClaimPeriodStartPage, ClaimPeriodEndPage) { (claimStartDate, claimEndDate) =>
      val preparedForm = request.userAnswers.getV(FurloughStartDatePage) match {
        case Invalid(e)   => form(claimEndDate)
        case Valid(value) => form(claimEndDate).fill(value)
      }

      Future.successful(Ok(view(preparedForm, claimStartDate)))
    }
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    getRequiredAnswersV(ClaimPeriodStartPage, ClaimPeriodEndPage) { (claimStartDate, claimEndDate) =>
      form(claimEndDate)
        .bindFromRequest()
        .fold(
          formWithErrors => Future.successful(BadRequest(view(formWithErrors, claimStartDate))),
          value =>
            userAnswerPersistence
              .persistAnswer(request.userAnswers, FurloughStartDatePage, value, None)
              .map { updatedAnswers =>
                Redirect(navigator.nextPage(FurloughStartDatePage, updatedAnswers, None))
            }
        )
    }
  }
}
