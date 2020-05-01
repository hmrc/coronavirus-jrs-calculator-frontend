/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package handlers

import java.time.LocalDate

import models.PayQuestion.{Regularly, Varies}
import models.VariableLengthEmployed.{No, Yes}
import models.{Amount, BranchingQuestion, Journey, JourneyCoreData, JourneyData, RegularPay, RegularPayData, UserAnswers, VariablePay, VariablePayData, VariablePayWithCylb}
import services.ReferencePayCalculator

trait JourneyBuilder extends DataExtractor {

  def define(data: BranchingQuestion): Journey = data match {
    case BranchingQuestion(Regularly, _, _)                                                   => RegularPay
    case BranchingQuestion(Varies, Some(No), Some(d)) if d.isAfter(LocalDate.of(2019, 4, 5))  => VariablePay
    case BranchingQuestion(Varies, Some(No), Some(d)) if d.isBefore(LocalDate.of(2019, 4, 6)) => VariablePayWithCylb
    case BranchingQuestion(Varies, Some(Yes), _)                                              => VariablePayWithCylb
  }

  def journeyData(journey: Journey, userAnswers: UserAnswers): Option[JourneyData] = journey match {
    case RegularPay  => regularPayData(userAnswers)
    case VariablePay => variablePayData(userAnswers)
  }

  private def variablePayData(userAnswers: UserAnswers): Option[VariablePayData] =
    for {
      coreData <- extractJourneyCoreData(userAnswers)
      grossPay <- extractVariableGrossPay(userAnswers)
      nonFurlough = extractNonFurlough(userAnswers)
      priorFurlough <- extractPriorFurloughPeriod(userAnswers)
    } yield VariablePayData(coreData, grossPay, nonFurlough, priorFurlough)

  private def regularPayData(userAnswers: UserAnswers): Option[RegularPayData] =
    for {
      coreData <- extractJourneyCoreData(userAnswers)
      salary   <- extractSalary(userAnswers)
    } yield RegularPayData(coreData, salary)
}
