/*
 * Copyright 2022 HM Revenue & Customs
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
import controllers.actions.DataRetrievalActionImpl
import forms.AnnualPayAmountFormProvider
import models.EmployeeStarted.{After1Feb2019, OnOrBefore1Feb2019}
import models.requests.DataRequest
import models.{AnnualPayAmount, EmployeeRTISubmission, UserAnswers}
import pages._
import play.api.libs.json.{JsString, Json}
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded}
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.UserAnswerPersistence
import views.html.AnnualPayAmountView

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AnnualPayAmountControllerSpec extends SpecBaseControllerSpecs {

  val formProvider = new AnnualPayAmountFormProvider()
  val form         = formProvider()

  lazy val annualPayAmountRoute = routes.AnnualPayAmountController.onPageLoad().url

  val claimStart         = LocalDate.parse("2020-04-01")
  val furloughStart      = LocalDate.parse("2020-04-01")
  val firstFurloughStart = LocalDate.parse("2020-03-01")
  val uiDateToShow       = furloughStart.minusDays(1)
  val empStart           = LocalDate.parse("2020-02-01")

  lazy val getRequest: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(GET, annualPayAmountRoute).withCSRFToken
      .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  lazy val postRequest: FakeRequest[AnyContentAsFormUrlEncoded] =
    FakeRequest(POST, annualPayAmountRoute).withCSRFToken
      .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
      .withFormUrlEncodedBody(("value", "123"))

  val userAnswers = UserAnswers(
    userAnswersId,
    Json.obj(
      ClaimPeriodStartPage.toString  -> JsString(claimStart.toString),
      FurloughStartDatePage.toString -> JsString(furloughStart.toString),
      EmployeeStartDatePage.toString -> JsString(empStart.toString),
      EmployeeStartedPage.toString   -> JsString(After1Feb2019.toString)
    )
  )

  val view = app.injector.instanceOf[AnnualPayAmountView]

  def controller(stubbedAnswers: Option[UserAnswers] = Some(emptyUserAnswers)) =
    new AnnualPayAmountController(
      messagesApi,
      mockSessionRepository,
      navigator,
      identifier,
      new DataRetrievalActionImpl(mockSessionRepository) {
        override protected val identifierRetrieval: String => Future[Option[UserAnswers]] =
          _ => Future.successful(stubbedAnswers)
      },
      dataRequired,
      formProvider,
      component,
      view
    ) {
      override val userAnswerPersistence = new UserAnswerPersistence(_ => Future.successful(true))
    }

  "AnnualPayAmountController" must {

    "return OK and the correct view for a GET without a First Furlough Period Answer" in {
      val result      = controller(Some(userAnswers)).onPageLoad()(getRequest)
      val dataRequest = DataRequest(getRequest, userAnswers.id, userAnswers)

      status(result) mustEqual OK
      contentAsString(result) mustEqual
        view(form, "since", Seq("31 March 2020"))(dataRequest, messages).toString
    }

    "return OK and the correct view for a GET with a First Furlough Period Answer" in {
      val userAnswers = UserAnswers(
        userAnswersId,
        Json.obj(
          ClaimPeriodStartPage.toString  -> JsString(claimStart.toString),
          FurloughStartDatePage.toString -> JsString(furloughStart.toString),
          EmployeeStartDatePage.toString -> JsString(empStart.toString),
          EmployeeStartedPage.toString   -> JsString(After1Feb2019.toString),
          FirstFurloughDatePage.toString -> JsString(firstFurloughStart.toString)
        )
      )

      val result      = controller(Some(userAnswers)).onPageLoad()(getRequest)
      val dataRequest = DataRequest(getRequest, userAnswers.id, userAnswers)

      status(result) mustEqual OK
      contentAsString(result) mustEqual
        view(form, "since", Seq("29 February 2020"))(dataRequest, messages).toString
    }

    "return OK and the correct view for a GET if the EmployeeStarted is OnOrBefore1Feb2019" in {
      val updatedAnswers = userAnswers.set(EmployeeStartedPage, OnOrBefore1Feb2019).success.value
      val dataRequest    = DataRequest(getRequest, userAnswers.id, userAnswers)
      val result         = controller(Some(updatedAnswers)).onPageLoad()(getRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual
        view(form, "from", Seq("6 April 2019", "31 March 2020"))(dataRequest, messages).toString
    }

    "return OK and the correct view for a GET if the EmployeeRTISubmission is No and EmployeeStartDate is inside 1/2/20 - 19/3/20" in {
      val updatedAnswers = userAnswers
        .set(ClaimPeriodStartPage, LocalDate.of(2020, 11, 1))
        .success
        .value
        .set(FurloughStartDatePage, LocalDate.of(2020, 11, 1))
        .success
        .value
        .set(EmployeeStartedPage, OnOrBefore1Feb2019)
        .success
        .value
        .set(EmployeeStartDatePage, LocalDate.of(2020, 2, 3))
        .success
        .value
        .set(EmployeeRTISubmissionPage, EmployeeRTISubmission.No)
        .success
        .value

      val dataRequest = DataRequest(getRequest, updatedAnswers.id, updatedAnswers)

      val result = controller(Some(updatedAnswers)).onPageLoad()(getRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual
        view(form, "from", Seq("6 April 2020", "31 October 2020"))(dataRequest, messages).toString
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val userAnswersUpdated = userAnswers.set(AnnualPayAmountPage, AnnualPayAmount(111)).success.value
      val dataRequest        = DataRequest(getRequest, userAnswersUpdated.id, userAnswersUpdated)
      val result             = controller(Some(userAnswersUpdated)).onPageLoad()(getRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual
        view(form.fill(AnnualPayAmount(111)), "since", Seq("31 March 2020"))(dataRequest, messages).toString
    }

    "redirect to the next page when valid data is submitted" in {
      val result = controller(Some(userAnswers)).onSubmit()(postRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual "/job-retention-scheme-calculator/topup-question"
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val request =
        FakeRequest(POST, annualPayAmountRoute).withCSRFToken
          .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
          .withFormUrlEncodedBody(("value", ""))

      val boundForm = form.bind(Map("value" -> ""))

      val dataRequest = DataRequest(request, userAnswers.id, userAnswers)
      val result      = controller(Some(userAnswers)).onSubmit()(request)

      status(result) mustEqual BAD_REQUEST
      contentAsString(result) mustEqual
        view(boundForm, "since", Seq("31 March 2020"))(dataRequest, messages).toString
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val request = FakeRequest(GET, annualPayAmountRoute)
      val result  = controller(None).onPageLoad()(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val request =
        FakeRequest(POST, annualPayAmountRoute)
          .withFormUrlEncodedBody(("value", "answer"))

      val result = controller(None).onSubmit()(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url
    }
  }
}
