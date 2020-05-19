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

import base.SpecBaseWithApplication
import forms.PayPeriodQuestionFormProvider
import models.{PayPeriodQuestion, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.{PayDatePage, PayPeriodQuestionPage}
import play.api.inject.bind
import play.api.mvc.{AnyContentAsEmpty, Call}
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.PayPeriodQuestionView

import scala.concurrent.Future

class PayPeriodQuestionControllerSpec extends SpecBaseWithApplication with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  private lazy val payPeriodQuestionRoute = routes.PayPeriodQuestionController.onPageLoad().url
  private lazy val payPeriodQuestionRoutePost = routes.PayPeriodQuestionController.onSubmit().url

  val formProvider = new PayPeriodQuestionFormProvider()
  val form = formProvider()

  private val payDate1 = LocalDate.of(2020, 3, 3)
  private val payDate2 = LocalDate.of(2020, 3, 15)
  private val payDate3 = LocalDate.of(2020, 4, 3)

  private val userAnswers = UserAnswers(userAnswersId)
    .set(PayDatePage, payDate1, Some(1))
    .success
    .value
    .set(PayDatePage, payDate2, Some(2))
    .success
    .value
    .set(PayDatePage, payDate3, Some(3))
    .success
    .value

  val getRequest: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(GET, payPeriodQuestionRoute).withCSRFToken
      .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  "PayPeriodQuestion Controller" must {

    "return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val result = route(application, getRequest).value

      val view = application.injector.instanceOf[PayPeriodQuestionView]

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form, Seq(payDate1, payDate2, payDate3).sliding(2))(getRequest, messages).toString

      application.stop()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswersUpdated = userAnswers
        .set(PayPeriodQuestionPage, PayPeriodQuestion.values.head)
        .success
        .value

      val application = applicationBuilder(userAnswers = Some(userAnswersUpdated)).build()

      val view = application.injector.instanceOf[PayPeriodQuestionView]

      val result = route(application, getRequest).value

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form.fill(PayPeriodQuestion.values.head), Seq(payDate1, payDate2, payDate3).sliding(2))(getRequest, messages).toString

      application.stop()
    }

    "redirect to the next page when valid data is submitted" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(userAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute)),
            bind[SessionRepository].toInstance(mockSessionRepository)
          )
          .build()

      val request =
        FakeRequest(POST, payPeriodQuestionRoutePost)
          .withFormUrlEncodedBody(("value", PayPeriodQuestion.values.head.toString))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual onwardRoute.url

      application.stop()
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val request =
        FakeRequest(POST, payPeriodQuestionRoutePost).withCSRFToken
          .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
          .withFormUrlEncodedBody(("value", "invalid value"))

      val boundForm = form.bind(Map("value" -> "invalid value"))

      val view = application.injector.instanceOf[PayPeriodQuestionView]

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      contentAsString(result) mustEqual
        view(boundForm, Seq(payDate1, payDate2, payDate3).sliding(2))(request, messages).toString

      application.stop()
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, payPeriodQuestionRoute)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request =
        FakeRequest(POST, payPeriodQuestionRoute)
          .withFormUrlEncodedBody(("value", PayPeriodQuestion.values.head.toString))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }
  }
}
