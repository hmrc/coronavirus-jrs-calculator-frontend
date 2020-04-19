/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import java.time.LocalDate

import controllers.actions._
<<<<<<< HEAD
import forms.FurloughPartialPayFormProvider
=======
import forms.VariableLengthPartialPayFormProvider
>>>>>>> 99695f13f65c4f3be36cb188c073ce349bf0618b
import handlers.ErrorHandler
import javax.inject.Inject
import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}
import models.{Mode, NormalMode, PaymentFrequency}
import navigation.Navigator
import pages._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import views.html.VariableLengthPartialPayView

import scala.concurrent.{ExecutionContext, Future}

class PartialPayBeforeFurloughController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  navigator: Navigator,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
<<<<<<< HEAD
  formProvider: FurloughPartialPayFormProvider,
=======
  formProvider: VariableLengthPartialPayFormProvider,
>>>>>>> 99695f13f65c4f3be36cb188c073ce349bf0618b
  val controllerComponents: MessagesControllerComponents,
  view: VariableLengthPartialPayView
)(implicit ec: ExecutionContext, errorHandler: ErrorHandler)
    extends BaseController with I18nSupport {

  val form = formProvider()

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    getRequiredAnswers(ClaimPeriodStartPage, FurloughStartDatePage) { (claimStartDate, furloughStartDate) =>
      val result =
        if (furloughStartDate.isAfter(claimStartDate.plusDays(1))) {
          val preparedForm = request.userAnswers.get(PartialPayBeforeFurloughPage) match {
            case None        => form
            case Some(value) => form.fill(value)
          }
          Ok(
            view(
              preparedForm,
              latestOf(claimStartDate, furloughStartDate, request.userAnswers.get(PaymentFrequencyPage)),
              furloughStartDate.minusDays(1),
              routes.PartialPayBeforeFurloughController.onSubmit()
            ))
        } else {
          Redirect(routes.PartialPayAfterFurloughController.onPageLoad())
        }
      Future.successful(result)
    }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    getRequiredAnswers(ClaimPeriodStartPage, FurloughStartDatePage) { (claimStartDate, furloughStartDate) =>
      form
        .bindFromRequest()
        .fold(
          formWithErrors =>
            Future.successful(BadRequest(
              view(formWithErrors, claimStartDate, furloughStartDate.minusDays(1), routes.PartialPayBeforeFurloughController.onSubmit()))),
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(PartialPayBeforeFurloughPage, value))
              _              <- sessionRepository.set(updatedAnswers)
            } yield {
              Redirect(navigator.nextPage(PartialPayBeforeFurloughPage, NormalMode, updatedAnswers))
          }
        )
    }
  }

  private def latestOf(claimStart: LocalDate, furloughStart: LocalDate, paymentFrequency: Option[PaymentFrequency]): LocalDate = {
    def latestOf(a: LocalDate, b: LocalDate): LocalDate = if (a.isAfter(b)) a else b
    paymentFrequency match {
      case Some(Weekly)      => latestOf(furloughStart.minusDays(7), claimStart)
      case Some(FortNightly) => latestOf(furloughStart.minusDays(14), claimStart)
      case Some(FourWeekly)  => latestOf(furloughStart.minusDays(28), claimStart)
      case Some(Monthly)     => latestOf(furloughStart.minusMonths(1), claimStart)
    }
  }
}
