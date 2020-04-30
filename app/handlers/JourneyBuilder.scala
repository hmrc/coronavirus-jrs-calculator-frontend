package handlers

import java.time.LocalDate

import models.PayQuestion.{Regularly, Varies}
import models.VariableLengthEmployed.{No, Yes}
import models.{BranchingQuestion, Journey, JourneyCoreData, JourneyData, RegularPay, RegularPayData, VariablePay, VariablePayWithCylb}

trait JourneyBuilder {

  def define(data: BranchingQuestion): Journey = data match {
    case BranchingQuestion(Regularly, _, _) => RegularPay
    case BranchingQuestion(Varies, Some(No), Some(d)) if d.isAfter(LocalDate.of(2019, 4, 5)) => VariablePay
    case BranchingQuestion(Varies, Some(No), Some(d)) if d.isBefore(LocalDate.of(2019, 4, 6)) => VariablePayWithCylb
    case BranchingQuestion(Varies, Some(Yes), _) => VariablePayWithCylb
  }


  def journeyData(journey: Journey, coreData: JourneyCoreData): JourneyData =
    RegularPayData(coreData, Seq.empty)

}



