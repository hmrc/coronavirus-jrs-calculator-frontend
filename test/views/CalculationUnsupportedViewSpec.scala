/*
 * Copyright 2022 HM Revenue & Customs
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

package views

import assets.messages.BaseMessages
import messages.CalculationUnsupportedMessages
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.twirl.api.HtmlFormat
import views.behaviours.ViewBehaviours
import views.html.CalculationUnsupportView

class CalculationUnsupportedViewSpec extends ViewBehaviours {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = "calculationUnsupported"
  val view             = injector.instanceOf[CalculationUnsupportView]

  "CalculationUnsupportedViewSpec" when {

    implicit val request: DataRequest[_] = fakeDataRequest()

    val expectedContent = Seq(
      Selectors.h1   -> CalculationUnsupportedMessages.heading,
      Selectors.p(1) -> CalculationUnsupportedMessages.p1
    )

    def applyView(): HtmlFormat.Appendable = view()

    implicit val doc: Document = asDocument(applyView())

    behave like normalPage(messageKeyPrefix)
    behave like pageWithBackLink
    behave like pageWithHeading(heading = CalculationUnsupportedMessages.heading)
    behave like pageWithExpectedMessages(expectedContent)
  }
}
