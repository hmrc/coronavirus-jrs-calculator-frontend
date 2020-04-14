/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import handlers.ErrorHandler
import models.requests.DataRequest
import pages.QuestionPage
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.libs.json.Reads
import play.api.mvc.Result
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController

import scala.concurrent.Future

trait BaseController extends FrontendBaseController with I18nSupport {

  def getAnswer[A](page: QuestionPage[A], idx: Int)(implicit request: DataRequest[_], reads: Reads[A]): Option[A] =
    getAnswer(page, Some(idx))

  def getAnswer[A](page: QuestionPage[A], idx: Option[Int] = None)(implicit request: DataRequest[_], reads: Reads[A]): Option[A] =
    request.userAnswers.get(page, idx)

  def getRequiredAnswer[A](page: QuestionPage[A], idx: Int)(
    f: A => Future[Result])(implicit request: DataRequest[_], reads: Reads[A], errorHandler: ErrorHandler): Future[Result] =
    getRequiredAnswer(page, Some(idx))(f)

  def getRequiredAnswer[A](page: QuestionPage[A], idx: Option[Int] = None)(
    f: A => Future[Result])(implicit request: DataRequest[_], reads: Reads[A], errorHandler: ErrorHandler): Future[Result] =
    getAnswer(page, idx) match {
      case Some(ans) => f(ans)
      case _ =>
        Logger.error(s"[BaseController][getRequiredAnswer] Failed to retrieve expected data for page: $page")
        Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
    }

}
