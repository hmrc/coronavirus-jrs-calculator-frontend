/*
 * Copyright 2023 HM Revenue & Customs
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

import com.typesafe.config.ConfigValue
import config.FrontendAppConfig
import controllers.actions._
import handlers.ErrorHandler
import models.UserAnswers.AnswerV
import navigation.Navigator
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice._
import play.api.Configuration
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.Injector
import play.api.libs.json.{JsError, JsPath, JsonValidationError}
import play.api.mvc.{AnyContentAsEmpty, MessagesControllerComponents}
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import repositories.SessionRepository
import utils.CoreTestData

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait SpecBaseControllerSpecs extends PlaySpec with GuiceOneAppPerSuite with CoreTestData with MockitoSugar {

  def injector: Injector                  = app.injector
  def messagesApi                         = injector.instanceOf[MessagesApi]
  val component                           = injector.instanceOf[MessagesControllerComponents]
  val identifier                          = injector.instanceOf[FakeIdentifierAction]
  val dataRequired                        = injector.instanceOf[DataRequiredActionImpl]
  val navigator                           = injector.instanceOf[Navigator]
  val dataRetrieval                       = new DataRetrievalActionImpl(mockSessionRepository)
  implicit val errorHandler: ErrorHandler = injector.instanceOf[ErrorHandler]
  implicit val appConf: FrontendAppConfig = injector.instanceOf[FrontendAppConfig]

  implicit class AnswerHelpers[A](val answer: AnswerV[A]) {}

  lazy val fakeRequest: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest("", "").withCSRFToken.asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
  implicit val messages: Messages = messagesApi.preferred(fakeRequest)

  private val configKeyValues: Set[(String, ConfigValue)] = injector.instanceOf[Configuration].entrySet

  def configValues(kv: (String, Any)): List[(String, Any)] =
    configKeyValues.toMap.+(kv._1 -> kv._2).toList

  lazy val mockSessionRepository: SessionRepository = {
    val mockSession = mock[SessionRepository]
    when(mockSession.set(any())) thenReturn Future.successful(true)
    mockSession
  }

  def emptyError(
    path: JsPath,
    error: String = "error.path.missing"
  ): JsError =
    JsError(path -> JsonValidationError(List(error)))

  def emptyError(
    path: JsPath,
    idx: Int,
    error: String
  ): JsError =
    JsError((path \ (idx - 1)) -> JsonValidationError(List(error)))

  def dateToStringFmt(date: LocalDate): String = s"${date.getYear}, ${date.getMonth}, ${date.getDayOfMonth}"

}
