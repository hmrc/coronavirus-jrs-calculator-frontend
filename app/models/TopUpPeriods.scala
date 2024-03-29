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

package models

import play.api.data.Form
import play.api.i18n.Messages
import play.api.libs.json.{Format, Json}
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.hint.Hint
import views.ViewUtils._

import java.time.LocalDate

case class TopUpPeriod(date: LocalDate, furloughGrant: Amount)

object TopUpPeriod {
  implicit val defaultFormat: Format[TopUpPeriod] = Json.format[TopUpPeriod]
}

object TopUpPeriods {

  def options(form: Form[_], payDates: Seq[FurloughBreakdown])(implicit messages: Messages): Seq[CheckboxItem] = payDates.zipWithIndex.map {
    value =>
      val periodEnd    = value._1.paymentWithPeriod.periodWithPaymentDate.period.period.end
      val periodAmount = value._1.grant.value.formatted("%.2f")

      CheckboxItem(
        name = Some(s"value[${value._2}]"),
        id = Some(s"topup-period_${value._2.toString}"),
        value = periodEnd.toString,
        content = Text(messages("topupPeriods.period", dateToString(periodEnd))),
        checked = form.data.values.exists(_ == periodEnd.toString),
        hint = Some(Hint(content = Text(messages("topupPeriods.amount", periodAmount))))
      )
  }

}
