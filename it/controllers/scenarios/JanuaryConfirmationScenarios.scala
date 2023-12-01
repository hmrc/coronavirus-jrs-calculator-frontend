package controllers.scenarios

import assets.BaseITConstants
import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}
import models.{EmployeeRTISubmission, FullPeriod, FurloughStatus, Hours, PartTimeHours, PartTimeQuestion, PartialPeriod, PayMethod, PayPeriodsList, Period, RegularLengthEmployed, UserAnswers, UsualHours}
import utils.{CreateRequestHelper, CustomMatchers, ITCoreTestData, IntegrationSpecBase}

object JanuaryConfirmationScenarios
    extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants with ITCoreTestData {

  val januaryFourWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "January Fixed Four Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-28")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(List(FullPeriod(Period("2021-01-01".toLocalDate, "2021-01-28".toLocalDate))))
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2000)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-31", "2021-01-28"))
        .withUsualHours(List(UsualHours("2021-01-28".toLocalDate, Hours(148.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-28".toLocalDate, Hours(40.0))))
        -> 1167.57,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-28")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withRegularPayAmount(3300)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-04", "2021-01-01", "2021-01-29"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 2258.20,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withRegularPayAmount(3300)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-31", "2021-01-28"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 2307.68,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-31")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-01-01".toLocalDate, "2021-01-28".toLocalDate)),
            PartialPeriod(
              Period("2021-01-29".toLocalDate, "2021-02-25".toLocalDate),
              Period("2021-01-29".toLocalDate, "2021-01-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(3500)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-31", "2021-01-28", "2021-02-25"))
        .withUsualHours(List(UsualHours("2021-01-28".toLocalDate, Hours(148.0)), UsualHours("2021-02-25".toLocalDate, Hours(15.86))))
        .withPartTimeHours(List(PartTimeHours("2021-01-28".toLocalDate, Hours(40.0)), PartTimeHours("2021-02-25".toLocalDate, Hours(1.86))))
        -> 1897.56,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-01-31")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-31".toLocalDate, "2021-02-27".toLocalDate),
              Period("2021-01-31".toLocalDate, "2021-01-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2200)
        .withFurloughStartDate("2021-01-29")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-30", "2021-02-27"))
        .withUsualHours(List(UsualHours("2021-02-27".toLocalDate, Hours(148.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-27".toLocalDate, Hours(25.0))))
        -> 52.24,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-31")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-29".toLocalDate, "2021-02-25".toLocalDate),
              Period("2021-01-29".toLocalDate, "2021-01-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(3500)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-31", "2021-01-28", "2021-02-25"))
        .withUsualHours(List(UsualHours("2021-02-25".toLocalDate, Hours(15.86))))
        .withPartTimeHours(List(PartTimeHours("2021-02-25".toLocalDate, Hours(1.86))))
        -> 2521.26,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-29".toLocalDate, "2021-02-25".toLocalDate),
              Period("2021-01-29".toLocalDate, "2021-01-29".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(3500)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-29")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-31", "2021-01-28", "2021-02-25"))
        .withUsualHours(List(UsualHours("2021-02-25".toLocalDate, Hours(15.86))))
        .withPartTimeHours(List(PartTimeHours("2021-02-25".toLocalDate, Hours(1.86))))
        -> 2378.87,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-06".toLocalDate, "2021-01-02".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2654.11)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-05", "2021-01-02"))
        .withUsualHours(List(UsualHours("2021-01-02".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-02".toLocalDate, Hours(11.4))))
        -> 101.55,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-08".toLocalDate, "2021-01-04".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(3200.11)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-07", "2021-01-04"))
        .withUsualHours(List(UsualHours("2021-01-04".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-04".toLocalDate, Hours(11.4))))
        -> 108.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withClaimPeriodStart("2021-01-01")
        .withClaimPeriodEnd("2021-01-02")
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withFurloughEndDate("2021-01-01")
        .withPaymentFrequency(FourWeekly)
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-10".toLocalDate, "2021-01-06".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2322.11)
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-09", "2021-01-06"))
        .withUsualHours(List(UsualHours("2021-01-06".toLocalDate, Hours(10.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-06".toLocalDate, Hours(6.5))))
        -> 23.22,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-02")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-05".toLocalDate, "2021-01-01".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-01".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-01-02".toLocalDate, "2021-01-29".toLocalDate),
              Period("2021-01-02".toLocalDate, "2021-01-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1465.55)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-04", "2021-01-01", "2021-01-29"))
        .withUsualHours(List(UsualHours("2021-01-01".toLocalDate, Hours(7.5)), UsualHours("2021-01-29".toLocalDate, Hours(7.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-01".toLocalDate, Hours(3.0)), PartTimeHours("2021-01-29".toLocalDate, Hours(3.0))))
        -> 50.24
    )
  )
  val januaryMonthlyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "January Fixed Monthly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(List(FullPeriod(Period("2021-01-01".toLocalDate, "2021-01-31".toLocalDate))))
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2400)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2020-12-31", "2021-01-31"))
        .withUsualHours(List(UsualHours("2021-01-31".toLocalDate, Hours(160.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-31".toLocalDate, Hours(40.0))))
        -> 1440.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withRegularPayAmount(3126)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2020-12-31", "2021-01-31"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 2500.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-21")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-01".toLocalDate, "2021-01-31".toLocalDate),
              Period("2021-01-05".toLocalDate, "2021-01-21".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2400)
        .withFurloughStartDate("2021-01-05")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2020-12-31", "2021-01-31"))
        .withUsualHours(List(UsualHours("2021-01-31".toLocalDate, Hours(127.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-31".toLocalDate, Hours(52.5))))
        -> 619.35,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-21")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-01".toLocalDate, "2021-01-31".toLocalDate),
              Period("2021-01-05".toLocalDate, "2021-01-21".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(6500)
        .withFurloughStartDate("2021-01-05")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2020-12-31", "2021-01-31"))
        .withUsualHours(List(UsualHours("2021-01-31".toLocalDate, Hours(127.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-31".toLocalDate, Hours(52.5))))
        -> 806.50,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-11")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-01".toLocalDate, "2021-01-31".toLocalDate),
              Period("2021-01-02".toLocalDate, "2021-01-11".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2400)
        .withFurloughStartDate("2021-01-02")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2020-12-31", "2021-01-31"))
        .withUsualHours(List(UsualHours("2021-01-31".toLocalDate, Hours(160.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-31".toLocalDate, Hours(40.0))))
        -> 464.51,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-20")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-01-02")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-01".toLocalDate, "2021-01-31".toLocalDate),
              Period("2021-01-02".toLocalDate, "2021-01-20".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(5555)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-21")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2020-12-31", "2021-01-31"))
        .withUsualHours(List(UsualHours("2021-01-31".toLocalDate, Hours(160.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-31".toLocalDate, Hours(40.0))))
        -> 1149.26,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-26".toLocalDate, "2021-01-25".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-25".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-01-26".toLocalDate, "2021-02-25".toLocalDate),
              Period("2021-01-26".toLocalDate, "2021-01-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(4900)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2020-12-25", "2021-01-25", "2021-02-25"))
        .withUsualHours(List(UsualHours("2021-01-25".toLocalDate, Hours(160.0)), UsualHours("2021-02-25".toLocalDate, Hours(160.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-25".toLocalDate, Hours(95.0)), PartTimeHours("2021-02-25".toLocalDate, Hours(95.0))))
        -> 1015.68,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-01".toLocalDate, "2021-01-31".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2654.11)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2020-12-31", "2021-01-31"))
        .withUsualHours(List(UsualHours("2021-01-31".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-31".toLocalDate, Hours(11.4))))
        -> 91.72,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-27".toLocalDate, "2021-01-25".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2654.11)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2020-12-26", "2021-01-25"))
        .withUsualHours(List(UsualHours("2021-01-25".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-25".toLocalDate, Hours(11.4))))
        -> 94.78,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-03".toLocalDate, "2021-01-02".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(3200.11)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2020-12-02", "2021-01-02"))
        .withUsualHours(List(UsualHours("2021-01-02".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-02".toLocalDate, Hours(11.4))))
        -> 108.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-01")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-10".toLocalDate, "2021-01-08".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2322.11)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2020-12-09", "2021-01-08"))
        .withUsualHours(List(UsualHours("2021-01-08".toLocalDate, Hours(10.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-08".toLocalDate, Hours(6.5))))
        -> 21.67,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-03".toLocalDate, "2021-01-01".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-01".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-01-02".toLocalDate, "2021-02-01".toLocalDate),
              Period("2021-01-02".toLocalDate, "2021-01-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1465.55)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2020-12-02", "2021-01-01", "2021-02-01"))
        .withUsualHours(List(UsualHours("2021-01-01".toLocalDate, Hours(7.5)), UsualHours("2021-02-01".toLocalDate, Hours(7.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-01".toLocalDate, Hours(3.0)), PartTimeHours("2021-02-01".toLocalDate, Hours(3.0))))
        -> 46.15,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-01".toLocalDate, "2021-01-31".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(3222)
        .withFurloughStartDate("2020-12-30")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-01")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2020-12-31", "2021-01-31"))
        .withUsualHours(List(UsualHours("2021-01-31".toLocalDate, Hours(7.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-31".toLocalDate, Hours(3.0))))
        -> 48.39
    )
  )
  val januaryTwoWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "January Fixed Two Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-01-01".toLocalDate, "2021-01-14".toLocalDate)),
            FullPeriod(Period("2021-01-15".toLocalDate, "2021-01-28".toLocalDate)),
            PartialPeriod(
              Period("2021-01-29".toLocalDate, "2021-02-11".toLocalDate),
              Period("2021-01-29".toLocalDate, "2021-01-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(650)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-31", "2021-01-14", "2021-01-28", "2021-02-11"))
        .withUsualHours(
          List(
            UsualHours("2021-01-14".toLocalDate, Hours(98.0)),
            UsualHours("2021-01-28".toLocalDate, Hours(98.0)),
            UsualHours("2021-02-11".toLocalDate, Hours(21.0))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-01-14".toLocalDate, Hours(48.0)),
            PartTimeHours("2021-01-28".toLocalDate, Hours(48.0)),
            PartTimeHours("2021-02-11".toLocalDate, Hours(6.0))
          )
        )
        -> 610.19,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-01-01".toLocalDate, "2021-01-14".toLocalDate)),
            FullPeriod(Period("2021-01-15".toLocalDate, "2021-01-28".toLocalDate)),
            PartialPeriod(
              Period("2021-01-29".toLocalDate, "2021-02-11".toLocalDate),
              Period("2021-01-29".toLocalDate, "2021-01-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2300.12)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-31", "2021-01-14", "2021-01-28", "2021-02-11"))
        .withUsualHours(
          List(
            UsualHours("2021-01-14".toLocalDate, Hours(98.0)),
            UsualHours("2021-01-28".toLocalDate, Hours(98.0)),
            UsualHours("2021-02-11".toLocalDate, Hours(21.0))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-01-14".toLocalDate, Hours(48.0)),
            PartTimeHours("2021-01-28".toLocalDate, Hours(48.0)),
            PartTimeHours("2021-02-11".toLocalDate, Hours(6.0))
          )
        )
        -> 1350.20,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-01-13")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-29".toLocalDate, "2021-02-11".toLocalDate),
              Period("2021-01-29".toLocalDate, "2021-01-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(650)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-31", "2021-01-14", "2021-01-28", "2021-02-11"))
        .withUsualHours(List(UsualHours("2021-02-11".toLocalDate, Hours(21.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-11".toLocalDate, Hours(6.0))))
        -> 673.88,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-11")
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-01-03")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-29".toLocalDate, "2021-01-11".toLocalDate),
              Period("2021-01-03".toLocalDate, "2021-01-11".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(464.28)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-11")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-28", "2021-01-11"))
        .withUsualHours(List(UsualHours("2021-01-11".toLocalDate, Hours(70.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-11".toLocalDate, Hours(43.0))))
        -> 92.10,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-01-27")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-28".toLocalDate, "2021-02-10".toLocalDate),
              Period("2021-01-29".toLocalDate, "2021-01-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(789.12)
        .withFurloughStartDate("2021-01-29")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-27", "2021-02-10"))
        .withUsualHours(List(UsualHours("2021-02-10".toLocalDate, Hours(140.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-10".toLocalDate, Hours(50.0))))
        -> 86.97,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-25")
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-01".toLocalDate, "2021-01-14".toLocalDate),
              Period("2021-01-05".toLocalDate, "2021-01-14".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-01-15".toLocalDate, "2021-01-28".toLocalDate),
              Period("2021-01-15".toLocalDate, "2021-01-25".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(643.12)
        .withFurloughStartDate("2021-01-05")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-29")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-31", "2021-01-14", "2021-01-28"))
        .withUsualHours(List(UsualHours("2021-01-14".toLocalDate, Hours(63.0)), UsualHours("2021-01-28".toLocalDate, Hours(77.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-14".toLocalDate, Hours(8.0)), PartTimeHours("2021-01-28".toLocalDate, Hours(12.0))))
        -> 662.08,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-19".toLocalDate, "2021-01-01".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-01".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-01-02".toLocalDate, "2021-01-15".toLocalDate),
              Period("2021-01-02".toLocalDate, "2021-01-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1000.34)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-18", "2021-01-01", "2021-01-15"))
        .withUsualHours(List(UsualHours("2021-01-01".toLocalDate, Hours(34.5)), UsualHours("2021-01-15".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-01".toLocalDate, Hours(11.4)), PartTimeHours("2021-01-15".toLocalDate, Hours(11.4))))
        -> 76.54,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-22".toLocalDate, "2021-01-04".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1500)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-21", "2021-01-04"))
        .withUsualHours(List(UsualHours("2021-01-04".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-04".toLocalDate, Hours(11.4))))
        -> 108.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-01")
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-23".toLocalDate, "2021-01-05".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1010.11)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-22", "2021-01-05"))
        .withUsualHours(List(UsualHours("2021-01-05".toLocalDate, Hours(10.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-05".toLocalDate, Hours(6.5))))
        -> 20.20,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-02")
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-19".toLocalDate, "2021-01-01".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-01".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-01-02".toLocalDate, "2021-01-15".toLocalDate),
              Period("2021-01-02".toLocalDate, "2021-01-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1465.55)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-18", "2021-01-01", "2021-01-15"))
        .withUsualHours(List(UsualHours("2021-01-01".toLocalDate, Hours(7.5)), UsualHours("2021-01-15".toLocalDate, Hours(7.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-01".toLocalDate, Hours(3.0)), PartTimeHours("2021-01-15".toLocalDate, Hours(3.0))))
        -> 96.78
    )
  )
  val januaryWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "January Fixed Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-01-01".toLocalDate, "2021-01-07".toLocalDate)),
            FullPeriod(Period("2021-01-08".toLocalDate, "2021-01-14".toLocalDate)),
            FullPeriod(Period("2021-01-15".toLocalDate, "2021-01-21".toLocalDate)),
            FullPeriod(Period("2021-01-22".toLocalDate, "2021-01-28".toLocalDate)),
            PartialPeriod(
              Period("2021-01-29".toLocalDate, "2021-02-04".toLocalDate),
              Period("2021-01-29".toLocalDate, "2021-01-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(600)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-31", "2021-01-07", "2021-01-14", "2021-01-21", "2021-01-28", "2021-02-04"))
        .withUsualHours(
          List(
            UsualHours("2021-01-07".toLocalDate, Hours(37.0)),
            UsualHours("2021-01-14".toLocalDate, Hours(37.0)),
            UsualHours("2021-01-21".toLocalDate, Hours(37.0)),
            UsualHours("2021-01-28".toLocalDate, Hours(37.0)),
            UsualHours("2021-02-04".toLocalDate, Hours(15.86))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-01-07".toLocalDate, Hours(10.0)),
            PartTimeHours("2021-01-14".toLocalDate, Hours(12.0)),
            PartTimeHours("2021-01-21".toLocalDate, Hours(10.0)),
            PartTimeHours("2021-01-28".toLocalDate, Hours(15.0)),
            PartTimeHours("2021-02-04".toLocalDate, Hours(1.06))
          )
        )
        -> 1502.24,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-01-02")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-04".toLocalDate, "2021-01-10".toLocalDate),
              Period("2021-01-06".toLocalDate, "2021-01-10".toLocalDate)
            ),
            FullPeriod(Period("2021-01-11".toLocalDate, "2021-01-17".toLocalDate)),
            FullPeriod(Period("2021-01-18".toLocalDate, "2021-01-24".toLocalDate)),
            PartialPeriod(
              Period("2021-01-25".toLocalDate, "2021-01-31".toLocalDate),
              Period("2021-01-25".toLocalDate, "2021-01-29".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1200)
        .withFurloughStartDate("2021-01-06")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-29")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-03", "2021-01-10", "2021-01-17", "2021-01-24", "2021-01-31"))
        .withUsualHours(
          List(
            UsualHours("2021-01-10".toLocalDate, Hours(37.0)),
            UsualHours("2021-01-17".toLocalDate, Hours(37.0)),
            UsualHours("2021-01-24".toLocalDate, Hours(37.0)),
            UsualHours("2021-01-31".toLocalDate, Hours(37.0))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-01-10".toLocalDate, Hours(10.0)),
            PartTimeHours("2021-01-17".toLocalDate, Hours(12.0)),
            PartTimeHours("2021-01-24".toLocalDate, Hours(10.0)),
            PartTimeHours("2021-01-31".toLocalDate, Hours(15.0))
          )
        )
        -> 1344.84,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-27")
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-01-02")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-04".toLocalDate, "2021-01-10".toLocalDate),
              Period("2021-01-06".toLocalDate, "2021-01-10".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-01-25".toLocalDate, "2021-01-31".toLocalDate),
              Period("2021-01-25".toLocalDate, "2021-01-27".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1200)
        .withFurloughStartDate("2021-01-06")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-27")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-03", "2021-01-10", "2021-01-17", "2021-01-24", "2021-01-31"))
        .withUsualHours(List(UsualHours("2021-01-10".toLocalDate, Hours(37.0)), UsualHours("2021-01-31".toLocalDate, Hours(15.86))))
        .withPartTimeHours(List(PartTimeHours("2021-01-10".toLocalDate, Hours(10.0)), PartTimeHours("2021-01-31".toLocalDate, Hours(1.06))))
        -> 1673.88,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-01-15".toLocalDate, "2021-01-21".toLocalDate)),
            FullPeriod(Period("2021-01-22".toLocalDate, "2021-01-28".toLocalDate)),
            PartialPeriod(
              Period("2021-01-29".toLocalDate, "2021-02-04".toLocalDate),
              Period("2021-01-29".toLocalDate, "2021-01-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(600)
        .withFurloughStartDate("2021-01-15")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-14", "2021-01-21", "2021-01-28", "2021-02-04"))
        .withUsualHours(
          List(
            UsualHours("2021-01-21".toLocalDate, Hours(37.0)),
            UsualHours("2021-01-28".toLocalDate, Hours(37.0)),
            UsualHours("2021-02-04".toLocalDate, Hours(15.86))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-01-21".toLocalDate, Hours(10.0)),
            PartTimeHours("2021-01-28".toLocalDate, Hours(15.0)),
            PartTimeHours("2021-02-04".toLocalDate, Hours(1.06))
          )
        )
        -> 827.64,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-01-29")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-29".toLocalDate, "2021-02-04".toLocalDate),
              Period("2021-01-29".toLocalDate, "2021-01-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(550)
        .withFurloughStartDate("2021-01-29")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-28", "2021-02-04"))
        .withUsualHours(List(UsualHours("2021-02-04".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-04".toLocalDate, Hours(14.0))))
        -> 122.57,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-08")
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-01-01".toLocalDate, "2021-01-07".toLocalDate)),
            PartialPeriod(
              Period("2021-01-08".toLocalDate, "2021-01-14".toLocalDate),
              Period("2021-01-08".toLocalDate, "2021-01-08".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(600)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-21")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-31", "2021-01-07", "2021-01-14"))
        .withUsualHours(List(UsualHours("2021-01-07".toLocalDate, Hours(37.0)), UsualHours("2021-01-14".toLocalDate, Hours(37.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-07".toLocalDate, Hours(10.0)), PartTimeHours("2021-01-14".toLocalDate, Hours(12.0))))
        -> 396.60,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-31".toLocalDate, "2021-01-06".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(666.12)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-30", "2021-01-06"))
        .withUsualHours(List(UsualHours("2021-01-06".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-06".toLocalDate, Hours(11.4))))
        -> 101.94,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-26".toLocalDate, "2021-01-01".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-01".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-01-02".toLocalDate, "2021-01-08".toLocalDate),
              Period("2021-01-02".toLocalDate, "2021-01-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(890.11)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-25", "2021-01-01", "2021-01-08"))
        .withUsualHours(List(UsualHours("2021-01-01".toLocalDate, Hours(34.5)), UsualHours("2021-01-08".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-01".toLocalDate, Hours(11.4)), PartTimeHours("2021-01-08".toLocalDate, Hours(11.4))))
        -> 108.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-01")
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-29".toLocalDate, "2021-01-04".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(500)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-28", "2021-01-04"))
        .withUsualHours(List(UsualHours("2021-01-04".toLocalDate, Hours(10.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-04".toLocalDate, Hours(6.5))))
        -> 20.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-02")
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-26".toLocalDate, "2021-01-01".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(754.44)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2020-12-25", "2021-01-01", "2021-01-08"))
        .withUsualHours(List(UsualHours("2021-01-01".toLocalDate, Hours(7.5))))
        .withPartTimeHours(List(PartTimeHours("2021-01-01".toLocalDate, Hours(3.0))))
        -> 129.04
    )
  )
  val januaryVariableFourWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "January Variable Four Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-10-29")
        .withFurloughEndDate("2021-01-28")
        .withPaymentFrequency(FourWeekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2020-12-06".toLocalDate, "2021-01-02".toLocalDate),
              Period("2021-01-01".toLocalDate, "2021-01-02".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-01-03".toLocalDate, "2021-01-30".toLocalDate),
              Period("2021-01-03".toLocalDate, "2021-01-28".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(1500)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withPayDate(List("2020-12-05", "2021-01-02", "2021-01-30"))
        .withUsualHours(List(UsualHours("2021-01-02".toLocalDate, Hours(40.0)), UsualHours("2021-01-30".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-02".toLocalDate, Hours(14.0)), PartTimeHours("2021-01-30".toLocalDate, Hours(15.0))))
        -> 365.67,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-04-04")
        .withFurloughEndDate("2021-01-31")
        .withPaymentFrequency(FourWeekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-01-31")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-30".toLocalDate, "2021-02-26".toLocalDate),
              Period("2021-01-31".toLocalDate, "2021-01-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(19564.4)
        .withFurloughStartDate("2021-01-29")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withPayDate(List("2021-01-29", "2021-02-26"))
        .withUsualHours(List(UsualHours("2021-02-26".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-26".toLocalDate, Hours(14.0))))
        -> 34.14,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-28")
        .withPaymentFrequency(FourWeekly)
        .withEmployeeStartedOnOrBefore1Feb2019()
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List("2020-01-30" -> 2800))
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withAnnualPayAmount(36000)
        .withFurloughStartDate("2021-01-04")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-28")
        .withPayDate(List("2020-12-31", "2021-01-28"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 2000.00
    )
  )
  val januaryVariableMonthlyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "January Variable Monthly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-08-01")
        .withFurloughEndDate("2021-01-31")
        .withPaymentFrequency(Monthly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(List(FullPeriod(Period("2021-01-01".toLocalDate, "2021-01-31".toLocalDate))))
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(10000)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withPayDate(List("2020-12-31", "2021-01-31"))
        .withUsualHours(List(UsualHours("2021-01-31".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-31".toLocalDate, Hours(14.0))))
        -> 1053.60,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-04-01")
        .withFurloughEndDate("2021-01-31")
        .withPaymentFrequency(Monthly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(List(FullPeriod(Period("2021-01-01".toLocalDate, "2021-01-31".toLocalDate))))
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(10000)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withPayDate(List("2020-12-31", "2021-01-31"))
        .withUsualHours(List(UsualHours("2021-01-31".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-31".toLocalDate, Hours(14.0))))
        -> 597.09,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-31")
        .withPaymentFrequency(Monthly)
        .withEmployeeStartedOnOrBefore1Feb2019()
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List("2020-01-31" -> 2000))
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withAnnualPayAmount(26000)
        .withFurloughStartDate("2020-03-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withPayDate(List("2020-12-31", "2021-01-31"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 1953.99
    )
  )
  val januaryVariableTwoWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "January Variable Two Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-08-27")
        .withFurloughEndDate("2021-01-31")
        .withPaymentFrequency(FortNightly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-01-13")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-01".toLocalDate, "2021-01-14".toLocalDate),
              Period("2021-01-13".toLocalDate, "2021-01-14".toLocalDate)
            ),
            FullPeriod(Period("2021-01-15".toLocalDate, "2021-01-28".toLocalDate)),
            PartialPeriod(
              Period("2021-01-29".toLocalDate, "2021-02-11".toLocalDate),
              Period("2021-01-29".toLocalDate, "2021-01-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(34000)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withPayDate(List("2020-12-31", "2021-01-14", "2021-01-28", "2021-02-11"))
        .withUsualHours(
          List(
            UsualHours("2021-01-14".toLocalDate, Hours(40.0)),
            UsualHours("2021-01-28".toLocalDate, Hours(50.0)),
            UsualHours("2021-02-11".toLocalDate, Hours(50.0))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-01-14".toLocalDate, Hours(14.0)),
            PartTimeHours("2021-01-28".toLocalDate, Hours(15.0)),
            PartTimeHours("2021-02-11".toLocalDate, Hours(15.0))
          )
        )
        -> 1081.91,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-03-20")
        .withFurloughEndDate("2021-01-25")
        .withPaymentFrequency(FortNightly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-01".toLocalDate, "2021-01-14".toLocalDate),
              Period("2021-01-05".toLocalDate, "2021-01-14".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-01-15".toLocalDate, "2021-01-28".toLocalDate),
              Period("2021-01-15".toLocalDate, "2021-01-25".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(19564.4)
        .withFurloughStartDate("2021-01-05")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-29")
        .withPayDate(List("2020-12-31", "2021-01-14", "2021-01-28"))
        .withUsualHours(List(UsualHours("2021-01-14".toLocalDate, Hours(40.0)), UsualHours("2021-01-28".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-14".toLocalDate, Hours(14.0)), PartTimeHours("2021-01-28".toLocalDate, Hours(15.0))))
        -> 811.10,
      emptyUserAnswers
        .withRtiSubmission(EmployeeRTISubmission.Yes)
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-03-19")
        .withFurloughEndDate("2021-01-25")
        .withPaymentFrequency(FortNightly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-01".toLocalDate, "2021-01-14".toLocalDate),
              Period("2021-01-05".toLocalDate, "2021-01-14".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-01-15".toLocalDate, "2021-01-28".toLocalDate),
              Period("2021-01-15".toLocalDate, "2021-01-25".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(1000)
        .withFurloughStartDate("2021-01-05")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-29")
        .withPayDate(List("2020-12-31", "2021-01-14", "2021-01-28"))
        .withUsualHours(List(UsualHours("2021-01-14".toLocalDate, Hours(40.0)), UsualHours("2021-01-28".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-14".toLocalDate, Hours(14.0)), PartTimeHours("2021-01-28".toLocalDate, Hours(15.0))))
        -> 631.16,
      emptyUserAnswers
        .withRtiSubmission(EmployeeRTISubmission.No)
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-03-19")
        .withFurloughEndDate("2021-01-25")
        .withPaymentFrequency(FortNightly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-01".toLocalDate, "2021-01-14".toLocalDate),
              Period("2021-01-05".toLocalDate, "2021-01-14".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-01-15".toLocalDate, "2021-01-28".toLocalDate),
              Period("2021-01-15".toLocalDate, "2021-01-25".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(1000)
        .withFurloughStartDate("2021-01-05")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-29")
        .withPayDate(List("2020-12-31", "2021-01-14", "2021-01-28"))
        .withUsualHours(List(UsualHours("2021-01-14".toLocalDate, Hours(40.0)), UsualHours("2021-01-28".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-14".toLocalDate, Hours(14.0)), PartTimeHours("2021-01-28".toLocalDate, Hours(15.0))))
        -> 41.47,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-28")
        .withPaymentFrequency(FortNightly)
        .withEmployeeStartedOnOrBefore1Feb2019()
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List("2020-01-16" -> 840, "2020-01-30" -> 980))
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withAnnualPayAmount(28000)
        .withFurloughStartDate("2021-01-04")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-28")
        .withPayDate(List("2020-12-31", "2021-01-14", "2021-01-28"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 1530.00
    )
  )
  val januaryVariableWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "January Variable Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-05-05")
        .withFurloughEndDate("2021-01-14")
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-01".toLocalDate, "2021-01-07".toLocalDate),
              Period("2021-01-04".toLocalDate, "2021-01-07".toLocalDate)
            ),
            FullPeriod(Period("2021-01-08".toLocalDate, "2021-01-14".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(19564.4)
        .withFurloughStartDate("2021-01-04")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withPayDate(List("2020-12-31", "2021-01-07", "2021-01-14"))
        .withUsualHours(List(UsualHours("2021-01-07".toLocalDate, Hours(40.0)), UsualHours("2021-01-14".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-01-14".toLocalDate, Hours(15.0))))
        -> 481.08,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-04-01")
        .withFurloughEndDate("2021-01-14")
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-01".toLocalDate, "2021-01-07".toLocalDate),
              Period("2021-01-04".toLocalDate, "2021-01-07".toLocalDate)
            ),
            FullPeriod(Period("2021-01-08".toLocalDate, "2021-01-14".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(26000)
        .withFurloughStartDate("2021-01-04")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withPayDate(List("2020-12-31", "2021-01-07", "2021-01-14"))
        .withUsualHours(List(UsualHours("2021-01-07".toLocalDate, Hours(40.0)), UsualHours("2021-01-14".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-01-14".toLocalDate, Hours(15.0))))
        -> 571.44,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-04-01")
        .withFurloughEndDate("2021-01-14")
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-01".toLocalDate, "2021-01-07".toLocalDate),
              Period("2021-01-04".toLocalDate, "2021-01-07".toLocalDate)
            ),
            FullPeriod(Period("2021-01-08".toLocalDate, "2021-01-14".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(1000)
        .withFurloughStartDate("2021-01-04")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withPayDate(List("2020-12-31", "2021-01-07", "2021-01-14"))
        .withUsualHours(List(UsualHours("2021-01-07".toLocalDate, Hours(40.0)), UsualHours("2021-01-14".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-01-14".toLocalDate, Hours(15.0))))
        -> 21.96,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-07")
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedOnOrBefore1Feb2019()
        .withClaimPeriodStart("2021-01-01")
        .withLastYear(List("2020-01-02" -> 420, "2020-01-09" -> 490))
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(List(FullPeriod(Period("2021-01-01".toLocalDate, "2021-01-07".toLocalDate))))
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(26000)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-31")
        .withPayDate(List("2020-12-31", "2021-01-07"))
        .withUsualHours(List(UsualHours("2021-01-07".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-01-07".toLocalDate, Hours(14.0))))
        ->
          258.58,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-01-24")
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedOnOrBefore1Feb2019()
        .withClaimPeriodStart("2021-01-03")
        .withLastYear(List("2020-01-09" -> 420, "2020-01-16" -> 490, "2020-01-23" -> 560, "2020-01-30" -> 630))
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withAnnualPayAmount(26000)
        .withFurloughStartDate("2021-01-04")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-01-28")
        .withPayDate(List("2020-12-31", "2021-01-07", "2021-01-14", "2021-01-21", "2021-01-28"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 1257.15
    )
  )
}
