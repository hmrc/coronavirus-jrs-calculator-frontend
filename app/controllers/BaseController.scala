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
import handlers.ErrorHandler
import models.UserAnswers.AnswerV
import models.requests.DataRequest
import models.{BackFirstPage, BackToPreviousPage, UserAnswers}
import navigation.Navigator
import pages.QuestionPage
import play.api.i18n.I18nSupport
import play.api.libs.json.Reads
import play.api.mvc.Result
import services.BackJourneyValidator
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import utils.LoggerUtil

import scala.concurrent.Future

trait BaseController extends FrontendBaseController with I18nSupport with BackJourneyValidator with LoggerUtil {

  def navigator: Navigator

  def getAnswerV[A](page: QuestionPage[A], idx: Int)(implicit request: DataRequest[_], reads: Reads[A]): AnswerV[A] =
    getAnswerV(page, Some(idx))

  def getAnswerV[A](page: QuestionPage[A], idx: Option[Int] = None)(implicit request: DataRequest[_], reads: Reads[A]): AnswerV[A] =
    request.userAnswers.getV(page, idx)

  def getRequiredAnswerV[A](page: QuestionPage[A], idx: Int)(
    f: A => Future[Result])(implicit request: DataRequest[_], reads: Reads[A], errorHandler: ErrorHandler): Future[Result] =
    getRequiredAnswerV(page, Some(idx))(f)

  def getRequiredAnswerV[A](page: QuestionPage[A], idx: Option[Int] = None)(
    f: A => Future[Result])(implicit request: DataRequest[_], reads: Reads[A], errorHandler: ErrorHandler): Future[Result] =
    getAnswerV(page, idx) match {
      case Valid(ans) => f(ans)
      case Invalid(errors) =>
        logger.error(s"[BaseController][getRequiredAnswer] Failed to retrieve expected data for page: $page")
        UserAnswers.logWarnings(errors)(logger.logger)
        Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
    }

  def getRequiredAnswerOrRedirectV[A](page: QuestionPage[A], idx: Option[Int] = None)(
    f: A => Future[Result])(implicit request: DataRequest[_], reads: Reads[A]): Future[Result] =
    getAnswerV(page, idx) match {
      case Valid(ans) => f(ans)
      case Invalid(errors) =>
        val requiredPage = navigator.routeFor(page)
        logger.error(s"Failed to retrieve expected data for page: $page, redirecting to $requiredPage")
        logger.error(errors.toChain.toList.mkString("\n"))
        Future.successful(Redirect(requiredPage))
    }

  //scalastyle:off
  def getRequiredAnswersV[A, B](
    pageA: QuestionPage[A],
    pageB: QuestionPage[B],
    idxA: Option[Int] = None,
    idxB: Option[Int] = None
  )(
    f: (A, B) => Future[Result]
  )(implicit request: DataRequest[_], readsA: Reads[A], readsB: Reads[B], errorHandler: ErrorHandler): Future[Result] = {

    import cats.syntax.apply._

    (getAnswerV(pageA, idxA), getAnswerV(pageB, idxB))
      .mapN { (ansA, ansB) =>
        f(ansA, ansB)
      }
      .fold(
        nel => {
          logger.error(s"[BaseController][getRequiredAnswers] Failed to retrieve expected data for page: $pageB")
          UserAnswers.logErrors(nel)(logger.logger)
          Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
        },
        identity
      )
  }
  //scalastyle:on

  //scalastyle:off
  def getRequiredAnswersOrRestartJourneyV[A, B](
    pageA: QuestionPage[A],
    pageB: QuestionPage[B],
    idxA: Option[Int] = None,
    idxB: Option[Int] = None
  )(
    f: (A, B) => Future[Result]
  )(implicit request: DataRequest[_], readsA: Reads[A], readsB: Reads[B], errorHandler: ErrorHandler): Future[Result] = {

    import cats.syntax.apply._

    (getAnswerV(pageA, idxA), getAnswerV(pageB, idxB))
      .mapN { (ansA, ansB) =>
        f(ansA, ansB)
      }
      .fold(
        nel => {
          logger.error(s"[BaseController][getRequiredAnswers] Failed to retrieve expected data for page: $pageB")
          UserAnswers.logErrors(nel)(logger.logger)
          Future.successful(previousPageOrRedirect(InternalServerError(errorHandler.internalServerErrorTemplate)))
        },
        identity
      )
  }
  //scalastyle:on

  def previousPageOrRedirect(view: Result)(implicit request: DataRequest[_]): Result = validateBackJourney(request.userAnswers) match {
    case BackToPreviousPage => view
    case BackFirstPage      => Redirect(routes.ResetCalculationController.onPageLoad())
  }
}
