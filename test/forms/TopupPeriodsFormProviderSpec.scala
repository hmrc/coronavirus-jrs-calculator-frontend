/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import java.time.LocalDate

import forms.behaviours.CheckboxFieldBehaviours
import org.scalacheck.{Gen, Shrink}
import generators.Generators
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.FormError

class TopupPeriodsFormProviderSpec extends CheckboxFieldBehaviours with Generators with ScalaCheckPropertyChecks {

  val form = new TopupPeriodsFormProvider()()

  implicit val noShrink: Shrink[Int] = Shrink.shrinkAny

  ".value" must {

    val fieldName = "value"
    val requiredKey = "topupPeriods.error.required"

    "bind valid values" in {
      val dateGen = datesBetween(
        LocalDate.of(2020, 3, 1),
        LocalDate.of(2020, 6, 30)
      )

      val listGen = for {
        length <- Gen.chooseNum(1, 10)
        list   <- Gen.listOfN(length, dateGen)
      } yield list

      forAll(listGen) { list =>
        val data = list.zipWithIndex.map(item => s"$fieldName[${item._2}]" -> item._1.toString).toMap
        form.bind(data).get shouldEqual list
      }

    }

    "fail to bind when the answer is invalid" in {
      val data = Map(
        s"$fieldName[0]" -> "invalid value"
      )
      form.bind(data).errors should contain(FormError(s"$fieldName[0]", "error.date"))
    }

    "fail to bind when no answers are selected" in {
      val data = Map.empty[String, String]
      form.bind(data).errors should contain(FormError(s"$fieldName", requiredKey))
    }
  }
}
