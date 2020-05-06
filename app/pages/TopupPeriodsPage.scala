/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages

import java.time.LocalDate

import play.api.libs.json.JsPath

case object TopupPeriodsPage extends QuestionPage[List[LocalDate]] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "topupPeriods"
}
