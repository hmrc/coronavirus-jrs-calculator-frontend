/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import java.time.LocalDate

import base.SpecBaseWithApplication
import models.Calculation.{FurloughCalculationResult, NicCalculationResult, PensionCalculationResult}
import models.{CalculationResult, PayPeriod, PayPeriodBreakdown, PayPeriodWithPayDay, PaymentDate}
import play.api.test.FakeRequest
import play.api.test.CSRFTokenHelper._
import play.api.test.Helpers._
import viewmodels.ConfirmationViewBreakdown
import views.html.ConfirmationView

class ConfirmationControllerSpec extends SpecBaseWithApplication {

  "Confirmation Controller" must {

    "return OK and the correct view for a GET" in {
      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val request = FakeRequest(GET, routes.ConfirmationController.onPageLoad().url)

      val result = route(application, request).value

      val view = application.injector.instanceOf[ConfirmationView]

      status(result) mustEqual OK

      contentAsString(result) mustEqual view(expected)(request, messages).toString

      application.stop()
    }
  }

  def periodBreakdownOne(amount: Double) =
    PayPeriodBreakdown(
      amount,
      PayPeriodWithPayDay(PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31)), PaymentDate(LocalDate.of(2020, 3, 31))))
  def periodBreakdownTwo(amount: Double) =
    PayPeriodBreakdown(
      amount,
      PayPeriodWithPayDay(PayPeriod(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30)), PaymentDate(LocalDate.of(2020, 4, 20))))
  val furlough = CalculationResult(FurloughCalculationResult, 3200.00, List(periodBreakdownOne(1600.00), periodBreakdownTwo(1600.00)))
  val nic = CalculationResult(NicCalculationResult, 241.36, List(periodBreakdownOne(121.58), periodBreakdownTwo(119.78)))
  val pension = CalculationResult(PensionCalculationResult, 65.07, List(periodBreakdownOne(32.67), periodBreakdownTwo(32.40)))

  val expected = ConfirmationViewBreakdown(furlough, nic, pension)
}
