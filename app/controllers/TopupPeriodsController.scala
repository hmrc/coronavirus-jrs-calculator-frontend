/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import controllers.actions._
import forms.TopupPeriodsFormProvider
import handlers.FurloughCalculationHandler
import javax.inject.Inject
import navigation.Navigator
import pages.TopupPeriodsPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.TopupPeriodsView

import scala.concurrent.{ExecutionContext, Future}

class TopupPeriodsController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  navigator: Navigator,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: TopupPeriodsFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: TopupPeriodsView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController with I18nSupport with FurloughCalculationHandler {

  val form = formProvider()

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    handleCalculationFurlough(request.userAnswers)
      .map { furlough =>
        val preparedForm = request.userAnswers.get(TopupPeriodsPage) match {
          case None => form
          case Some(selectedDates) =>
            form.fill(selectedDates)
        }

        Ok(view(preparedForm, furlough.payPeriodBreakdowns))
      }
      .getOrElse(
        Redirect(routes.ErrorController.somethingWentWrong())
      )
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    handleCalculationFurlough(request.userAnswers)
      .map { furlough =>
        form
          .bindFromRequest()
          .fold(
            formWithErrors => Future.successful(BadRequest(view(formWithErrors, furlough.payPeriodBreakdowns))), { dates =>
              for {
                updatedAnswers <- Future.fromTry(request.userAnswers.set(TopupPeriodsPage, dates))
                _              <- sessionRepository.set(updatedAnswers)
              } yield Ok(dates.toString())
            }
          )
      }
      .getOrElse(
        Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
      )
  }
}
