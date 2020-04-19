/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import java.time.LocalDate

import controllers.actions._
import forms.LastPayDateFormProvider
import javax.inject.Inject
import models.Mode
import navigation.Navigator
import pages.LastPayDatePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.LastPayDateView

import scala.concurrent.{ExecutionContext, Future}

class LastPayDateController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  navigator: Navigator,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: LastPayDateFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: LastPayDateView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController with I18nSupport {

  // TODO: This needs to be the latest date value from pay-dates loop
  def form = formProvider(LocalDate.now())

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    val preparedForm = request.userAnswers.get(LastPayDatePage) match {
      case None        => form
      case Some(value) => form.fill(value)
    }

    Ok(view(preparedForm, mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form
      .bindFromRequest()
      .fold(
        formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(LastPayDatePage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(LastPayDatePage, mode, updatedAnswers))
      )
  }
}
