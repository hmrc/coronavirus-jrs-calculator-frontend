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
import forms.FurloughOngoingFormProvider
import models.FurloughStatus
import models.requests.DataRequest
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.AnyContentAsEmpty
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import views.html.FurloughOngoingView

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FurloughOngoingControllerSpec extends SpecBaseControllerSpecs with MockitoSugar {

  lazy val furloughOngoingRoute = routes.FurloughOngoingController.onPageLoad().url

  val formProvider = new FurloughOngoingFormProvider()
  val form = formProvider()
  val phaseOne = period("2020,3,1", "2020,3,31")
  val phaseTwo = period("2020,7,1", "2020,7,31")

  val getRequest: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(GET, furloughOngoingRoute).withCSRFToken
      .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  val view = app.injector.instanceOf[FurloughOngoingView]

  val controller = new FurloughOngoingController(
    messagesApi,
    mockSessionRepository,
    navigator,
    identifier,
    dataRetrieval,
    dataRequired,
    formProvider,
    component,
    view)

  "furloughOngoing Controller" must {

    "return OK and the correct view for a GET" in {
      val userAnswers = emptyUserAnswers
        .withClaimPeriodStart(phaseOne.start.toString)
        .withClaimPeriodEnd(phaseOne.end.toString)
      when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswers))

      val result = controller.onPageLoad()(getRequest)
      val dataRequest = DataRequest(getRequest, userAnswers.id, userAnswers)

      status(result) mustEqual OK
      contentAsString(result) mustEqual
        view(form, phaseOne.start, phaseOne.end)(dataRequest, messages).toString
    }

    "return OK and the correct view for a GET if phase two" in {
      val userAnswers = emptyUserAnswers
        .withClaimPeriodStart(phaseTwo.start.toString)
        .withClaimPeriodEnd(phaseTwo.end.toString)
      when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswers))

      val result = controller.onPageLoad()(getRequest)
      val dataRequest = DataRequest(getRequest, userAnswers.id, userAnswers)

      status(result) mustEqual OK
      contentAsString(result) mustEqual
        view(form, phaseTwo.start, phaseTwo.end)(dataRequest, messages).toString
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val userAnswers = emptyUserAnswers
        .withFurloughStatus(FurloughStatus.conditionalValues(phaseOne.start).head)
        .withClaimPeriodStart(phaseOne.start.toString)
        .withClaimPeriodEnd(phaseOne.end.toString)

      when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswers))

      val result = controller.onPageLoad()(getRequest)

      status(result) mustEqual OK

      val dataRequest = DataRequest(getRequest, userAnswers.id, userAnswers)

      contentAsString(result) mustEqual
        view(form.fill(FurloughStatus.values.head), phaseOne.start, phaseOne.end)(dataRequest, messages).toString
    }

    "redirect to the next page when valid data is submitted" in {
      val userAnswers = emptyUserAnswers
        .withClaimPeriodStart(phaseOne.start.toString)
        .withClaimPeriodEnd(phaseOne.end.toString)
      when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswers))

      val request =
        FakeRequest(POST, furloughOngoingRoute)
          .withFormUrlEncodedBody(("value", FurloughStatus.conditionalValues(phaseOne.start).head.toString))

      val result = controller.onSubmit()(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual "/job-retention-scheme-calculator/furlough-end"
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val userAnswers = emptyUserAnswers
        .withClaimPeriodStart(phaseOne.start.toString)
        .withClaimPeriodEnd(phaseOne.end.toString)
      when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswers))

      val request =
        FakeRequest(POST, furloughOngoingRoute).withCSRFToken
          .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
          .withFormUrlEncodedBody(("value", "invalid value"))

      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller.onSubmit()(request)

      status(result) mustEqual BAD_REQUEST

      val dataRequest = DataRequest(request, userAnswers.id, userAnswers)

      contentAsString(result) mustEqual
        view(boundForm, phaseOne.start, phaseOne.end)(dataRequest, messages).toString
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      when(mockSessionRepository.get(any())) thenReturn Future.successful(None)
      val result = controller.onPageLoad()(getRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      when(mockSessionRepository.get(any())) thenReturn Future.successful(None)
      val request =
        FakeRequest(POST, furloughOngoingRoute)
          .withFormUrlEncodedBody(("value", FurloughStatus.conditionalValues(phaseOne.start).head.toString))

      val result = controller.onSubmit()(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url
    }
  }
}
