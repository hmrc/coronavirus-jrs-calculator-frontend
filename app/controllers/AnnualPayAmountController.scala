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
import controllers.actions.FeatureFlag.VariableJourneyFlag
import controllers.actions._
import forms.AnnualPayAmountFormProvider
import javax.inject.Inject
import models.AnnualPayAmount
import navigation.Navigator
import pages.{AnnualPayAmountPage, FurloughStartDatePage}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import views.html.AnnualPayAmountView

import scala.concurrent.{ExecutionContext, Future}

class AnnualPayAmountController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  val navigator: Navigator,
  identify: IdentifierAction,
  feature: FeatureFlagActionProvider,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  formProvider: AnnualPayAmountFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: AnnualPayAmountView
)(implicit ec: ExecutionContext)
    extends BaseController with I18nSupport {

  val form: Form[AnnualPayAmount] = formProvider()

  def onPageLoad(): Action[AnyContent] =
    (identify andThen feature(VariableJourneyFlag) andThen getData andThen requireData).async { implicit request =>
      request.userAnswers.getV(FurloughStartDatePage) match {
        case Valid(furloughStart) =>
          val preparedForm = request.userAnswers.getV(AnnualPayAmountPage) match {
            case Invalid(e)   => form
            case Valid(value) => form.fill(value)
          }

          Future.successful(Ok(view(preparedForm, furloughStart)))

        case Invalid(e) => Future.successful(Redirect(routes.FurloughStartDateController.onPageLoad()))
      }
    }

  def onSubmit(): Action[AnyContent] =
    (identify andThen feature(VariableJourneyFlag) andThen getData andThen requireData).async { implicit request =>
      request.userAnswers.getV(FurloughStartDatePage) match {
        case Valid(furloughStart) =>
          form
            .bindFromRequest()
            .fold(
              formWithErrors => Future.successful(BadRequest(view(formWithErrors, furloughStart))),
              value =>
                for {
                  updatedAnswers <- Future.fromTry(request.userAnswers.set(AnnualPayAmountPage, value))
                  _              <- sessionRepository.set(updatedAnswers)
                } yield Redirect(navigator.nextPage(AnnualPayAmountPage, updatedAnswers))
            )

        case Invalid(_) => Future.successful(Redirect(routes.FurloughStartDateController.onPageLoad()))
      }
    }
}
