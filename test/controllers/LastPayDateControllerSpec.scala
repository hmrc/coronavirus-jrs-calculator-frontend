/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import java.time.{LocalDate, ZoneOffset}

import base.SpecBaseWithApplication
import forms.LastPayDateFormProvider
import models.{NormalMode, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.{LastPayDatePage, PayDatePage}
import play.api.inject.bind
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded, Call}
import play.api.test.FakeRequest
import play.api.test.CSRFTokenHelper._
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.LastPayDateView

import scala.concurrent.Future

class LastPayDateControllerSpec extends SpecBaseWithApplication with MockitoSugar {

  val formProvider = new LastPayDateFormProvider()
  //TODO This should be a date from user answers based on pay date loop
  private def form = formProvider(LocalDate.now())

  def onwardRoute = Call("GET", "/foo")

  val validAnswer = LocalDate.now()

  lazy val lastPayDateRoute = routes.LastPayDateController.onPageLoad(NormalMode).url

  val userAnswers = UserAnswers(userAnswersId).set(PayDatePage, validAnswer, Some(1)).success.value

  lazy val getRequest: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(GET, lastPayDateRoute).withCSRFToken
      .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  lazy val postRequest: FakeRequest[AnyContentAsFormUrlEncoded] =
    FakeRequest(POST, lastPayDateRoute).withCSRFToken
      .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
      .withFormUrlEncodedBody(
        "value.day"   -> validAnswer.getDayOfMonth.toString,
        "value.month" -> validAnswer.getMonthValue.toString,
        "value.year"  -> validAnswer.getYear.toString
      )

  "LastPayDate Controller" must {

    "return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val result = route(application, getRequest).value

      val view = application.injector.instanceOf[LastPayDateView]

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form, NormalMode, validAnswer)(getRequest, messages).toString

      application.stop()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswersUpdated = userAnswers.set(LastPayDatePage, LocalDate.now()).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswersUpdated)).build()

      val view = application.injector.instanceOf[LastPayDateView]

      val result = route(application, getRequest).value

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form.fill(validAnswer), NormalMode, validAnswer)(getRequest, messages).toString

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

      val result = route(application, postRequest).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual onwardRoute.url

      application.stop()
    }

    "redirect to the /pay-date/1 when there is no pay-dates saved already in mongo" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(UserAnswers(userAnswersId)))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute)),
            bind[SessionRepository].toInstance(mockSessionRepository)
          )
          .build()

      val result = route(application, postRequest).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual routes.PayDateController.onPageLoad(1).url

      application.stop()
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val request =
        FakeRequest(POST, lastPayDateRoute).withCSRFToken
          .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
          .withFormUrlEncodedBody(("value", "invalid value"))

      val boundForm = form.bind(Map("value" -> "invalid value"))

      val view = application.injector.instanceOf[LastPayDateView]

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      contentAsString(result) mustEqual
        view(boundForm, NormalMode, validAnswer)(request, messages).toString

      application.stop()
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val result = route(application, getRequest).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val result = route(application, postRequest).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }
  }
}
