/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import play.api.data.Form
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

sealed trait FurloughDates

object FurloughDates extends Enumerable.Implicits {

  case object Startedinclaim extends WithName("startedInClaim") with FurloughDates
  case object Endedinclaim extends WithName("endedInClaim") with FurloughDates

  val values: Seq[FurloughDates] = Seq(
    Startedinclaim,
    Endedinclaim
  )

  def options(form: Form[_])(implicit messages: Messages): Seq[RadioItem] = values.map { value =>
    RadioItem(
      value = Some(value.toString),
      content = Text(messages(s"furloughDates.${value.toString}")),
      checked = form("value").value.contains(value.toString)
    )
  }

  implicit val enumerable: Enumerable[FurloughDates] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
