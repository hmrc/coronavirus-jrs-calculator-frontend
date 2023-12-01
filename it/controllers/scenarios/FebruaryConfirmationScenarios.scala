package controllers.scenarios

import assets._
import models.PaymentFrequency._
import models._
import utils._

object FebruaryConfirmationScenarios
    extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants with ITCoreTestData {

  val februaryFourWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "February Fixed Four Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-28")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(List(FullPeriod(Period("2021-02-01".toLocalDate, "2021-02-28".toLocalDate))))
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2000)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-31", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-28".toLocalDate, Hours(148.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-28".toLocalDate, Hours(40.0))))
        -> 1167.57,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-27")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withRegularPayAmount(3300)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-04", "2021-02-01", "2021-03-01"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 2410.83,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withRegularPayAmount(3300)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-31", "2021-02-28"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 2307.68,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-02-28")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-28".toLocalDate, "2021-03-27".toLocalDate),
              Period("2021-02-28".toLocalDate, "2021-02-28".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2200)
        .withFurloughStartDate("2021-02-28")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-02-27", "2021-03-27"))
        .withUsualHours(List(UsualHours("2021-03-27".toLocalDate, Hours(148.0))))
        .withPartTimeHours(List(PartTimeHours("2021-03-27".toLocalDate, Hours(25.0))))
        -> 52.24,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-28")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-02-10")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-06".toLocalDate, "2021-03-05".toLocalDate),
              Period("2021-02-10".toLocalDate, "2021-02-28".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(3500)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-02-05", "2021-03-05"))
        .withUsualHours(List(UsualHours("2021-03-05".toLocalDate, Hours(15.86))))
        .withPartTimeHours(List(PartTimeHours("2021-03-05".toLocalDate, Hours(1.86))))
        -> 1497.55,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-28")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-02-10")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-06".toLocalDate, "2021-03-05".toLocalDate),
              Period("2021-02-10".toLocalDate, "2021-02-28".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1500)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-02-05", "2021-03-05"))
        .withUsualHours(List(UsualHours("2021-03-05".toLocalDate, Hours(22))))
        .withPartTimeHours(List(PartTimeHours("2021-03-05".toLocalDate, Hours(17))))
        -> 185.06,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-06".toLocalDate, "2021-02-02".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2654.11)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-05", "2021-02-02"))
        .withUsualHours(List(UsualHours("2021-02-02".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-02-02".toLocalDate, Hours(11.4))))
        -> 101.55,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-08".toLocalDate, "2021-02-04".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(3200.11)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-07", "2021-02-04"))
        .withUsualHours(List(UsualHours("2021-02-04".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-02-04".toLocalDate, Hours(11.4))))
        -> 119.57,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-01")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-10".toLocalDate, "2021-02-06".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2322.11)
        .withFurloughStartDate("2021-01-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-09", "2021-02-06"))
        .withUsualHours(List(UsualHours("2021-02-06".toLocalDate, Hours(10.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-06".toLocalDate, Hours(6.5))))
        -> 23.22,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-02")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-05".toLocalDate, "2021-02-01".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-01".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-02-02".toLocalDate, "2021-03-01".toLocalDate),
              Period("2021-02-02".toLocalDate, "2021-02-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1465.55)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-04", "2021-02-01", "2021-03-01"))
        .withUsualHours(List(UsualHours("2021-02-01".toLocalDate, Hours(7.5)), UsualHours("2021-03-01".toLocalDate, Hours(7.5))))
        .withPartTimeHours(List(PartTimeHours("2021-02-01".toLocalDate, Hours(3.0)), PartTimeHours("2021-03-01".toLocalDate, Hours(3.0))))
        -> 50.24
    )
  )

  val februaryMonthlyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "February Fixed Monthly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(List(FullPeriod(Period("2021-02-01".toLocalDate, "2021-02-28".toLocalDate))))
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2400)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-01-31", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-28".toLocalDate, Hours(160.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-28".toLocalDate, Hours(40.0))))
        -> 1440.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withRegularPayAmount(3126)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-01-31", "2021-02-28"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 2500.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-21")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-01".toLocalDate, "2021-02-28".toLocalDate),
              Period("2021-02-05".toLocalDate, "2021-02-21".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2400)
        .withFurloughStartDate("2021-02-05")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-01-31", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-28".toLocalDate, Hours(127.5))))
        .withPartTimeHours(List(PartTimeHours("2021-02-28".toLocalDate, Hours(52.5))))
        -> 685.71,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-21")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-01".toLocalDate, "2021-02-28".toLocalDate),
              Period("2021-02-05".toLocalDate, "2021-02-21".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(6500)
        .withFurloughStartDate("2021-02-05")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-01-31", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-28".toLocalDate, Hours(127.5))))
        .withPartTimeHours(List(PartTimeHours("2021-02-28".toLocalDate, Hours(52.5))))
        -> 892.90,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-11")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-01".toLocalDate, "2021-02-28".toLocalDate),
              Period("2021-02-02".toLocalDate, "2021-02-11".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2400)
        .withFurloughStartDate("2021-02-02")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-01-31", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-28".toLocalDate, Hours(160.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-28".toLocalDate, Hours(40.0))))
        -> 514.29,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-20")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-02-02")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-01".toLocalDate, "2021-02-28".toLocalDate),
              Period("2021-02-02".toLocalDate, "2021-02-20".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(5555)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-21")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-01-31", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-28".toLocalDate, Hours(160.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-28".toLocalDate, Hours(40.0))))
        -> 1272.38,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-26".toLocalDate, "2021-02-25".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-25".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-02-26".toLocalDate, "2021-03-25".toLocalDate),
              Period("2021-02-26".toLocalDate, "2021-02-28".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(4900)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-01-25", "2021-02-25", "2021-03-25"))
        .withUsualHours(List(UsualHours("2021-02-25".toLocalDate, Hours(160.0)), UsualHours("2021-03-25".toLocalDate, Hours(160.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-25".toLocalDate, Hours(95.0)), PartTimeHours("2021-03-25".toLocalDate, Hours(95.0))))
        -> 1015.67,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-01".toLocalDate, "2021-02-28".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2654.11)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-01-31", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-28".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-02-28".toLocalDate, Hours(11.4))))
        -> 101.55,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-27".toLocalDate, "2021-02-25".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2654.11)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-01-26", "2021-02-25"))
        .withUsualHours(List(UsualHours("2021-02-25".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-02-25".toLocalDate, Hours(11.4))))
        -> 94.78,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-03".toLocalDate, "2021-02-02".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(3200.11)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-01-02", "2021-02-02"))
        .withUsualHours(List(UsualHours("2021-02-02".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-02-02".toLocalDate, Hours(11.4))))
        -> 110.59,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-01")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-10".toLocalDate, "2021-02-08".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2322.11)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-01-09", "2021-02-08"))
        .withUsualHours(List(UsualHours("2021-02-08".toLocalDate, Hours(10.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-08".toLocalDate, Hours(6.5))))
        -> 21.67,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-03".toLocalDate, "2021-02-01".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-01".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-02-02".toLocalDate, "2021-03-01".toLocalDate),
              Period("2021-02-02".toLocalDate, "2021-02-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1465.55)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-01-02", "2021-02-01", "2021-03-01"))
        .withUsualHours(List(UsualHours("2021-02-01".toLocalDate, Hours(7.5)), UsualHours("2021-03-01".toLocalDate, Hours(7.5))))
        .withPartTimeHours(List(PartTimeHours("2021-02-01".toLocalDate, Hours(3.0)), PartTimeHours("2021-03-01".toLocalDate, Hours(3.0))))
        -> 48.57,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-01".toLocalDate, "2021-02-28".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(3222)
        .withFurloughStartDate("2020-12-30")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-01")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-01-31", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-28".toLocalDate, Hours(7.5))))
        .withPartTimeHours(List(PartTimeHours("2021-02-28".toLocalDate, Hours(3.0))))
        -> 53.57
    )
  )

  val februaryTwoWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "February Fixed Two Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-02-01".toLocalDate, "2021-02-14".toLocalDate)),
            FullPeriod(Period("2021-02-15".toLocalDate, "2021-02-28".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(650)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-31", "2021-02-14", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-14".toLocalDate, Hours(98.0)), UsualHours("2021-02-28".toLocalDate, Hours(98.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-14".toLocalDate, Hours(48.0)), PartTimeHours("2021-02-28".toLocalDate, Hours(48.0))))
        -> 530.60,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-02-01".toLocalDate, "2021-02-14".toLocalDate)),
            FullPeriod(Period("2021-02-15".toLocalDate, "2021-02-28".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2300.12)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-31", "2021-02-14", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-14".toLocalDate, Hours(98.0)), UsualHours("2021-02-28".toLocalDate, Hours(98.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-14".toLocalDate, Hours(48.0)), PartTimeHours("2021-02-28".toLocalDate, Hours(48.0))))
        -> 1177.38,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-02-27")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-27".toLocalDate, "2021-03-12".toLocalDate),
              Period("2021-02-27".toLocalDate, "2021-02-28".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(789.12)
        .withFurloughStartDate("2021-02-27")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-02-26", "2021-03-12"))
        .withUsualHours(List(UsualHours("2021-03-12".toLocalDate, Hours(140.0))))
        .withPartTimeHours(List(PartTimeHours("2021-03-12".toLocalDate, Hours(50.0))))
        -> 57.98,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-28")
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-01".toLocalDate, "2021-02-14".toLocalDate),
              Period("2021-02-05".toLocalDate, "2021-02-14".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-02-15".toLocalDate, "2021-02-28".toLocalDate),
              Period("2021-02-15".toLocalDate, "2021-02-28".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(643.12)
        .withFurloughStartDate("2021-02-05")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-31", "2021-02-14", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-14".toLocalDate, Hours(63.0)), UsualHours("2021-02-28".toLocalDate, Hours(77.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-14".toLocalDate, Hours(8.0)), PartTimeHours("2021-02-28".toLocalDate, Hours(12.0))))
        -> 755.14,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-19".toLocalDate, "2021-02-01".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-01".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-02-02".toLocalDate, "2021-02-15".toLocalDate),
              Period("2021-02-02".toLocalDate, "2021-02-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1000.34)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-18", "2021-02-01", "2021-02-15"))
        .withUsualHours(List(UsualHours("2021-02-01".toLocalDate, Hours(34.5)), UsualHours("2021-02-15".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-02-01".toLocalDate, Hours(11.4)), PartTimeHours("2021-02-15".toLocalDate, Hours(11.4))))
        -> 76.54,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-22".toLocalDate, "2021-02-04".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1500)
        .withFurloughStartDate("2020-04-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-21", "2021-02-04"))
        .withUsualHours(List(UsualHours("2021-02-04".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-02-04".toLocalDate, Hours(11.4))))
        -> 114.78,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-01")
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-23".toLocalDate, "2021-02-05".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1010.11)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-22", "2021-02-05"))
        .withUsualHours(List(UsualHours("2021-02-05".toLocalDate, Hours(10.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-05".toLocalDate, Hours(6.5))))
        -> 20.20,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-02")
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-19".toLocalDate, "2021-02-01".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-01".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-02-02".toLocalDate, "2021-02-15".toLocalDate),
              Period("2021-02-02".toLocalDate, "2021-02-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1465.55)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-18", "2021-02-01", "2021-02-15"))
        .withUsualHours(List(UsualHours("2021-02-01".toLocalDate, Hours(7.5)), UsualHours("2021-02-15".toLocalDate, Hours(7.5))))
        .withPartTimeHours(List(PartTimeHours("2021-02-01".toLocalDate, Hours(3.0)), PartTimeHours("2021-02-15".toLocalDate, Hours(3.0))))
        -> 100.50
    )
  )

  val februaryWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "February Fixed Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-02-01".toLocalDate, "2021-02-07".toLocalDate)),
            FullPeriod(Period("2021-02-08".toLocalDate, "2021-02-14".toLocalDate)),
            FullPeriod(Period("2021-02-15".toLocalDate, "2021-02-21".toLocalDate)),
            FullPeriod(Period("2021-02-22".toLocalDate, "2021-02-28".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(600)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-31", "2021-02-07", "2021-02-14", "2021-02-21", "2021-02-28"))
        .withUsualHours(
          List(
            UsualHours("2021-02-07".toLocalDate, Hours(37.0)),
            UsualHours("2021-02-14".toLocalDate, Hours(37.0)),
            UsualHours("2021-02-21".toLocalDate, Hours(37.0)),
            UsualHours("2021-02-28".toLocalDate, Hours(37.0))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-02-07".toLocalDate, Hours(10.0)),
            PartTimeHours("2021-02-14".toLocalDate, Hours(12.0)),
            PartTimeHours("2021-02-21".toLocalDate, Hours(10.0)),
            PartTimeHours("2021-02-28".toLocalDate, Hours(15.0))
          )
        )
        -> 1310.28,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-02-05")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-04".toLocalDate, "2021-02-10".toLocalDate),
              Period("2021-02-06".toLocalDate, "2021-02-10".toLocalDate)
            ),
            FullPeriod(Period("2021-02-11".toLocalDate, "2021-02-17".toLocalDate)),
            FullPeriod(Period("2021-02-18".toLocalDate, "2021-02-24".toLocalDate)),
            PartialPeriod(
              Period("2021-02-25".toLocalDate, "2021-03-02".toLocalDate),
              Period("2021-02-25".toLocalDate, "2021-02-28".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1200)
        .withFurloughStartDate("2021-02-06")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-02-03", "2021-02-10", "2021-02-17", "2021-02-24", "2021-03-02"))
        .withUsualHours(
          List(
            UsualHours("2021-02-10".toLocalDate, Hours(37.0)),
            UsualHours("2021-02-17".toLocalDate, Hours(37.0)),
            UsualHours("2021-02-24".toLocalDate, Hours(37.0)),
            UsualHours("2021-02-28".toLocalDate, Hours(37.0))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-02-10".toLocalDate, Hours(10.0)),
            PartTimeHours("2021-02-17".toLocalDate, Hours(12.0)),
            PartTimeHours("2021-02-24".toLocalDate, Hours(10.0)),
            PartTimeHours("2021-02-28".toLocalDate, Hours(15.0))
          )
        )
        -> 1493.76,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-27")
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-02-02")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-04".toLocalDate, "2021-02-10".toLocalDate),
              Period("2021-02-06".toLocalDate, "2021-02-10".toLocalDate)
            ),
            FullPeriod(Period("2021-02-11".toLocalDate, "2021-02-17".toLocalDate)),
            FullPeriod(Period("2021-02-18".toLocalDate, "2021-02-24".toLocalDate)),
            PartialPeriod(
              Period("2021-02-25".toLocalDate, "2021-03-02".toLocalDate),
              Period("2021-02-25".toLocalDate, "2021-02-27".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1200)
        .withFurloughStartDate("2021-02-06")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-27")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-02-03", "2021-02-10", "2021-02-17", "2021-02-24", "2021-03-02"))
        .withUsualHours(List(UsualHours("2021-02-10".toLocalDate, Hours(37.0)), UsualHours("2021-03-02".toLocalDate, Hours(15.86))))
        .withPartTimeHours(List(PartTimeHours("2021-02-10".toLocalDate, Hours(10.0)), PartTimeHours("2021-03-02".toLocalDate, Hours(1.06))))
        -> 1729.60,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-02-15")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-02-15".toLocalDate, "2021-02-21".toLocalDate)),
            FullPeriod(Period("2021-02-22".toLocalDate, "2021-02-28".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(600)
        .withFurloughStartDate("2021-02-15")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-02-14", "2021-02-21", "2021-02-28"))
        .withUsualHours(
          List(
            UsualHours("2021-02-21".toLocalDate, Hours(37.0)),
            UsualHours("2021-02-28".toLocalDate, Hours(37.0))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-02-21".toLocalDate, Hours(10.0)),
            PartTimeHours("2021-02-28".toLocalDate, Hours(15.0))
          )
        )
        -> 635.68,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-02-28")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-28".toLocalDate, "2021-03-06".toLocalDate),
              Period("2021-02-28".toLocalDate, "2021-02-28".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(550)
        .withFurloughStartDate("2021-02-28")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-02-27", "2021-03-06"))
        .withUsualHours(List(UsualHours("2021-03-06".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-03-06".toLocalDate, Hours(14.0))))
        -> 40.86,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-08")
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-02-01".toLocalDate, "2021-02-07".toLocalDate)),
            PartialPeriod(
              Period("2021-02-08".toLocalDate, "2021-02-14".toLocalDate),
              Period("2021-02-08".toLocalDate, "2021-02-08".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(600)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-21")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-31", "2021-02-07", "2021-02-14"))
        .withUsualHours(List(UsualHours("2021-02-07".toLocalDate, Hours(37.0)), UsualHours("2021-02-14".toLocalDate, Hours(37.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-07".toLocalDate, Hours(10.0)), PartTimeHours("2021-02-14".toLocalDate, Hours(12.0))))
        -> 396.60,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-31".toLocalDate, "2021-02-06".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(666.12)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-30", "2021-02-06"))
        .withUsualHours(List(UsualHours("2021-02-06".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-02-06".toLocalDate, Hours(11.4))))
        -> 101.94,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-26".toLocalDate, "2021-02-01".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-01".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-02-02".toLocalDate, "2021-02-08".toLocalDate),
              Period("2021-02-02".toLocalDate, "2021-02-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(890.11)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-25", "2021-02-01", "2021-02-08"))
        .withUsualHours(
          List(
            UsualHours("2021-02-01".toLocalDate, Hours(34.5)),
            UsualHours("2021-02-08".toLocalDate, Hours(34.5))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-02-01".toLocalDate, Hours(11.4)),
            PartTimeHours("2021-02-08".toLocalDate, Hours(11.4))
          )
        )
        -> 119.58,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-01")
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-28".toLocalDate, "2021-02-04".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(500)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-28", "2021-02-04"))
        .withUsualHours(List(UsualHours("2021-02-04".toLocalDate, Hours(10.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-04".toLocalDate, Hours(6.5))))
        -> 20.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-02")
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-26".toLocalDate, "2021-02-01".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-01".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-02-02".toLocalDate, "2021-02-08".toLocalDate),
              Period("2021-02-02".toLocalDate, "2021-02-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(754.44)
        .withFurloughStartDate("2020-12-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-01-25", "2021-02-01", "2021-02-08"))
        .withUsualHours(List(UsualHours("2021-02-01".toLocalDate, Hours(7.5))))
        .withPartTimeHours(List(PartTimeHours("2021-02-01".toLocalDate, Hours(3.0))))
        -> 137.96
    )
  )

  val februaryVariableFourWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "February Variable Four Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-10-29")
        .withFurloughEndDate("2021-02-28")
        .withPaymentFrequency(FourWeekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-01-06".toLocalDate, "2021-02-02".toLocalDate),
              Period("2021-02-01".toLocalDate, "2021-02-02".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-02-03".toLocalDate, "2021-03-02".toLocalDate),
              Period("2021-02-03".toLocalDate, "2021-02-28".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(1500)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-01-05", "2021-02-02", "2021-03-02"))
        .withUsualHours(List(UsualHours("2021-02-02".toLocalDate, Hours(40.0)), UsualHours("2021-03-02".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-02".toLocalDate, Hours(14.0)), PartTimeHours("2021-03-02".toLocalDate, Hours(15.0))))
        -> 246.32,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-04-04")
        .withFurloughEndDate("2021-02-28")
        .withPaymentFrequency(FourWeekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-02-28")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-28".toLocalDate, "2021-03-26".toLocalDate),
              Period("2021-02-28".toLocalDate, "2021-02-28".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(19564.4)
        .withFurloughStartDate("2021-02-28")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-02-27", "2021-03-26"))
        .withUsualHours(List(UsualHours("2021-03-26".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-03-26".toLocalDate, Hours(14.0))))
        -> 31.02,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-28")
        .withPaymentFrequency(FourWeekly)
        .withEmployeeStartedOnOrBefore1Feb2019()
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List("2020-03-01" -> 2800))
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withAnnualPayAmount(36000)
        .withFurloughStartDate("2021-02-04")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-01-31", "2021-02-28"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 2000.00
    )
  )

  val februaryVariableMonthlyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "February Variable Monthly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-08-01")
        .withFurloughEndDate("2021-02-28")
        .withPaymentFrequency(Monthly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(List(FullPeriod(Period("2021-02-01".toLocalDate, "2021-02-28".toLocalDate))))
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(10000)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-01-31", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-28".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-28".toLocalDate, Hours(14.0))))
        -> 791.34,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-04-01")
        .withFurloughEndDate("2021-02-28")
        .withPaymentFrequency(Monthly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(List(FullPeriod(Period("2021-02-01".toLocalDate, "2021-02-28".toLocalDate))))
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(10000)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-01-31", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-28".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-28".toLocalDate, Hours(14.0))))
        -> 483.68,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-28")
        .withPaymentFrequency(Monthly)
        .withEmployeeStartedOnOrBefore1Feb2019()
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List("2020-02-28" -> 2000))
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withAnnualPayAmount(26000)
        .withFurloughStartDate("2020-03-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-01-31", "2021-02-28"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 1764.90
    )
  )

  val februaryVariableTwoWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "February Variable Two Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-08-27")
        .withFurloughEndDate("2021-02-28")
        .withPaymentFrequency(FortNightly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-02-13")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-01".toLocalDate, "2021-02-14".toLocalDate),
              Period("2021-02-13".toLocalDate, "2021-02-14".toLocalDate)
            ),
            FullPeriod(Period("2021-02-15".toLocalDate, "2021-02-28".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(34000)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-01-31", "2021-02-14", "2021-02-28"))
        .withUsualHours(
          List(
            UsualHours("2021-02-14".toLocalDate, Hours(40.0)),
            UsualHours("2021-02-28".toLocalDate, Hours(50.0))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-02-14".toLocalDate, Hours(14.0)),
            PartTimeHours("2021-02-28".toLocalDate, Hours(15.0))
          )
        )
        -> 923.77,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-03-20")
        .withFurloughEndDate("2021-02-28")
        .withPaymentFrequency(FortNightly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-01".toLocalDate, "2021-02-14".toLocalDate),
              Period("2021-02-05".toLocalDate, "2021-02-14".toLocalDate)
            ),
            FullPeriod(Period("2021-02-15".toLocalDate, "2021-02-28".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(19564.4)
        .withFurloughStartDate("2021-02-05")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-01-31", "2021-02-14", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-14".toLocalDate, Hours(40.0)), UsualHours("2021-02-28".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-14".toLocalDate, Hours(14.0)), PartTimeHours("2021-02-28".toLocalDate, Hours(15.0))))
        -> 836.52,
      emptyUserAnswers
        .withRtiSubmission(EmployeeRTISubmission.Yes)
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-03-19")
        .withFurloughEndDate("2021-02-28")
        .withPaymentFrequency(FortNightly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-01".toLocalDate, "2021-02-14".toLocalDate),
              Period("2021-02-05".toLocalDate, "2021-02-14".toLocalDate)
            ),
            FullPeriod(Period("2021-02-15".toLocalDate, "2021-03-28".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(1000)
        .withFurloughStartDate("2021-02-05")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-01-31", "2021-02-14", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-14".toLocalDate, Hours(40.0)), UsualHours("2021-02-28".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-14".toLocalDate, Hours(14.0)), PartTimeHours("2021-02-28".toLocalDate, Hours(15.0))))
        -> 724.50,
      emptyUserAnswers
        .withRtiSubmission(EmployeeRTISubmission.No)
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-03-19")
        .withFurloughEndDate("2021-03-28")
        .withPaymentFrequency(FortNightly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-01".toLocalDate, "2021-02-14".toLocalDate),
              Period("2021-02-05".toLocalDate, "2021-02-14".toLocalDate)
            ),
            FullPeriod(Period("2021-02-15".toLocalDate, "2021-02-28".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(1000)
        .withFurloughStartDate("2021-02-05")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-01-31", "2021-02-14", "2021-02-28"))
        .withUsualHours(List(UsualHours("2021-02-14".toLocalDate, Hours(40.0)), UsualHours("2021-02-28".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-14".toLocalDate, Hours(14.0)), PartTimeHours("2021-02-28".toLocalDate, Hours(15.0))))
        -> 42.77,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-28")
        .withPaymentFrequency(FortNightly)
        .withEmployeeStartedOnOrBefore1Feb2019()
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List("2020-02-16" -> 840, "2020-02-28" -> 980))
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withAnnualPayAmount(28000)
        .withFurloughStartDate("2021-02-04")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-01-31", "2021-02-14", "2021-02-28"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 1530.00
    )
  )

  val februaryVariableWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "February Variable Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-05-05")
        .withFurloughEndDate("2021-02-14")
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-01".toLocalDate, "2021-02-07".toLocalDate),
              Period("2021-02-04".toLocalDate, "2021-02-07".toLocalDate)
            ),
            FullPeriod(Period("2021-02-08".toLocalDate, "2021-02-14".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(19564.4)
        .withFurloughStartDate("2021-02-04")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-01-31", "2021-02-07", "2021-02-14"))
        .withUsualHours(List(UsualHours("2021-02-07".toLocalDate, Hours(40.0)), UsualHours("2021-02-14".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-02-14".toLocalDate, Hours(15.0))))
        -> 426.84,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-04-01")
        .withFurloughEndDate("2021-02-14")
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-01".toLocalDate, "2021-02-07".toLocalDate),
              Period("2021-02-04".toLocalDate, "2021-02-07".toLocalDate)
            ),
            FullPeriod(Period("2021-02-08".toLocalDate, "2021-02-14".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(26000)
        .withFurloughStartDate("2021-02-04")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-01-31", "2021-02-07", "2021-02-14"))
        .withUsualHours(List(UsualHours("2021-02-07".toLocalDate, Hours(40.0)), UsualHours("2021-02-14".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-02-14".toLocalDate, Hours(15.0))))
        -> 513.18,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-04-01")
        .withFurloughEndDate("2021-02-14")
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-02-01".toLocalDate, "2021-02-07".toLocalDate),
              Period("2021-02-04".toLocalDate, "2021-02-07".toLocalDate)
            ),
            FullPeriod(Period("2021-02-08".toLocalDate, "2021-02-14".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(1000)
        .withFurloughStartDate("2021-02-04")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-01-31", "2021-02-07", "2021-02-14"))
        .withUsualHours(List(UsualHours("2021-02-07".toLocalDate, Hours(40.0)), UsualHours("2021-02-14".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-02-14".toLocalDate, Hours(15.0))))
        -> 19.74,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-07")
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedOnOrBefore1Feb2019()
        .withClaimPeriodStart("2021-02-01")
        .withLastYear(List("2020-02-02" -> 420, "2020-02-09" -> 490))
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(List(FullPeriod(Period("2021-02-01".toLocalDate, "2021-02-07".toLocalDate))))
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(26000)
        .withFurloughStartDate("2021-02-01")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-01-31", "2021-02-07"))
        .withUsualHours(List(UsualHours("2021-02-07".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-02-07".toLocalDate, Hours(14.0))))
        -> 258.58,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-02-24")
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedOnOrBefore1Feb2019()
        .withClaimPeriodStart("2021-02-03")
        .withLastYear(List("2020-02-09" -> 420, "2020-02-16" -> 490, "2020-02-23" -> 560, "2020-02-28" -> 630))
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withAnnualPayAmount(26000)
        .withFurloughStartDate("2021-02-04")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withClaimPeriodEnd("2021-02-28")
        .withPayDate(List("2021-01-31", "2021-02-07", "2021-02-14", "2021-02-21", "2021-02-28"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 1227.65
    )
  )
}
