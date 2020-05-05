/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import play.api.libs.json.{JsError, JsString, Json}

class TopupPeriodsSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues with ModelGenerators {

  "TopupPeriods" must {

    "deserialise valid values" in {

      val gen = arbitrary[TopupPeriods]

      forAll(gen) { topupPeriods =>
        JsString(topupPeriods.toString).validate[TopupPeriods].asOpt.value mustEqual topupPeriods
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!TopupPeriods.values.map(_.toString).contains(_))

      forAll(gen) { invalidValue =>
        JsString(invalidValue).validate[TopupPeriods] mustEqual JsError("error.invalid")
      }
    }

    "serialise" in {

      val gen = arbitrary[TopupPeriods]

      forAll(gen) { topupPeriods =>
        Json.toJson(topupPeriods) mustEqual JsString(topupPeriods.toString)
      }
    }
  }
}
