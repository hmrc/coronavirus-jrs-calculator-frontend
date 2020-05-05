/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import play.api.data.Form
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem

sealed trait TopupPeriods

object TopupPeriods extends Enumerable.Implicits {

  case object Period1 extends WithName("period1") with TopupPeriods
  case object Period2 extends WithName("period2") with TopupPeriods

  val values: Seq[TopupPeriods] = Seq(
    Period1,
    Period2
  )

  def options(form: Form[_])(implicit messages: Messages): Seq[CheckboxItem] = values.map { value =>
    CheckboxItem(
      name = Some("value[]"),
      id = Some(value.toString),
      value = value.toString,
      content = Text(messages(s"topupPeriods.${value.toString}")),
      checked = form.data.exists(_._2 == value.toString)
    )
  }

  implicit val enumerable: Enumerable[TopupPeriods] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
