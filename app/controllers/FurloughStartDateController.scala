/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import java.time.LocalDate

import controllers.actions._
import forms.FurloughStartDateFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import navigation.Navigator
import pages.{ClaimPeriodEndPage, ClaimPeriodStartPage, FurloughStartDatePage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.FurloughStartDateView

import scala.concurrent.{ExecutionContext, Future}

class FurloughStartDateController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  navigator: Navigator,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: FurloughStartDateFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: FurloughStartDateView
)(implicit ec: ExecutionContext, errorHandler: ErrorHandler)
    extends BaseController with I18nSupport {

  def form(claimStartDate: LocalDate, claimEndDate: LocalDate) = formProvider(claimStartDate, claimEndDate)

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    getRequiredAnswers(ClaimPeriodStartPage, ClaimPeriodEndPage) { (claimStartDate, claimEndDate) =>
      val preparedForm = request.userAnswers.get(FurloughStartDatePage) match {
        case None        => form(claimStartDate, claimEndDate)
        case Some(value) => form(claimStartDate, claimEndDate).fill(value)
      }

      Future.successful(Ok(view(preparedForm, mode)))
    }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    getRequiredAnswers(ClaimPeriodStartPage, ClaimPeriodEndPage) { (claimStartDate, claimEndDate) =>
      form(claimStartDate, claimEndDate)
        .bindFromRequest()
        .fold(
          formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(FurloughStartDatePage, value))
              _              <- sessionRepository.set(updatedAnswers)
            } yield Redirect(navigator.nextPage(FurloughStartDatePage, mode, updatedAnswers))
        )
    }
  }
}
