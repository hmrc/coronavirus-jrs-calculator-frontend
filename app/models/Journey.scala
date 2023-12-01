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

package models

import java.time.LocalDate

sealed trait Journey
case object RegularPay          extends Journey
case object VariablePay         extends Journey
case object VariablePayWithCylb extends Journey

case class BranchingQuestions(payMethod: PayMethod, employeeStarted: Option[EmployeeStarted], employeeStartDate: Option[LocalDate])

case class ReferencePayData(furloughPeriod: FurloughWithinClaim, periods: Seq[PeriodWithPaymentDate], frequency: PaymentFrequency)

sealed trait ReferencePay {
  val referencePayData: ReferencePayData

  def furloughPeriod: FurloughWithinClaim = referencePayData.furloughPeriod
  def periods: Seq[PeriodWithPaymentDate] = referencePayData.periods
  def frequency: PaymentFrequency         = referencePayData.frequency
}

case class RegularPayData(referencePayData: ReferencePayData, wage: Amount) extends ReferencePay

case class VariablePayData(referencePayData: ReferencePayData, grossPay: Amount, nonFurloughPay: NonFurloughPay, priorFurlough: Period)
    extends ReferencePay

case class VariablePayWithCylbData(
  referencePayData: ReferencePayData,
  grossPay: Amount,
  nonFurloughPay: NonFurloughPay,
  priorFurlough: Period,
  cylbPayments: Seq[LastYearPayment]
) extends ReferencePay

sealed trait PhaseTwoJourney
case object PhaseTwoRegularPay          extends PhaseTwoJourney
case object PhaseTwoVariablePay         extends PhaseTwoJourney
case object PhaseTwoVariablePayWithCylb extends PhaseTwoJourney

case class StatutoryLeaveData(days: Int, pay: BigDecimal)

case class PhaseTwoReferencePayData(
  furloughPeriod: FurloughWithinClaim,
  periods: Seq[PhaseTwoPeriod],
  frequency: PaymentFrequency,
  statutoryLeave: Option[StatutoryLeaveData] = None
)

sealed trait PhaseTwoReferencePay {
  val referencePayData: PhaseTwoReferencePayData

  def furloughPeriod: FurloughWithinClaim        = referencePayData.furloughPeriod
  def periods: Seq[PhaseTwoPeriod]               = referencePayData.periods
  def frequency: PaymentFrequency                = referencePayData.frequency
  def statutoryLeave: Option[StatutoryLeaveData] = referencePayData.statutoryLeave
}

case class PhaseTwoRegularPayData(referencePayData: PhaseTwoReferencePayData, wage: Amount) extends PhaseTwoReferencePay

case class PhaseTwoVariablePayData(referencePayData: PhaseTwoReferencePayData, annualPay: Amount, priorFurlough: Period)
    extends PhaseTwoReferencePay

case class PhaseTwoVariablePayWithCylbData(
  referencePayData: PhaseTwoReferencePayData,
  annualPay: Amount,
  priorFurlough: Period,
  cylbPayments: Seq[LastYearPayment]
) extends PhaseTwoReferencePay
