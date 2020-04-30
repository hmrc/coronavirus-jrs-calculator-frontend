package handlers

import java.time.LocalDate

import base.{CoreTestDataBuilder, SpecBase}
import com.softwaremill.quicklens._
import models.PayQuestion.{Regularly, Varies}
import models.VariableLengthEmployed.{No, Yes}
import models.{BranchingQuestion, RegularPay, RegularPayData, VariablePay, VariablePayWithCylb}

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
    define(branchData
      .modify(_.variableLengthEmployed.each).setTo(No)
      .modify(_.employeeStartDate).setTo(Some(LocalDate.of(2019, 4, 5)))
    ) mustBe VariablePayWithCylb
  }

  "build a Regular pay data if is a RegularPay journey" in new JourneyBuilder {

    journeyData(RegularPay, defaultJourneyCoreData) must matchPattern {
      case RegularPayData(_, _) =>
    }
  }
}
