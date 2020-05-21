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

package views.includes

import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

import com.google.inject.Inject
import models.{FullPeriodCap, FurloughCap, PartialPeriodCap, PeriodSpansMonthCap}
import play.api.i18n.Messages

class FurloughCapHelper @Inject()() {

  def calculationFor(cap: FurloughCap)(implicit messages: Messages): Option[String] =
    cap match {
      case FullPeriodCap(_) => None
      case PeriodSpansMonthCap(value, monthOneFurloughDays, monthOne, monthOneDaily, monthTwoFurloughDays, monthTwo, monthTwoDaily) =>
        Some(
          messages(
            "furloughBreakdown.furloughCap.periodSpansMonthCap",
            monthOneFurloughDays,
            Month.of(monthOne).getDisplayName(TextStyle.FULL, Locale.ENGLISH),
            monthOneDaily.formatted("%.2f"),
            monthTwoFurloughDays,
            Month.of(monthTwo).getDisplayName(TextStyle.FULL, Locale.ENGLISH),
            monthTwoDaily.formatted("%.2f"),
            value.formatted("%.2f")
          ))
      case PartialPeriodCap(value, furloughDays, month, dailyCap) =>
        Some(
          messages(
            "furloughBreakdown.furloughCap.partialPeriodCap",
            furloughDays,
            Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH),
            dailyCap.formatted("%.2f"),
            value.formatted("%.2f")
          ))
    }

}