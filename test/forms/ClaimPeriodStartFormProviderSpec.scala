/*
 * Copyright 2023 HM Revenue & Customs
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

package forms

import base.SpecBaseControllerSpecs
import forms.behaviours.DateBehaviours
import forms.mappings.LocalDateFormatter
import play.api.data.FormError
import views.ViewUtils.dateToString

class ClaimPeriodStartFormProviderSpec extends SpecBaseControllerSpecs {

  val form           = new ClaimPeriodStartFormProvider()()
  val dateBehaviours = new DateBehaviours
  val policyEndDate  = appConf.schemeEndDate

  import dateBehaviours._

  ".startDate" must {

    "bind valid data" in {

      forAll(claimPeriodDatesGen -> "valid date") { date =>
        val data = Map(
          "startDate.day"   -> date.getDayOfMonth.toString,
          "startDate.month" -> date.getMonthValue.toString,
          "startDate.year"  -> date.getYear.toString
        )

        val result = form.bind(data)

        result.value.value mustEqual date
      }
    }

    "fail to bind an empty date" in {
      val result = form.bind(Map.empty[String, String])

      result.errors must contain allElementsOf List(
        FormError(s"startDate.day", LocalDateFormatter.dayBlankErrorKey),
        FormError(s"startDate.month", LocalDateFormatter.monthBlankErrorKey),
        FormError(s"startDate.year", LocalDateFormatter.yearBlankErrorKey)
      )
    }

    "fail for invalid before min date" in {

      val data = Map(
        "startDate.day"   -> "1",
        "startDate.month" -> "2",
        "startDate.year"  -> "2020"
      )

      val result = form.bind(data)

      result.errors mustBe List(
        FormError("startDate", "claimPeriodStart.error.outofrange", Seq("1 March 2020", dateToString(policyEndDate)))
      )
    }

    "fail with invalid after max possible end date" in {

      val data = Map(
        "startDate.day"   -> "1",
        "startDate.month" -> s"${appConf.schemeEndDate.plusMonths(1).getMonthValue}",
        "startDate.year"  -> s"${appConf.schemeEndDate.getYear}"
      )

      val result = form.bind(data)

      result.errors mustBe List(
        FormError(key = "startDate", message = "claimPeriodStart.error.outofrange", args = Seq("1 March 2020", dateToString(policyEndDate)))
      )
    }
  }
}
