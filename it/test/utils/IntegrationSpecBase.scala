/*
 * Copyright 2024 HM Revenue & Customs
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

package utils

import config.FrontendAppConfig
import models.UserAnswers
import org.scalatest._
import org.scalatest.concurrent.{Eventually, IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.i18n.{Lang, MessagesApi}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, Environment, Mode}
import repositories.SessionRepository
import uk.gov.hmrc.http.HeaderCarrier

import java.time.LocalDate
import java.util.Locale
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

trait IntegrationSpecBase
  extends PlaySpec with GivenWhenThen with TestSuite with ScalaFutures with IntegrationPatience with WiremockHelper
    with GuiceOneServerPerSuite with TryValues with BeforeAndAfterEach with BeforeAndAfterAll with Eventually with CreateRequestHelper
    with CustomMatchers {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global
  implicit val hc: HeaderCarrier    = HeaderCarrier()
  implicit lazy val messagesApi     = app.injector.instanceOf[MessagesApi]
  implicit lazy val messages        = messagesApi.preferred(Seq(Lang(Locale.UK)))
  implicit lazy val appConfig        = app.injector.instanceOf[FrontendAppConfig]

  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .in(Environment.simple(mode = Mode.Dev))
    .configure(config)
    .build
  val mockHost = WiremockHelper.wiremockHost
  val mockPort = WiremockHelper.wiremockPort.toString
  val mockUrl  = s"http://$mockHost:$mockPort"

  def config: Map[String, String] = Map(
    "play.filters.csrf.header.bypassHeaders.Csrf-Token"          -> "nocheck",
    "play.http.router"                                           -> "testOnlyDoNotUseInAppConf.Routes",
    "auditing.enabled"                                           -> "false",
    "microservice.services.job-retention-scheme-calculator.host" -> mockHost,
    "microservice.services.job-retention-scheme-calculator.port" -> mockPort
  )

  lazy val mongo: SessionRepository = app.injector.instanceOf[SessionRepository]

  def setAnswers(userAnswers: UserAnswers)(implicit timeout: Duration): Unit  = Await.result(mongo.set(userAnswers), timeout)
  def getAnswers(id: String)(implicit timeout: Duration): Option[UserAnswers] = Await.result(mongo.get(id), timeout)

  override def beforeAll(): Unit = {
    super.beforeAll()
    stopWiremock()
    startWiremock()
  }

  override def afterAll(): Unit = {
    stopWiremock()
    super.afterAll()
  }

  def dateToStringFmt(date: LocalDate): String = s"${date.getYear}-${date.getMonthValue}-${date.getDayOfMonth}"


}
