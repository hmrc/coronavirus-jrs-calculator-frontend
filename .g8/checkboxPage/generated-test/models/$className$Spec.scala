package models

import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.OptionValues
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsError, JsString, Json}

class $className$Spec extends PlaySpec with ScalaCheckPropertyChecks with ModelGenerators {

  "$className$" must {

    "deserialise valid values" in {

      val gen = arbitrary[$className$]

      forAll(gen) {
        $className;format="decap"$ =>

          JsString($className;format="decap"$.toString).validate[$className$].asOpt.value mustEqual $className;format="decap"$
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!$className$.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[$className$] mustEqual JsError("error.invalid")
      }
    }

    "serialise" in {

      val gen = arbitrary[$className$]

      forAll(gen) {
        $className;format="decap"$ =>

          Json.toJson($className;format="decap"$) mustEqual JsString($className;format="decap"$.toString)
      }
    }
  }
}
