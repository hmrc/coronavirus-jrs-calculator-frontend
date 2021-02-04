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

package assets.constants

import models.PaymentFrequency
import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

object PaymentFrequencyConstants {

  def allRadioOptions(checked: Boolean = false)(implicit messages: Messages): Seq[RadioItem] = {

    val radioButton = { (paymentFrequency: PaymentFrequency) =>
      RadioItem(
        value = Some(paymentFrequency.toString),
        content = Text(messages(s"payFrequency.${paymentFrequency.toString}")),
        checked = checked,
        id = Some(paymentFrequency.toString)
      )
    }

    Seq(
      radioButton(Weekly),
      radioButton(FortNightly),
      radioButton(FourWeekly),
      radioButton(Monthly)
    )
  }

}
