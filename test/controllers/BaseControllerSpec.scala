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

import akka.util.Timeout
import base.SpecBaseWithApplication
import cats.data.Validated.Valid
import handlers.ErrorHandler
import models.Salary
import models.requests.DataRequest
import navigation.Navigator
import org.scalatestplus.mockito.MockitoSugar
import pages.{PayDatePage, RegularPayAmountPage}
import play.api.http.Status._
import play.api.mvc.Results._
import play.api.mvc.{MessagesControllerComponents, Result}
import play.api.test.Helpers.{contentAsString, status}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

class BaseControllerSpec extends SpecBaseWithApplication with MockitoSugar {
  lazy val fakeDataRequest = DataRequest(fakeRequest, "id", emptyUserAnswers)

  def futureResult[A]: A => Future[Result] = x => Future.successful(Ok(s"Answer: $x"))
  implicit lazy val errorHandler = injector.instanceOf[ErrorHandler]

  object BaseController extends BaseController {
    override protected def controllerComponents: MessagesControllerComponents = injector.instanceOf[MessagesControllerComponents]
    override val navigator: Navigator = injector.instanceOf[Navigator]
  }

  implicit val duration: Timeout = 5 seconds

  "getAnswer without index" when {

    "answer is not found" must {

      "return None" in {
        val result = BaseController.getAnswerV(RegularPayAmountPage)(DataRequest(fakeRequest, "id", emptyUserAnswers), implicitly)

        result mustBe None
      }
    }

    "valid answer is not found" must {

      "return None" in {
        val result = BaseController.getAnswerV(
          RegularPayAmountPage
        )(DataRequest(fakeRequest, "id", emptyUserAnswers), implicitly)

        result mustBe emptyError(RegularPayAmountPage.path)
      }
    }

    "answer is found" must {

      "return Some(A)" in {
        val userAnswers = emptyUserAnswers.set(RegularPayAmountPage, Salary(100))(implicitly).success.value

        val result = BaseController.getAnswerV(RegularPayAmountPage)(DataRequest(fakeRequest, "id", userAnswers), implicitly)

        result mustBe Some(Salary(100))
      }
    }

    "valid answer is found" must {

      "return Some(A)" in {
        val userAnswers = emptyUserAnswers.set(RegularPayAmountPage, Salary(100))(implicitly).success.value

        val result = BaseController.getAnswerV(RegularPayAmountPage)(DataRequest(fakeRequest, "id", userAnswers), implicitly)

        result mustBe Valid(Salary(100))
      }
    }
  }

  "getAnswer with index" when {

    "answer is not found" must {

      "return None" in {
        val result = BaseController.getAnswerV(PayDatePage, 1)(DataRequest(fakeRequest, "id", emptyUserAnswers), implicitly)

        result mustBe None
      }

      "return Invalid" in {
        val result = BaseController.getAnswerV(
          PayDatePage,
          1
        )(DataRequest(fakeRequest, "id", emptyUserAnswers), implicitly)

        result mustBe emptyError(PayDatePage.path, 1, "error.path.missing")
      }
    }

    "answer is found" must {

      "return Some(A)" in {
        val userAnswers = emptyUserAnswers.set(PayDatePage, LocalDate.of(2000, 1, 1), Some(1))(implicitly).success.value

        val result = BaseController.getAnswerV(PayDatePage, 1)(DataRequest(fakeRequest, "id", userAnswers), implicitly)

        result mustBe Some(LocalDate.of(2000, 1, 1))
      }

      "return Valid(A)" in {
        val userAnswers = emptyUserAnswers.set(PayDatePage, LocalDate.of(2000, 1, 1), Some(1))(implicitly).success.value

        val result = BaseController.getAnswerV(PayDatePage, 1)(DataRequest(fakeRequest, "id", userAnswers), implicitly)

        result mustBe Valid(LocalDate.of(2000, 1, 1))
      }

    }

  }

  "getRequiredAnswer without index" when {

    "answer is not found" must {

      "return internal server error" in {
        val result = BaseController.getRequiredAnswerV(RegularPayAmountPage)(futureResult)(
          DataRequest(fakeRequest, "id", emptyUserAnswers),
          implicitly,
          errorHandler)

        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }

    "validated answer is not found" must {

      "return internal server error" in {
        val result = BaseController.getRequiredAnswerV(RegularPayAmountPage)(futureResult)(
          DataRequest(fakeRequest, "id", emptyUserAnswers),
          implicitly,
          errorHandler)

        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }

    "answer is found" must {

      "execute provided function" in {
        val userAnswers = emptyUserAnswers.set(RegularPayAmountPage, Salary(123.45))(implicitly).success.value

        val result = BaseController.getRequiredAnswerV(RegularPayAmountPage)(futureResult)(
          DataRequest(fakeRequest, "id", userAnswers),
          implicitly,
          errorHandler)

        status(result) mustBe OK
        contentAsString(result) mustBe "Answer: Salary(123.45)"
      }
    }

    "valid answer is found" must {

      "execute provided function" in {
        val userAnswers = emptyUserAnswers.set(RegularPayAmountPage, Salary(123.45))(implicitly).success.value

        val result = BaseController.getRequiredAnswerV(RegularPayAmountPage)(futureResult)(
          DataRequest(fakeRequest, "id", userAnswers),
          implicitly,
          errorHandler)

        status(result) mustBe OK
        contentAsString(result) mustBe "Answer: Salary(123.45)"
      }
    }

  }

  "getRequiredAnswer with index" when {

    "answer is not found" must {

      "return internal server error" in {
        val result = BaseController.getRequiredAnswerV(PayDatePage, 1)(futureResult)(
          DataRequest(fakeRequest, "id", emptyUserAnswers),
          implicitly,
          errorHandler)

        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }

    "valid answer is not found" must {

      "return internal server error" in {
        val result = BaseController.getRequiredAnswerV(PayDatePage, 1)(futureResult)(
          DataRequest(fakeRequest, "id", emptyUserAnswers),
          implicitly,
          errorHandler)

        status(result) mustBe INTERNAL_SERVER_ERROR
      }

    }

    "answer is found" must {

      "execute provided function" in {
        val userAnswers = emptyUserAnswers.set(PayDatePage, LocalDate.of(2000, 1, 1), Some(1))(implicitly).success.value

        val result = BaseController.getRequiredAnswerV(PayDatePage, 1)(futureResult)(
          DataRequest(fakeRequest, "id", userAnswers),
          implicitly,
          errorHandler)

        status(result) mustBe OK
        contentAsString(result) mustBe "Answer: 2000-01-01"
      }
    }

    "valid answer is found" must {
      "execute provided function" in {
        val userAnswers = emptyUserAnswers.set(PayDatePage, LocalDate.of(2000, 1, 1), Some(1))(implicitly).success.value

        val result = BaseController.getRequiredAnswerV(PayDatePage, 1)(futureResult)(
          DataRequest(fakeRequest, "id", userAnswers),
          implicitly,
          errorHandler)

        status(result) mustBe OK
        contentAsString(result) mustBe "Answer: 2000-01-01"
      }
    }

  }

}
