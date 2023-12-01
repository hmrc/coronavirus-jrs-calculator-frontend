package controllers.scenarios

import assets.BaseITConstants
import models.PaymentFrequency.{FortNightly, FourWeekly, Monthly, Weekly}
import models.{EmployeeRTISubmission, FullPeriod, FurloughStatus, Hours, PartTimeHours, PartTimeQuestion, PartialPeriod, PayMethod, PayPeriodsList, Period, RegularLengthEmployed, UserAnswers, UsualHours}
import utils.{CreateRequestHelper, CustomMatchers, ITCoreTestData, IntegrationSpecBase}

object MarchConfirmationScenarios
    extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants with ITCoreTestData {

  val marchFourWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] =
    Seq(
      "March Fixed Four Weekly Scenarios" -> Seq(
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughEnded)
          .withFurloughEndDate("2021-03-28")
          .withPaymentFrequency(FourWeekly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(List(FullPeriod(Period("2021-03-01".toLocalDate, "2021-03-28".toLocalDate))))
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(2000)
          .withFurloughStartDate("2021-03-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-31")
          .withRegularLengthEmployed(RegularLengthEmployed.Yes)
          .withPayDate(List("2021-02-28", "2021-03-28"))
          .withUsualHours(List(UsualHours("2021-03-28".toLocalDate, Hours(148.0))))
          .withPartTimeHours(List(PartTimeHours("2021-03-28".toLocalDate, Hours(40.0))))
          -> 1167.57,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughEnded)
          .withFurloughEndDate("2021-03-28")
          .withPaymentFrequency(FourWeekly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
          .withRegularPayAmount(3300)
          .withFurloughStartDate("2021-03-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-31")
          .withRegularLengthEmployed(RegularLengthEmployed.Yes)
          .withPayDate(List("2021-02-04", "2021-03-04", "2021-04-01"))
          .withUsualHours(List())
          .withPartTimeHours(List())
          -> 2258.20,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughOngoing)
          .withPaymentFrequency(FourWeekly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
          .withRegularPayAmount(3300)
          .withFurloughStartDate("2021-03-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-28")
          .withRegularLengthEmployed(RegularLengthEmployed.Yes)
          .withPayDate(List("2021-02-28", "2021-03-28"))
          .withUsualHours(List())
          .withPartTimeHours(List())
          -> 2307.68,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughEnded)
          .withFurloughEndDate("2021-03-31")
          .withPaymentFrequency(FourWeekly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              FullPeriod(Period("2021-03-01".toLocalDate, "2021-03-28".toLocalDate)),
              PartialPeriod(
                Period("2021-03-29".toLocalDate, "2021-04-25".toLocalDate),
                Period("2021-03-29".toLocalDate, "2021-03-31".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(3500)
          .withFurloughStartDate("2020-04-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-31")
          .withRegularLengthEmployed(RegularLengthEmployed.Yes)
          .withPayDate(List("2021-02-28", "2021-03-28", "2021-04-25"))
          .withUsualHours(List(UsualHours("2021-03-28".toLocalDate, Hours(148.0)), UsualHours("2021-04-25".toLocalDate, Hours(15.86))))
          .withPartTimeHours(
            List(PartTimeHours("2021-03-28".toLocalDate, Hours(40.0)), PartTimeHours("2021-04-25".toLocalDate, Hours(1.86)))
          )
          -> 1897.56,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughOngoing)
          .withPaymentFrequency(FourWeekly)
          .withClaimPeriodStart("2021-03-31")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-03-31".toLocalDate, "2021-04-27".toLocalDate),
                Period("2021-03-31".toLocalDate, "2021-03-31".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(2200)
          .withFurloughStartDate("2021-03-29")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-31")
          .withRegularLengthEmployed(RegularLengthEmployed.Yes)
          .withPayDate(List("2021-03-30", "2021-04-27"))
          .withUsualHours(List(UsualHours("2021-04-27".toLocalDate, Hours(148.0))))
          .withPartTimeHours(List(PartTimeHours("2021-04-27".toLocalDate, Hours(25.0))))
          -> 52.24,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughEnded)
          .withFurloughEndDate("2021-03-31")
          .withPaymentFrequency(FourWeekly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-03-29".toLocalDate, "2021-04-25".toLocalDate),
                Period("2021-03-29".toLocalDate, "2021-03-31".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(3500)
          .withFurloughStartDate("2021-03-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-31")
          .withRegularLengthEmployed(RegularLengthEmployed.Yes)
          .withPayDate(List("2021-02-28", "2021-03-28", "2021-04-25"))
          .withUsualHours(List(UsualHours("2021-04-25".toLocalDate, Hours(15.86))))
          .withPartTimeHours(List(PartTimeHours("2021-04-25".toLocalDate, Hours(1.86))))
          -> 2521.26,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughOngoing)
          .withPaymentFrequency(FourWeekly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-03-29".toLocalDate, "2021-04-25".toLocalDate),
                Period("2021-03-29".toLocalDate, "2021-03-29".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(3500)
          .withFurloughStartDate("2021-03-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-29")
          .withRegularLengthEmployed(RegularLengthEmployed.Yes)
          .withPayDate(List("2021-02-28", "2021-03-28", "2021-04-25"))
          .withUsualHours(List(UsualHours("2021-04-25".toLocalDate, Hours(15.86))))
          .withPartTimeHours(List(PartTimeHours("2021-04-25".toLocalDate, Hours(1.86))))
          -> 2378.87,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughOngoing)
          .withPaymentFrequency(FourWeekly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-02-06".toLocalDate, "2021-03-05".toLocalDate),
                Period("2021-03-01".toLocalDate, "2021-03-02".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(2654.11)
          .withFurloughStartDate("2020-04-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-02")
          .withRegularLengthEmployed(RegularLengthEmployed.Yes)
          .withPayDate(List("2021-02-05", "2021-03-05"))
          .withUsualHours(List(UsualHours("2021-03-05".toLocalDate, Hours(34.5))))
          .withPartTimeHours(List(PartTimeHours("2021-03-05".toLocalDate, Hours(11.4))))
          -> 101.55,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughOngoing)
          .withPaymentFrequency(FourWeekly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-02-08".toLocalDate, "2021-03-07".toLocalDate),
                Period("2021-03-01".toLocalDate, "2021-03-02".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(3200.11)
          .withFurloughStartDate("2020-04-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-02")
          .withRegularLengthEmployed(RegularLengthEmployed.Yes)
          .withPayDate(List("2021-02-07", "2021-03-07"))
          .withUsualHours(List(UsualHours("2021-03-07".toLocalDate, Hours(34.5))))
          .withPartTimeHours(List(PartTimeHours("2021-03-07".toLocalDate, Hours(11.4))))
          -> 108.00,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughEnded)
          .withFurloughEndDate("2021-03-01")
          .withPaymentFrequency(FourWeekly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-02-10".toLocalDate, "2021-03-09".toLocalDate),
                Period("2021-03-01".toLocalDate, "2021-03-01".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(2322.11)
          .withFurloughStartDate("2021-02-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-02")
          .withRegularLengthEmployed(RegularLengthEmployed.Yes)
          .withPayDate(List("2021-02-09", "2021-03-09"))
          .withUsualHours(List(UsualHours("2021-03-09".toLocalDate, Hours(10.0))))
          .withPartTimeHours(List(PartTimeHours("2021-03-09".toLocalDate, Hours(6.5))))
          -> 23.22,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughEnded)
          .withFurloughEndDate("2021-03-02")
          .withPaymentFrequency(FourWeekly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-02-05".toLocalDate, "2021-03-04".toLocalDate),
                Period("2021-03-01".toLocalDate, "2021-03-02".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(1465.55)
          .withFurloughStartDate("2021-02-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-02")
          .withRegularLengthEmployed(RegularLengthEmployed.Yes)
          .withPayDate(List("2021-02-04", "2021-03-04"))
          .withUsualHours(List(UsualHours("2021-03-04".toLocalDate, Hours(7.5))))
          .withPartTimeHours(List(PartTimeHours("2021-03-04".toLocalDate, Hours(3.0))))
          -> 50.25
      )
    )

  val marchMonthlyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] =
    Seq(
      "March Fixed Monthly Scenarios" -> Seq(
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughOngoing)
          .withPaymentFrequency(Monthly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(List(FullPeriod(Period("2021-03-01".toLocalDate, "2021-03-31".toLocalDate))))
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(2400)
          .withFurloughStartDate("2021-03-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-31")
          .withRegularLengthEmployed(RegularLengthEmployed.No)
          .withPayDate(List("2021-02-28", "2021-03-31"))
          .withUsualHours(List(UsualHours("2021-03-31".toLocalDate, Hours(160.0))))
          .withPartTimeHours(List(PartTimeHours("2021-03-31".toLocalDate, Hours(40.0))))
          -> 1440.00,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughOngoing)
          .withPaymentFrequency(Monthly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
          .withRegularPayAmount(3126)
          .withFurloughStartDate("2021-03-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-31")
          .withRegularLengthEmployed(RegularLengthEmployed.No)
          .withPayDate(List("2021-02-28", "2021-03-31"))
          .withUsualHours(List())
          .withPartTimeHours(List())
          -> 2500.00,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughEnded)
          .withFurloughEndDate("2021-03-21")
          .withPaymentFrequency(Monthly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-03-01".toLocalDate, "2021-03-31".toLocalDate),
                Period("2021-03-05".toLocalDate, "2021-03-21".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(2400)
          .withFurloughStartDate("2021-03-05")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-31")
          .withRegularLengthEmployed(RegularLengthEmployed.No)
          .withPayDate(List("2021-02-28", "2021-03-31"))
          .withUsualHours(List(UsualHours("2021-03-31".toLocalDate, Hours(127.5))))
          .withPartTimeHours(List(PartTimeHours("2021-03-31".toLocalDate, Hours(52.5))))
          -> 619.35,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughEnded)
          .withFurloughEndDate("2021-03-21")
          .withPaymentFrequency(Monthly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-03-01".toLocalDate, "2021-03-31".toLocalDate),
                Period("2021-03-05".toLocalDate, "2021-03-21".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(6500)
          .withFurloughStartDate("2021-03-05")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-31")
          .withRegularLengthEmployed(RegularLengthEmployed.No)
          .withPayDate(List("2021-02-28", "2021-03-31"))
          .withUsualHours(List(UsualHours("2021-03-31".toLocalDate, Hours(127.5))))
          .withPartTimeHours(List(PartTimeHours("2021-03-31".toLocalDate, Hours(52.5))))
          -> 806.50,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughEnded)
          .withFurloughEndDate("2021-03-11")
          .withPaymentFrequency(Monthly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-03-01".toLocalDate, "2021-03-31".toLocalDate),
                Period("2021-03-02".toLocalDate, "2021-03-11".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(2400)
          .withFurloughStartDate("2021-03-02")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-31")
          .withRegularLengthEmployed(RegularLengthEmployed.No)
          .withPayDate(List("2021-02-28", "2021-03-31"))
          .withUsualHours(List(UsualHours("2021-03-31".toLocalDate, Hours(160.0))))
          .withPartTimeHours(List(PartTimeHours("2021-03-31".toLocalDate, Hours(40.0))))
          -> 464.51,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughEnded)
          .withFurloughEndDate("2021-03-20")
          .withPaymentFrequency(Monthly)
          .withClaimPeriodStart("2021-03-02")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-03-01".toLocalDate, "2021-03-31".toLocalDate),
                Period("2021-03-02".toLocalDate, "2021-03-20".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(5555)
          .withFurloughStartDate("2021-03-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-21")
          .withRegularLengthEmployed(RegularLengthEmployed.No)
          .withPayDate(List("2021-02-28", "2021-03-31"))
          .withUsualHours(List(UsualHours("2021-03-31".toLocalDate, Hours(160.0))))
          .withPartTimeHours(List(PartTimeHours("2021-03-31".toLocalDate, Hours(40.0))))
          -> 1149.26,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughOngoing)
          .withPaymentFrequency(Monthly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-02-26".toLocalDate, "2021-03-25".toLocalDate),
                Period("2021-03-01".toLocalDate, "2021-03-25".toLocalDate)
              ),
              PartialPeriod(
                Period("2021-03-26".toLocalDate, "2021-04-25".toLocalDate),
                Period("2021-03-26".toLocalDate, "2021-03-31".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(4900)
          .withFurloughStartDate("2020-04-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-31")
          .withRegularLengthEmployed(RegularLengthEmployed.No)
          .withPayDate(List("2021-02-25", "2021-03-25", "2021-04-25"))
          .withUsualHours(List(UsualHours("2021-03-25".toLocalDate, Hours(160.0)), UsualHours("2021-04-25".toLocalDate, Hours(160.0))))
          .withPartTimeHours(
            List(PartTimeHours("2021-03-25".toLocalDate, Hours(95.0)), PartTimeHours("2021-04-25".toLocalDate, Hours(95.0)))
          )
          -> 1015.68,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughOngoing)
          .withPaymentFrequency(Monthly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-03-01".toLocalDate, "2021-03-31".toLocalDate),
                Period("2021-03-01".toLocalDate, "2021-03-02".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(2654.11)
          .withFurloughStartDate("2020-04-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-02")
          .withRegularLengthEmployed(RegularLengthEmployed.No)
          .withPayDate(List("2021-02-28", "2021-03-31"))
          .withUsualHours(List(UsualHours("2021-03-31".toLocalDate, Hours(34.5))))
          .withPartTimeHours(List(PartTimeHours("2021-03-31".toLocalDate, Hours(11.4))))
          -> 91.72,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughOngoing)
          .withPaymentFrequency(Monthly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-02-24".toLocalDate, "2021-03-25".toLocalDate),
                Period("2021-03-01".toLocalDate, "2021-03-02".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(2654.11)
          .withFurloughStartDate("2020-04-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-02")
          .withRegularLengthEmployed(RegularLengthEmployed.No)
          .withPayDate(List("2021-02-23", "2021-03-25"))
          .withUsualHours(List(UsualHours("2021-03-25".toLocalDate, Hours(34.5))))
          .withPartTimeHours(List(PartTimeHours("2021-03-25".toLocalDate, Hours(11.4))))
          -> 94.78,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughOngoing)
          .withPaymentFrequency(Monthly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-02-03".toLocalDate, "2021-03-02".toLocalDate),
                Period("2021-03-01".toLocalDate, "2021-03-02".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(3200.11)
          .withFurloughStartDate("2020-04-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-02")
          .withRegularLengthEmployed(RegularLengthEmployed.No)
          .withPayDate(List("2021-02-02", "2021-03-02"))
          .withUsualHours(List(UsualHours("2021-03-02".toLocalDate, Hours(34.5))))
          .withPartTimeHours(List(PartTimeHours("2021-03-02".toLocalDate, Hours(11.4))))
          -> 108.00,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughEnded)
          .withFurloughEndDate("2021-03-01")
          .withPaymentFrequency(Monthly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-02-07".toLocalDate, "2021-03-08".toLocalDate),
                Period("2021-03-01".toLocalDate, "2021-03-01".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(2322.11)
          .withFurloughStartDate("2020-03-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-02")
          .withRegularLengthEmployed(RegularLengthEmployed.No)
          .withPayDate(List("2021-02-06", "2021-03-08"))
          .withUsualHours(List(UsualHours("2021-03-08".toLocalDate, Hours(10.0))))
          .withPartTimeHours(List(PartTimeHours("2021-03-08".toLocalDate, Hours(6.5))))
          -> 21.67,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughOngoing)
          .withPaymentFrequency(Monthly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-02-02".toLocalDate, "2021-03-01".toLocalDate),
                Period("2021-03-01".toLocalDate, "2021-03-01".toLocalDate)
              ),
              PartialPeriod(
                Period("2021-03-02".toLocalDate, "2021-04-01".toLocalDate),
                Period("2021-03-02".toLocalDate, "2021-03-02".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(1465.55)
          .withFurloughStartDate("2021-03-01")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-02")
          .withRegularLengthEmployed(RegularLengthEmployed.No)
          .withPayDate(List("2021-02-01", "2021-03-01", "2021-04-01"))
          .withUsualHours(List(UsualHours("2021-03-01".toLocalDate, Hours(7.5)), UsualHours("2021-04-01".toLocalDate, Hours(7.5))))
          .withPartTimeHours(List(PartTimeHours("2021-03-01".toLocalDate, Hours(3.0)), PartTimeHours("2021-04-01".toLocalDate, Hours(3.0))))
          -> 47.82,
        emptyUserAnswers
          .withFurloughStatus(FurloughStatus.FurloughOngoing)
          .withPaymentFrequency(Monthly)
          .withClaimPeriodStart("2021-03-01")
          .withLastYear(List())
          .withPayPeriodsList(PayPeriodsList.Yes)
          .withPartTimePeriods(
            List(
              PartialPeriod(
                Period("2021-03-01".toLocalDate, "2021-03-31".toLocalDate),
                Period("2021-03-01".toLocalDate, "2021-03-01".toLocalDate)
              )
            )
          )
          .withPayMethod(PayMethod.Regular)
          .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
          .withRegularPayAmount(3222)
          .withFurloughStartDate("2020-03-30")
          .withPreviousFurloughedPeriodsAnswer(false)
          .withClaimPeriodEnd("2021-03-01")
          .withRegularLengthEmployed(RegularLengthEmployed.No)
          .withPayDate(List("2021-02-28", "2021-03-31"))
          .withUsualHours(List(UsualHours("2021-03-31".toLocalDate, Hours(7.5))))
          .withPartTimeHours(List(PartTimeHours("2021-03-31".toLocalDate, Hours(3.0))))
          -> 48.39
      )
    )

  val marchTwoWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] =
    Seq(
      "March Fixed Two Weekly Scenarios" ->
        Seq(
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughOngoing)
            .withPaymentFrequency(FortNightly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                FullPeriod(Period("2021-03-01".toLocalDate, "2021-03-14".toLocalDate)),
                FullPeriod(Period("2021-03-15".toLocalDate, "2021-03-28".toLocalDate)),
                PartialPeriod(
                  Period("2021-03-29".toLocalDate, "2021-04-11".toLocalDate),
                  Period("2021-03-29".toLocalDate, "2021-03-31".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(650)
            .withFurloughStartDate("2021-03-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-28", "2021-03-14", "2021-03-28", "2021-04-11"))
            .withUsualHours(
              List(
                UsualHours("2021-03-14".toLocalDate, Hours(98.0)),
                UsualHours("2021-03-28".toLocalDate, Hours(98.0)),
                UsualHours("2021-04-11".toLocalDate, Hours(21.0))
              )
            )
            .withPartTimeHours(
              List(
                PartTimeHours("2021-03-14".toLocalDate, Hours(48.0)),
                PartTimeHours("2021-03-28".toLocalDate, Hours(48.0)),
                PartTimeHours("2021-04-11".toLocalDate, Hours(6.0))
              )
            )
            -> 610.19,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughOngoing)
            .withPaymentFrequency(FortNightly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                FullPeriod(Period("2021-03-01".toLocalDate, "2021-03-14".toLocalDate)),
                FullPeriod(Period("2021-03-15".toLocalDate, "2021-03-28".toLocalDate)),
                PartialPeriod(
                  Period("2021-03-29".toLocalDate, "2021-04-11".toLocalDate),
                  Period("2021-03-29".toLocalDate, "2021-03-31".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(2300.12)
            .withFurloughStartDate("2021-03-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-28", "2021-03-14", "2021-03-28", "2021-04-11"))
            .withUsualHours(
              List(
                UsualHours("2021-03-14".toLocalDate, Hours(98.0)),
                UsualHours("2021-03-28".toLocalDate, Hours(98.0)),
                UsualHours("2021-04-11".toLocalDate, Hours(21.0))
              )
            )
            .withPartTimeHours(
              List(
                PartTimeHours("2021-03-14".toLocalDate, Hours(48.0)),
                PartTimeHours("2021-03-28".toLocalDate, Hours(48.0)),
                PartTimeHours("2021-04-11".toLocalDate, Hours(6.0))
              )
            )
            -> 1350.20,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughOngoing)
            .withPaymentFrequency(FortNightly)
            .withClaimPeriodStart("2021-03-13")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-29".toLocalDate, "2021-04-11".toLocalDate),
                  Period("2021-03-29".toLocalDate, "2021-03-31".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(650)
            .withFurloughStartDate("2021-03-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-28", "2021-03-14", "2021-03-28", "2021-04-11"))
            .withUsualHours(List(UsualHours("2021-04-11".toLocalDate, Hours(21.0))))
            .withPartTimeHours(List(PartTimeHours("2021-04-11".toLocalDate, Hours(6.0))))
            -> 673.88,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withFurloughEndDate("2021-03-11")
            .withPaymentFrequency(FortNightly)
            .withClaimPeriodStart("2021-03-03")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-01".toLocalDate, "2021-03-14".toLocalDate),
                  Period("2021-03-03".toLocalDate, "2021-03-11".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(464.28)
            .withFurloughStartDate("2020-04-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-11")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-28", "2021-03-14"))
            .withUsualHours(List(UsualHours("2021-03-14".toLocalDate, Hours(70.0))))
            .withPartTimeHours(List(PartTimeHours("2021-03-14".toLocalDate, Hours(43.0))))
            -> 92.10,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughOngoing)
            .withPaymentFrequency(FortNightly)
            .withClaimPeriodStart("2021-03-27")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-28".toLocalDate, "2021-04-10".toLocalDate),
                  Period("2021-03-29".toLocalDate, "2021-03-31".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(789.12)
            .withFurloughStartDate("2021-03-29")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-03-27", "2021-04-10"))
            .withUsualHours(List(UsualHours("2021-04-10".toLocalDate, Hours(140.0))))
            .withPartTimeHours(List(PartTimeHours("2021-04-10".toLocalDate, Hours(50.0))))
            -> 86.97,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withFurloughEndDate("2021-03-25")
            .withPaymentFrequency(FortNightly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-01".toLocalDate, "2021-03-14".toLocalDate),
                  Period("2021-03-05".toLocalDate, "2021-03-14".toLocalDate)
                ),
                PartialPeriod(
                  Period("2021-03-15".toLocalDate, "2021-03-28".toLocalDate),
                  Period("2021-03-15".toLocalDate, "2021-03-25".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(643.12)
            .withFurloughStartDate("2021-03-05")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-29")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-28", "2021-03-14", "2021-03-28"))
            .withUsualHours(List(UsualHours("2021-03-14".toLocalDate, Hours(63.0)), UsualHours("2021-03-28".toLocalDate, Hours(77.0))))
            .withPartTimeHours(
              List(PartTimeHours("2021-03-14".toLocalDate, Hours(8.0)), PartTimeHours("2021-03-28".toLocalDate, Hours(12.0)))
            )
            -> 662.08,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughOngoing)
            .withPaymentFrequency(FortNightly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-02-19".toLocalDate, "2021-03-04".toLocalDate),
                  Period("2021-03-01".toLocalDate, "2021-03-02".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(1000.34)
            .withFurloughStartDate("2020-04-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-02")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-18", "2021-03-04"))
            .withUsualHours(List(UsualHours("2021-03-04".toLocalDate, Hours(34.5))))
            .withPartTimeHours(List(PartTimeHours("2021-03-04".toLocalDate, Hours(11.4))))
            -> 76.55,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughOngoing)
            .withPaymentFrequency(FortNightly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-02-22".toLocalDate, "2021-03-07".toLocalDate),
                  Period("2021-03-01".toLocalDate, "2021-03-02".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(1500)
            .withFurloughStartDate("2020-04-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-02")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-21", "2021-03-07"))
            .withUsualHours(List(UsualHours("2021-03-07".toLocalDate, Hours(34.5))))
            .withPartTimeHours(List(PartTimeHours("2021-03-07".toLocalDate, Hours(11.4))))
            -> 108.00,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withFurloughEndDate("2021-03-01")
            .withPaymentFrequency(FortNightly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-02-23".toLocalDate, "2021-03-08".toLocalDate),
                  Period("2021-03-01".toLocalDate, "2021-03-01".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(1010.11)
            .withFurloughStartDate("2021-02-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-02")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-22", "2021-03-08"))
            .withUsualHours(List(UsualHours("2021-03-08".toLocalDate, Hours(10.0))))
            .withPartTimeHours(List(PartTimeHours("2021-03-08".toLocalDate, Hours(6.5))))
            -> 20.20,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withFurloughEndDate("2021-03-02")
            .withPaymentFrequency(FortNightly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-02-19".toLocalDate, "2021-03-04".toLocalDate),
                  Period("2021-03-01".toLocalDate, "2021-03-02".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(1465.55)
            .withFurloughStartDate("2021-02-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-02")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-18", "2021-03-04"))
            .withUsualHours(List(UsualHours("2021-03-04".toLocalDate, Hours(7.5))))
            .withPartTimeHours(List(PartTimeHours("2021-03-04".toLocalDate, Hours(3.0))))
            -> 96.78,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughOngoing)
            .withPaymentFrequency(FortNightly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                FullPeriod(Period("2021-03-01".toLocalDate, "2021-03-14".toLocalDate)),
                FullPeriod(Period("2021-03-15".toLocalDate, "2021-03-28".toLocalDate)),
                PartialPeriod(
                  Period("2021-03-29".toLocalDate, "2021-04-11".toLocalDate),
                  Period("2021-03-29".toLocalDate, "2021-03-31".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(650)
            .withFurloughStartDate("2021-03-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-28", "2021-03-14", "2021-03-28", "2021-04-11"))
            .withUsualHours(
              List(
                UsualHours("2021-03-14".toLocalDate, Hours(98.0)),
                UsualHours("2021-03-28".toLocalDate, Hours(98.0)),
                UsualHours("2021-04-11".toLocalDate, Hours(21.0))
              )
            )
            .withPartTimeHours(
              List(
                PartTimeHours("2021-03-14".toLocalDate, Hours(48.0)),
                PartTimeHours("2021-03-28".toLocalDate, Hours(48.0)),
                PartTimeHours("2021-04-11".toLocalDate, Hours(6.0))
              )
            )
            -> 610.19
        )
    )

  val marchWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] =
    Seq(
      "March Fixed Weekly Scenarios" ->
        Seq(
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughOngoing)
            .withPaymentFrequency(Weekly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                FullPeriod(Period("2021-03-01".toLocalDate, "2021-03-07".toLocalDate)),
                FullPeriod(Period("2021-03-08".toLocalDate, "2021-03-14".toLocalDate)),
                FullPeriod(Period("2021-03-15".toLocalDate, "2021-03-21".toLocalDate)),
                FullPeriod(Period("2021-03-22".toLocalDate, "2021-03-28".toLocalDate)),
                PartialPeriod(
                  Period("2021-03-29".toLocalDate, "2021-04-04".toLocalDate),
                  Period("2021-03-29".toLocalDate, "2021-03-31".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(600)
            .withFurloughStartDate("2021-03-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-28", "2021-03-07", "2021-03-14", "2021-03-21", "2021-03-28", "2021-04-04"))
            .withUsualHours(
              List(
                UsualHours("2021-03-07".toLocalDate, Hours(37.0)),
                UsualHours("2021-03-14".toLocalDate, Hours(37.0)),
                UsualHours("2021-03-21".toLocalDate, Hours(37.0)),
                UsualHours("2021-03-28".toLocalDate, Hours(37.0)),
                UsualHours("2021-04-04".toLocalDate, Hours(15.86))
              )
            )
            .withPartTimeHours(
              List(
                PartTimeHours("2021-03-07".toLocalDate, Hours(10.0)),
                PartTimeHours("2021-03-14".toLocalDate, Hours(12.0)),
                PartTimeHours("2021-03-21".toLocalDate, Hours(10.0)),
                PartTimeHours("2021-03-28".toLocalDate, Hours(15.0)),
                PartTimeHours("2021-04-04".toLocalDate, Hours(1.06))
              )
            )
            -> 1502.24,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughOngoing)
            .withPaymentFrequency(Weekly)
            .withClaimPeriodStart("2021-03-02")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-04".toLocalDate, "2021-03-10".toLocalDate),
                  Period("2021-03-06".toLocalDate, "2021-03-10".toLocalDate)
                ),
                FullPeriod(Period("2021-03-11".toLocalDate, "2021-03-17".toLocalDate)),
                FullPeriod(Period("2021-03-18".toLocalDate, "2021-03-24".toLocalDate)),
                PartialPeriod(
                  Period("2021-03-25".toLocalDate, "2021-03-31".toLocalDate),
                  Period("2021-03-25".toLocalDate, "2021-03-29".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(1200)
            .withFurloughStartDate("2021-03-06")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-29")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-03-03", "2021-03-10", "2021-03-17", "2021-03-24", "2021-03-31"))
            .withUsualHours(
              List(
                UsualHours("2021-03-10".toLocalDate, Hours(37.0)),
                UsualHours("2021-03-17".toLocalDate, Hours(37.0)),
                UsualHours("2021-03-24".toLocalDate, Hours(37.0)),
                UsualHours("2021-03-31".toLocalDate, Hours(37.0))
              )
            )
            .withPartTimeHours(
              List(
                PartTimeHours("2021-03-10".toLocalDate, Hours(10.0)),
                PartTimeHours("2021-03-17".toLocalDate, Hours(12.0)),
                PartTimeHours("2021-03-24".toLocalDate, Hours(10.0)),
                PartTimeHours("2021-03-31".toLocalDate, Hours(15.0))
              )
            )
            -> 1344.84,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withFurloughEndDate("2021-03-27")
            .withPaymentFrequency(Weekly)
            .withClaimPeriodStart("2021-03-02")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-04".toLocalDate, "2021-03-10".toLocalDate),
                  Period("2021-03-06".toLocalDate, "2021-03-10".toLocalDate)
                ),
                PartialPeriod(
                  Period("2021-03-25".toLocalDate, "2021-03-31".toLocalDate),
                  Period("2021-03-25".toLocalDate, "2021-03-27".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(1200)
            .withFurloughStartDate("2021-03-06")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-27")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-03-03", "2021-03-10", "2021-03-17", "2021-03-24", "2021-03-31"))
            .withUsualHours(List(UsualHours("2021-03-10".toLocalDate, Hours(37.0)), UsualHours("2021-03-31".toLocalDate, Hours(15.86))))
            .withPartTimeHours(
              List(PartTimeHours("2021-03-10".toLocalDate, Hours(10.0)), PartTimeHours("2021-03-31".toLocalDate, Hours(1.06)))
            )
            -> 1673.88,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughOngoing)
            .withPaymentFrequency(Weekly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                FullPeriod(Period("2021-03-15".toLocalDate, "2021-03-21".toLocalDate)),
                FullPeriod(Period("2021-03-22".toLocalDate, "2021-03-28".toLocalDate)),
                PartialPeriod(
                  Period("2021-03-29".toLocalDate, "2021-04-04".toLocalDate),
                  Period("2021-03-29".toLocalDate, "2021-03-31".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(600)
            .withFurloughStartDate("2021-03-15")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-03-14", "2021-03-21", "2021-03-28", "2021-04-04"))
            .withUsualHours(
              List(
                UsualHours("2021-03-21".toLocalDate, Hours(37.0)),
                UsualHours("2021-03-28".toLocalDate, Hours(37.0)),
                UsualHours("2021-04-04".toLocalDate, Hours(15.86))
              )
            )
            .withPartTimeHours(
              List(
                PartTimeHours("2021-03-21".toLocalDate, Hours(10.0)),
                PartTimeHours("2021-03-28".toLocalDate, Hours(15.0)),
                PartTimeHours("2021-04-04".toLocalDate, Hours(1.06))
              )
            )
            -> 827.64,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughOngoing)
            .withPaymentFrequency(Weekly)
            .withClaimPeriodStart("2021-03-29")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-29".toLocalDate, "2021-04-04".toLocalDate),
                  Period("2021-03-29".toLocalDate, "2021-03-31".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(550)
            .withFurloughStartDate("2021-03-29")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-03-28", "2021-04-04"))
            .withUsualHours(List(UsualHours("2021-04-04".toLocalDate, Hours(40.0))))
            .withPartTimeHours(List(PartTimeHours("2021-04-04".toLocalDate, Hours(14.0))))
            -> 122.57,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withFurloughEndDate("2021-03-08")
            .withPaymentFrequency(Weekly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                FullPeriod(Period("2021-03-01".toLocalDate, "2021-03-07".toLocalDate)),
                PartialPeriod(
                  Period("2021-03-08".toLocalDate, "2021-03-14".toLocalDate),
                  Period("2021-03-08".toLocalDate, "2021-03-08".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(600)
            .withFurloughStartDate("2021-03-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-21")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-28", "2021-03-07", "2021-03-14"))
            .withUsualHours(List(UsualHours("2021-03-07".toLocalDate, Hours(37.0)), UsualHours("2021-03-14".toLocalDate, Hours(37.0))))
            .withPartTimeHours(
              List(PartTimeHours("2021-03-07".toLocalDate, Hours(10.0)), PartTimeHours("2021-03-14".toLocalDate, Hours(12.0)))
            )
            -> 396.60,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughOngoing)
            .withPaymentFrequency(Weekly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-01".toLocalDate, "2021-03-07".toLocalDate),
                  Period("2021-03-01".toLocalDate, "2021-03-02".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(666.12)
            .withFurloughStartDate("2021-02-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-02")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-28", "2021-03-07"))
            .withUsualHours(List(UsualHours("2021-03-07".toLocalDate, Hours(34.5))))
            .withPartTimeHours(List(PartTimeHours("2021-03-07".toLocalDate, Hours(11.4))))
            -> 101.94,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughOngoing)
            .withPaymentFrequency(Weekly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-02-23".toLocalDate, "2021-03-01".toLocalDate),
                  Period("2021-03-01".toLocalDate, "2021-03-01".toLocalDate)
                ),
                PartialPeriod(
                  Period("2021-03-02".toLocalDate, "2021-03-08".toLocalDate),
                  Period("2021-03-02".toLocalDate, "2021-03-02".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(890.11)
            .withFurloughStartDate("2021-02-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-02")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-22", "2021-03-01", "2021-03-08"))
            .withUsualHours(List(UsualHours("2021-03-01".toLocalDate, Hours(34.5)), UsualHours("2021-03-08".toLocalDate, Hours(34.5))))
            .withPartTimeHours(
              List(PartTimeHours("2021-03-01".toLocalDate, Hours(11.4)), PartTimeHours("2021-03-08".toLocalDate, Hours(11.4)))
            )
            -> 108.00,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withFurloughEndDate("2021-03-01")
            .withPaymentFrequency(Weekly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-01".toLocalDate, "2021-03-07".toLocalDate),
                  Period("2021-03-01".toLocalDate, "2021-03-01".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(500)
            .withFurloughStartDate("2021-02-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-02")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-28", "2021-03-07"))
            .withUsualHours(List(UsualHours("2021-03-07".toLocalDate, Hours(10.0))))
            .withPartTimeHours(List(PartTimeHours("2021-03-07".toLocalDate, Hours(6.5))))
            -> 20.00,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withFurloughEndDate("2021-03-02")
            .withPaymentFrequency(Weekly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-02-23".toLocalDate, "2021-03-01".toLocalDate),
                  Period("2021-03-01".toLocalDate, "2021-03-01".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(754.44)
            .withFurloughStartDate("2021-02-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-02")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-22", "2021-03-01", "2021-03-08"))
            .withUsualHours(List(UsualHours("2021-03-01".toLocalDate, Hours(7.5))))
            .withPartTimeHours(List(PartTimeHours("2021-03-01".toLocalDate, Hours(3.0))))
            -> 129.04,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughOngoing)
            .withPaymentFrequency(Weekly)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                FullPeriod(Period("2021-03-01".toLocalDate, "2021-03-07".toLocalDate)),
                FullPeriod(Period("2021-03-08".toLocalDate, "2021-03-14".toLocalDate)),
                FullPeriod(Period("2021-03-15".toLocalDate, "2021-03-21".toLocalDate)),
                FullPeriod(Period("2021-03-22".toLocalDate, "2021-03-28".toLocalDate)),
                PartialPeriod(
                  Period("2021-03-29".toLocalDate, "2021-04-04".toLocalDate),
                  Period("2021-03-29".toLocalDate, "2021-03-31".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Regular)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withRegularPayAmount(600)
            .withFurloughStartDate("2021-03-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withRegularLengthEmployed(RegularLengthEmployed.Yes)
            .withPayDate(List("2021-02-28", "2021-03-07", "2021-03-14", "2021-03-21", "2021-03-28", "2021-04-04"))
            .withUsualHours(
              List(
                UsualHours("2021-03-07".toLocalDate, Hours(37.0)),
                UsualHours("2021-03-14".toLocalDate, Hours(37.0)),
                UsualHours("2021-03-21".toLocalDate, Hours(37.0)),
                UsualHours("2021-03-28".toLocalDate, Hours(37.0)),
                UsualHours("2021-04-04".toLocalDate, Hours(15.86))
              )
            )
            .withPartTimeHours(
              List(
                PartTimeHours("2021-03-07".toLocalDate, Hours(10.0)),
                PartTimeHours("2021-03-14".toLocalDate, Hours(12.0)),
                PartTimeHours("2021-03-21".toLocalDate, Hours(10.0)),
                PartTimeHours("2021-03-28".toLocalDate, Hours(15.0)),
                PartTimeHours("2021-04-04".toLocalDate, Hours(1.06))
              )
            )
            -> 1502.24
        )
    )

  val marchVariableFourWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] =
    Seq(
      "March Variable Four Weekly Scenarios" ->
        Seq(
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withEmployeeStartDate("2020-10-29")
            .withFurloughEndDate("2021-03-28")
            .withPaymentFrequency(FourWeekly)
            .withEmployeeStartedAfter1Feb2019()
            .withOnPayrollBefore30thOct2020(true)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-02-06".toLocalDate, "2021-03-05".toLocalDate),
                  Period("2021-03-01".toLocalDate, "2021-03-05".toLocalDate)
                ),
                PartialPeriod(
                  Period("2021-03-06".toLocalDate, "2021-04-02".toLocalDate),
                  Period("2021-03-06".toLocalDate, "2021-03-28".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withAnnualPayAmount(1500)
            .withFurloughStartDate("2021-03-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withPayDate(List("2021-02-05", "2021-03-05", "2021-04-02"))
            .withUsualHours(List(UsualHours("2021-03-05".toLocalDate, Hours(40.0)), UsualHours("2021-04-02".toLocalDate, Hours(50.0))))
            .withPartTimeHours(
              List(PartTimeHours("2021-03-05".toLocalDate, Hours(14.0)), PartTimeHours("2021-04-02".toLocalDate, Hours(15.0)))
            )
            -> 188.86,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withFurloughEndDate("2021-03-28")
            .withPaymentFrequency(FourWeekly)
            .withEmployeeStartedOnOrBefore1Feb2019()
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List("2019-03-31" -> 2800))
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
            .withAnnualPayAmount(36000)
            .withFurloughStartDate("2021-03-04")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-28")
            .withPayDate(List("2021-02-28", "2021-03-28"))
            .withUsualHours(List())
            .withPartTimeHours(List())
            -> 2000.00
        )
    )

  val marchVariableMonthlyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] =
    Seq(
      "March Variable Monthly Scenarios" ->
        Seq(
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withEmployeeStartDate("2020-09-29")
            .withFurloughEndDate("2021-03-31")
            .withPaymentFrequency(Monthly)
            .withEmployeeStartedAfter1Feb2019()
            .withOnPayrollBefore30thOct2020(true)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(List(FullPeriod(Period("2021-03-01".toLocalDate, "2021-03-31".toLocalDate))))
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withAnnualPayAmount(10000)
            .withFurloughStartDate("2021-03-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withPayDate(List("2021-02-28", "2021-03-31"))
            .withUsualHours(List(UsualHours("2021-03-31".toLocalDate, Hours(40.0))))
            .withPartTimeHours(List(PartTimeHours("2021-03-31".toLocalDate, Hours(14.0))))
            -> 1053.60,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withEmployeeStartDate("2020-06-01")
            .withFurloughEndDate("2021-03-31")
            .withPaymentFrequency(Monthly)
            .withEmployeeStartedAfter1Feb2019()
            .withOnPayrollBefore30thOct2020(true)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(List(FullPeriod(Period("2021-03-01".toLocalDate, "2021-03-31".toLocalDate))))
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withAnnualPayAmount(10000)
            .withFurloughStartDate("2021-03-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withPayDate(List("2021-02-28", "2021-03-31"))
            .withUsualHours(List(UsualHours("2021-03-31".toLocalDate, Hours(40.0))))
            .withPartTimeHours(List(PartTimeHours("2021-03-31".toLocalDate, Hours(14.0))))
            -> 590.47,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withFurloughEndDate("2021-03-31")
            .withPaymentFrequency(Monthly)
            .withEmployeeStartedOnOrBefore1Feb2019()
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List("2019-03-31" -> 2000))
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
            .withAnnualPayAmount(26000)
            .withFurloughStartDate("2020-03-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withPayDate(List("2021-02-28", "2021-03-31"))
            .withUsualHours(List())
            .withPartTimeHours(List())
            -> 1953.99
        )
    )

  val marchVariableTwoWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] =
    Seq(
      "March Variable Two Weekly Scenarios" ->
        Seq(
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withEmployeeStartDate("2020-08-27")
            .withFurloughEndDate("2021-03-31")
            .withPaymentFrequency(FortNightly)
            .withEmployeeStartedAfter1Feb2019()
            .withOnPayrollBefore30thOct2020(true)
            .withClaimPeriodStart("2021-03-13")
            .withLastYear(List())
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-01".toLocalDate, "2021-03-14".toLocalDate),
                  Period("2021-03-13".toLocalDate, "2021-03-14".toLocalDate)
                ),
                FullPeriod(Period("2021-03-15".toLocalDate, "2021-03-28".toLocalDate)),
                PartialPeriod(
                  Period("2021-03-29".toLocalDate, "2021-04-11".toLocalDate),
                  Period("2021-03-29".toLocalDate, "2021-03-31".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withAnnualPayAmount(34000)
            .withFurloughStartDate("2021-03-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withPayDate(List("2021-02-28", "2021-03-14", "2021-03-28", "2021-04-11"))
            .withUsualHours(
              List(
                UsualHours("2021-03-14".toLocalDate, Hours(40.0)),
                UsualHours("2021-03-28".toLocalDate, Hours(50.0)),
                UsualHours("2021-04-11".toLocalDate, Hours(50.0))
              )
            )
            .withPartTimeHours(
              List(
                PartTimeHours("2021-03-14".toLocalDate, Hours(14.0)),
                PartTimeHours("2021-03-28".toLocalDate, Hours(15.0)),
                PartTimeHours("2021-04-11".toLocalDate, Hours(15.0))
              )
            )

            -> 1081.91,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withEmployeeStartDate("2020-05-20")
            .withFurloughEndDate("2021-03-25")
            .withPaymentFrequency(FortNightly)
            .withEmployeeStartedAfter1Feb2019()
            .withOnPayrollBefore30thOct2020(true)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-01".toLocalDate, "2021-03-14".toLocalDate),
                  Period("2021-03-05".toLocalDate, "2021-03-14".toLocalDate)
                ),
                PartialPeriod(
                  Period("2021-03-15".toLocalDate, "2021-03-28".toLocalDate),
                  Period("2021-03-15".toLocalDate, "2021-03-25".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withAnnualPayAmount(19564.4)
            .withFurloughStartDate("2021-03-05")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-29")
            .withPayDate(List("2021-02-28", "2021-03-14", "2021-03-28"))
            .withUsualHours(List(UsualHours("2021-03-14".toLocalDate, Hours(40.0)), UsualHours("2021-03-28".toLocalDate, Hours(50.0))))
            .withPartTimeHours(
              List(PartTimeHours("2021-03-14".toLocalDate, Hours(14.0)), PartTimeHours("2021-03-28".toLocalDate, Hours(15.0)))
            )
            -> 769.07,
          emptyUserAnswers
            .withRtiSubmission(EmployeeRTISubmission.Yes)
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withEmployeeStartDate("2020-03-19")
            .withFurloughEndDate("2021-03-25")
            .withPaymentFrequency(FortNightly)
            .withEmployeeStartedAfter1Feb2019()
            .withOnPayrollBefore30thOct2020(true)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-01".toLocalDate, "2021-03-14".toLocalDate),
                  Period("2021-03-05".toLocalDate, "2021-03-14".toLocalDate)
                ),
                PartialPeriod(
                  Period("2021-03-15".toLocalDate, "2021-03-28".toLocalDate),
                  Period("2021-03-15".toLocalDate, "2021-03-25".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withAnnualPayAmount(1000)
            .withFurloughStartDate("2021-03-05")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-29")
            .withPayDate(List("2021-02-28", "2021-03-14", "2021-03-28"))
            .withUsualHours(List(UsualHours("2021-03-14".toLocalDate, Hours(40.0)), UsualHours("2021-03-28".toLocalDate, Hours(50.0))))
            .withPartTimeHours(
              List(PartTimeHours("2021-03-14".toLocalDate, Hours(14.0)), PartTimeHours("2021-03-28".toLocalDate, Hours(15.0)))
            )
            -> 631.16,
          emptyUserAnswers
            .withRtiSubmission(EmployeeRTISubmission.No)
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withEmployeeStartDate("2020-03-19")
            .withFurloughEndDate("2021-03-25")
            .withPaymentFrequency(FortNightly)
            .withEmployeeStartedAfter1Feb2019()
            .withOnPayrollBefore30thOct2020(true)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-01".toLocalDate, "2021-03-14".toLocalDate),
                  Period("2021-03-05".toLocalDate, "2021-03-14".toLocalDate)
                ),
                PartialPeriod(
                  Period("2021-03-15".toLocalDate, "2021-03-28".toLocalDate),
                  Period("2021-03-15".toLocalDate, "2021-03-25".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withAnnualPayAmount(1000)
            .withFurloughStartDate("2021-03-05")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-29")
            .withPayDate(List("2021-02-28", "2021-03-14", "2021-03-28"))
            .withUsualHours(List(UsualHours("2021-03-14".toLocalDate, Hours(40.0)), UsualHours("2021-03-28".toLocalDate, Hours(50.0))))
            .withPartTimeHours(
              List(PartTimeHours("2021-03-14".toLocalDate, Hours(14.0)), PartTimeHours("2021-03-28".toLocalDate, Hours(15.0)))
            )
            -> 34.08,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withFurloughEndDate("2021-03-28")
            .withPaymentFrequency(FortNightly)
            .withEmployeeStartedOnOrBefore1Feb2019()
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List("2019-03-17" -> 840, "2020-03-31" -> 980))
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
            .withAnnualPayAmount(28000)
            .withFurloughStartDate("2021-03-04")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-28")
            .withPayDate(List("2021-02-28", "2021-03-14", "2021-03-28"))
            .withUsualHours(List())
            .withPartTimeHours(List())
            -> 1530.00,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withEmployeeStartDate("2020-01-01")
            .withFurloughEndDate("2021-03-31")
            .withPaymentFrequency(FortNightly)
            .withEmployeeStartedAfter1Feb2019()
            .withOnPayrollBefore30thOct2020(true)
            .withClaimPeriodStart("2021-03-01")
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                FullPeriod(Period("2021-03-01".toLocalDate, "2021-03-14".toLocalDate)),
                FullPeriod(Period("2021-03-15".toLocalDate, "2021-03-28".toLocalDate)),
                PartialPeriod(
                  Period("2021-03-29".toLocalDate, "2021-04-11".toLocalDate),
                  Period("2021-03-29".toLocalDate, "2021-03-31".toLocalDate)
                )
              )
            )
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withAnnualPayAmount(10000)
            .withFurloughStartDate("2021-03-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withPayDate(List("2021-02-28", "2021-03-14", "2021-03-28", "2021-04-11"))
            .withUsualHours(
              List(
                UsualHours("2021-03-14".toLocalDate, Hours(40.0)),
                UsualHours("2021-03-28".toLocalDate, Hours(50.0)),
                UsualHours("2021-04-11".toLocalDate, Hours(50.0))
              )
            )
            .withPartTimeHours(
              List(
                PartTimeHours("2021-03-14".toLocalDate, Hours(14.0)),
                PartTimeHours("2021-03-28".toLocalDate, Hours(15.0)),
                PartTimeHours("2021-04-11".toLocalDate, Hours(15.0))
              )
            )
            -> 1727.06
        )
    )

  val marchVariableWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] =
    Seq(
      "March Variable Weekly Scenarios" ->
        Seq(
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withEmployeeStartDate("2020-07-03")
            .withFurloughEndDate("2021-03-14")
            .withPaymentFrequency(Weekly)
            .withEmployeeStartedAfter1Feb2019()
            .withOnPayrollBefore30thOct2020(true)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-01".toLocalDate, "2021-03-07".toLocalDate),
                  Period("2021-03-04".toLocalDate, "2021-03-07".toLocalDate)
                ),
                FullPeriod(Period("2021-03-08".toLocalDate, "2021-03-14".toLocalDate))
              )
            )
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withAnnualPayAmount(19564.4)
            .withFurloughStartDate("2021-03-04")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withPayDate(List("2021-02-28", "2021-03-07", "2021-03-14"))
            .withUsualHours(List(UsualHours("2021-03-07".toLocalDate, Hours(40.0)), UsualHours("2021-03-14".toLocalDate, Hours(50.0))))
            .withPartTimeHours(
              List(PartTimeHours("2021-03-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-03-14".toLocalDate, Hours(15.0)))
            )
            -> 481.08,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withEmployeeStartDate("2020-06-04")
            .withFurloughEndDate("2021-03-14")
            .withPaymentFrequency(Weekly)
            .withEmployeeStartedAfter1Feb2019()
            .withOnPayrollBefore30thOct2020(true)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-01".toLocalDate, "2021-03-07".toLocalDate),
                  Period("2021-03-04".toLocalDate, "2021-03-07".toLocalDate)
                ),
                FullPeriod(Period("2021-03-08".toLocalDate, "2021-03-14".toLocalDate))
              )
            )
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withAnnualPayAmount(26000)
            .withFurloughStartDate("2021-03-04")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withPayDate(List("2021-02-28", "2021-03-07", "2021-03-14"))
            .withUsualHours(List(UsualHours("2021-03-07".toLocalDate, Hours(40.0)), UsualHours("2021-03-14".toLocalDate, Hours(50.0))))
            .withPartTimeHours(
              List(PartTimeHours("2021-03-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-03-14".toLocalDate, Hours(15.0)))
            )
            -> 571.44,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withEmployeeStartDate("2020-06-04")
            .withFurloughEndDate("2021-03-14")
            .withPaymentFrequency(Weekly)
            .withEmployeeStartedAfter1Feb2019()
            .withOnPayrollBefore30thOct2020(true)
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List())
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                PartialPeriod(
                  Period("2021-03-01".toLocalDate, "2021-03-07".toLocalDate),
                  Period("2021-03-04".toLocalDate, "2021-03-07".toLocalDate)
                ),
                FullPeriod(Period("2021-03-08".toLocalDate, "2021-03-14".toLocalDate))
              )
            )
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withAnnualPayAmount(1000)
            .withFurloughStartDate("2021-03-04")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withPayDate(List("2021-02-28", "2021-03-07", "2021-03-14"))
            .withUsualHours(List(UsualHours("2021-03-07".toLocalDate, Hours(40.0)), UsualHours("2021-03-14".toLocalDate, Hours(50.0))))
            .withPartTimeHours(
              List(PartTimeHours("2021-03-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-03-14".toLocalDate, Hours(15.0)))
            )
            -> 21.96,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withFurloughEndDate("2021-03-07")
            .withPaymentFrequency(Weekly)
            .withEmployeeStartedOnOrBefore1Feb2019()
            .withClaimPeriodStart("2021-03-01")
            .withLastYear(List("2019-03-03" -> 420, "2019-03-10" -> 490))
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(List(FullPeriod(Period("2021-03-01".toLocalDate, "2021-03-07".toLocalDate))))
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withAnnualPayAmount(26000)
            .withFurloughStartDate("2021-03-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withPayDate(List("2021-02-28", "2021-03-07"))
            .withUsualHours(List(UsualHours("2021-03-07".toLocalDate, Hours(40.0))))
            .withPartTimeHours(List(PartTimeHours("2021-03-07".toLocalDate, Hours(14.0))))
            -> 258.58,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withFurloughEndDate("2021-03-24")
            .withPaymentFrequency(Weekly)
            .withEmployeeStartedOnOrBefore1Feb2019()
            .withClaimPeriodStart("2021-03-03")
            .withLastYear(List("2019-03-10" -> 420, "2019-03-17" -> 490, "2019-03-24" -> 560, "2019-03-31" -> 630))
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
            .withAnnualPayAmount(26000)
            .withFurloughStartDate("2021-03-04")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-28")
            .withPayDate(List("2021-02-28", "2021-03-07", "2021-03-14", "2021-03-21", "2021-03-28"))
            .withUsualHours(List())
            .withPartTimeHours(List())
            -> 1241.15,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withEmployeeStartDate("2020-01-01")
            .withFurloughEndDate("2021-03-14")
            .withPaymentFrequency(Weekly)
            .withEmployeeStartedAfter1Feb2019()
            .withOnPayrollBefore30thOct2020(true)
            .withClaimPeriodStart("2021-03-01")
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                FullPeriod(Period("2021-03-01".toLocalDate, "2021-03-07".toLocalDate)),
                FullPeriod(Period("2021-03-08".toLocalDate, "2021-03-14".toLocalDate))
              )
            )
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withAnnualPayAmount(10000)
            .withFurloughStartDate("2021-03-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withClaimPeriodEnd("2021-03-31")
            .withPayDate(List("2021-02-28", "2021-03-07", "2021-03-14"))
            .withUsualHours(List(UsualHours("2021-03-07".toLocalDate, Hours(40.0)), UsualHours("2021-03-14".toLocalDate, Hours(50.0))))
            .withPartTimeHours(
              List(PartTimeHours("2021-03-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-03-14".toLocalDate, Hours(15.0)))
            )
            -> 778.84,
          emptyUserAnswers
            .withFurloughStatus(FurloughStatus.FurloughEnded)
            .withEmployeeStartDate("2020-01-01")
            .withFurloughEndDate("2021-03-14")
            .withPaymentFrequency(Weekly)
            .withEmployeeStartedAfter1Feb2019()
            .withOnPayrollBefore30thOct2020(true)
            .withClaimPeriodStart("2021-03-01")
            .withFurloughInLastTaxYear(false)
            .withPayPeriodsList(PayPeriodsList.Yes)
            .withPartTimePeriods(
              List(
                FullPeriod(Period("2021-03-01".toLocalDate, "2021-03-07".toLocalDate)),
                FullPeriod(Period("2021-03-08".toLocalDate, "2021-03-14".toLocalDate))
              )
            )
            .withPayMethod(PayMethod.Variable)
            .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
            .withAnnualPayAmount(10000)
            .withFurloughStartDate("2021-02-01")
            .withPreviousFurloughedPeriodsAnswer(false)
            .withFirstFurloughDate("2020-04-01")
            .withClaimPeriodEnd("2021-03-31")
            .withPayDate(List("2021-02-28", "2021-03-07", "2021-03-14"))
            .withUsualHours(List(UsualHours("2021-03-07".toLocalDate, Hours(40.0)), UsualHours("2021-03-14".toLocalDate, Hours(50.0))))
            .withPartTimeHours(
              List(PartTimeHours("2021-03-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-03-14".toLocalDate, Hours(15.0)))
            )
            -> 778.84
        )
    )

}
