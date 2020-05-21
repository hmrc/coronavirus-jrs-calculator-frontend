package utils

import java.time.LocalDate

import models.UserAnswers
import pages.PayDatePage

import scala.util.Try

trait UserAnswersHelper {

  def addPayDates(userAnswers: UserAnswers, answers: List[LocalDate]): Try[UserAnswers] = {
    val zipped: List[(LocalDate, Int)] = answers.zip(1 to answers.length)

    def rec(userAnswers: Try[UserAnswers], dates: List[(LocalDate, Int)]): Try[UserAnswers] =
      dates match {
        case Nil => userAnswers
        case (d: LocalDate, idx) :: tail =>
          userAnswers.flatMap(answers => rec(answers.setListWithInvalidation(PayDatePage, d, idx), tail))
      }
    rec(Try(userAnswers), zipped)
  }
}
