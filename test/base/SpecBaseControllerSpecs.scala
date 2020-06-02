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

package base

import controllers.actions._
import handlers.ErrorHandler
import models.UserAnswers
import models.UserAnswers.AnswerV
import navigation.Navigator
import org.scalatest.TryValues
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice._
import play.api.Configuration
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject.{Injector, bind}
import play.api.mvc.{AnyContentAsEmpty, MessagesControllerComponents}
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import utils.CoreTestData
import views.html.AdditionalPaymentAmountView

trait SpecBaseControllerSpecs
    extends PlaySpec with GuiceOneAppPerSuite with TryValues with ScalaFutures with IntegrationPatience with CoreTestData {

  def injector: Injector = app.injector
  def messagesApi = app.injector.instanceOf[MessagesApi]
  val component = app.injector.instanceOf[MessagesControllerComponents]
  val view = app.injector.instanceOf[AdditionalPaymentAmountView]
  val identifier = app.injector.instanceOf[FakeIdentifierAction]
  val dataRequired = app.injector.instanceOf[DataRequiredActionImpl]
  val navigator = app.injector.instanceOf[Navigator]
  implicit val errorHandler: ErrorHandler = app.injector.instanceOf[ErrorHandler]
  implicit val conf: Configuration = app.injector.instanceOf[Configuration]

  implicit class AnswerHelpers[A](val answer: AnswerV[A]) {}

  lazy val fakeRequest: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest("", "").withCSRFToken.asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  implicit def messages: Messages = messagesApi.preferred(fakeRequest)

  protected def applicationBuilder(
    userAnswers: Option[UserAnswers] = None,
    config: Map[String, Any] = Map("variable.journey.enabled" -> true, "topup.journey.enabled" -> true)): GuiceApplicationBuilder =
    new GuiceApplicationBuilder()
      .overrides(
        bind[DataRequiredAction].to[DataRequiredActionImpl],
        bind[IdentifierAction].to[FakeIdentifierAction],
        bind[DataRetrievalAction].toInstance(new FakeDataRetrievalAction(userAnswers))
      )
      .configure(config)
}
