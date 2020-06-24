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
import controllers.actions.FeatureFlag.FastTrackJourneyFlag
import forms.FurloughPeriodQuestionFormProvider
import models.ClaimPeriodQuestion.ClaimOnSamePeriod
import models.FurloughPeriodQuestion
import models.FurloughPeriodQuestion.FurloughedOnSamePeriod
import models.FurloughStatus.{FurloughEnded, FurloughOngoing}
import models.requests.DataRequest
import navigation.{FakeNavigator, Navigator}
import org.scalatest.OptionValues
import org.scalatestplus.mockito.MockitoSugar
import pages.FurloughPeriodQuestionPage
import play.api.inject.bind
import play.api.mvc.{AnyContentAsEmpty, Call}
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import views.html.FurloughPeriodQuestionView

class FurloughPeriodQuestionControllerSpec extends SpecBaseWithApplication with MockitoSugar with OptionValues {

  def onwardRoute = Call("GET", "/foo")

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

  "FurloughPeriodQuestion Controller" must {

    "return OK and the correct view for a GET when Furlough is Ongoing" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val result = route(application, getRequest).value

      val view = application.injector.instanceOf[FurloughPeriodQuestionView]

      status(result) mustEqual OK

      val dataRequest = DataRequest(getRequest, userAnswers.id, userAnswers)

      contentAsString(result) mustEqual
        view(form, claimStart, furloughStart, furloughStatus, None)(dataRequest, messages).toString

      application.stop()
    }

    "return OK and the correct view for a GET when Furlough is Ended" in {
      val userAnswersUpdated = emptyUserAnswers
        .withClaimPeriodStart(claimStart.toString)
        .withClaimPeriodEnd(furloughEnd.toString)
//        .withFurloughStartDate(furloughStart.toString)
//        .withFurloughEndDate(furloughEnd.toString)
        .withFurloughStatus(FurloughEnded)
//        .withPayDate(List(furloughEnd.toString))

      val application = applicationBuilder(userAnswers = Some(userAnswersUpdated)).build()

      val result = route(application, getRequest).value

      val view = application.injector.instanceOf[FurloughPeriodQuestionView]

      status(result) mustEqual OK

      val dataRequest = DataRequest(getRequest, userAnswersUpdated.id, userAnswersUpdated)

      contentAsString(result) mustEqual
        view(form, claimStart, furloughStart, FurloughEnded, Some(furloughEnd))(dataRequest, messages).toString

      application.stop()
    }

    "return OK and the correct view for a GET with phase two content" in {
      val userAnswersUpdated = emptyUserAnswers
        .withClaimPeriodStart("2020,7,1")
        .withClaimPeriodEnd("2020,7,30")
        .withFurloughStartDate("2020,7,1")
        .withFurloughEndDate("2020,7,30")
        .withFurloughStatus(FurloughEnded)
        .withPayDate(List(furloughEnd.toString))

      val application = applicationBuilder(userAnswers = Some(userAnswersUpdated)).build()

      val result = route(application, getRequest).value

      status(result) mustEqual OK

      contentAsString(result) must include("<title> Are the furlough dates the same for this employee?")
      contentAsString(result) must include("""<h1 class="govuk-heading-xl">Are the furlough dates the same for this employee?</h1>""")

      application.stop()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswersUpdated = userAnswers.set(FurloughPeriodQuestionPage, FurloughPeriodQuestion.values.head).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswersUpdated)).build()

      val view = application.injector.instanceOf[FurloughPeriodQuestionView]

      val result = route(application, getRequest).value

      status(result) mustEqual OK

      val dataRequest = DataRequest(getRequest, userAnswersUpdated.id, userAnswersUpdated)

      contentAsString(result) mustEqual
        view(form.fill(FurloughPeriodQuestion.values.head), claimStart, furloughStart, furloughStatus, None)(dataRequest, messages).toString

      application.stop()
    }

    "redirect to 404 page for a GET if FastTrackJourneyFlag is disabled" in {

      val application = applicationBuilder(config = Map(FastTrackJourneyFlag.key -> false), userAnswers = Some(emptyUserAnswers)).build()

      val result = route(application, getRequest).value

      status(result) mustEqual NOT_FOUND

      application.stop()
    }

    "redirect to 404 page for a POST if FastTrackJourneyFlag is disabled" in {

      val application = applicationBuilder(config = Map(FastTrackJourneyFlag.key -> false), userAnswers = Some(emptyUserAnswers)).build()

      val request =
        FakeRequest(POST, furloughPeriodQuestionRoutePost)
          .withFormUrlEncodedBody(("value", FurloughedOnSamePeriod.toString))

      val result = route(application, request).value

      status(result) mustEqual NOT_FOUND

      application.stop()
    }

    "redirect to the next page when valid data is submitted" in {

      val application =
        applicationBuilder(userAnswers = Some(userAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute))
          )
          .build()

      val request =
        FakeRequest(POST, furloughPeriodQuestionRoutePost)
          .withFormUrlEncodedBody(("value", FurloughPeriodQuestion.values.head.toString))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual onwardRoute.url

      application.stop()
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val request =
        FakeRequest(POST, furloughPeriodQuestionRoutePost).withCSRFToken
          .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
          .withFormUrlEncodedBody(("value", "invalid value"))

      val boundForm = form.bind(Map("value" -> "invalid value"))

      val view = application.injector.instanceOf[FurloughPeriodQuestionView]

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      val dataRequest = DataRequest(request, userAnswers.id, userAnswers)

      contentAsString(result) mustEqual
        view(boundForm, claimStart, furloughStart, furloughStatus, None)(dataRequest, messages).toString

      application.stop()
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, furloughPeriodQuestionRoute)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request =
        FakeRequest(POST, furloughPeriodQuestionRoute)
          .withFormUrlEncodedBody(("value", FurloughPeriodQuestion.values.head.toString))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }
  }
}
