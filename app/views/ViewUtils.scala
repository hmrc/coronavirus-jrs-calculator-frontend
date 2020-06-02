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

package views

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import models.EmployeeStarted
import models.EmployeeStarted.{After1Feb2019, OnOrBefore1Feb2019}
import play.api.data.Form
import play.api.i18n.Messages
import utils.LocalDateHelpers

object ViewUtils {

  val apr5th2020 = LocalDate.of(2020, 4, 5)

  def title(form: Form[_], titleStr: String, section: Option[String] = None, titleMessageArgs: Seq[String] = Seq())(
    implicit messages: Messages): String =
    titleNoForm(s"${errorPrefix(form)} ${messages(titleStr, titleMessageArgs: _*)}", section)

  def titleNoForm(title: String, section: Option[String] = None, titleMessageArgs: Seq[String] = Seq())(
    implicit messages: Messages): String =
    s"${messages(title, titleMessageArgs: _*)} - ${section.fold("")(messages(_) + " - ")}${messages("service.name")} - ${messages("site.govuk")}"

  def errorPrefix(form: Form[_])(implicit messages: Messages): String =
    if (form.hasErrors || form.hasGlobalErrors) messages("error.browser.title.prefix") else ""

  private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
  private val dateFormatterWithoutYear = DateTimeFormatter.ofPattern("d MMMM")
  private val dateFormatterOnlyMonth = DateTimeFormatter.ofPattern("MMMM")
  def dateToString(date: LocalDate): String = dateFormatter.format(date)
  def dateToStringWithoutYear(date: LocalDate): String = dateFormatterWithoutYear.format(date)
  def dateToStringOnlyMonth(date: LocalDate): String = dateFormatterOnlyMonth.format(date)

  def cappedFurloughStart(furloughStart: LocalDate, employeeStarted: EmployeeStarted) =
    employeeStarted match {
      case After1Feb2019      => furloughStart.minusDays(1)
      case OnOrBefore1Feb2019 => LocalDateHelpers.earliestOf(apr5th2020, furloughStart.minusDays(1))
    }
}
