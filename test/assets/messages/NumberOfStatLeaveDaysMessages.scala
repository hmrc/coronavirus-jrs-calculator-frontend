/*
 * Copyright 2022 HM Revenue & Customs
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

package messages

import models.{EmployeeType, Type4}
import play.api.i18n.Messages
import views.ViewUtils._

import java.time.LocalDate

object NumberOfStatLeaveDaysMessages {

  def h1(boundaryStart: LocalDate, boundaryEnd: LocalDate, employeeType: EmployeeType)(implicit messages: Messages): String =
    employeeType match {
      case Type4 =>
        s"How many days was this employee on statutory leave between the day their employment started and ${dateToString(boundaryEnd)}?"
      case _ =>
        s"How many days was this employee on statutory leave between ${dateToString(boundaryStart)} and ${dateToString(boundaryEnd)}?"
    }

  val dayEmploymentStarted = "the day their employment started"

  val dropDown          = "What does statutory leave include?"
  val dropDownParagraph = "For this calculation, statutory leave only includes:"
  val bullet1           = "statutory sick pay related leave"
  val bullet2           = "family related statutory leave, for example paternity leave"
  val bullet3           = "reduced rate paid leave following a period of statutory sick pay related leave"
  val bullet4           = "reduced rate paid leave following a period of family related statutory leave"

}
