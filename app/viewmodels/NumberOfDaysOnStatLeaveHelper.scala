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

package viewmodels

import utils.{EmployeeTypeUtil, KeyDatesUtil}
import java.time.LocalDate

import cats.data.Validated.Valid
import config.FrontendAppConfig
import models.requests.DataRequest
import pages.{EmployeeStartDatePage, FirstFurloughDatePage, FurloughStartDatePage}
import play.api.Logger
import play.api.i18n.Messages
import uk.gov.hmrc.http.InternalServerException
import utils.LocalDateHelpers._
import utils.{EmployeeTypeUtil, KeyDatesUtil}
import views.ViewUtils.dateToString

class NumberOfDaysOnStatLeaveHelper extends EmployeeTypeUtil with KeyDatesUtil {

  def boundaryStartDateMessage()(implicit request: DataRequest[_], appConfig: FrontendAppConfig, messages: Messages): String =
    variablePayResolver[String](
      type3EmployeeResult = Some(dateToString(apr6th2019)),
      type4EmployeeResult = Some(messages("hasEmployeeBeenOnStatutoryLeave.dayEmploymentStarted")),
      type5aEmployeeResult = type5BoundaryStartMessage(),
      type5bEmployeeResult = type5BoundaryStartMessage()
    ).fold(
      throw new InternalServerException("[NumberOfDaysOnStatLeaveHelper][boundaryStartDateMessage] failed to resolve employee type")
    )(identity)

  def boundaryEndMessage()(implicit request: DataRequest[_], appConfig: FrontendAppConfig, messages: Messages): String =
    variablePayResolver[String](
      type3EmployeeResult = Some(type3And4BoundaryEndMessage()),
      type4EmployeeResult = Some(type3And4BoundaryEndMessage()),
      type5aEmployeeResult = Some(dateToString(firstFurloughDate().minusDays(1))),
      type5bEmployeeResult = Some(dateToString(firstFurloughDate().minusDays(1)))
    ).fold(
      throw new InternalServerException("[NumberOfDaysOnStatLeaveHelper][boundaryEndMessage] failed to resolve employee type")
    )(identity)

  private def type5BoundaryStartMessage()(implicit request: DataRequest[_], messages: Messages): Option[String] =
    request.userAnswers.getV(EmployeeStartDatePage) match {
      case Valid(startDate) =>
        Logger.debug(s"[NumberOfDaysOnStatLeaveHelper][type5BoundaryStart] start date: $startDate")
        if (startDate.isAfter(apr6th2020)) {
          Some(messages("hasEmployeeBeenOnStatutoryLeave.dayEmploymentStarted"))
        } else {
          Some(dateToString(apr6th2020))
        }
      case _ =>
        Logger.debug("[NumberOfDaysOnStatLeaveHelper][type5BoundaryStart] no answer for EmployeeStartDatePage")
        None
    }

  private def type3And4BoundaryEndMessage()(implicit request: DataRequest[_], messages: Messages): String = {
    val dayBeforeFirstFurlough = firstFurloughDate().minusDays(1)
    Logger.debug(s"[NumberOfDaysOnStatLeaveHelper][type3And4BoundaryEnd] dayBeforeFirstFurlough: $dayBeforeFirstFurlough")
    dateToString(earliestOf(apr5th2020, dayBeforeFirstFurlough))
  }

  def boundaryStartDate()(implicit request: DataRequest[_], appConfig: FrontendAppConfig, messages: Messages): LocalDate = {

    val employeeStartDate: Option[LocalDate] = request.userAnswers.getV(EmployeeStartDatePage).toOption

    val type5BoundaryStartDate: Option[LocalDate] = {
      Logger.debug(s"[NumberOfDaysOnStatLeaveHelper][boundaryStartDate] type5BoundaryStartDate: $employeeStartDate")
      employeeStartDate.map(employeeStartDate => latestOf(apr6th2020, employeeStartDate))
    }

    variablePayResolver[LocalDate](
      type3EmployeeResult = Some(apr6th2019),
      type4EmployeeResult = employeeStartDate,
      type5aEmployeeResult = type5BoundaryStartDate,
      type5bEmployeeResult = type5BoundaryStartDate,
    ).fold(
      throw new InternalServerException("[NumberOfDaysOnStatLeaveHelper][boundaryStart] failed to resolve employee type")
    )(identity)
  }

  def boundaryEndDate()(implicit request: DataRequest[_], appConfig: FrontendAppConfig, messages: Messages): LocalDate = {

    val earliestOfApr5th2020OrDayBeforeFirstFurlough: LocalDate =
      request.userAnswers.getV(FirstFurloughDatePage) match {
        case Valid(firstFurloughDate) => firstFurloughDate.minusDays(1)
        case _ =>
          request.userAnswers.getV(FurloughStartDatePage) match {
            case Valid(furloughStartDate) => furloughStartDate.minusDays(1)
            case _                        => apr5th2020
          }
      }

    earliestOfApr5th2020OrDayBeforeFirstFurlough
  }

}