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

package views.behaviours

import models.Salary
import play.api.data.Form
import play.twirl.api.HtmlFormat

trait SalaryViewBehaviours extends QuestionViewBehaviours[Salary] {

  def salaryPage(form: Form[Salary],
                 createView: Form[Salary] => HtmlFormat.Appendable,
                 messageKeyPrefix: String,
                 headingArgs: Seq[String] = Seq(),
                 section: Option[String] = None): Unit =
    "behave like a page with a salary field" when {

      val salary = Salary(BigDecimal(123.45))

      "rendered" must {

        "contain a label for the value" in {

          val doc = asDocument(createView(form))
          assertContainsLabel(doc, "value", messages(s"$messageKeyPrefix.label", headingArgs: _*))
        }

        "contain an input for the value" in {

          val doc = asDocument(createView(form))
          assertRenderedById(doc, "value")
        }
      }

      "rendered with a valid form" must {

        "include the form's value in the value input" in {

          val doc = asDocument(createView(form.fill(salary)))
          doc.getElementById("value").attr("value") mustBe salary.amount.toString
        }
      }

      "rendered with an error" must {

        "show an error summary" in {

          val doc = asDocument(createView(form.withError(error)))
          assertRenderedById(doc, "error-summary-title")
        }

        "show an error associated with the value field" in {

          val doc       = asDocument(createView(form.withError(error)))
          val errorSpan = doc.getElementsByClass("govuk-error-message").first
          errorSpan.text mustBe (messages("error.browser.title.prefix") + " " + messages(errorMessage))
        }

        "show an error prefix in the browser title" in {

          val doc = asDocument(createView(form.withError(error)))
          assertEqualsValue(doc, "title", s"""${messages("error.browser.title.prefix")} ${title(messages(s"$messageKeyPrefix.title",
                                                                                                         headingArgs: _*),
                                                                                                section)}""")
        }
      }
    }

}
