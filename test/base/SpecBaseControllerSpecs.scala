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

