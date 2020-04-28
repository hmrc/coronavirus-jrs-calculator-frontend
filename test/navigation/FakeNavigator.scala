/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package navigation

import config.FrontendAppConfig
import play.api.mvc.Call
import pages._
import models.{Mode, UserAnswers}

class FakeNavigator(desiredRoute: Call)(implicit appConfig: FrontendAppConfig) extends Navigator(appConfig) {

  override def nextPage(page: Page, userAnswers: UserAnswers, idx: Option[Int] = None): Call =
    desiredRoute
}
