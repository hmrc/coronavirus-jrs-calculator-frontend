/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import java.time.LocalDate

import forms.mappings.Mappings
import javax.inject.Inject
import models.PaymentFrequency
import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}
import play.api.data.Form
import play.api.data.validation.{Constraint, Invalid, Valid}
import views.ViewUtils

class PayDateFormProvider @Inject() extends Mappings {

  def apply(
    beforeDate: Option[LocalDate] = None,
    afterDate: Option[LocalDate] = None,
    latestPayDate: Option[LocalDate] = None,
    paymentFrequency: Option[PaymentFrequency] = None): Form[LocalDate] =
    Form(
      "value" -> localDate(
        invalidKey = "payDate.error.invalid",
        allRequiredKey = "payDate.error.required.all",
        twoRequiredKey = "payDate.error.required.two",
        requiredKey = "payDate.error.required"
      ).verifying(isBeforeIfDefined(beforeDate))
        .verifying(isAfterIfDefined(afterDate))
        .verifying(matchesAsPerPaymentFrequency(latestPayDate, paymentFrequency))
    )

  private def isBeforeIfDefined(beforeDate: Option[LocalDate]): Constraint[LocalDate] = Constraint { date =>
    if (beforeDate.forall(date.isBefore(_))) Valid
    else Invalid("payDate.error.mustBeBefore", ViewUtils.dateToString(beforeDate.get))
  }

  private def isAfterIfDefined(afterDate: Option[LocalDate]): Constraint[LocalDate] = Constraint { date =>
    if (afterDate.forall(date.isAfter(_))) Valid
    else Invalid("payDate.error.mustBeAfter", ViewUtils.dateToString(afterDate.get))
  }

  private def matchesAsPerPaymentFrequency(maybeLatestDate: Option[LocalDate], maybePF: Option[PaymentFrequency]): Constraint[LocalDate] =
    Constraint { date =>
      (maybeLatestDate, maybePF) match {
        case (Some(latestDate), Some(pf)) =>
          if (date == expectedPayDate(latestDate, pf)) Valid else Invalid("payDate.error.invalid.as.per.paymentFrequency")
        case (None, _) => Valid
        case (_, None) => Valid
      }
    }

  def expectedPayDate(latestDate: LocalDate, paymentFrequency: PaymentFrequency): LocalDate =
    paymentFrequency match {
      case Monthly     => latestDate.plusMonths(1)
      case FourWeekly  => latestDate.plusDays(28)
      case FortNightly => latestDate.plusDays(14)
      case Weekly      => latestDate.plusDays(7)
    }
}
