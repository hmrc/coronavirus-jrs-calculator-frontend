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

package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class PayMethodSpec extends PlaySpec with ScalaCheckPropertyChecks {

  "PayMethod" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf(PayMethod.values.toSeq)

      forAll(gen) { payMethod =>
        JsString(payMethod.toString).validate[PayMethod].asOpt.value mustEqual payMethod
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!PayMethod.values.map(_.toString).contains(_))

      forAll(gen) { invalidValue =>
        JsString(invalidValue).validate[PayMethod] mustEqual JsError("error.invalid")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(PayMethod.values.toSeq)

      forAll(gen) { payMethod =>
        Json.toJson(payMethod) mustEqual JsString(payMethod.toString)
      }
    }
  }
}
