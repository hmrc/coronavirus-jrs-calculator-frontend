/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package handlers

import java.time.LocalDate

import base.{CoreTestDataBuilder, SpecBase}
import com.softwaremill.quicklens._
import models.NicCategory.Payable
import models.PayQuestion.{Regularly, Varies}
import models.PaymentFrequency.Monthly
import models.VariableLengthEmployed.{No, Yes}
import models.{Amount, BranchingQuestion, JourneyCoreData, NonFurloughPay, PensionContribution, PeriodWithPaymentDate, RegularPay, RegularPayData, Salary, UserAnswers, VariableGrossPay, VariablePay, VariablePayData, VariablePayWithCylb}
import pages.{EmployeeStartDatePage, PayQuestionPage, SalaryQuestionPage, VariableGrossPayPage}

class JourneyBuilderSpec extends SpecBase with CoreTestDataBuilder {

  "return regular journey if pay question is Regularly" in new JourneyBuilder {
    val branchData = BranchingQuestion(Regularly, None, None)

    define(branchData) mustBe RegularPay
  }

  "return variable journey if pay question is Varies and no Cylb eligible" in new JourneyBuilder {
    val branchData = BranchingQuestion(Varies, Some(No), Some(LocalDate.of(2019, 4, 6)))

    define(branchData) mustBe VariablePay
  }

  "return variable journey if pay question is Varies and Cylb eligible" in new JourneyBuilder {
    val branchData = BranchingQuestion(Varies, Some(Yes), None)

    define(branchData) mustBe VariablePayWithCylb
    define(
      branchData
        .modify(_.variableLengthEmployed.each)
        .setTo(No)
        .modify(_.employeeStartDate)
        .setTo(Some(LocalDate.of(2019, 4, 5)))) mustBe VariablePayWithCylb
  }

  "build a Regular pay data for a regular pay journey" in new JourneyBuilder {
    val initialAnswers: UserAnswers = mandatoryAnswer
      .set(SalaryQuestionPage, Salary(1000.0))
      .get
    val periods: Seq[PeriodWithPaymentDate] = defaultJourneyCoreData.periods
    val expectedCoreData = JourneyCoreData(period("2020-03-01", "2020-03-31"), periods, Monthly, Payable, PensionContribution.Yes)

    val actual = journeyData(RegularPay, initialAnswers)

    actual mustBe Some(RegularPayData(expectedCoreData, Amount(1000.0)))
  }

  "build a Variable pay with no cylb for a variable pay journey" in new JourneyBuilder {
    val initialAnswers: UserAnswers = mandatoryAnswer
      .set(VariableGrossPayPage, VariableGrossPay(1000.0))
      .get
      .set(PayQuestionPage, Varies)
      .get
      .set(EmployeeStartDatePage, LocalDate.of(2019, 12, 1))
      .get

    val periods: Seq[PeriodWithPaymentDate] = defaultJourneyCoreData.periods
    val expectedCoreData = JourneyCoreData(period("2020-03-01", "2020-03-31"), periods, Monthly, Payable, PensionContribution.Yes)

    val actual = journeyData(VariablePay, initialAnswers)

    actual mustBe Some(VariablePayData(expectedCoreData, Amount(1000.0), NonFurloughPay(None, None), period("2019-12-01", "2020-02-29")))
  }
}
