package controllers.scenarios

import assets.BaseITConstants
import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}
import models.{EmployeeRTISubmission, FullPeriod, FurloughStatus, Hours, PartTimeHours, PartTimeQuestion, PartialPeriod, PayMethod, PayPeriodsList, Period, RegularLengthEmployed, UserAnswers, UsualHours}
import utils.{CreateRequestHelper, CustomMatchers, ITCoreTestData, IntegrationSpecBase}

object NovemberConfirmationScenarios extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers
 with BaseITConstants with ITCoreTestData {

  val novemberVariableWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq("November Variable Weekly Scenarios" -> Seq(
    emptyUserAnswers
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withEmployeeStartDate("2020-05-05")
      .withOnPayrollBefore30thOct2020(true)
      .withFurloughEndDate("2020, 11, 14")
      .withPaymentFrequency(Weekly)
      .withEmployeeStartedAfter1Feb2019()
      .withClaimPeriodStart("2020, 11, 1")
      .withFurloughInLastTaxYear(false)
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withPartTimePeriods(List(
        PartialPeriod(
          Period("2020, 11, 01".toLocalDate, "2020, 11, 7".toLocalDate),
          Period("2020, 11, 4".toLocalDate, "2020, 11, 7".toLocalDate)
        ),
        FullPeriod(Period("2020, 11, 8".toLocalDate, "2020, 11, 14".toLocalDate))
      ))
      .withPayMethod(PayMethod.Variable)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withAnnualPayAmount(19564.4)
      .withFurloughStartDate("2020, 11, 4")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withClaimPeriodEnd("2020, 11, 30")
      .withPayDate(List("2020-10-31", "2020-11-07", "2020-11-14"))
      .withUsualHours(List(
        UsualHours("2020, 11, 7".toLocalDate, Hours(40.0)),
        UsualHours("2020, 11, 14".toLocalDate, Hours(50.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 7".toLocalDate, Hours(14.0)),
        PartTimeHours("2020, 11, 14".toLocalDate, Hours(15.0))
      ))
      -> 620.52,
    emptyUserAnswers
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withEmployeeStartDate("2020-04-01")
      .withOnPayrollBefore30thOct2020(true)
      .withFurloughEndDate("2020, 11, 14")
      .withPaymentFrequency(Weekly)
      .withEmployeeStartedAfter1Feb2019()
      .withClaimPeriodStart("2020, 11, 1")
      .withFurloughInLastTaxYear(false)
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withPartTimePeriods(List(
        PartialPeriod(
          Period("2020, 11, 01".toLocalDate, "2020, 11, 7".toLocalDate),
          Period("2020, 11, 4".toLocalDate, "2020, 11, 7".toLocalDate)
        ),
        FullPeriod(Period("2020, 11, 8".toLocalDate, "2020, 11, 14".toLocalDate))
      ))
      .withPayMethod(PayMethod.Variable)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withAnnualPayAmount(19564.4)
      .withFurloughStartDate("2020, 11, 4")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withClaimPeriodEnd("2020, 11, 30")
      .withPayDate(List("2020-10-31", "2020-11-07", "2020-11-14"))
      .withUsualHours(List(
        UsualHours("2020, 11, 7".toLocalDate, Hours(40.0)),
        UsualHours("2020, 11, 14".toLocalDate, Hours(50.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 7".toLocalDate, Hours(14.0)),
        PartTimeHours("2020, 11, 14".toLocalDate, Hours(15.0))
      ))
      -> 553.68,
    emptyUserAnswers
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withEmployeeStartDate("2020-04-01")
      .withOnPayrollBefore30thOct2020(true)
      .withFurloughEndDate("2020, 11, 14")
      .withPaymentFrequency(Weekly)
      .withEmployeeStartedAfter1Feb2019()
      .withClaimPeriodStart("2020, 11, 1")
      .withFurloughInLastTaxYear(false)
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withPartTimePeriods(List(
        PartialPeriod(
          Period("2020, 11, 01".toLocalDate, "2020, 11, 7".toLocalDate),
          Period("2020, 11, 4".toLocalDate, "2020, 11, 7".toLocalDate)
        ),
        FullPeriod(Period("2020, 11, 8".toLocalDate, "2020, 11, 14".toLocalDate))
      ))
      .withPayMethod(PayMethod.Variable)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withAnnualPayAmount(1000.0)
      .withFurloughStartDate("2020, 11, 4")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withClaimPeriodEnd("2020, 11, 30")
      .withPayDate(List("2020-10-31", "2020-11-07", "2020-11-14"))
      .withUsualHours(List(
        UsualHours("2020, 11, 7".toLocalDate, Hours(40.0)),
        UsualHours("2020, 11, 14".toLocalDate, Hours(50.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 7".toLocalDate, Hours(14.0)),
        PartTimeHours("2020, 11, 14".toLocalDate, Hours(15.0))
      ))
      -> 28.32,
    emptyUserAnswers
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withFurloughEndDate("2020, 11, 7")
      .withPaymentFrequency(Weekly)
      .withEmployeeStartedOnOrBefore1Feb2019()
      .withClaimPeriodStart("2020, 11, 1")
      .withLastYear(List("2019-11-02" -> 420, "2019-11-09" -> 490))
      .withFurloughInLastTaxYear(false)
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withPartTimePeriods(List(
        FullPeriod(Period("2020, 11, 1".toLocalDate, "2020, 11, 7".toLocalDate))
      ))
      .withPayMethod(PayMethod.Variable)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withAnnualPayAmount(26000.0)
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withClaimPeriodEnd("2020, 11, 30")
      .withPayDate(List("2020-10-31", "2020-11-07"))
      .withUsualHours(List(
        UsualHours("2020, 11, 7".toLocalDate, Hours(40.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 7".toLocalDate, Hours(14.0)),
      ))
      -> 258.58,
    emptyUserAnswers
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withFurloughEndDate("2020, 11, 24")
      .withPaymentFrequency(Weekly)
      .withEmployeeStartedOnOrBefore1Feb2019()
      .withClaimPeriodStart("2020, 11, 3")
      .withLastYear(List("2019-11-09" -> 420, "2019-11-16" -> 490, "2019-11-23" -> 560, "2019-11-30" -> 630))
      .withFurloughInLastTaxYear(false)
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withPayMethod(PayMethod.Variable)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
      .withAnnualPayAmount(26000.0)
      .withFurloughStartDate("2020, 11, 4")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withClaimPeriodEnd("2020, 11, 28")
      .withPayDate(List("2020-10-31", "2020-11-07", "2020-11-14", "2020-11-21", "2020-11-28"))
      -> 1257.15
  ))

  val novemberVariableTwoWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq("November Variable Two Weekly Scenarios" -> Seq(
    emptyUserAnswers
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withEmployeeStartDate("2020-08-27")
      .withOnPayrollBefore30thOct2020(true)
      .withFurloughEndDate("2020, 11, 30")
      .withPaymentFrequency(FortNightly)
      .withEmployeeStartedAfter1Feb2019()
      .withClaimPeriodStart("2020, 11, 13")
      .withFurloughInLastTaxYear(false)
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withPartTimePeriods(List(
        PartialPeriod(
          Period("2020, 11, 01".toLocalDate, "2020, 11, 14".toLocalDate),
          Period("2020, 11, 13".toLocalDate, "2020, 11, 14".toLocalDate)
        ),
        FullPeriod(Period("2020, 11, 15".toLocalDate, "2020, 11, 28".toLocalDate)),
        PartialPeriod(
          Period("2020, 11, 29".toLocalDate, "2020, 12, 12".toLocalDate),
          Period("2020, 11, 29".toLocalDate, "2020, 11, 30".toLocalDate)
        )
      ))
      .withPayMethod(PayMethod.Variable)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withAnnualPayAmount(34000.00)
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withClaimPeriodEnd("2020, 11, 30")
      .withPayDate(List("2020-10-31", "2020-11-14", "2020-11-28", "2020-12-12"))
      .withUsualHours(List(
        UsualHours("2020, 11, 14".toLocalDate, Hours(40.0)),
        UsualHours("2020, 11, 28".toLocalDate, Hours(50.0)),
        UsualHours("2020, 12, 12".toLocalDate, Hours(50.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 14".toLocalDate, Hours(14.0)),
        PartTimeHours("2020, 11, 28".toLocalDate, Hours(15.0)),
        PartTimeHours("2020, 12, 12".toLocalDate, Hours(15.0))
      ))
      -> 1032.71,
    emptyUserAnswers
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withEmployeeStartDate("2020-03-20")
      .withOnPayrollBefore30thOct2020(true)
      .withFurloughEndDate("2020, 11, 25")
      .withPaymentFrequency(FortNightly)
      .withEmployeeStartedAfter1Feb2019()
      .withClaimPeriodStart("2020, 11, 1")
      .withFurloughInLastTaxYear(false)
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withPartTimePeriods(List(
        PartialPeriod(
          Period("2020, 11, 01".toLocalDate, "2020, 11, 14".toLocalDate),
          Period("2020, 11, 5".toLocalDate, "2020, 11, 14".toLocalDate)
        ),
        PartialPeriod(
          Period("2020, 11, 15".toLocalDate, "2020, 11, 28".toLocalDate),
          Period("2020, 11, 15".toLocalDate, "2020, 11, 25".toLocalDate)
        )
      ))
      .withPayMethod(PayMethod.Variable)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withAnnualPayAmount(19564.40)
      .withFurloughStartDate("2020, 11, 5")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withClaimPeriodEnd("2020, 11, 29")
      .withPayDate(List("2020-10-31", "2020-11-14", "2020-11-28"))
      .withUsualHours(List(
        UsualHours("2020, 11, 14".toLocalDate, Hours(40.0)),
        UsualHours("2020, 11, 28".toLocalDate, Hours(50.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 14".toLocalDate, Hours(14.0)),
        PartTimeHours("2020, 11, 28".toLocalDate, Hours(15.0))
      ))
      -> 1043.42,
    emptyUserAnswers
      .withRtiSubmission(EmployeeRTISubmission.Yes)
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withEmployeeStartDate("2020-03-19")
      .withOnPayrollBefore30thOct2020(true)
      .withFurloughEndDate("2020, 11, 25")
      .withPaymentFrequency(FortNightly)
      .withEmployeeStartedAfter1Feb2019()
      .withClaimPeriodStart("2020, 11, 1")
      .withFurloughInLastTaxYear(false)
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withPartTimePeriods(List(
        PartialPeriod(
          Period("2020, 11, 01".toLocalDate, "2020, 11, 14".toLocalDate),
          Period("2020, 11, 5".toLocalDate, "2020, 11, 14".toLocalDate)
        ),
        PartialPeriod(
          Period("2020, 11, 15".toLocalDate, "2020, 11, 28".toLocalDate),
          Period("2020, 11, 15".toLocalDate, "2020, 11, 25".toLocalDate)
        )
      ))
      .withPayMethod(PayMethod.Variable)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withAnnualPayAmount(1000.00)
      .withFurloughStartDate("2020, 11, 5")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withClaimPeriodEnd("2020, 11, 29")
      .withPayDate(List("2020-10-31", "2020-11-14", "2020-11-28"))
      .withUsualHours(List(
        UsualHours("2020, 11, 14".toLocalDate, Hours(40.0)),
        UsualHours("2020, 11, 28".toLocalDate, Hours(50.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 14".toLocalDate, Hours(14.0)),
        PartTimeHours("2020, 11, 28".toLocalDate, Hours(15.0))
      ))
      -> 631.16,
    emptyUserAnswers
      .withRtiSubmission(EmployeeRTISubmission.No)
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withEmployeeStartDate("2020-03-19")
      .withOnPayrollBefore30thOct2020(true)
      .withFurloughEndDate("2020, 11, 25")
      .withPaymentFrequency(FortNightly)
      .withEmployeeStartedAfter1Feb2019()
      .withClaimPeriodStart("2020, 11, 1")
      .withFurloughInLastTaxYear(false)
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withPartTimePeriods(List(
        PartialPeriod(
          Period("2020, 11, 01".toLocalDate, "2020, 11, 14".toLocalDate),
          Period("2020, 11, 5".toLocalDate, "2020, 11, 14".toLocalDate)
        ),
        PartialPeriod(
          Period("2020, 11, 15".toLocalDate, "2020, 11, 28".toLocalDate),
          Period("2020, 11, 15".toLocalDate, "2020, 11, 25".toLocalDate)
        )
      ))
      .withPayMethod(PayMethod.Variable)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withAnnualPayAmount(1000.00)
      .withFurloughStartDate("2020, 11, 5")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withClaimPeriodEnd("2020, 11, 29")
      .withPayDate(List("2020-10-31", "2020-11-14", "2020-11-28"))
      .withUsualHours(List(
        UsualHours("2020, 11, 14".toLocalDate, Hours(40.0)),
        UsualHours("2020, 11, 28".toLocalDate, Hours(50.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 14".toLocalDate, Hours(14.0)),
        PartTimeHours("2020, 11, 28".toLocalDate, Hours(15.0))
      ))
      -> 53.28,
    emptyUserAnswers
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withFurloughEndDate("2020, 11, 28")
      .withPaymentFrequency(FortNightly)
      .withEmployeeStartedOnOrBefore1Feb2019()
      .withClaimPeriodStart("2020, 11, 1")
      .withFurloughInLastTaxYear(false)
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withLastYear(List("2019-11-16" -> 840, "2019-11-30" -> 980))
      .withPayMethod(PayMethod.Variable)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
      .withAnnualPayAmount(28000.00)
      .withFurloughStartDate("2020, 11, 4")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withClaimPeriodEnd("2020, 11, 28")
      .withPayDate(List("2020-10-31", "2020-11-14", "2020-11-28"))
      -> 1530.00
  ))

  val novemberVariableMonthlyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq("November Variable Monthly Scenarios" -> Seq(
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughEndDate("2020, 11, 30")
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withPaymentFrequency(Monthly)
      .withPayMethod(PayMethod.Variable)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-31", "2020-11-30"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withAnnualPayAmount(10000.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        FullPeriod(Period("2020, 11, 1".toLocalDate, "2020, 11, 30".toLocalDate))
      ))
      .withUsualHours(List(
        UsualHours("2020, 11, 30".toLocalDate, Hours(40.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 30".toLocalDate, Hours(14.0))
      ))
      .withEmployeeStartDate("2020-08-01")
      .withEmployeeStartedAfter1Feb2019()
      .withOnPayrollBefore30thOct2020(true)
      .withFurloughInLastTaxYear(false)
      -> 1625.00,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughEndDate("2020, 11, 30")
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withPaymentFrequency(Monthly)
      .withPayMethod(PayMethod.Variable)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-31", "2020-11-30"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withAnnualPayAmount(10000.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        FullPeriod(Period("2020, 11, 1".toLocalDate, "2020, 11, 30".toLocalDate))
      ))
      .withUsualHours(List(
        UsualHours("2020, 11, 30".toLocalDate, Hours(40.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 30".toLocalDate, Hours(14.0))
      ))
      .withEmployeeStartDate("2020-04-01")
      .withOnPayrollBefore30thOct2020(true)
      .withEmployeeStartedAfter1Feb2019()
      .withFurloughInLastTaxYear(false)
      -> 746.46,
    emptyUserAnswers
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withFurloughEndDate("2020, 11, 30")
      .withPaymentFrequency(Monthly)
      .withEmployeeStartedOnOrBefore1Feb2019()
      .withClaimPeriodStart("2020, 11, 1")
      .withLastYear(List("2019-11-30" -> 2000))
      .withFurloughInLastTaxYear(false)
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withPayMethod(PayMethod.Variable)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
      .withAnnualPayAmount(26000.00)
      .withFurloughStartDate("2020, 3, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withClaimPeriodEnd("2020, 11, 30")
      .withPayDate(List("2020-10-31", "2020-11-30"))
      -> 1890.96,
    emptyUserAnswers
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withEmployeeStartDate("2019-12-01")
      .withFurloughEndDate("2020, 11, 30")
      .withPaymentFrequency(Monthly)
      .withEmployeeStartedAfter1Feb2019()
      .withOnPayrollBefore30thOct2020(true)
      .withClaimPeriodStart("2020, 11, 1")
      .withFurloughInLastTaxYear(false)
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withPayMethod(PayMethod.Variable)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
      .withAnnualPayAmount(12000.00)
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withClaimPeriodEnd("2020, 11, 30")
      .withPayDate(List("2020-10-31", "2020-11-30"))
      -> 2267.76
  ))

  val novemberVariableFourWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq("November Variable Four Weekly Scenarios" -> Seq(
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughEndDate("2020, 11, 28")
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withPaymentFrequency(FourWeekly)
      .withPayMethod(PayMethod.Variable)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-04", "2020-11-01", "2020-11-29"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withAnnualPayAmount(1500.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        PartialPeriod(Period("2020, 10, 5".toLocalDate, "2020, 11, 1".toLocalDate),
          Period("2020, 11, 1".toLocalDate, "2020, 11, 1".toLocalDate)),
        PartialPeriod(Period("2020, 11, 2".toLocalDate, "2020, 11, 29".toLocalDate),
          Period("2020, 11, 2".toLocalDate, "2020, 11, 28".toLocalDate)),
      ))
      .withUsualHours(List(
        UsualHours("2020, 11, 1".toLocalDate, Hours(40.0)),
        UsualHours("2020, 11, 29".toLocalDate, Hours(50.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 1".toLocalDate, Hours(14.0)),
        PartTimeHours("2020, 11, 29".toLocalDate, Hours(15.0))
      ))
      .withEmployeeStartDate("2020-10-29")
      .withEmployeeStartedAfter1Feb2019()
      .withOnPayrollBefore30thOct2020(true)
      .withFurloughInLastTaxYear(false)
      -> 1629.30,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 28")
      .withFurloughStartDate("2020, 11, 4")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughEndDate("2020, 11, 28")
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withPaymentFrequency(FourWeekly)
      .withPayMethod(PayMethod.Variable)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-31", "2020-11-28"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withAnnualPayAmount(36000)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
      .withLastYear(List("2019-11-30" -> 2800))
      .withEmployeeStartedOnOrBefore1Feb2019()
      .withFurloughInLastTaxYear(false)
      -> 2000.00
  ))

  val novemberWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq("November Fixed Weekly Scenarios" -> Seq(
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughStatus(FurloughStatus.FurloughOngoing)
      .withPaymentFrequency(Weekly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-31", "2020-11-07", "2020-11-14", "2020-11-21", "2020-11-28", "2020-12-05"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(600.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        FullPeriod(Period("2020, 11, 1".toLocalDate, "2020, 11, 7".toLocalDate)),
        FullPeriod(Period("2020, 11, 8".toLocalDate, "2020, 11, 14".toLocalDate)),
        FullPeriod(Period("2020, 11, 15".toLocalDate, "2020, 11, 21".toLocalDate)),
        FullPeriod(Period("2020, 11, 22".toLocalDate, "2020, 11, 28".toLocalDate)),
        PartialPeriod(Period("2020, 11, 29".toLocalDate, "2020, 12, 05".toLocalDate),
          Period("2020, 11, 29".toLocalDate, "2020, 11, 30".toLocalDate)),
      ))
      .withUsualHours(List(
        UsualHours("2020, 11, 7".toLocalDate, Hours(37.0)),
        UsualHours("2020, 11, 14".toLocalDate, Hours(37.0)),
        UsualHours("2020, 11, 21".toLocalDate, Hours(37.0)),
        UsualHours("2020, 11, 28".toLocalDate, Hours(37.0)),
        UsualHours("2020, 12, 5".toLocalDate, Hours(15.86))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 7".toLocalDate, Hours(10.0)),
        PartTimeHours("2020, 11, 14".toLocalDate, Hours(12.0)),
        PartTimeHours("2020, 11, 21".toLocalDate, Hours(10.0)),
        PartTimeHours("2020, 11, 28".toLocalDate, Hours(15.0)),
        PartTimeHours("2020, 12, 5".toLocalDate, Hours(1.06)),
      ))
      -> 1438.26,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 2")
      .withClaimPeriodEnd("2020, 11, 29")
      .withFurloughStartDate("2020, 11, 6")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughStatus(FurloughStatus.FurloughOngoing)
      .withPaymentFrequency(Weekly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-11-03", "2020-11-10", "2020-11-17", "2020-11-24", "2020-12-01"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(1200.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        FullPeriod(Period("2020, 11, 11".toLocalDate, "2020, 11, 17".toLocalDate)),
        FullPeriod(Period("2020, 11, 18".toLocalDate, "2020, 11, 24".toLocalDate)),
        PartialPeriod(Period("2020, 11, 4".toLocalDate, "2020, 11, 10".toLocalDate),
          Period("2020, 11, 6".toLocalDate, "2020, 11, 10".toLocalDate)),
        PartialPeriod(Period("2020, 11, 25".toLocalDate, "2020, 12, 1".toLocalDate),
          Period("2020, 11, 25".toLocalDate, "2020, 11, 29".toLocalDate)),
      ))
      .withUsualHours(List(
        UsualHours("2020, 11, 10".toLocalDate, Hours(37.0)),
        UsualHours("2020, 11, 17".toLocalDate, Hours(37.0)),
        UsualHours("2020, 11, 24".toLocalDate, Hours(37.0)),
        UsualHours("2020, 12, 1".toLocalDate, Hours(37.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 10".toLocalDate, Hours(10.0)),
        PartTimeHours("2020, 11, 17".toLocalDate, Hours(12.0)),
        PartTimeHours("2020, 11, 24".toLocalDate, Hours(10.0)),
        PartTimeHours("2020, 12, 1".toLocalDate, Hours(15.0)),
      ))
      -> 1362.66,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 2")
      .withClaimPeriodEnd("2020, 11, 29")
      .withFurloughStartDate("2020, 11, 6")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughEndDate("2020, 11, 27")
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withPaymentFrequency(Weekly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-11-03", "2020-11-10", "2020-11-17", "2020-11-24", "2020-12-01"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(1200.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        PartialPeriod(Period("2020, 11, 4".toLocalDate, "2020, 11, 10".toLocalDate),
          Period("2020, 11, 6".toLocalDate, "2020, 11, 10".toLocalDate)),
        PartialPeriod(Period("2020, 11, 25".toLocalDate, "2020, 12, 1".toLocalDate),
          Period("2020, 11, 25".toLocalDate, "2020, 11, 27".toLocalDate)),
      ))
      .withUsualHours(List(
        UsualHours("2020, 11, 10".toLocalDate, Hours(37.0)),
        UsualHours("2020, 12, 1".toLocalDate, Hours(15.86))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 10".toLocalDate, Hours(10.0)),
        PartTimeHours("2020, 12, 1".toLocalDate, Hours(1.06)),
      ))
      -> 1691.23,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 15")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughEndDate("2020, 11, 27")
      .withFurloughStatus(FurloughStatus.FurloughOngoing)
      .withPaymentFrequency(Weekly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-11-14", "2020-11-21", "2020-11-28", "2020-12-05"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(600.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        FullPeriod(Period("2020, 11, 15".toLocalDate, "2020, 11, 21".toLocalDate)),
        FullPeriod(Period("2020, 11, 22".toLocalDate, "2020, 11, 28".toLocalDate)),
        PartialPeriod(Period("2020, 11, 29".toLocalDate, "2020, 12, 5".toLocalDate),
          Period("2020, 11, 29".toLocalDate, "2020, 11, 30".toLocalDate))
      ))
      .withUsualHours(List(
        UsualHours("2020, 11, 21".toLocalDate, Hours(37.0)),
        UsualHours("2020, 11, 28".toLocalDate, Hours(37.0)),
        UsualHours("2020, 12, 5".toLocalDate, Hours(15.86))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 21".toLocalDate, Hours(10.0)),
        PartTimeHours("2020, 11, 28".toLocalDate, Hours(15.0)),
        PartTimeHours("2020, 12, 5".toLocalDate, Hours(1.06)),
      ))
      -> 763.66,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 29")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 29")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughStatus(FurloughStatus.FurloughOngoing)
      .withPaymentFrequency(Weekly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-11-28", "2020-12-05"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(550.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        PartialPeriod(Period("2020, 11, 29".toLocalDate, "2020, 12, 5".toLocalDate),
          Period("2020, 11, 29".toLocalDate, "2020, 11, 30".toLocalDate))
      ))
      .withUsualHours(List(
        UsualHours("2020, 12, 5".toLocalDate, Hours(40.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 12, 5".toLocalDate, Hours(14.0))
      ))
      -> 81.71,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 21")
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughEndDate("2020, 11, 8")
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withPaymentFrequency(Weekly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-31", "2020-11-07", "2020-11-14"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(600.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        FullPeriod(Period("2020, 11, 1".toLocalDate, "2020, 11, 7".toLocalDate)),
        PartialPeriod(Period("2020, 11, 8".toLocalDate, "2020, 11, 14".toLocalDate),
          Period("2020, 11, 8".toLocalDate, "2020, 11, 8".toLocalDate))
      ))
      .withUsualHours(List(
        UsualHours("2020, 11, 7".toLocalDate, Hours(37.0)),
        UsualHours("2020, 11, 14".toLocalDate, Hours(37.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 7".toLocalDate, Hours(10.0)),
        PartTimeHours("2020, 11, 14".toLocalDate, Hours(12.0))
      ))
      -> 396.60
  ))

  val novemberTwoWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq("November Fixed Two Weekly Scenarios" -> Seq(
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughStatus(FurloughStatus.FurloughOngoing)
      .withPaymentFrequency(FortNightly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-31", "2020-11-14", "2020-11-28", "2020-12-12"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(650.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        FullPeriod(Period("2020, 11, 1".toLocalDate, "2020, 11, 14".toLocalDate)),
        FullPeriod(Period("2020, 11, 15".toLocalDate, "2020, 11, 28".toLocalDate)),
        PartialPeriod(Period("2020, 11, 29".toLocalDate, "2020, 12, 12".toLocalDate),
          Period("2020, 11, 29".toLocalDate, "2020, 11, 30".toLocalDate)),
      ))
      .withUsualHours(List(
        UsualHours("2020, 11, 14".toLocalDate, Hours(98.0)),
        UsualHours("2020, 11, 28".toLocalDate, Hours(98.0)),
        UsualHours("2020, 12, 12".toLocalDate, Hours(21.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 14".toLocalDate, Hours(48.0)),
        PartTimeHours("2020, 11, 28".toLocalDate, Hours(48.0)),
        PartTimeHours("2020, 12, 12".toLocalDate, Hours(6.0))
      ))
      -> 583.66,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughStatus(FurloughStatus.FurloughOngoing)
      .withPaymentFrequency(FortNightly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-31", "2020-11-14", "2020-11-28", "2020-12-12"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(2300.12)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        FullPeriod(Period("2020, 11, 1".toLocalDate, "2020, 11, 14".toLocalDate)),
        FullPeriod(Period("2020, 11, 15".toLocalDate, "2020, 11, 28".toLocalDate)),
        PartialPeriod(Period("2020, 11, 29".toLocalDate, "2020, 12, 12".toLocalDate),
          Period("2020, 11, 29".toLocalDate, "2020, 11, 30".toLocalDate)),
      ))
      .withUsualHours(List(
        UsualHours("2020, 11, 14".toLocalDate, Hours(98.0)),
        UsualHours("2020, 11, 28".toLocalDate, Hours(98.0)),
        UsualHours("2020, 12, 12".toLocalDate, Hours(21.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 14".toLocalDate, Hours(48.0)),
        PartTimeHours("2020, 11, 28".toLocalDate, Hours(48.0)),
        PartTimeHours("2020, 12, 12".toLocalDate, Hours(6.0))
      ))
      -> 1296.44,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 13")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughStatus(FurloughStatus.FurloughOngoing)
      .withPaymentFrequency(FortNightly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-31", "2020-11-14", "2020-11-28", "2020-12-12"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(650.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        PartialPeriod(Period("2020, 11, 29".toLocalDate, "2020, 12, 12".toLocalDate),
          Period("2020, 11, 29".toLocalDate, "2020, 11, 30".toLocalDate)),
      ))
      .withUsualHours(List(
        UsualHours("2020, 12, 12".toLocalDate, Hours(21.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 12, 12".toLocalDate, Hours(6.0))
      ))
      -> 647.35,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 03")
      .withClaimPeriodEnd("2020, 11, 11")
      .withFurloughStartDate("2020, 3, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughEndDate("2020, 11, 11")
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withPaymentFrequency(FortNightly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-28", "2020-11-11"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(464.28)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        PartialPeriod(Period("2020, 10, 29".toLocalDate, "2020, 11, 11".toLocalDate),
          Period("2020, 11, 03".toLocalDate, "2020, 11, 11".toLocalDate)),
      ))
      .withUsualHours(List(
        UsualHours("2020, 11, 11".toLocalDate, Hours(70.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 11".toLocalDate, Hours(43.0))
      ))
      -> 92.10,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 27")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 29")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughEndDate("2020, 11, 11")
      .withFurloughStatus(FurloughStatus.FurloughOngoing)
      .withPaymentFrequency(FortNightly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-11-27", "2020-12-11"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(789.12)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        PartialPeriod(Period("2020, 11, 28".toLocalDate, "2020, 12, 11".toLocalDate),
          Period("2020, 11, 29".toLocalDate, "2020, 11, 30".toLocalDate)),
      ))
      .withUsualHours(List(
        UsualHours("2020, 12, 11".toLocalDate, Hours(140.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 12, 11".toLocalDate, Hours(50.0))
      ))
      -> 57.98,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 29")
      .withFurloughStartDate("2020, 11, 5")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughEndDate("2020, 11, 25")
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withPaymentFrequency(FortNightly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-31", "2020-11-14", "2020-11-28"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(643.12)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        PartialPeriod(Period("2020, 11, 1".toLocalDate, "2020, 11, 14".toLocalDate),
          Period("2020, 11, 05".toLocalDate, "2020, 11, 14".toLocalDate)),
        PartialPeriod(Period("2020, 11, 15".toLocalDate, "2020, 11, 28".toLocalDate),
          Period("2020, 11, 15".toLocalDate, "2020, 11, 25".toLocalDate)),
      ))
      .withUsualHours(List(
        UsualHours("2020, 11, 14".toLocalDate, Hours(63.0)),
        UsualHours("2020, 11, 28".toLocalDate, Hours(77.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 14".toLocalDate, Hours(8.0)),
        PartTimeHours("2020, 11, 28".toLocalDate, Hours(12.0))
      ))
      -> 662.08
  ))

  val novemberMonthlyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq("November Fixed Monthly Scenarios" -> Seq(
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughStatus(FurloughStatus.FurloughOngoing)
      .withPaymentFrequency(Monthly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.No)
      .withPayDate(List("2020-10-31", "2020-11-30"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(2400.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(FullPeriod(Period("2020, 11, 1".toLocalDate, "2020, 11, 30".toLocalDate))))
      .withUsualHours(List(UsualHours("2020, 11, 30".toLocalDate, Hours(160.0))))
      .withPartTimeHours(List(PartTimeHours("2020, 11, 30".toLocalDate, Hours(40.0))))
      -> 1440.00,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughStatus(FurloughStatus.FurloughOngoing)
      .withPaymentFrequency(Monthly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.No)
      .withPayDate(List("2020-10-31", "2020-11-30"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(3126.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
      -> 2500.00,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 05")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughEndDate("2020, 11, 21")
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withPaymentFrequency(Monthly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.No)
      .withPayDate(List("2020-10-31", "2020-11-30"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(2400.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(PartialPeriod(
        Period("2020, 11, 1".toLocalDate, "2020, 11, 30".toLocalDate),
        Period("2020, 11, 05".toLocalDate, "2020, 11, 21".toLocalDate)
      )))
      .withUsualHours(List(UsualHours("2020, 11, 30".toLocalDate, Hours(127.50))))
      .withPartTimeHours(List(PartTimeHours("2020, 11, 30".toLocalDate, Hours(52.50))))
      -> 640.00,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 05")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughEndDate("2020, 11, 21")
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withPaymentFrequency(Monthly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.No)
      .withPayDate(List("2020-10-31", "2020-11-30"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(6500.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(PartialPeriod(
        Period("2020, 11, 1".toLocalDate, "2020, 11, 30".toLocalDate),
        Period("2020, 11, 05".toLocalDate, "2020, 11, 21".toLocalDate)
      )))
      .withUsualHours(List(UsualHours("2020, 11, 30".toLocalDate, Hours(127.50))))
      .withPartTimeHours(List(PartTimeHours("2020, 11, 30".toLocalDate, Hours(52.50))))
      -> 833.40,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 02")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughEndDate("2020, 11, 11")
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withPaymentFrequency(Monthly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.No)
      .withPayDate(List("2020-10-31", "2020-11-30"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(2400.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(PartialPeriod(
        Period("2020, 11, 1".toLocalDate, "2020, 11, 30".toLocalDate),
        Period("2020, 11, 02".toLocalDate, "2020, 11, 11".toLocalDate)
      )))
      .withUsualHours(List(UsualHours("2020, 11, 30".toLocalDate, Hours(160.0))))
      .withPartTimeHours(List(PartTimeHours("2020, 11, 30".toLocalDate, Hours(40.00))))
      -> 480.00,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 02")
      .withClaimPeriodEnd("2020, 11, 20")
      .withFurloughStartDate("2020, 11, 01")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughEndDate("2020, 11, 20")
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withPaymentFrequency(Monthly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.No)
      .withPayDate(List("2020-10-31", "2020-11-30"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(5555.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(PartialPeriod(
        Period("2020, 11, 1".toLocalDate, "2020, 11, 30".toLocalDate),
        Period("2020, 11, 02".toLocalDate, "2020, 11, 20".toLocalDate)
      )))
      .withUsualHours(List(UsualHours("2020, 11, 30".toLocalDate, Hours(160.0))))
      .withPartTimeHours(List(PartTimeHours("2020, 11, 30".toLocalDate, Hours(40.00))))
      -> 1187.60,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 01")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 03, 01")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughEndDate("2020, 11, 20")
      .withFurloughStatus(FurloughStatus.FurloughOngoing)
      .withPaymentFrequency(Monthly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.No)
      .withPayDate(List("2020-10-25", "2020-11-25", "2020-12-25"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(4900.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(PartialPeriod(
        Period("2020, 11, 26".toLocalDate, "2020, 12, 25".toLocalDate),
        Period("2020, 11, 26".toLocalDate, "2020, 11, 30".toLocalDate)
      )))
      .withUsualHours(List(
        UsualHours("2020, 11, 25".toLocalDate, Hours(160.0)),
        UsualHours("2020, 12, 25".toLocalDate, Hours(160.0))
      ))
      .withPartTimeHours(List(
        PartTimeHours("2020, 11, 25".toLocalDate, Hours(95.00)),
        PartTimeHours("2020, 12, 25".toLocalDate, Hours(95.00))
      ))
      -> 1015.70,
  ))

  val novemberFourWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq("November Fixed Four Weekly Scenarios" -> Seq(
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withFurloughEndDate("2020, 11, 28")
      .withPaymentFrequency(FourWeekly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-31", "2020-11-28"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(2000.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(FullPeriod(Period("2020, 11, 1".toLocalDate, "2020, 11, 28".toLocalDate))))
      .withUsualHours(List(UsualHours("2020, 11, 28".toLocalDate, Hours(148.0))))
      .withPartTimeHours(List(PartTimeHours("2020, 11, 28".toLocalDate, Hours(40.0))))
      -> 1167.57,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withFurloughEndDate("2020, 11, 28")
      .withPaymentFrequency(FourWeekly)
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-04", "2020-11-01", "2020-11-29"))
      .withRegularPayAmount(3300.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
      -> 2333.52,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 28")
      .withFurloughStartDate("2020, 11, 1")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughStatus(FurloughStatus.FurloughOngoing)
      .withPaymentFrequency(FourWeekly)
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-31", "2020-11-28"))
      .withRegularPayAmount(3300.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
      -> 2307.68,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 03, 01")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withFurloughEndDate("2020, 11, 30")
      .withPaymentFrequency(FourWeekly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-31", "2020-11-28", "2020-12-26"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(3500.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(FullPeriod(Period("2020, 11, 1".toLocalDate, "2020, 11, 28".toLocalDate)),
        PartialPeriod(
          Period("2020, 11, 29".toLocalDate, "2020, 12, 26".toLocalDate),
          Period("2020, 11, 29".toLocalDate, "2020, 11, 30".toLocalDate))))
      .withUsualHours(List(UsualHours("2020, 11, 28".toLocalDate, Hours(148.0)),
        UsualHours("2020, 12, 26".toLocalDate, Hours(15.86))))
      .withPartTimeHours(List(PartTimeHours("2020, 11, 28".toLocalDate, Hours(40.0)),
        PartTimeHours("2020, 12, 26".toLocalDate, Hours(1.86))))
      -> 1831.11,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 30")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 29")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughStatus(FurloughStatus.FurloughOngoing)
      .withPaymentFrequency(FourWeekly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-11-29", "2020-12-27"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(2200.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        PartialPeriod(
          Period("2020, 11, 30".toLocalDate, "2020, 12, 27".toLocalDate),
          Period("2020, 11, 30".toLocalDate, "2020, 11, 30".toLocalDate))))
      .withUsualHours(List(UsualHours("2020, 12, 27".toLocalDate, Hours(148.0))))
      .withPartTimeHours(List(PartTimeHours("2020, 12, 27".toLocalDate, Hours(25.0))))
      -> 52.24,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 30")
      .withFurloughStartDate("2020, 11, 01")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughStatus(FurloughStatus.FurloughEnded)
      .withFurloughEndDate("2020, 11, 30")
      .withPaymentFrequency(FourWeekly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-31", "2020-11-28", "2020-12-26"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(3500.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        PartialPeriod(
          Period("2020, 11, 29".toLocalDate, "2020, 12, 26".toLocalDate),
          Period("2020, 11, 29".toLocalDate, "2020, 11, 30".toLocalDate))))
      .withUsualHours(List(UsualHours("2020, 12, 26".toLocalDate, Hours(15.86))))
      .withPartTimeHours(List(PartTimeHours("2020, 12, 26".toLocalDate, Hours(1.86))))
      -> 2454.81,
    emptyUserAnswers
      .withClaimPeriodStart("2020, 11, 1")
      .withClaimPeriodEnd("2020, 11, 29")
      .withFurloughStartDate("2020, 11, 01")
      .withPreviousFurloughedPeriodsAnswer(false)
      .withFurloughStatus(FurloughStatus.FurloughOngoing)
      .withPaymentFrequency(FourWeekly)
      .withPayMethod(PayMethod.Regular)
      .withRegularLengthEmployed(RegularLengthEmployed.Yes)
      .withPayDate(List("2020-10-31", "2020-11-28", "2020-12-26"))
      .withPayPeriodsList(PayPeriodsList.Yes)
      .withRegularPayAmount(3500.00)
      .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
      .withPartTimePeriods(List(
        PartialPeriod(
          Period("2020, 11, 29".toLocalDate, "2020, 12, 26".toLocalDate),
          Period("2020, 11, 29".toLocalDate, "2020, 11, 29".toLocalDate))))
      .withUsualHours(List(UsualHours("2020, 12, 26".toLocalDate, Hours(15.86))))
      .withPartTimeHours(List(PartTimeHours("2020, 12, 26".toLocalDate, Hours(1.86))))
      -> 2381.25
  ))
}
