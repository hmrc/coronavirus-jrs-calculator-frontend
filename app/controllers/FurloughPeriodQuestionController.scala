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
import controllers.actions.FeatureFlag.FastTrackJourneyFlag
import controllers.actions._
import forms.FurloughPeriodQuestionFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.{FurloughEnded, FurloughOngoing, FurloughPeriodQuestion}
import navigation.Navigator
import pages.{FurloughPeriodQuestionPage, FurloughStartDatePage, FurloughStatusPage}
import play.api.Logger
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
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
    extends BaseController with FurloughPeriodExtractor {

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
            Logger.error("Failed to extract furlough period.")
            Logger.error(errors.toChain.toList.mkString("\n"))
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
            formWithErrors =>
              extractFurloughPeriodV(request.userAnswers) match {
                case Valid(FurloughOngoing(_)) =>
                  Future.successful(BadRequest(view(formWithErrors, furloughStart, furloughStatus, None)))
                case Valid(FurloughEnded(_, end)) =>
                  Future.successful(BadRequest(view(formWithErrors, furloughStart, furloughStatus, Some(end))))
                case Invalid(e) =>
                  logErrors("Failued to extract furlough period", e)
                  Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
            },
            value =>
              for {
                updatedAnswers <- Future.fromTry(request.userAnswers.set(FurloughPeriodQuestionPage, value))
                _              <- sessionRepository.set(updatedAnswers)
              } yield Redirect(navigator.nextPage(FurloughPeriodQuestionPage, updatedAnswers))
          )
      }
  }
}
