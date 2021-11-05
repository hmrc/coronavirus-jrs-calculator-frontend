/*
 * Copyright 2021 HM Revenue & Customs
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

package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.OptionValues
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsError, JsString, Json}

class PayPeriodsListSpec extends PlaySpec with ScalaCheckPropertyChecks {

  "PayPeriodsList" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf(PayPeriodsList.values.toSeq)

      forAll(gen) { payPeriodsList =>
        JsString(payPeriodsList.toString).validate[PayPeriodsList].asOpt.value mustEqual payPeriodsList
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!PayPeriodsList.values.map(_.toString).contains(_))

      forAll(gen) { invalidValue =>
        JsString(invalidValue).validate[PayPeriodsList] mustEqual JsError("error.invalid")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(PayPeriodsList.values.toSeq)

      forAll(gen) { payPeriodsList =>
        Json.toJson(payPeriodsList) mustEqual JsString(payPeriodsList.toString)
      }
    }
  }
}
