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
import forms.ClaimPeriodQuestionFormProvider
import handlers.{ErrorHandler, FastJourneyUserAnswersHandler}
import models.ClaimPeriodQuestion
import models.requests.DataRequest
import navigation.Navigator
import pages.{ClaimPeriodEndPage, ClaimPeriodQuestionPage, ClaimPeriodStartPage}
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.libs.json.JsObject
import play.api.mvc._
import repositories.SessionRepository
import services.UserAnswerPersistence
import views.html.ClaimPeriodQuestionView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ClaimPeriodQuestionController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  val navigator: Navigator,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: ClaimPeriodQuestionFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: ClaimPeriodQuestionView
)(implicit ec: ExecutionContext, errorHandler: ErrorHandler)
    extends BaseController with FastJourneyUserAnswersHandler {

  val form: Form[ClaimPeriodQuestion] = formProvider()
  protected val userAnswerPersistence = new UserAnswerPersistence(sessionRepository.set)

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request: DataRequest[AnyContent] =>
    if (didNotReuseDates(request.headers.toSimpleMap.get("Referer"), request.userAnswers.data)) {
      Future.successful(Redirect(routes.ResetCalculationController.onPageLoad()))
    } else {
      processOnLoad
    }
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    getRequiredAnswersV(ClaimPeriodStartPage, ClaimPeriodEndPage) { (claimStart, claimEnd) =>
      form
        .bindFromRequest()
        .fold(
          formWithErrors => Future.successful(BadRequest(view(formWithErrors, claimStart, claimEnd))),
          value => processSubmittedAnswer(request, value)
        )
    }
  }

  private def processOnLoad(implicit request: DataRequest[AnyContent]): Future[Result] =
    getRequiredAnswersV(ClaimPeriodStartPage, ClaimPeriodEndPage) { (claimStart, claimEnd) =>
      val filledForm: Form[ClaimPeriodQuestion] =
        request.userAnswers.getV(ClaimPeriodQuestionPage).fold(_ => form, form.fill)

      Future.successful(previousPageOrRedirect(Ok(view(filledForm, claimStart, claimEnd))))
    }

  private def processSubmittedAnswer(request: DataRequest[AnyContent], value: ClaimPeriodQuestion): Future[Result] =
    userAnswerPersistence
      .persistAnswer(request.userAnswers, ClaimPeriodQuestionPage, value, None)
      .map { updatedAnswers =>
        {
          Redirect(navigator.nextPage(ClaimPeriodQuestionPage, updatedAnswers, None))
          val call = navigator.nextPage(ClaimPeriodQuestionPage, updatedAnswers)
          updateJourney(updatedAnswers) match {
            case Valid(updatedJourney) =>
              sessionRepository.set(updatedJourney.updated)
              Redirect(call)
            case Invalid(_) =>
              InternalServerError(errorHandler.internalServerErrorTemplate(request))
          }
        }
      }

  protected val didNotReuseDates: (Option[String], JsObject) => Boolean =
    (referer, data) => referer.isDefined && referer.getOrElse("").endsWith("/confirmation") && data.keys.isEmpty
}
