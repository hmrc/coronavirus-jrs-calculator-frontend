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

import base.SpecBaseControllerSpecs
import forms.FurloughPeriodQuestionFormProvider
import models.ClaimPeriodQuestion.ClaimOnSamePeriod
import models.FurloughPeriodQuestion
import models.FurloughStatus.{FurloughEnded, FurloughOngoing}
import models.requests.DataRequest
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.FurloughPeriodQuestionPage
import play.api.mvc.AnyContentAsEmpty
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import views.html.FurloughPeriodQuestionView

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FurloughPeriodQuestionControllerSpec extends SpecBaseControllerSpecs with MockitoSugar {

  lazy val furloughPeriodQuestionRoute = routes.FurloughPeriodQuestionController.onPageLoad().url
  lazy val furloughPeriodQuestionRoutePost = routes.FurloughPeriodQuestionController.onSubmit().url

  val formProvider = new FurloughPeriodQuestionFormProvider()
  val form = formProvider()

  val getRequest: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(GET, furloughPeriodQuestionRoute).withCSRFToken
      .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  val claimStart = LocalDate.of(2020, 4, 1)
  val furloughStart = LocalDate.of(2020, 4, 1)
  val furloughEnd = furloughStart.plusDays(20)
  val furloughStatus = FurloughOngoing

  val userAnswers = dummyUserAnswers
    .withFurloughStartDate(furloughStart.toString)
    .withFurloughStatus(furloughStatus)
    .withClaimPeriodQuestion(ClaimOnSamePeriod)

  val view = app.injector.instanceOf[FurloughPeriodQuestionView]

  val controller = new FurloughPeriodQuestionController(
    messagesApi,
    mockSessionRepository,
    navigator,
    identifier,
    dataRetrieval,
    dataRequired,
    formProvider,
    component,
    view)

  "FurloughPeriodQuestion Controller" must {

    "return OK and the correct view for a GET when Furlough is Ongoing" in {
      when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswers))

      val result = controller.onPageLoad()(getRequest)

      status(result) mustEqual OK

      val dataRequest = DataRequest(getRequest, userAnswers.id, userAnswers)

      contentAsString(result) mustEqual
        view(form, claimStart, furloughStart, furloughStatus, None)(dataRequest, messages).toString
    }

    "return OK and the correct view for a GET when Furlough is Ended" in {
      val userAnswersUpdated = emptyUserAnswers
        .withClaimPeriodStart(claimStart.toString)
        .withClaimPeriodEnd(furloughEnd.toString)
        .withFurloughStartDate(furloughStart.toString)
        .withFurloughEndDate(furloughEnd.toString)
        .withFurloughStatus(FurloughEnded)
        .withPayDate(List(furloughEnd.toString))

      when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswersUpdated))

      val result = controller.onPageLoad()(getRequest)

      status(result) mustEqual OK

      val dataRequest = DataRequest(getRequest, userAnswersUpdated.id, userAnswersUpdated)

      contentAsString(result) mustEqual
        view(form, claimStart, furloughStart, FurloughEnded, Some(furloughEnd))(dataRequest, messages).toString
    }

    "return OK and the correct view for a GET with phase two content" in {
      val userAnswersUpdated = emptyUserAnswers
        .withClaimPeriodStart("2020,7,1")
        .withClaimPeriodEnd("2020,7,30")
        .withFurloughStartDate("2020,7,1")
        .withFurloughEndDate("2020,7,30")
        .withFurloughStatus(FurloughEnded)
        .withPayDate(List(furloughEnd.toString))

      when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswersUpdated))

      val result = controller.onPageLoad()(getRequest)
      status(result) mustEqual OK

      contentAsString(result) must include("<title> Are the furlough dates the same for this employee?")
      contentAsString(result) must include("""<h1 class="govuk-heading-xl">Are the furlough dates the same for this employee?</h1>""")
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val userAnswersUpdated = userAnswers.set(FurloughPeriodQuestionPage, FurloughPeriodQuestion.values.head).success.value

      when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswersUpdated))

      val result = controller.onPageLoad()(getRequest)

      status(result) mustEqual OK

      val dataRequest = DataRequest(getRequest, userAnswersUpdated.id, userAnswersUpdated)

      contentAsString(result) mustEqual
        view(form.fill(FurloughPeriodQuestion.values.head), claimStart, furloughStart, furloughStatus, None)(dataRequest, messages).toString
    }

    "redirect to the next page when valid data is submitted" in {
      when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswers))

      val request =
        FakeRequest(POST, furloughPeriodQuestionRoutePost)
          .withFormUrlEncodedBody(("value", FurloughPeriodQuestion.values.head.toString))

      val result = controller.onSubmit()(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual "/job-retention-scheme-calculator/pay-period-question"
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      when(mockSessionRepository.get(any())) thenReturn Future.successful(Some(userAnswers))

      val request =
        FakeRequest(POST, furloughPeriodQuestionRoutePost).withCSRFToken
          .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
          .withFormUrlEncodedBody(("value", "invalid value"))

      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller.onSubmit()(request)

      status(result) mustEqual BAD_REQUEST

      val dataRequest = DataRequest(request, userAnswers.id, userAnswers)

      contentAsString(result) mustEqual
        view(boundForm, claimStart, furloughStart, furloughStatus, None)(dataRequest, messages).toString
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      when(mockSessionRepository.get(any())) thenReturn Future.successful(None)

      val request = FakeRequest(GET, furloughPeriodQuestionRoute)
      val result = controller.onPageLoad()(getRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      when(mockSessionRepository.get(any())) thenReturn Future.successful(None)

      val request =
        FakeRequest(POST, furloughPeriodQuestionRoute)
          .withFormUrlEncodedBody(("value", FurloughPeriodQuestion.values.head.toString))

      val result = controller.onSubmit()(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url
    }
  }
}
