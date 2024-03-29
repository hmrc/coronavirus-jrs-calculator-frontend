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

package controllers

import assets.constants.PaymentFrequencyConstants.allRadioOptions
import base.SpecBaseControllerSpecs
import forms.PaymentFrequencyFormProvider
import models.requests.DataRequest
import models.{PaymentFrequency, UserAnswers}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded}
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import views.html.PaymentFrequencyView

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PaymentFrequencyControllerSpec extends SpecBaseControllerSpecs with MockitoSugar {

  lazy val paymentFrequencyRoute     = routes.PaymentFrequencyController.onPageLoad().url
  lazy val paymentFrequencyRoutePost = routes.PaymentFrequencyController.onSubmit().url

  val formProvider = new PaymentFrequencyFormProvider()
  val form         = formProvider()

  lazy val getRequest: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(GET, paymentFrequencyRoute).withCSRFToken
      .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  lazy val postRequest: FakeRequest[AnyContentAsFormUrlEncoded] =
    FakeRequest(POST, paymentFrequencyRoutePost).withCSRFToken
      .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
      .withFormUrlEncodedBody(
        "value" -> "weekly"
      )

  val view = app.injector.instanceOf[PaymentFrequencyView]

  val controller = new PaymentFrequencyController(messagesApi,
                                                  mockSessionRepository,
                                                  navigator,
                                                  identifier,
                                                  dataRetrieval,
                                                  dataRequired,
                                                  formProvider,
                                                  component,
                                                  view)

  "PaymentFrequency Controller" must {

    "return OK and the correct view for a GET" in {
      when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(emptyUserAnswers))
      val result      = controller.onPageLoad()(getRequest)
      val dataRequest = DataRequest(getRequest, emptyUserAnswers.id, emptyUserAnswers)

      status(result) mustEqual OK
      contentAsString(result) mustEqual
        view(
          form = form,
          postAction = controllers.routes.PaymentFrequencyController.onSubmit(),
          radioItems = allRadioOptions()
        )(dataRequest, messages).toString
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val userAnswers = UserAnswers(userAnswersId).withPaymentFrequency(PaymentFrequency.values.head)
      when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswers))
      val result      = controller.onPageLoad()(getRequest)
      val dataRequest = DataRequest(getRequest, userAnswers.id, userAnswers)

      status(result) mustEqual OK
      contentAsString(result) mustEqual
        view(form = form.fill(PaymentFrequency.values.head),
             postAction = controllers.routes.PaymentFrequencyController.onSubmit(),
             radioItems = allRadioOptions())(dataRequest, messages).toString
    }

    "redirect to the next page when valid data is submitted" in {
      when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(emptyUserAnswers))
      val result = controller.onSubmit()(postRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual "/job-retention-scheme-calculator/pay-method"
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(emptyUserAnswers))
      val boundForm = form.bind(Map("value" -> "invalid value"))
      val request =
        FakeRequest(POST, paymentFrequencyRoutePost).withCSRFToken
          .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
          .withFormUrlEncodedBody(("value", "invalid value"))

      val dataRequest = DataRequest(request, emptyUserAnswers.id, emptyUserAnswers)
      val result      = controller.onSubmit()(request)

      status(result) mustEqual BAD_REQUEST
      contentAsString(result) mustEqual
        view(
          form = boundForm,
          postAction = controllers.routes.PaymentFrequencyController.onSubmit(),
          radioItems = allRadioOptions()
        )(dataRequest, messages).toString
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      when(mockSessionRepository.get(any())) thenReturn Future.successful(None)
      val result = controller.onPageLoad()(getRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      when(mockSessionRepository.get(any())) thenReturn Future.successful(None)
      val result = controller.onSubmit()(postRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url
    }
  }
}
