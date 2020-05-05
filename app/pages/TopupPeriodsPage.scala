/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages

import models.TopupPeriods
import play.api.libs.json.JsPath

case object TopupPeriodsPage extends QuestionPage[Set[TopupPeriods]] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "topupPeriods"
}
