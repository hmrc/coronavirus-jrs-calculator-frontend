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

import base.SpecBaseControllerSpecs
import config.featureSwitch.FeatureSwitching
import forms.StatutoryLeavePayFormProvider
import models.requests.DataRequest
import models.{Amount, AnnualPayAmount}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.{AnnualPayAmountPage, StatutoryLeavePayPage}
import play.api.data.Form
import play.api.mvc.AnyContentAsEmpty
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import views.html.StatutoryLeavePayView

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class StatutoryLeavePayControllerSpec extends SpecBaseControllerSpecs with MockitoSugar with FeatureSwitching {

  val formProvider                                 = new StatutoryLeavePayFormProvider()
  def form(referencePay: BigDecimal): Form[Amount] = formProvider(referencePay)

  lazy val statutoryLeavePayRoute: String = routes.StatutoryLeavePayController.onPageLoad().url

  val getRequest: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(GET, statutoryLeavePayRoute).withCSRFToken
      .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  val view: StatutoryLeavePayView = app.injector.instanceOf[StatutoryLeavePayView]

  val controller = new StatutoryLeavePayController(messagesApi,
                                                   mockSessionRepository,
                                                   navigator,
                                                   identifier,
                                                   dataRetrieval,
                                                   dataRequired,
                                                   formProvider,
                                                   component,
                                                   view)

  "StatutoryLeavePay Controller" must {

    "onPageLoad" must {
      val referencePay = BigDecimal(420.00)

      "return OK and the correct view for a GET" in {
        val userAnswers = emptyUserAnswers
          .set(AnnualPayAmountPage, AnnualPayAmount(referencePay))
          .success
          .value
        when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswers))
        val result = controller.onPageLoad()(getRequest)

        status(result) mustEqual OK
        val dataRequest = DataRequest(getRequest, userAnswers.id, userAnswers)

        contentAsString(result) mustEqual
          view(form(referencePay))(dataRequest, messages).toString
      }

      "populate the view correctly on a GET when the question has previously been answered" in {
        val amountThatShouldBePrePopulated = BigDecimal(420.10)

        val userAnswers = emptyUserAnswers
          .set(StatutoryLeavePayPage, Amount(amountThatShouldBePrePopulated.bigDecimal))
          .success
          .value
          .set(AnnualPayAmountPage, AnnualPayAmount(BigDecimal(420.20)))
          .success
          .value
        when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswers))

        val result = controller.onPageLoad()(getRequest)
        status(result) mustEqual OK
        val dataRequest = DataRequest(getRequest, userAnswers.id, userAnswers)

        contentAsString(result) mustEqual
          view(form(referencePay).fill(Amount(amountThatShouldBePrePopulated.bigDecimal)))(dataRequest, messages).toString
      }

      "redirect to Session Expired for a GET if no existing data is found" in {
        when(mockSessionRepository.get(any())) thenReturn Future.successful(None)
        val result = controller.onPageLoad()(getRequest)

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url
      }

      "show an ISE when the user hasn't entered the AnnualPayAmount previously" in {
        when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(emptyUserAnswers))
        val result = controller.onPageLoad()(getRequest)

        status(result) mustEqual INTERNAL_SERVER_ERROR
      }
    }

    "onSubmit" must {
      val referencePay = BigDecimal(420.01)

      "redirect to the next page when valid data is submitted" in {
        val userAnswers = emptyUserAnswers
          .set(AnnualPayAmountPage, AnnualPayAmount(BigDecimal(420.20)))
          .success
          .value
        when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswers))
        val request =
          FakeRequest(POST, statutoryLeavePayRoute)
            .withFormUrlEncodedBody(("value", "420.19"))

        val result = controller.onSubmit()(request)

        status(result) mustEqual SEE_OTHER

        redirectLocation(result).value mustEqual routes.PartTimeQuestionController.onPageLoad().url
      }

      "return a Bad Request and errors when invalid data is submitted" in {
        val userAnswers = emptyUserAnswers
          .set(AnnualPayAmountPage, AnnualPayAmount(BigDecimal(420.01)))
          .success
          .value
        when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswers))
        val request =
          FakeRequest(POST, statutoryLeavePayRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form(referencePay).bind(Map("value" -> "invalid value"))

        val result      = controller.onSubmit()(request)
        val dataRequest = DataRequest(request, userAnswers.id, userAnswers)

        status(result) mustEqual BAD_REQUEST

        contentAsString(result) mustEqual
          view(boundForm)(dataRequest, messages).toString
      }

      "return a Bad Request and errors when the value entered is EQUAL to the reference pay" in {
        val userAnswers = emptyUserAnswers
          .set(AnnualPayAmountPage, AnnualPayAmount(BigDecimal(420.01)))
          .success
          .value
        when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswers))
        val request =
          FakeRequest(POST, statutoryLeavePayRoute)
            .withFormUrlEncodedBody(("value", "420.01"))

        val boundForm = form(referencePay).bind(Map("value" -> "420.01"))

        val result      = controller.onSubmit()(request)
        val dataRequest = DataRequest(request, userAnswers.id, userAnswers)

        status(result) mustEqual BAD_REQUEST

        contentAsString(result) mustEqual
          view(boundForm)(dataRequest, messages).toString
      }

      "return a Bad Request and errors when the value entered is MORE THAN the reference pay" in {
        val userAnswers = emptyUserAnswers
          .set(AnnualPayAmountPage, AnnualPayAmount(BigDecimal(420.01)))
          .success
          .value
        when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswers))
        val request =
          FakeRequest(POST, statutoryLeavePayRoute)
            .withFormUrlEncodedBody(("value", "420.02"))

        val boundForm = form(referencePay).bind(Map("value" -> "420.02"))

        val result      = controller.onSubmit()(request)
        val dataRequest = DataRequest(request, userAnswers.id, userAnswers)

        status(result) mustEqual BAD_REQUEST

        contentAsString(result) mustEqual
          view(boundForm)(dataRequest, messages).toString
      }

      "redirect to Session Expired for a POST if no existing data is found" in {
        when(mockSessionRepository.get(any())) thenReturn Future.successful(None)
        val request = FakeRequest(POST, statutoryLeavePayRoute)
        val result  = controller.onSubmit()(request)

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url
      }
    }
  }
}
