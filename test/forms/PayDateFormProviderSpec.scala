/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import java.time.{LocalDate, ZoneOffset}

import forms.behaviours.DateBehaviours
import models.PaymentFrequency
import models.PaymentFrequency.Weekly
import org.scalacheck.Gen
import play.api.data.FormError
import play.api.libs.json.JsString
import reactivemongo.play.json.ValidationError
import views.ViewUtils

import scala.collection.mutable

class PayDateFormProviderSpec extends DateBehaviours {

  val form = new PayDateFormProvider()()

  "PayDateFormProvider" should {

    ".value" should {

      val validData = datesBetween(
        min = LocalDate.of(2000, 1, 1),
        max = LocalDate.now(ZoneOffset.UTC)
      )

      behave like dateField(form, "value", validData)

      behave like mandatoryDateField(form, "value", "payDate.error.required.all")
    }

    "before constraint" must {

      "accept valid data" in {
        val gen = for {
          daysMinus <- Gen.choose(1, 100)
          validDates <- datesBetween(
                         min = LocalDate.of(2020, 3, 1),
                         max = LocalDate.of(2020, 5, 1)
                       )
        } yield (validDates, daysMinus)

        forAll(gen -> "valid dates") {
          case (date: LocalDate, minusDays: Int) =>
            val dateBefore = date.minusDays(minusDays)

            val data = Map(
              "value.day"   -> dateBefore.getDayOfMonth.toString,
              "value.month" -> dateBefore.getMonthValue.toString,
              "value.year"  -> dateBefore.getYear.toString
            )

            val result = new PayDateFormProvider().apply(beforeDate = Some(date)).bind(data)

            result.errors shouldBe empty
            result.value.value shouldEqual dateBefore
        }
      }

      "reject invalid data" in {
        val gen = for {
          daysPlus <- Gen.choose(1, 100)
          validDates <- datesBetween(
                         min = LocalDate.of(2020, 3, 1),
                         max = LocalDate.of(2020, 5, 1)
                       )
        } yield (validDates, daysPlus)

        forAll(gen -> "valid dates") {
          case (date: LocalDate, plusDays: Int) =>
            val dateAfter = date.plusDays(plusDays)

            val data = Map(
              "value.day"   -> dateAfter.getDayOfMonth.toString,
              "value.month" -> dateAfter.getMonthValue.toString,
              "value.year"  -> dateAfter.getYear.toString
            )

            val result = new PayDateFormProvider().apply(beforeDate = Some(date)).bind(data)

            result.errors.head.key shouldBe "value"
            result.errors.head.message shouldBe "payDate.error.mustBeBefore"
            result.errors.head.args should contain only ViewUtils.dateToString(date)
        }
      }
    }

    "after constraint" must {

      "accept valid data" in {
        val gen = for {
          daysPlus <- Gen.choose(1, 100)
          validDates <- datesBetween(
                         min = LocalDate.of(2020, 3, 1),
                         max = LocalDate.of(2020, 5, 1)
                       )
        } yield (validDates, daysPlus)

        forAll(gen -> "valid dates") {
          case (date: LocalDate, plusDays: Int) =>
            val dateAfter = date.plusDays(plusDays)

            val data = Map(
              "value.day"   -> dateAfter.getDayOfMonth.toString,
              "value.month" -> dateAfter.getMonthValue.toString,
              "value.year"  -> dateAfter.getYear.toString
            )

            val result = new PayDateFormProvider().apply(afterDate = Some(date)).bind(data)

            result.errors shouldBe empty
            result.value.value shouldEqual dateAfter
        }
      }

      "reject invalid data" in {
        val gen = for {
          daysPlus <- Gen.choose(1, 100)
          validDates <- datesBetween(
                         min = LocalDate.of(2020, 3, 1),
                         max = LocalDate.of(2020, 5, 1)
                       )
        } yield (validDates, daysPlus)

        forAll(gen -> "valid dates") {
          case (date: LocalDate, plusDays: Int) =>
            val dateBefore = date.minusDays(plusDays)

            val data = Map(
              "value.day"   -> dateBefore.getDayOfMonth.toString,
              "value.month" -> dateBefore.getMonthValue.toString,
              "value.year"  -> dateBefore.getYear.toString
            )

            val result = new PayDateFormProvider().apply(afterDate = Some(date)).bind(data)

            result.errors.head.key shouldBe "value"
            result.errors.head.message shouldBe "payDate.error.mustBeAfter"
            result.errors.head.args should contain only ViewUtils.dateToString(date)
        }
      }
    }

    "with paymentFrequency" must {

      "accept valid date as per paymentFrequency" in {
        val formProvider = new PayDateFormProvider()
        val latestPayDate = LocalDate.now().minusDays(50)
        val gen = Gen.oneOf(PaymentFrequency.values.toSeq)

        forAll(gen) { paymentFrequency =>
          val validFormData = formProvider.expectedPayDate(latestPayDate, paymentFrequency)

          val data = Map(
            "value.day"   -> validFormData.getDayOfMonth.toString,
            "value.month" -> validFormData.getMonthValue.toString,
            "value.year"  -> validFormData.getYear.toString
          )

          val result = formProvider
            .apply(latestPayDate = Some(latestPayDate), paymentFrequency = Some(paymentFrequency))
            .bind(data)

          result.errors shouldBe empty
          result.value.value shouldEqual validFormData
        }
      }

      "reject date if its NOT as per paymentFrequency" in {
        val formProvider = new PayDateFormProvider()
        val latestPayDate = LocalDate.now().minusDays(50)
        val gen = Gen.oneOf(PaymentFrequency.values.toSeq)

        forAll(gen) { paymentFrequency =>
          val invalidData = latestPayDate.plusDays(10)

          val data = Map(
            "value.day"   -> invalidData.getDayOfMonth.toString,
            "value.month" -> invalidData.getMonthValue.toString,
            "value.year"  -> invalidData.getYear.toString
          )

          val result = formProvider
            .apply(latestPayDate = Some(latestPayDate), paymentFrequency = Some(paymentFrequency))
            .bind(data)

          result.errors.head.key shouldBe "value"
          result.errors.head.message shouldBe "payDate.error.invalid.as.per.paymentFrequency"
        }
      }
    }
  }
}
