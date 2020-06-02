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
import forms.FurloughPartialPayFormProvider
import models.PaymentFrequency.Weekly
import models.requests.DataRequest
import models.{FurloughPartialPay, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages._
import play.api.inject.bind
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded, Call}
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.VariableLengthPartialPayView

import scala.concurrent.Future

class PartialPayAfterFurloughControllerSpec extends SpecBaseWithApplication with MockitoSugar {

  lazy val pageLoadAfterFurloughRoute = routes.PartialPayAfterFurloughController.onPageLoad().url
  lazy val submitAfterFurloughRoute = routes.PartialPayAfterFurloughController.onSubmit().url
  val formProvider = new FurloughPartialPayFormProvider()
  val form = formProvider()
  val payPeriod1 = LocalDate.of(2020, 3, 22)
  val payPeriod2 = LocalDate.of(2020, 3, 29)
  val payPeriod3 = LocalDate.of(2020, 4, 5)
  val payPeriod4 = LocalDate.of(2020, 4, 12)
  val furloughStartDate = LocalDate.of(2020, 3, 27)
  val furloughEndDate = LocalDate.of(2020, 4, 6)
  val claimPeriodStart = LocalDate.of(2020, 3, 27)
  val claimPeriodEnd = LocalDate.of(2020, 4, 6)
  val userAnswers = UserAnswers(userAnswersId)
    .set(PayDatePage, payPeriod1, Some(1))
    .success
    .value
    .set(PayDatePage, payPeriod2, Some(2))
    .success
    .value
    .set(PayDatePage, payPeriod3, Some(3))
    .success
    .value
    .set(PayDatePage, payPeriod4, Some(4))
    .success
    .value
    .set(ClaimPeriodStartPage, claimPeriodStart)
    .success
    .value
    .set(ClaimPeriodEndPage, claimPeriodEnd)
    .success
    .value
    .set(FurloughStartDatePage, furloughStartDate)
    .success
    .value
    .set(FurloughStartDatePage, furloughEndDate)
    .success
    .value
    .set(PaymentFrequencyPage, Weekly)
    .success
    .value

  def onwardRoute = Call("GET", "/foo")

  def getRequest(url: String): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(GET, url).withCSRFToken
      .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  def postRequest(url: String): FakeRequest[AnyContentAsFormUrlEncoded] =
    FakeRequest(POST, url).withCSRFToken
      .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
      .withFormUrlEncodedBody(("value", "123"))

  "PartialPayAfterFurloughController" must {

    "return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val r = getRequest(pageLoadAfterFurloughRoute)

      val result = route(application, r).value

      val view = application.injector.instanceOf[VariableLengthPartialPayView]

      status(result) mustEqual OK

      val dataRequest = DataRequest(r, userAnswers.id, userAnswers)

      contentAsString(result) mustEqual
        view(
          form,
          LocalDate.of(2020, 4, 7),
          LocalDate.of(2020, 4, 12),
          routes.PartialPayAfterFurloughController.onSubmit()
        )(dataRequest, messages).toString

      application.stop()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers1 = userAnswers.set(PartialPayAfterFurloughPage, FurloughPartialPay(111)).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers1)).build()

      val view = application.injector.instanceOf[VariableLengthPartialPayView]

      val r = getRequest(pageLoadAfterFurloughRoute)

      val result = route(application, r).value

      status(result) mustEqual OK

      val dataRequest = DataRequest(r, userAnswers1.id, userAnswers1)

      contentAsString(result) mustEqual
        view(
          form.fill(FurloughPartialPay(111)),
          LocalDate.of(2020, 4, 7),
          LocalDate.of(2020, 4, 12),
          routes.PartialPayAfterFurloughController.onSubmit()
        )(dataRequest, messages).toString

      application.stop()
    }

    "redirect to the next page when valid data is submitted" in {

      val application =
        applicationBuilder(userAnswers = Some(userAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute))
          )
          .build()

      val result = route(application, postRequest(submitAfterFurloughRoute)).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual onwardRoute.url

      application.stop()
    }

    "redirect to something went wrong if there is No furlough end stored in UserAnswers for POST" in {

      val modifiedUserAnswers = userAnswers.remove(FurloughStartDatePage).success.value

      val application =
        applicationBuilder(userAnswers = Some(modifiedUserAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute))
          )
          .build()

      val result = route(application, postRequest(submitAfterFurloughRoute)).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.ErrorController.somethingWentWrong().url

      application.stop()
    }

    "redirect to something went wrong when there no saved data for PayDatePage in mongo for POST" in {

      val modifiedUserAnswers = userAnswers.remove(PayDatePage).success.value

      val application =
        applicationBuilder(userAnswers = Some(modifiedUserAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute))
          )
          .build()

      val result = route(application, postRequest(submitAfterFurloughRoute)).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.ErrorController.somethingWentWrong().url

      application.stop()
    }

    "redirect to the /furlough-question when there no saved data for FurloughEndDate and ClaimEndDate in mongo for POST" in {

      val modifiedUserAnswers = userAnswers
        .remove(FurloughEndDatePage)
        .success
        .value
        .remove(ClaimPeriodEndPage)
        .success
        .value

      val application =
        applicationBuilder(userAnswers = Some(modifiedUserAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute))
          )
          .build()

      val result = route(application, postRequest(submitAfterFurloughRoute)).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.ErrorController.somethingWentWrong().url

      application.stop()
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val request =
        FakeRequest(POST, submitAfterFurloughRoute).withCSRFToken
          .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
          .withFormUrlEncodedBody(("value", ""))

      val boundForm = form.bind(Map("value" -> ""))

      val view = application.injector.instanceOf[VariableLengthPartialPayView]

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      val dataRequest = DataRequest(request, userAnswers.id, userAnswers)

      contentAsString(result) mustEqual
        view(
          boundForm,
          LocalDate.of(2020, 4, 7),
          LocalDate.of(2020, 4, 12),
          routes.PartialPayAfterFurloughController.onSubmit()
        )(dataRequest, messages).toString
      application.stop()
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, pageLoadAfterFurloughRoute)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request =
        FakeRequest(POST, submitAfterFurloughRoute)
          .withFormUrlEncodedBody(("value", "answer"))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }
  }
}
