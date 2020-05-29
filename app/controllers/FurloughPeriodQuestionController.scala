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
import controllers.actions.FeatureFlag.FastTrackJourneyFlag
import controllers.actions._
import forms.FurloughPeriodQuestionFormProvider
import handlers.{ErrorHandler, FastJourneyUserAnswersHandler}
import javax.inject.Inject
import models.requests.DataRequest
import models.{FurloughEnded, FurloughOngoing, FurloughPeriodQuestion, FurloughStatus}
import navigation.Navigator
import pages.{FurloughPeriodQuestionPage, FurloughStartDatePage, FurloughStatusPage}
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import repositories.SessionRepository
import services.FurloughPeriodExtractor
import views.html.FurloughPeriodQuestionView

import scala.concurrent.{ExecutionContext, Future}

class FurloughPeriodQuestionController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  val navigator: Navigator,
  feature: FeatureFlagActionProvider,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: FurloughPeriodQuestionFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: FurloughPeriodQuestionView
)(implicit ec: ExecutionContext, errorHandler: ErrorHandler)
    extends BaseController with FurloughPeriodExtractor with FastJourneyUserAnswersHandler {

  val form: Form[FurloughPeriodQuestion] = formProvider()

  def onPageLoad(): Action[AnyContent] = (identify andThen feature(FastTrackJourneyFlag) andThen getData andThen requireData).async {
    implicit request =>
      getRequiredAnswersV(FurloughStartDatePage, FurloughStatusPage) { (furloughStart, furloughStatus) =>
        val preparedForm = request.userAnswers.getV(FurloughPeriodQuestionPage) match {
          case Invalid(_)   => form
          case Valid(value) => form.fill(value)
        }

        extractFurloughPeriodV(request.userAnswers) match {
          case Valid(FurloughOngoing(_)) =>
            Future.successful(Ok(view(preparedForm, furloughStart, furloughStatus, None)))
          case Valid(FurloughEnded(_, end)) =>
            Future.successful(Ok(view(preparedForm, furloughStart, furloughStatus, Some(end))))
          case Invalid(errors) =>
            logger.error("Failed to extract furlough period.")
            logger.error(errors.toChain.toList.mkString("\n"))
            Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
        }
      }
  }

  def onSubmit(): Action[AnyContent] = (identify andThen feature(FastTrackJourneyFlag) andThen getData andThen requireData).async {
    implicit request =>
      getRequiredAnswersV(FurloughStartDatePage, FurloughStatusPage) { (furloughStart, furloughStatus) =>
        form
          .bindFromRequest()
          .fold(
            formWithErrors => processSubmissionWithErrors(furloughStart, furloughStatus, formWithErrors),
            value => processSubmittedAnswer(request, value)
          )
      }
  }

  private def processSubmissionWithErrors(
    furloughStart: LocalDate,
    furloughStatus: FurloughStatus,
    formWithErrors: Form[FurloughPeriodQuestion])(implicit request: DataRequest[AnyContent]): Future[Result] =
    extractFurloughPeriod(request.userAnswers) match {
      case Some(FurloughOngoing(_)) =>
        Future.successful(BadRequest(view(formWithErrors, furloughStart, furloughStatus, None)))
      case Some(FurloughEnded(_, end)) =>
        Future.successful(BadRequest(view(formWithErrors, furloughStart, furloughStatus, Some(end))))
    }

  private def processSubmittedAnswer(request: DataRequest[AnyContent], value: FurloughPeriodQuestion): Future[Result] =
    for {
      updatedAnswers <- Future.fromTry(request.userAnswers.set(FurloughPeriodQuestionPage, value))
      _              <- sessionRepository.set(updatedAnswers)
    } yield {
      updateJourney(updatedAnswers) match {
        case Valid(updatedJourney) =>
          Redirect(navigator.nextPage(FurloughPeriodQuestionPage, updatedJourney.updated))
        case Invalid(errors) =>
          InternalServerError(errorHandler.internalServerErrorTemplate(request))
      }
    }
}
