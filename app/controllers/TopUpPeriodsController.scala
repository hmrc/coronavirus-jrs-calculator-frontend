/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import controllers.actions.FeatureFlag.TopUpJourneyFlag
import controllers.actions._
import forms.TopUpPeriodsFormProvider
import handlers.FurloughCalculationHandler
import javax.inject.Inject
import models.{TopUpPeriod, UserAnswers}
import navigation.Navigator
import pages.TopUpPeriodsPage
import play.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.TopUpPeriodsView

import scala.concurrent.{ExecutionContext, Future}

class TopUpPeriodsController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  navigator: Navigator,
  identify: IdentifierAction,
  feature: FeatureFlagActionProvider,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: TopUpPeriodsFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: TopUpPeriodsView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController with I18nSupport with FurloughCalculationHandler {

  val form = formProvider()

  def onPageLoad(): Action[AnyContent] = (identify andThen feature(TopUpJourneyFlag) andThen getData andThen requireData).async {
    implicit request =>
      handleCalculationFurlough(request.userAnswers)
        .map { furlough =>
          furlough.payPeriodBreakdowns match {
            case breakdown :: Nil =>
              saveAndRedirect(request.userAnswers, List(TopUpPeriod(breakdown.periodWithPaymentDate.period.period.end, breakdown.grant)))
            case _ =>
              val preparedForm = request.userAnswers.get(TopUpPeriodsPage) match {
                case None => form
                case Some(selectedDates) =>
                  form.fill(selectedDates.map(_.date))
              }
              Future.successful(Ok(view(preparedForm, furlough.payPeriodBreakdowns)))
          }
        }
        .getOrElse(
          Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
        )
  }

  def onSubmit(): Action[AnyContent] = (identify andThen feature(TopUpJourneyFlag) andThen getData andThen requireData).async {
    implicit request =>
      handleCalculationFurlough(request.userAnswers)
        .map { furlough =>
          form
            .bindFromRequest()
            .fold(
              formWithErrors => Future.successful(BadRequest(view(formWithErrors, furlough.payPeriodBreakdowns))), { dates =>
                val topUpPeriods = dates.flatMap { date =>
                  furlough.payPeriodBreakdowns
                    .find(_.periodWithPaymentDate.period.period.end == date)
                    .map(_.grant)
                    .map(
                      TopUpPeriod(date, _)
                    )
                }

                if (dates.length != topUpPeriods.length) {
                  Logger.warn("[TopUpPeriodsController][onSubmit] Dates in furlough and input do not align")
                  Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
                } else {
                  saveAndRedirect(request.userAnswers, topUpPeriods)
                }
              }
            )
        }
        .getOrElse(
          Future.successful(Redirect(routes.ErrorController.somethingWentWrong()))
        )
  }

  private def saveAndRedirect(userAnswers: UserAnswers, topUpPeriods: List[TopUpPeriod]) =
    for {
      updatedAnswers <- Future.fromTry(userAnswers.set(TopUpPeriodsPage, topUpPeriods))
      _              <- sessionRepository.set(updatedAnswers)
    } yield Redirect(navigator.nextPage(TopUpPeriodsPage, updatedAnswers))
}
