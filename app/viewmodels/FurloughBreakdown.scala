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

package viewmodels

import models.{Amount, FullPeriod, FurloughCap, PartialPeriod, Periods}

case class FurloughBreakdown(employeesWages: Amount, furloughCap: FurloughCap, furloughGrant: Amount, period: Periods) {
  def fullPeriodDays: Int = period.period.countDays

  def furloughDays: Int = period match {
    case fp: FullPeriod    => fp.period.countDays
    case pp: PartialPeriod => pp.partial.countDays
  }

  def isCapped: Boolean = (employeesWages.value * 0.8) > furloughCap.value
}
