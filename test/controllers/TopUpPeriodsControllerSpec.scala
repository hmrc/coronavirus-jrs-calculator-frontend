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
import controllers.actions.FeatureFlag._
import forms.TopUpPeriodsFormProvider
import models.TopUpPeriods.TopUpPeriod
import models.{Amount, FullPeriodBreakdown, PeriodBreakdown, Salary, TopUpPayment, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.{SalaryQuestionPage, TopUpPeriodsPage}
import play.api.inject.bind
import play.api.mvc.{AnyContentAsEmpty, Call}
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.TopUpPeriodsView

import scala.concurrent.Future

class TopUpPeriodsControllerSpec extends SpecBaseWithApplication with MockitoSugar with CoreTestDataBuilder {

  def onwardRoute = Call("GET", "/foo")

  lazy val topupPeriodsRoute = routes.TopUpPeriodsController.onPageLoad().url

  lazy val getRequest = FakeRequest(GET, topupPeriodsRoute).withCSRFToken
    .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
  val formProvider = new TopUpPeriodsFormProvider()
  val form = formProvider()

  val dates = List(LocalDate.of(2020, 3, 31))
  val periodBreakdowns: Seq[PeriodBreakdown] = Seq(
    FullPeriodBreakdown(Amount(1600.00), fullPeriodWithPaymentDate("2020-03-01", "2020-03-31", "2020-03-31"))
  )

  "TopupPeriods Controller" must {

    "return OK and the correct view for a GET" in {

      val userAnswers = mandatoryAnswers
        .setValue(SalaryQuestionPage, Salary(2000))

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val result = route(application, getRequest).value

      val view = application.injector.instanceOf[TopUpPeriodsView]

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form, periodBreakdowns)(getRequest, messages).toString

      application.stop()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val topUpPeriod = dates.map(TopUpPeriod(_, furloughGrant = Amount(100)))

      val userAnswers = mandatoryAnswers
        .setValue(SalaryQuestionPage, Salary(2000))
        .setValue(TopUpPeriodsPage, topUpPeriod)

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val view = application.injector.instanceOf[TopUpPeriodsView]

      val result = route(application, getRequest).value

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form.fill(dates), periodBreakdowns)(getRequest, messages).toString

      application.stop()
    }

    "redirect to the next page when valid data is submitted" in {

      val userAnswers = mandatoryAnswers
        .setValue(SalaryQuestionPage, Salary(2000))

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
        FakeRequest(POST, topupPeriodsRoute)
          .withFormUrlEncodedBody(("value[0]" -> dates.head.toString()))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual onwardRoute.url

      application.stop()
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val userAnswers = mandatoryAnswers
        .setValue(SalaryQuestionPage, Salary(2000))

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val request =
        FakeRequest(POST, topupPeriodsRoute).withCSRFToken
          .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
          .withFormUrlEncodedBody(("value[0]", "invalid value"))

      val boundForm = form.bind(Map("value[0]" -> "invalid value"))

      val view = application.injector.instanceOf[TopUpPeriodsView]

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      contentAsString(result) mustEqual
        view(boundForm, periodBreakdowns)(request, messages).toString

      application.stop()
    }

    "redirect to 404 page for a GET if topups flag is disabled" in {

      val application = applicationBuilder(config = Map(TopUpJourneyFlag.key -> false), userAnswers = Some(UserAnswers("id"))).build()

      val request = FakeRequest(GET, topupPeriodsRoute)

      val result = route(application, request).value

      status(result) mustEqual NOT_FOUND

      application.stop()
    }

    "redirect to 404 page for a POST if topups flag is disabled" in {

      val application = applicationBuilder(config = Map(TopUpJourneyFlag.key -> false), userAnswers = Some(UserAnswers("id"))).build()

      val request =
        FakeRequest(POST, topupPeriodsRoute)
          .withFormUrlEncodedBody(("value[0]", dates.head.toString))

      val result = route(application, request).value

      status(result) mustEqual NOT_FOUND

      application.stop()
    }

    "redirect to error page for a GET if missing values for furlough pay calculation" in {

      val application = applicationBuilder(userAnswers = Some(UserAnswers("id"))).build()

      val request =
        FakeRequest(POST, topupPeriodsRoute)
          .withFormUrlEncodedBody(("value[0]", dates.head.toString))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual routes.ErrorController.somethingWentWrong().url

      application.stop()
    }

    "redirect to error page for a POST if missing values for furlough pay calculation" in {

      val application = applicationBuilder(userAnswers = Some(UserAnswers("id"))).build()

      val request =
        FakeRequest(POST, topupPeriodsRoute)
          .withFormUrlEncodedBody(("value[0]", dates.head.toString))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual routes.ErrorController.somethingWentWrong().url

      application.stop()
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, topupPeriodsRoute)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request =
        FakeRequest(POST, topupPeriodsRoute)
          .withFormUrlEncodedBody(("value[0]", dates.head.toString))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }
  }
}
