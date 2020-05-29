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

import java.time.LocalDate

import base.{CoreTestDataBuilder, SpecBaseWithApplication}
import config.FrontendAppConfig
import controllers.actions.FeatureFlag.TopUpJourneyFlag
import controllers.actions.{DataRequiredActionImpl, FakeDataRetrievalAction, FakeIdentifierAction, FeatureFlagActionProvider}
import forms.AdditionalPaymentAmountFormProvider
import models.requests.DataRequest
import models.{AdditionalPayment, Amount, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.i18n.MessagesApi
import play.api.inject.bind
import play.api.mvc.{AnyContentAsEmpty, Call, MessagesControllerComponents}
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.AdditionalPaymentAmountView

import scala.concurrent.Future

class AdditionalPaymentAmountControllerSpec extends SpecBaseWithApplication with MockitoSugar with CoreTestDataBuilder {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new AdditionalPaymentAmountFormProvider()
  val form = formProvider()

  def additionalPaymentAmountRoute(idx: Int) = routes.AdditionalPaymentAmountController.onPageLoad(idx).url
  def getRequest(method: String, idx: Int) =
    FakeRequest(method, additionalPaymentAmountRoute(idx)).withCSRFToken.asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  import scala.concurrent.ExecutionContext.Implicits.global
  val msgApi = app.injector.instanceOf[MessagesApi]
  val flagProvider = app.injector.instanceOf[FeatureFlagActionProvider]
  val component = app.injector.instanceOf[MessagesControllerComponents]
  val view = app.injector.instanceOf[AdditionalPaymentAmountView]
  val appConf = app.injector.instanceOf[FrontendAppConfig]
  val identifier = app.injector.instanceOf[FakeIdentifierAction]
  val dataRequired = app.injector.instanceOf[DataRequiredActionImpl]
  val navigator = app.injector.instanceOf[Navigator]
  val mockRepository = mock[SessionRepository]
  val dataRetrieval: Option[UserAnswers] => FakeDataRetrievalAction =
    stubbedAnswer => new FakeDataRetrievalAction(stubbedAnswer)

  def controller(stubbedAnswer: UserAnswers = emptyUserAnswers) =
    new AdditionalPaymentAmountController(
      msgApi,
      mockRepository,
      navigator,
      identifier,
      flagProvider,
      dataRetrieval(Some(stubbedAnswer)),
      dataRequired,
      formProvider,
      component,
      view)

  "AdditionalPaymentAmount Controller" must {

    "return OK and the correct view for a GET" in {
      val additionalPaymentPeriod = LocalDate.of(2020, 3, 31)
      val request = getRequest(GET, 1)
      val userAnswers = mandatoryAnswersOnRegularMonthly
        .withAdditionalPaymentPeriods(List(additionalPaymentPeriod.toString))
      val result = controller(
        userAnswers).onPageLoad(1)(request)

      status(result) mustEqual OK

      val dataRequest = DataRequest(request, userAnswers.id, userAnswers)

      contentAsString(result) mustEqual
        view(form, additionalPaymentPeriod, 1)(dataRequest, messages).toString
    }

    "redirect to error page for GET when index is not valid" when {
      "index is zero, negative or too high" in {
        val additionalPaymentPeriod = LocalDate.of(2020, 3, 31)
        val userAnswers = mandatoryAnswersOnRegularMonthly
          .withAdditionalPaymentPeriods(List(additionalPaymentPeriod.toString))

        val resultOfZero = controller(userAnswers).onPageLoad(0).apply(getRequest(GET, 0))
        val resultOfNegative = controller(userAnswers).onPageLoad(-1).apply(getRequest(GET, -1))
        val resultTooHigh = controller(userAnswers).onPageLoad(2).apply(getRequest(GET, 2))

        status(resultOfZero) mustEqual SEE_OTHER
        redirectLocation(resultOfZero).value mustEqual routes.ErrorController.somethingWentWrong().url
        redirectLocation(resultOfNegative).value mustEqual routes.ErrorController.somethingWentWrong().url
        redirectLocation(resultTooHigh).value mustEqual routes.ErrorController.somethingWentWrong().url
      }
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val additionalPaymentPeriod = LocalDate.of(2020, 3, 31)
      val amount = Amount(100)
      val additionalPayment = AdditionalPayment(additionalPaymentPeriod, amount)

      val userAnswers = mandatoryAnswersOnRegularMonthly
        .withAdditionalPaymentPeriods(List(additionalPaymentPeriod.toString))
        .withAdditionalPaymentAmount(additionalPayment, Some(1))

      val request = getRequest(GET, 1)
      val result = controller(userAnswers).onPageLoad(1)(request)

      status(result) mustEqual OK

      val dataRequest = DataRequest(request, userAnswers.id, userAnswers)

      contentAsString(result) mustEqual
        view(form.fill(amount), additionalPaymentPeriod, 1)(dataRequest, messages).toString
    }

    "redirect to the next page when valid data is submitted" in {
      val additionalPaymentPeriod = LocalDate.of(2020, 3, 31)

      val userAnswers = mandatoryAnswersOnRegularMonthly.withAdditionalPaymentPeriods(List(additionalPaymentPeriod.toString))

      when(mockRepository.set(any())) thenReturn Future.successful(true)

      val request =
        getRequest(POST, 1)
          .withFormUrlEncodedBody(("value", "100.00"))

      val result = controller(userAnswers).onSubmit(1)(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual "/job-retention-scheme-calculator/ni-category-letter"
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val additionalPaymentPeriod = LocalDate.of(2020, 3, 31)

      val userAnswers = mandatoryAnswersOnRegularMonthly.withAdditionalPaymentPeriods(List(additionalPaymentPeriod.toString))

      val request =
        getRequest(POST, 1).withCSRFToken
          .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
          .withFormUrlEncodedBody(("value", ""))

      val boundForm = form.bind(Map("value" -> ""))

      val result = controller(userAnswers).onSubmit(1)(request)

      status(result) mustEqual BAD_REQUEST

      val dataRequest = DataRequest(request, userAnswers.id, userAnswers)

      contentAsString(result) mustEqual
        view(boundForm, additionalPaymentPeriod, 1)(dataRequest, messages).toString
    }

    "redirect to 404 page for a GET if topups flag is disabled" in {

      val application = applicationBuilder(config = Map(TopUpJourneyFlag.key -> false), userAnswers = Some(UserAnswers("id"))).build()

      val request = getRequest(GET, 1)

      val result = route(application, request).value

      status(result) mustEqual NOT_FOUND

      application.stop()
    }

    "redirect to 404 page for a POST if topups flag is disabled" in {

      val application = applicationBuilder(config = Map(TopUpJourneyFlag.key -> false), userAnswers = Some(UserAnswers("id"))).build()

      val request =
        getRequest(POST, 1)
          .withFormUrlEncodedBody(("value", "100.00"))

      val result = route(application, request).value

      status(result) mustEqual NOT_FOUND

      application.stop()
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = getRequest(GET, 1)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request =
        getRequest(POST, 1)
          .withFormUrlEncodedBody(("value", "100.00"))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }
  }
}
