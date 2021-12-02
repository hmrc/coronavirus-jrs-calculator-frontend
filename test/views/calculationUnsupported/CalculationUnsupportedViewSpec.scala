/*
 * Copyright 2021 HM Revenue & Customs
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

package views.calculationUnsupported

import assets.constants.PaymentFrequencyConstants.allRadioOptions
import assets.messages.{BaseMessages, CalculationUnsupportedMessages}
import forms.PaymentFrequencyFormProvider
import models.PaymentFrequency
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.api.data.Form
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem
import views.behaviours.ViewBehaviours
import views.html.CalculationUnsupportedView

class CalculationUnsupportedViewSpec extends ViewBehaviours {

  object Selectors extends BaseSelectors

  val messageKeyPrefix                 = "calculationUnsupported"
  val view: CalculationUnsupportedView = injector.instanceOf[CalculationUnsupportedView]

  val expectedContent = Seq(
    Selectors.h1   -> CalculationUnsupportedMessages.heading,
    Selectors.p(1) -> CalculationUnsupportedMessages.ineligiblePara1,
    Selectors.p(2) -> CalculationUnsupportedMessages.ineligiblePara2
  )

  "CalculationUnsupportedViewSpec" when {

    "it is the ineligible RTI version of the view 1stFeb2020 - 19thMarch2020 journey view"

    implicit val request: DataRequest[_] = fakeDataRequest()

    def applyView(): HtmlFormat.Appendable = view(ineligible = true)

    implicit val doc: Document = asDocument(applyView())

    behave like normalPage(messageKeyPrefix)
    behave like pageWithBackLink
    behave like pageWithHeading(heading = CalculationUnsupportedMessages.heading)
    behave like pageWithSubmitButton(BaseMessages.continue)
    behave like pageWithExpectedMessages(expectedContent)
  }

}
