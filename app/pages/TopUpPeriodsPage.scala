/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages

import models.TopUpPeriod
import play.api.libs.json.JsPath

case object TopUpPeriodsPage extends QuestionPage[List[TopUpPeriod]] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "topupPeriods"
}
