/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import java.time.LocalDate

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form
import play.api.data.validation.{Constraint, Invalid, Valid}
import utils.ImplicitDateFormatter

class EmployeeStartDateFormProvider @Inject() extends Mappings with ImplicitDateFormatter {

  val validStart = LocalDate.of(2019, 2, 2)
  val validEnd = LocalDate.of(2020, 3, 19)

  def apply(furloughStart: LocalDate): Form[LocalDate] =
    Form(
      "value" -> localDate(
        invalidKey = "employeeStartDate.error.invalid",
        allRequiredKey = "employeeStartDate.error.required.all",
        twoRequiredKey = "employeeStartDate.error.required.two",
        requiredKey = "employeeStartDate.error.required"
      ).verifying(validStartDate)
        .verifying(beforeFurloughStart(furloughStart))
    )

  private def validStartDate: Constraint[LocalDate] = Constraint { date =>
    if (!date.isBefore(validStart) &&
        !date.isAfter(validEnd)) {
      Valid
    } else {
      Invalid("employeeStartDate.error.outofrange", dateToString(validStart), dateToString(validEnd))
    }
  }

  private def beforeFurloughStart(furloughStart: LocalDate): Constraint[LocalDate] = Constraint { date =>
    if (date.isBefore(furloughStart)) {
      Valid
    } else {
      Invalid("employeeStartDate.error.on.or.after.furlough")
    }
  }
}
