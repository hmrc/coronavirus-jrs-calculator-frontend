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
import forms.FurloughPeriodQuestionFormProvider
import handlers.{ErrorHandler, FastJourneyUserAnswersHandler}
import models.requests.DataRequest
import models.{FurloughEnded, FurloughOngoing, FurloughPeriodQuestion, FurloughStatus, UserAnswers}
import navigation.Navigator
import pages.{ClaimPeriodStartPage, FurloughPeriodQuestionPage, FurloughStartDatePage, FurloughStatusPage}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import repositories.SessionRepository
import services.{FurloughPeriodExtractor, UserAnswerPersistence}
import views.html.FurloughPeriodQuestionView
import controllers.BaseController
import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class FurloughPeriodQuestionController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  val navigator: Navigator,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: FurloughPeriodQuestionFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: FurloughPeriodQuestionView
)(implicit ec: ExecutionContext, errorHandler: ErrorHandler)
    extends BaseController with FurloughPeriodExtractor with FastJourneyUserAnswersHandler with I18nSupport {

  val form: Form[FurloughPeriodQuestion] = formProvider()

  protected val userAnswerPersistence = new UserAnswerPersistence(sessionRepository.set)

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    getRequiredAnswersOrRestartJourneyV(FurloughStartDatePage, FurloughStatusPage) { (furloughStart, furloughStatus) =>
      getRequiredAnswerV(ClaimPeriodStartPage) { claimStart =>
        val preparedForm = request.userAnswers.getV(FurloughPeriodQuestionPage) match {
          case Invalid(_) =>
            form
          case Valid(value) => form.fill(value)
        }

        extractFurloughPeriodV(request.userAnswers) match {
          case Valid(FurloughOngoing(_)) =>
            Future.successful(previousPageOrRedirect(Ok(view(preparedForm, claimStart, furloughStart, furloughStatus, None))))
          case Valid(FurloughEnded(_, end)) =>
            Future.successful(previousPageOrRedirect(Ok(view(preparedForm, claimStart, furloughStart, furloughStatus, Some(end)))))
          case Invalid(errors) =>
            logger.error("Failed to extract furlough period.")
            UserAnswers.logErrors(errors)(logger.logger)
            Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
        }
      }
    }
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    getRequiredAnswersV(FurloughStartDatePage, FurloughStatusPage) { (furloughStart, furloughStatus) =>
      getRequiredAnswerV(ClaimPeriodStartPage) { claimStart =>
        form
          .bindFromRequest()
          .fold(
            formWithErrors => processSubmissionWithErrors(claimStart, furloughStart, furloughStatus, formWithErrors),
            value => processSubmittedAnswer(request, value)
          )
      }
    }
  }

  private def processSubmissionWithErrors(
    claimStart: LocalDate,
    furloughStart: LocalDate,
    furloughStatus: FurloughStatus,
    formWithErrors: Form[FurloughPeriodQuestion])(implicit request: DataRequest[AnyContent]): Future[Result] =
    extractFurloughPeriodV(request.userAnswers) match {
      case Valid(FurloughOngoing(_)) =>
        Future.successful(BadRequest(view(formWithErrors, claimStart, furloughStart, furloughStatus, None)))
      case Valid(FurloughEnded(_, end)) =>
        Future.successful(BadRequest(view(formWithErrors, claimStart, furloughStart, furloughStatus, Some(end))))
      case Invalid(errors) =>
        UserAnswers.logErrors(errors)(logger.logger)
        Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
    }

  private def processSubmittedAnswer(
    request: DataRequest[AnyContent],
    value: FurloughPeriodQuestion
  ): Future[Result] =
    for {
      updatedAnswers <- userAnswerPersistence.persistAnswer(request.userAnswers, FurloughPeriodQuestionPage, value, None)
      call = navigator.nextPage(FurloughPeriodQuestionPage, updatedAnswers, None)
      result <- evaluate(request, updatedAnswers, call)
    } yield result

  private def evaluate(request: DataRequest[AnyContent], updatedAnswers: UserAnswers, call: Call): Future[Result] =
    furloughQuestionV(updatedAnswers) match {
      case Valid(updatedJourney) =>
        sessionRepository.set(updatedJourney.updated).map { _ =>
          Redirect(call)
        }
      case Invalid(e) =>
        UserAnswers.logErrors(e)(logger.logger)
        Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate(request)))
    }
}
