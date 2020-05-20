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

package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.OptionValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class FurloughStatusSpec extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "furloughStatus" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf(FurloughStatus.values)

      forAll(gen) { furloughOngoing =>
        JsString(furloughOngoing.toString).validate[FurloughStatus].asOpt.value mustEqual furloughOngoing
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!FurloughStatus.values.map(_.toString).contains(_))

      forAll(gen) { invalidValue =>
        JsString(invalidValue).validate[FurloughStatus] mustEqual JsError("error.invalid")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(FurloughStatus.values)

      forAll(gen) { furloughOngoing =>
        Json.toJson(furloughOngoing) mustEqual JsString(furloughOngoing.toString)
      }
    }
  }
}
