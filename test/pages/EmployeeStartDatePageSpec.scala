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

package pages

import models.UserAnswers
import pages.behaviours.PageBehaviours

import java.time.LocalDate

class EmployeeStartDatePageSpec extends PageBehaviours {

  "EmployeeStartDatePage" must {

    beRetrievable[LocalDate](EmployeeStartDatePage)

    beSettable[LocalDate](EmployeeStartDatePage)

    beRemovable[LocalDate](EmployeeStartDatePage)

    "remove the answer for the furloughed before 30th October page" in {

      val userAnswers = UserAnswers("foo").set(OnPayrollBefore30thOct2020Page, true).get

      val resultantAnswers = EmployeeStartDatePage.cleanup(None, userAnswers)

      resultantAnswers.get.getO(OnPayrollBefore30thOct2020Page) mustBe None
    }
  }
}
