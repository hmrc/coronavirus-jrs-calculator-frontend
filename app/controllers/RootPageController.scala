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

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, ShowNewStartPage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.{RootPageView, StartPageView}

import javax.inject.Inject

class RootPageController @Inject()(override val messagesApi: MessagesApi,
                                   val controllerComponents: MessagesControllerComponents,
                                   view: RootPageView,
                                   newView: StartPageView)(implicit appConfig: FrontendAppConfig)
    extends FrontendBaseController with I18nSupport with FeatureSwitching {

  def onPageLoad: Action[AnyContent] = Action { _ =>
    Redirect(routes.RootPageController.start())
  }

  def start: Action[AnyContent] = Action { implicit request =>
    fsGuard[Result](ShowNewStartPage, Ok(view())) {
      Ok(newView(routes.ClaimPeriodStartController.onPageLoad()))
    }
  }
}
