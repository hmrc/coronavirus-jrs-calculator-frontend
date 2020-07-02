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

import base.SpecBaseControllerSpecs
import config.FrontendAppConfig
import controllers.actions._
import models.{Language, UserAnswers}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{MustMatchers, OptionValues}
import org.scalatestplus.mockito.MockitoSugar
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository

class LanguageSwitchControllerSpec extends SpecBaseControllerSpecs with MustMatchers with OptionValues with ScalaFutures with MockitoSugar {

  def applicationBuilder(): GuiceApplicationBuilder =
    new GuiceApplicationBuilder()
      .overrides(
        bind[SessionRepository].toInstance(mock[SessionRepository]),
        bind[DataRequiredAction].to[DataRequiredActionImpl],
        bind[IdentifierAction].to[FakeIdentifierAction],
        bind[DataRetrievalAction].toInstance(new FakeDataRetrievalAction(Some(new UserAnswers("id"))))
      )

  "switching language when translation is enabled" should {
    "should set the language to Cymraeg" in {

      val application = applicationBuilder()
        .configure(
          "features.welsh-translation" -> true
        )
        .build()

      running(application) {
        val request = FakeRequest(GET, routes.LanguageSwitchController.switchToLanguage(Language.Cymraeg).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        cookies(result).get("PLAY_LANG").value.value mustEqual "cy"
      }
    }

    "set the language to English" in {

      val application = applicationBuilder()
        .configure(
          "features.welsh-translation" -> true
        )
        .build()

      running(application) {
        val request = FakeRequest(GET, routes.LanguageSwitchController.switchToLanguage(Language.English).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        cookies(result).get("PLAY_LANG").value.value mustEqual "en"
      }
    }
  }

  "when translation is disabled" should {

    "should set the language to English regardless of what is requested" in {
      implicit val appConf: FrontendAppConfig = new FrontendAppConfig {
        override lazy val languageTranslationEnabled: Boolean = false
      }
      val controller = new LanguageSwitchController(appConf, messagesApi, component)

      val cymraegRequest = FakeRequest(GET, routes.LanguageSwitchController.switchToLanguage(Language.Cymraeg).url)
      val englishRequest = FakeRequest(GET, routes.LanguageSwitchController.switchToLanguage(Language.English).url)

      val cymraegResult = controller.switchToLanguage(Language.Cymraeg)(cymraegRequest)
      val englishResult = controller.switchToLanguage(Language.English)(englishRequest)

      status(cymraegResult) mustEqual SEE_OTHER
      cookies(cymraegResult).get("PLAY_LANG").value.value mustEqual "en"

      status(englishResult) mustEqual SEE_OTHER
      cookies(englishResult).get("PLAY_LANG").value.value mustEqual "en"
    }
  }
}
