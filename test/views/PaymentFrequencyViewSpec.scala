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

package views

import assets.constants.PaymentFrequencyConstants.{allRadioOptions, radioItem}
import assets.messages.{BaseMessages, PaymentFrequencyMessages}
import forms.PaymentFrequencyFormProvider
import models.PaymentFrequency
import models.PaymentFrequency._
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.api.data.Form
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem
import views.behaviours.ViewBehaviours
import views.html.PaymentFrequencyView

class PaymentFrequencyViewSpec extends ViewBehaviours {

  object Selectors extends BaseSelectors

  val messageKeyPrefix             = "payFrequency"
  val view: PaymentFrequencyView   = injector.instanceOf[PaymentFrequencyView]
  val form: Form[PaymentFrequency] = new PaymentFrequencyFormProvider()()

  val expectedContent = Seq(
    Selectors.h1      -> PaymentFrequencyMessages.heading,
    Selectors.detail  -> PaymentFrequencyMessages.indent,
    Selectors.p(1)    -> PaymentFrequencyMessages.p1,
    Selectors.p(2)    -> s"${PaymentFrequencyMessages.p2} ${PaymentFrequencyMessages.link}",
    Selectors.link(1) -> PaymentFrequencyMessages.link
  )

  "PaymentFrequencyViewSpec" when {

    implicit val request: DataRequest[_] = fakeDataRequest()

    def applyView(): HtmlFormat.Appendable =
      view(
        form = form,
        postAction = controllers.routes.PaymentFrequencyController.onSubmit(),
        radioItems = allRadioOptions()
      )

    implicit val doc: Document = asDocument(applyView())

    behave like normalPage(messageKeyPrefix)
    behave like pageWithBackLink
    behave like pageWithHeading(heading = PaymentFrequencyMessages.heading)
    behave like pageWithSubmitButton(BaseMessages.continue)
    behave like pageWithExpectedMessages(expectedContent)

    allRadioOptions().foreach { option =>
      s"contain radio buttons for the value '${option.value.get}'" in {

        val doc = asDocument(applyView())
        assertContainsRadioButton(doc, id = option.value.get, name = "value", value = option.value.get, isChecked = false)
      }

      s"rendered with a value of '${option.value.get}'" must {

        s"have the '${option.value.get}' radio button selected" in {

          def applyView(form: Form[_], radioItems: Seq[RadioItem]): HtmlFormat.Appendable =
            view.apply(
              form = form,
              postAction = controllers.routes.PaymentFrequencyController.onSubmit(),
              radioItems = radioItems
            )

          val allSelectedRadioOptions = option.value.get match {
            case "weekly"      => Seq(radioItem(Weekly, checked = true), radioItem(FortNightly), radioItem(FourWeekly), radioItem(Monthly))
            case "fortnightly" => Seq(radioItem(Weekly), radioItem(FortNightly, checked = true), radioItem(FourWeekly), radioItem(Monthly))
            case "fourweekly"  => Seq(radioItem(Weekly), radioItem(FortNightly), radioItem(FourWeekly, checked = true), radioItem(Monthly))
            case "monthly"     => Seq(radioItem(Weekly), radioItem(FortNightly), radioItem(FourWeekly), radioItem(Monthly, checked = true))
          }

          val formWithData: Form[PaymentFrequency] = form.bind(Map("value" -> s"${option.value.get}"))
          val doc                                  = asDocument(applyView(formWithData, allSelectedRadioOptions))

          assertContainsRadioButton(doc, id = option.value.get, name = "value", value = option.value.get, isChecked = true)
        }
      }
    }
  }

}
