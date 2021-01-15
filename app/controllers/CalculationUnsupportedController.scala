/*
 * Copyright 2021 HM Revenue & Customs
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

import controllers.actions.{DataRetrievalAction, IdentifierAction}
import javax.inject.Inject
import models.UserAnswers
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.CalculationUnsupportView

import scala.concurrent.ExecutionContext

class CalculationUnsupportedController @Inject()(
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  calculationUnsupportedView: CalculationUnsupportView,
  val controllerComponents: MessagesControllerComponents)(implicit ec: ExecutionContext)
    extends FrontendBaseController with I18nSupport {

  def multipleFurloughUnsupported: Action[AnyContent] = (identify andThen getData) { implicit request =>
    Ok(calculationUnsupportedView())
  }

  def startDateWithinLookbackUnsupported: Action[AnyContent] = (identify andThen getData) { implicit request =>
    Ok(calculationUnsupportedView())
  }
}
