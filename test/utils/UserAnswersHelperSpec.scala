package utils

import java.time.LocalDate

import base.SpecBase
import models.UserAnswers
import pages.PayDatePage

import scala.util.Try

class UserAnswersHelperSpec extends SpecBase with CoreTestData {

  "Add pay dates to a user answers" in new UserAnswersHelper {
    val datesToAdd = LocalDate.of(2020, 1, 1) :: LocalDate.of(2020, 2, 1) :: dummyUserAnswers.getList(PayDatePage).toList
    val actual: Try[UserAnswers] = addPayDates(dummyUserAnswers, datesToAdd)

    actual.get.getList(PayDatePage) mustBe datesToAdd
  }

}
