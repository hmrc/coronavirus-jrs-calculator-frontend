package models

import java.time.LocalDate


sealed trait Journey
case object RegularPay extends Journey
case object VariablePay extends Journey
case object VariablePayWithCylb extends Journey

case class BranchingQuestion(payQuestion: PayQuestion, variableLengthEmployed: Option[VariableLengthEmployed], employeeStartDate: Option[LocalDate])



case class JourneyCoreData(furloughPeriod: Period,
                            periods: Seq[PeriodWithPaymentDate],
                            frequency: PaymentFrequency,
                            nic: NicCategory,
                            pension: PensionContribution)

sealed trait JourneyData
case class RegularPayData(data: JourneyCoreData, payments: Seq[PaymentWithPeriod]) extends JourneyData