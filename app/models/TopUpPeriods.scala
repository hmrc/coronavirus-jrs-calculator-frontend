/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import java.time.LocalDate

import play.api.data.Form
import play.api.i18n.Messages
import play.api.libs.json.{Format, Json}
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem
import uk.gov.hmrc.govukfrontend.views.viewmodels.hint.Hint
import views.ViewUtils._

case class TopUpPeriod(date: LocalDate, furloughGrant: Amount)

object TopUpPeriod {
  implicit val defaultFormat: Format[TopUpPeriod] = Json.format
}

object TopUpPeriods {

  def options(form: Form[_], payDates: Seq[PeriodBreakdown])(implicit messages: Messages): Seq[CheckboxItem] = payDates.zipWithIndex.map {
    value =>
      val periodEnd = value._1.periodWithPaymentDate.period.period.end
      val periodAmount = value._1.grant.value.formatted("%.2f")

      CheckboxItem(
        name = Some("value[]"),
        id = Some(s"topup-period_${value._2.toString}"),
        value = periodEnd,
        content = Text(messages("topupPeriods.period", dateToString(periodEnd))),
        checked = form.data.values.contains(periodEnd.toString),
        hint = Some(Hint(content = Text(messages("topupPeriods.amount", periodAmount))))
      )
  }

}
