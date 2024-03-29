/*
 * Copyright 2023 HM Revenue & Customs
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

package assets.messages

import views.ViewUtils.dateToString

import java.time.LocalDate

import navigation.FakeNavigators.messages

object FirstFurloughDateMessages {

  val heading             = "When was this employee first furloughed?"
  def p1(date: LocalDate) = s"This is the date when the employee was first furloughed, on or after ${dateToString(date)}."
  val p2 =
    "We need this information because you told us this employee has been on more than one period of furlough since 1 November 2020, and was not eligible for the Coronavirus Job Retention Scheme before this date."

}
