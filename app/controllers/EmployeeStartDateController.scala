/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import controllers.actions.FeatureFlag.VariableJourneyFlag
import controllers.actions._
import forms.EmployeeStartDateFormProvider
import javax.inject.Inject
import models.Mode
import navigation.Navigator
import pages.EmployeeStartDatePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.EmployeeStartDateView

import scala.concurrent.{ExecutionContext, Future}

class EmployeeStartDateController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  navigator: Navigator,
  identify: IdentifierAction,
  feature: FeatureFlagActionProvider,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: EmployeeStartDateFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: EmployeeStartDateView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController with I18nSupport {

  def form = formProvider()

  def onPageLoad(): Action[AnyContent] = (identify andThen feature(VariableJourneyFlag) andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.get(EmployeeStartDatePage) match {
        case None        => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm))
  }

  def onSubmit(): Action[AnyContent] = (identify andThen feature(VariableJourneyFlag) andThen getData andThen requireData).async {
    implicit request =>
      form
        .bindFromRequest()
        .fold(
          formWithErrors => Future.successful(BadRequest(view(formWithErrors))),
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(EmployeeStartDatePage, value))
              _              <- sessionRepository.set(updatedAnswers)
            } yield Redirect(navigator.nextPage(EmployeeStartDatePage, updatedAnswers))
        )
  }
}
