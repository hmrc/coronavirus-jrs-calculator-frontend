package controllers.scenarios

import assets.BaseITConstants
import models.PaymentFrequency._
import models._
import utils.{CreateRequestHelper, CustomMatchers, ITCoreTestData, IntegrationSpecBase}

object MayConfirmationScenarios
    extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants with ITCoreTestData {

  val mayFixedFourWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "May Fixed Four Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-31")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-05-01".toLocalDate, "2021-05-28".toLocalDate)),
            PartialPeriod(
              Period("2021-05-29".toLocalDate, "2021-06-25".toLocalDate),
              Period("2021-05-29".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2000)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-30", "2021-05-28", "2021-06-25"))
        .withUsualHours(List(UsualHours("2021-05-28".toLocalDate, Hours(148.0)), UsualHours("2021-06-25".toLocalDate, Hours(15.86))))
        .withPartTimeHours(List(PartTimeHours("2021-05-28".toLocalDate, Hours(40.0)), PartTimeHours("2021-06-25".toLocalDate, Hours(1.86))))
        -> 1318.90,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-31")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withRegularPayAmount(3300)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-30", "2021-05-28", "2021-06-25"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 2549.63,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withRegularPayAmount(3300)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-28")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-30", "2021-05-28"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 2307.68,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-31")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-05-01".toLocalDate, "2021-05-28".toLocalDate)),
            PartialPeriod(
              Period("2021-05-29".toLocalDate, "2021-06-25".toLocalDate),
              Period("2021-05-29".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(3500)
        .withFurloughStartDate("2020-03-01")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-30", "2021-05-28", "2021-06-25"))
        .withUsualHours(List(UsualHours("2021-05-28".toLocalDate, Hours(148.0)), UsualHours("2021-06-25".toLocalDate, Hours(15.86))))
        .withPartTimeHours(List(PartTimeHours("2021-05-28".toLocalDate, Hours(40.0)), PartTimeHours("2021-06-25".toLocalDate, Hours(1.86))))
        -> 1897.56,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-31")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-05-31")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-27".toLocalDate, "2021-06-23".toLocalDate),
              Period("2021-05-31".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2200)
        .withFurloughStartDate("2021-05-30")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-05-26", "2021-06-23"))
        .withUsualHours(List(UsualHours("2021-06-23".toLocalDate, Hours(148.0))))
        .withPartTimeHours(List(PartTimeHours("2021-06-23".toLocalDate, Hours(25.0))))
        -> 52.24,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-31")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-29".toLocalDate, "2021-06-25".toLocalDate),
              Period("2021-05-29".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2000)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-30", "2021-05-28", "2021-06-25"))
        .withUsualHours(List(UsualHours("2021-06-25".toLocalDate, Hours(15.86))))
        .withPartTimeHours(List(PartTimeHours("2021-06-25".toLocalDate, Hours(1.86))))
        -> 1751.33,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-29".toLocalDate, "2021-06-25".toLocalDate),
              Period("2021-05-29".toLocalDate, "2021-05-30".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(3500)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-30")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-30", "2021-05-28", "2021-06-25"))
        .withUsualHours(List(UsualHours("2021-06-25".toLocalDate, Hours(15.86))))
        .withPartTimeHours(List(PartTimeHours("2021-06-25".toLocalDate, Hours(1.86))))
        -> 2450.06,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-06".toLocalDate, "2021-05-03".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2654.11)
        .withFurloughStartDate("2021-04-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-05", "2021-05-03"))
        .withUsualHours(List(UsualHours("2021-05-03".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-03".toLocalDate, Hours(11.4))))
        -> 101.55,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-02")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-08".toLocalDate, "2021-05-05".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(3200.11)
        .withFurloughStartDate("2021-04-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-07", "2021-05-05"))
        .withUsualHours(List(UsualHours("2021-05-05".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-05".toLocalDate, Hours(11.4))))
        -> 108.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-01")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-10".toLocalDate, "2021-05-07".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2322.11)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-09", "2021-05-07"))
        .withUsualHours(List(UsualHours("2021-05-07".toLocalDate, Hours(10.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-07".toLocalDate, Hours(6.5))))
        -> 23.22,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-02")
        .withPaymentFrequency(FourWeekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-04".toLocalDate, "2021-05-01".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-01".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-05-02".toLocalDate, "2021-05-29".toLocalDate),
              Period("2021-05-02".toLocalDate, "2021-05-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1465.55)
        .withFurloughStartDate("2021-04-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-03", "2021-05-01", "2021-05-29"))
        .withUsualHours(List(UsualHours("2021-05-01".toLocalDate, Hours(7.5)), UsualHours("2021-05-29".toLocalDate, Hours(7.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-01".toLocalDate, Hours(3.0)), PartTimeHours("2021-05-29".toLocalDate, Hours(3.0))))
        -> 50.24
    )
  )

  val mayFixedMonthlyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "May Fixed Monthly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-31")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(List(FullPeriod(Period("2021-05-01".toLocalDate, "2021-05-31".toLocalDate))))
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2400)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-04-30", "2021-05-31"))
        .withUsualHours(List(UsualHours("2021-05-31".toLocalDate, Hours(160.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-31".toLocalDate, Hours(40.0))))
        -> 1440.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-31")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withRegularPayAmount(3126)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-04-30", "2021-05-31"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 2500.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-21")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-01".toLocalDate, "2021-05-31".toLocalDate),
              Period("2021-05-05".toLocalDate, "2021-05-21".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2400)
        .withFurloughStartDate("2021-05-05")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-04-30", "2021-05-31"))
        .withUsualHours(List(UsualHours("2021-05-31".toLocalDate, Hours(127.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-31".toLocalDate, Hours(52.5))))
        -> 619.35,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-21")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-01".toLocalDate, "2021-05-31".toLocalDate),
              Period("2021-05-05".toLocalDate, "2021-05-21".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(6500)
        .withFurloughStartDate("2021-05-05")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-04-30", "2021-05-31"))
        .withUsualHours(List(UsualHours("2021-05-31".toLocalDate, Hours(127.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-31".toLocalDate, Hours(52.5))))
        -> 806.50,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-11")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-01".toLocalDate, "2021-05-31".toLocalDate),
              Period("2021-05-02".toLocalDate, "2021-05-11".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2400)
        .withFurloughStartDate("2021-05-02")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-04-30", "2021-05-31"))
        .withUsualHours(List(UsualHours("2021-05-31".toLocalDate, Hours(160.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-31".toLocalDate, Hours(40.0))))
        -> 464.51,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-20")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-05-02")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-01".toLocalDate, "2021-05-31".toLocalDate),
              Period("2021-05-02".toLocalDate, "2021-05-20".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(5555)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-20")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-04-30", "2021-05-31"))
        .withUsualHours(List(UsualHours("2021-05-31".toLocalDate, Hours(160.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-31".toLocalDate, Hours(40.0))))
        -> 1149.26,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-26".toLocalDate, "2021-05-25".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-25".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-05-26".toLocalDate, "2021-06-25".toLocalDate),
              Period("2021-05-26".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(4900)
        .withFurloughStartDate("2021-04-01")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-04-25", "2021-05-25", "2021-06-25"))
        .withUsualHours(List(UsualHours("2021-05-25".toLocalDate, Hours(160.0)), UsualHours("2021-06-25".toLocalDate, Hours(160.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-25".toLocalDate, Hours(95.0)), PartTimeHours("2021-06-25".toLocalDate, Hours(95.0))))
        -> 1015.68,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-01".toLocalDate, "2021-05-31".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2654.11)
        .withFurloughStartDate("2021-04-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-04-30", "2021-05-31"))
        .withUsualHours(List(UsualHours("2021-05-31".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-31".toLocalDate, Hours(11.4))))
        -> 91.72,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-26".toLocalDate, "2021-05-25".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2654.11)
        .withFurloughStartDate("2021-04-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-04-25", "2021-05-25"))
        .withUsualHours(List(UsualHours("2021-05-25".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-25".toLocalDate, Hours(11.4))))
        -> 94.78,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-02")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-03".toLocalDate, "2021-05-02".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(3200.11)
        .withFurloughStartDate("2021-04-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-04-02", "2021-05-02"))
        .withUsualHours(List(UsualHours("2021-05-02".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-02".toLocalDate, Hours(11.4))))
        -> 108.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-01")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-09".toLocalDate, "2021-05-08".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2322.11)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-04-08", "2021-05-08"))
        .withUsualHours(List(UsualHours("2021-05-08".toLocalDate, Hours(10.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-08".toLocalDate, Hours(6.5))))
        -> 21.67,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-02")
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-02".toLocalDate, "2021-05-01".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-01".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-05-02".toLocalDate, "2021-06-01".toLocalDate),
              Period("2021-05-02".toLocalDate, "2021-05-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1465.55)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-04-01", "2021-05-01", "2021-06-01"))
        .withUsualHours(List(UsualHours("2021-05-01".toLocalDate, Hours(7.5)), UsualHours("2021-06-01".toLocalDate, Hours(7.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-01".toLocalDate, Hours(3.0)), PartTimeHours("2021-06-01".toLocalDate, Hours(3.0))))
        -> 46.15,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Monthly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-01".toLocalDate, "2021-05-31".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(3222)
        .withFurloughStartDate("2021-04-30")
        .withClaimPeriodEnd("2021-05-01")
        .withRegularLengthEmployed(RegularLengthEmployed.No)
        .withPayDate(List("2021-04-30", "2021-05-31"))
        .withUsualHours(List(UsualHours("2021-05-31".toLocalDate, Hours(7.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-31".toLocalDate, Hours(3.0))))
        -> 48.39
    )
  )

  val mayFixedTwoWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "May Fixed Two Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-31")
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-05-01".toLocalDate, "2021-05-14".toLocalDate)),
            FullPeriod(Period("2021-05-15".toLocalDate, "2021-05-28".toLocalDate)),
            PartialPeriod(
              Period("2021-05-29".toLocalDate, "2021-06-11".toLocalDate),
              Period("2021-05-29".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(650)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-30", "2021-05-14", "2021-05-28", "2021-06-11"))
        .withUsualHours(
          List(
            UsualHours("2021-05-14".toLocalDate, Hours(98.0)),
            UsualHours("2021-05-28".toLocalDate, Hours(98.0)),
            UsualHours("2021-06-11".toLocalDate, Hours(21.0))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-05-14".toLocalDate, Hours(48.0)),
            PartTimeHours("2021-05-28".toLocalDate, Hours(48.0)),
            PartTimeHours("2021-06-11".toLocalDate, Hours(6.0))
          )
        )
        -> 610.19,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-31")
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-05-01".toLocalDate, "2021-05-14".toLocalDate)),
            FullPeriod(Period("2021-05-15".toLocalDate, "2021-05-28".toLocalDate)),
            PartialPeriod(
              Period("2021-05-29".toLocalDate, "2021-06-11".toLocalDate),
              Period("2021-05-29".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(2300.12)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-30", "2021-05-14", "2021-05-28", "2021-06-11"))
        .withUsualHours(
          List(
            UsualHours("2021-05-14".toLocalDate, Hours(98.0)),
            UsualHours("2021-05-28".toLocalDate, Hours(98.0)),
            UsualHours("2021-06-11".toLocalDate, Hours(21.0))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-05-14".toLocalDate, Hours(48.0)),
            PartTimeHours("2021-05-28".toLocalDate, Hours(48.0)),
            PartTimeHours("2021-06-11".toLocalDate, Hours(6.0))
          )
        )
        -> 1350.20,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-31")
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-05-13")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-29".toLocalDate, "2021-06-11".toLocalDate),
              Period("2021-05-29".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(650)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-30", "2021-05-14", "2021-05-28", "2021-06-11"))
        .withUsualHours(List(UsualHours("2021-06-11".toLocalDate, Hours(21.0))))
        .withPartTimeHours(List(PartTimeHours("2021-06-11".toLocalDate, Hours(6.0))))
        -> 673.88,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-11")
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-05-04")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-29".toLocalDate, "2021-05-12".toLocalDate),
              Period("2021-05-04".toLocalDate, "2021-05-11".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(464.28)
        .withFurloughStartDate("2021-04-01")
        .withClaimPeriodEnd("2021-05-11")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-28", "2021-05-12"))
        .withUsualHours(List(UsualHours("2021-05-12".toLocalDate, Hours(70.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-12".toLocalDate, Hours(43.0))))
        -> 81.86,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-05-28")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-29".toLocalDate, "2021-06-11".toLocalDate),
              Period("2021-05-30".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(789.12)
        .withFurloughStartDate("2021-05-30")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-05-28", "2021-06-11"))
        .withUsualHours(List(UsualHours("2021-06-11".toLocalDate, Hours(140.0))))
        .withPartTimeHours(List(PartTimeHours("2021-06-11".toLocalDate, Hours(50.0))))
        -> 57.98,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-25")
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-01".toLocalDate, "2021-05-14".toLocalDate),
              Period("2021-05-05".toLocalDate, "2021-05-14".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-05-15".toLocalDate, "2021-05-28".toLocalDate),
              Period("2021-05-15".toLocalDate, "2021-05-25".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(643.12)
        .withFurloughStartDate("2021-05-05")
        .withClaimPeriodEnd("2021-05-30")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-30", "2021-05-14", "2021-05-28"))
        .withUsualHours(List(UsualHours("2021-05-14".toLocalDate, Hours(63.0)), UsualHours("2021-05-28".toLocalDate, Hours(77.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-14".toLocalDate, Hours(8.0)), PartTimeHours("2021-05-28".toLocalDate, Hours(12.0))))
        -> 662.08,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-19".toLocalDate, "2021-05-02".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1000.34)
        .withFurloughStartDate("2021-04-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-18", "2021-05-02"))
        .withUsualHours(List(UsualHours("2021-05-02".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-02".toLocalDate, Hours(11.4))))
        -> 76.55,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-21".toLocalDate, "2021-05-04".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1500)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-20", "2021-05-04"))
        .withUsualHours(List(UsualHours("2021-05-04".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-04".toLocalDate, Hours(11.4))))
        -> 108.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-01")
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-24".toLocalDate, "2021-05-07".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1010.11)
        .withFurloughStartDate("2021-04-01")
        .withClaimPeriodEnd("2021-05-01")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-23", "2021-05-07"))
        .withUsualHours(List(UsualHours("2021-05-07".toLocalDate, Hours(10.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-07".toLocalDate, Hours(6.5))))
        -> 20.20,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-02")
        .withPaymentFrequency(FortNightly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-18".toLocalDate, "2021-05-01".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-01".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-05-02".toLocalDate, "2021-05-15".toLocalDate),
              Period("2021-05-02".toLocalDate, "2021-05-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1465.55)
        .withFurloughStartDate("2021-04-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-17", "2021-05-01", "2021-05-15"))
        .withUsualHours(List(UsualHours("2021-05-01".toLocalDate, Hours(7.5)), UsualHours("2021-05-15".toLocalDate, Hours(7.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-01".toLocalDate, Hours(3.0)), PartTimeHours("2021-05-15".toLocalDate, Hours(3.0))))
        -> 96.78
    )
  )

  val mayFixedWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "May Fixed Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-12")
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-05-06")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(List(FullPeriod(Period("2021-05-06".toLocalDate, "2021-05-12".toLocalDate))))
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(550)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-12")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-05-05", "2021-05-12"))
        .withUsualHours(List(UsualHours("2021-05-12".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-12".toLocalDate, Hours(14.0))))
        -> 286.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-05-01".toLocalDate, "2021-05-07".toLocalDate)),
            FullPeriod(Period("2021-05-08".toLocalDate, "2021-05-14".toLocalDate)),
            FullPeriod(Period("2021-05-15".toLocalDate, "2021-05-21".toLocalDate)),
            FullPeriod(Period("2021-05-22".toLocalDate, "2021-05-28".toLocalDate)),
            PartialPeriod(
              Period("2021-05-29".toLocalDate, "2021-06-04".toLocalDate),
              Period("2021-05-29".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(600)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-30", "2021-05-07", "2021-05-14", "2021-05-21", "2021-05-28", "2021-06-04"))
        .withUsualHours(
          List(
            UsualHours("2021-05-07".toLocalDate, Hours(37.0)),
            UsualHours("2021-05-14".toLocalDate, Hours(37.0)),
            UsualHours("2021-05-21".toLocalDate, Hours(37.0)),
            UsualHours("2021-05-28".toLocalDate, Hours(37.0)),
            UsualHours("2021-06-04".toLocalDate, Hours(15.86))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-05-07".toLocalDate, Hours(10.0)),
            PartTimeHours("2021-05-14".toLocalDate, Hours(12.0)),
            PartTimeHours("2021-05-21".toLocalDate, Hours(10.0)),
            PartTimeHours("2021-05-28".toLocalDate, Hours(15.0)),
            PartTimeHours("2021-06-04".toLocalDate, Hours(1.06))
          )
        )
        -> 1502.24,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-05-02")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-04".toLocalDate, "2021-05-10".toLocalDate),
              Period("2021-05-06".toLocalDate, "2021-05-10".toLocalDate)
            ),
            FullPeriod(Period("2021-05-11".toLocalDate, "2021-05-17".toLocalDate)),
            FullPeriod(Period("2021-05-18".toLocalDate, "2021-05-24".toLocalDate)),
            PartialPeriod(
              Period("2021-05-25".toLocalDate, "2021-05-31".toLocalDate),
              Period("2021-05-25".toLocalDate, "2021-05-29".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1200)
        .withFurloughStartDate("2021-05-06")
        .withClaimPeriodEnd("2021-05-29")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-05-03", "2021-05-10", "2021-05-17", "2021-05-24", "2021-05-31"))
        .withUsualHours(
          List(
            UsualHours("2021-05-10".toLocalDate, Hours(37.0)),
            UsualHours("2021-05-17".toLocalDate, Hours(37.0)),
            UsualHours("2021-05-24".toLocalDate, Hours(37.0)),
            UsualHours("2021-05-31".toLocalDate, Hours(37.0))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-05-10".toLocalDate, Hours(10.0)),
            PartTimeHours("2021-05-17".toLocalDate, Hours(12.0)),
            PartTimeHours("2021-05-24".toLocalDate, Hours(10.0)),
            PartTimeHours("2021-05-31".toLocalDate, Hours(15.0))
          )
        )
        -> 1344.84,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-27")
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-05-02")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-04".toLocalDate, "2021-05-10".toLocalDate),
              Period("2021-05-06".toLocalDate, "2021-05-10".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-05-25".toLocalDate, "2021-05-31".toLocalDate),
              Period("2021-05-25".toLocalDate, "2021-05-27".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(1200)
        .withFurloughStartDate("2021-05-06")
        .withClaimPeriodEnd("2021-05-29")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-05-03", "2021-05-10", "2021-05-17", "2021-05-24", "2021-05-31"))
        .withUsualHours(List(UsualHours("2021-05-10".toLocalDate, Hours(37.0)), UsualHours("2021-05-31".toLocalDate, Hours(15.86))))
        .withPartTimeHours(List(PartTimeHours("2021-05-10".toLocalDate, Hours(10.0)), PartTimeHours("2021-05-31".toLocalDate, Hours(1.06))))
        -> 1673.88,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-05-14".toLocalDate, "2021-05-20".toLocalDate)),
            FullPeriod(Period("2021-05-21".toLocalDate, "2021-05-27".toLocalDate)),
            PartialPeriod(
              Period("2021-05-28".toLocalDate, "2021-06-03".toLocalDate),
              Period("2021-05-28".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(600)
        .withFurloughStartDate("2021-05-14")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-05-13", "2021-05-20", "2021-05-27", "2021-06-03"))
        .withUsualHours(
          List(
            UsualHours("2021-05-20".toLocalDate, Hours(37.0)),
            UsualHours("2021-05-27".toLocalDate, Hours(37.0)),
            UsualHours("2021-06-03".toLocalDate, Hours(15.86))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-05-20".toLocalDate, Hours(10.0)),
            PartTimeHours("2021-05-27".toLocalDate, Hours(15.0)),
            PartTimeHours("2021-06-03".toLocalDate, Hours(1.06))
          )
        )
        -> 891.64,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-05-30")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-30".toLocalDate, "2021-06-05".toLocalDate),
              Period("2021-05-30".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(550)
        .withFurloughStartDate("2021-05-30")
        .withClaimPeriodEnd("2021-05-31")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-05-29", "2021-06-05"))
        .withUsualHours(List(UsualHours("2021-06-05".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-06-05".toLocalDate, Hours(14.0))))
        -> 81.71,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-08")
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-05-01".toLocalDate, "2021-05-07".toLocalDate)),
            PartialPeriod(
              Period("2021-05-08".toLocalDate, "2021-05-14".toLocalDate),
              Period("2021-05-08".toLocalDate, "2021-05-08".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(600)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-21")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-30", "2021-05-07", "2021-05-14"))
        .withUsualHours(List(UsualHours("2021-05-07".toLocalDate, Hours(37.0)), UsualHours("2021-05-14".toLocalDate, Hours(37.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-07".toLocalDate, Hours(10.0)), PartTimeHours("2021-05-14".toLocalDate, Hours(12.0))))
        -> 396.60,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughOngoing)
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-01".toLocalDate, "2021-05-07".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(666.12)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-30", "2021-05-07"))
        .withUsualHours(List(UsualHours("2021-05-07".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-07".toLocalDate, Hours(11.4))))
        -> 101.94,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-02")
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-27".toLocalDate, "2021-05-03".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-02".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(890.11)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-26", "2021-05-03"))
        .withUsualHours(List(UsualHours("2021-05-03".toLocalDate, Hours(34.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-03".toLocalDate, Hours(11.4))))
        -> 108.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-01")
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-29".toLocalDate, "2021-05-05".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(500)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-28", "2021-05-05"))
        .withUsualHours(List(UsualHours("2021-05-05".toLocalDate, Hours(10.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-05".toLocalDate, Hours(6.5))))
        -> 20.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-02")
        .withPaymentFrequency(Weekly)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-25".toLocalDate, "2021-05-01".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-01".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Regular)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withRegularPayAmount(754.44)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-02")
        .withRegularLengthEmployed(RegularLengthEmployed.Yes)
        .withPayDate(List("2021-04-24", "2021-05-01", "2021-05-08"))
        .withUsualHours(List(UsualHours("2021-05-01".toLocalDate, Hours(7.5))))
        .withPartTimeHours(List(PartTimeHours("2021-05-01".toLocalDate, Hours(3.0))))
        -> 129.04
    )
  )

  val mayVariableFourWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "May Variable Four Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-10-30")
        .withFurloughEndDate("2021-05-29")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withPaymentFrequency(FourWeekly)
        .withOnPayrollBefore30thOct2020(true)
        .withEmployeeStartedAfter1Feb2019()
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-04-06".toLocalDate, "2021-05-03".toLocalDate),
              Period("2021-05-01".toLocalDate, "2021-05-03".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-05-04".toLocalDate, "2021-05-31".toLocalDate),
              Period("2021-05-04".toLocalDate, "2021-05-29".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(1500)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-04-05", "2021-05-03", "2021-05-31"))
        .withUsualHours(List(UsualHours("2021-05-03".toLocalDate, Hours(40.0)), UsualHours("2021-05-31".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-03".toLocalDate, Hours(14.0)), PartTimeHours("2021-05-31".toLocalDate, Hours(15.0))))
        -> 132.18,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-08-04")
        .withFurloughEndDate("2021-05-31")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withPaymentFrequency(FourWeekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-05-31")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-31".toLocalDate, "2021-06-27".toLocalDate),
              Period("2021-05-31".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(19564.4)
        .withFurloughStartDate("2021-05-30")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-05-30", "2021-06-27"))
        .withUsualHours(List(UsualHours("2021-06-27".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-06-27".toLocalDate, Hours(14.0))))
        -> 34.02,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-28")
        .withPaymentFrequency(FourWeekly)
        .withEmployeeStartedOnOrBefore1Feb2019()
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List("2019-05-31" -> 2800))
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withAnnualPayAmount(36000)
        .withFurloughStartDate("2021-05-04")
        .withClaimPeriodEnd("2021-05-28")
        .withPayDate(List("2021-04-30", "2021-05-28"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 2000.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-04-01")
        .withFurloughEndDate("2021-05-31")
        .withPreviousFurloughedPeriodsAnswer(true)
        .withFirstFurloughDate("2020-11-02")
        .withPaymentFrequency(FourWeekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-05-01".toLocalDate, "2021-05-28".toLocalDate)),
            PartialPeriod(
              Period("2021-05-29".toLocalDate, "2021-06-25".toLocalDate),
              Period("2021-05-29".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(10000)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-04-30", "2021-05-28", "2021-06-25"))
        .withUsualHours(List(UsualHours("2021-05-28".toLocalDate, Hours(40.0)), UsualHours("2021-06-25".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-28".toLocalDate, Hours(14.0)), PartTimeHours("2021-06-25".toLocalDate, Hours(14.0))))
        -> 767.63
    )
  )

  val mayVariableMonthlyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "May Variable Monthly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-10-30")
        .withFurloughEndDate("2021-05-31")
        .withPreviousFurloughedPeriodsAnswer(true)
        .withFirstFurloughDate("2020-11-02")
        .withPaymentFrequency(Monthly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(List(FullPeriod(Period("2021-05-01".toLocalDate, "2021-05-31".toLocalDate))))
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(10000)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-04-30", "2021-05-31"))
        .withUsualHours(List(UsualHours("2021-05-31".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-31".toLocalDate, Hours(14.0))))
        -> 1625.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-08-01")
        .withFurloughEndDate("2021-05-31")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withPaymentFrequency(Monthly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(List(FullPeriod(Period("2021-05-01".toLocalDate, "2021-05-31".toLocalDate))))
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(10000)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-04-30", "2021-05-31"))
        .withUsualHours(List(UsualHours("2021-05-31".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-31".toLocalDate, Hours(14.0))))
        -> 590.47,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-31")
        .withPaymentFrequency(Monthly)
        .withEmployeeStartedOnOrBefore1Feb2019()
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List("2019-05-31" -> 2000))
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withAnnualPayAmount(26000)
        .withFurloughStartDate("2021-04-01")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-04-30", "2021-05-31"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 1761.79,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-04-01")
        .withFurloughEndDate("2021-05-31")
        .withPreviousFurloughedPeriodsAnswer(true)
        .withFirstFurloughDate("2020-11-02")
        .withPaymentFrequency(Monthly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withAnnualPayAmount(12000)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-04-30", "2021-05-31"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 1417.07
    )
  )

  val mayVariableTwoWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "May Variable Two Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-10-30")
        .withFurloughEndDate("2021-05-31")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withPaymentFrequency(FortNightly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-05-13")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-01".toLocalDate, "2021-05-14".toLocalDate),
              Period("2021-05-13".toLocalDate, "2021-05-14".toLocalDate)
            ),
            FullPeriod(Period("2021-05-15".toLocalDate, "2021-05-28".toLocalDate)),
            PartialPeriod(
              Period("2021-05-29".toLocalDate, "2021-06-11".toLocalDate),
              Period("2021-05-29".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(34000)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-04-30", "2021-05-14", "2021-05-28", "2021-06-11"))
        .withUsualHours(
          List(
            UsualHours("2021-05-14".toLocalDate, Hours(40.0)),
            UsualHours("2021-05-28".toLocalDate, Hours(50.0)),
            UsualHours("2021-06-11".toLocalDate, Hours(50.0))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-05-14".toLocalDate, Hours(14.0)),
            PartTimeHours("2021-05-28".toLocalDate, Hours(15.0)),
            PartTimeHours("2021-06-11".toLocalDate, Hours(15.0))
          )
        )
        -> 1081.91,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-07-20")
        .withFurloughEndDate("2021-05-25")
        .withPreviousFurloughedPeriodsAnswer(true)
        .withFirstFurloughDate("2020-11-02")
        .withPaymentFrequency(FortNightly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-01".toLocalDate, "2021-05-14".toLocalDate),
              Period("2021-05-05".toLocalDate, "2021-05-14".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-05-15".toLocalDate, "2021-05-28".toLocalDate),
              Period("2021-05-15".toLocalDate, "2021-05-25".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(19564.4)
        .withFurloughStartDate("2021-05-05")
        .withClaimPeriodEnd("2021-05-30")
        .withPayDate(List("2021-04-30", "2021-05-14", "2021-05-28"))
        .withUsualHours(List(UsualHours("2021-05-14".toLocalDate, Hours(40.0)), UsualHours("2021-05-28".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-14".toLocalDate, Hours(14.0)), PartTimeHours("2021-05-28".toLocalDate, Hours(15.0))))
        -> 1145.24,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-07-19")
        .withFurloughEndDate("2021-05-25")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withPaymentFrequency(FortNightly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-01".toLocalDate, "2021-05-14".toLocalDate),
              Period("2021-05-05".toLocalDate, "2021-05-14".toLocalDate)
            ),
            PartialPeriod(
              Period("2021-05-15".toLocalDate, "2021-05-28".toLocalDate),
              Period("2021-05-15".toLocalDate, "2021-05-25".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(1000)
        .withFurloughStartDate("2021-05-05")
        .withClaimPeriodEnd("2021-05-30")
        .withPayDate(List("2021-04-30", "2021-05-14", "2021-05-28"))
        .withUsualHours(List(UsualHours("2021-05-14".toLocalDate, Hours(40.0)), UsualHours("2021-05-28".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-14".toLocalDate, Hours(14.0)), PartTimeHours("2021-05-28".toLocalDate, Hours(15.0))))
        -> 39.20,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-28")
        .withPaymentFrequency(FortNightly)
        .withEmployeeStartedOnOrBefore1Feb2019()
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List("2019-05-17" -> 840, "2019-05-31" -> 980))
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withAnnualPayAmount(28000)
        .withFurloughStartDate("2021-05-04")
        .withClaimPeriodEnd("2021-05-28")
        .withPayDate(List("2021-04-30", "2021-05-14", "2021-05-28"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 1530.00,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-10-30")
        .withFurloughEndDate("2021-05-31")
        .withPreviousFurloughedPeriodsAnswer(true)
        .withFirstFurloughDate("2020-11-02")
        .withPaymentFrequency(FortNightly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-05-01".toLocalDate, "2021-05-14".toLocalDate)),
            FullPeriod(Period("2021-05-15".toLocalDate, "2021-05-28".toLocalDate)),
            PartialPeriod(
              Period("2021-05-29".toLocalDate, "2021-06-11".toLocalDate),
              Period("2021-05-29".toLocalDate, "2021-05-31".toLocalDate)
            )
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(19564.4)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-04-30", "2021-05-14", "2021-05-28", "2021-06-11"))
        .withUsualHours(
          List(
            UsualHours("2021-05-14".toLocalDate, Hours(40.0)),
            UsualHours("2021-05-28".toLocalDate, Hours(50.0)),
            UsualHours("2021-06-11".toLocalDate, Hours(50.0))
          )
        )
        .withPartTimeHours(
          List(
            PartTimeHours("2021-05-14".toLocalDate, Hours(14.0)),
            PartTimeHours("2021-05-28".toLocalDate, Hours(15.0)),
            PartTimeHours("2021-06-11".toLocalDate, Hours(15.0))
          )
        )
        -> 1727.06
    )
  )

  val mayVariableWeeklyScenarios: Seq[(String, Seq[(UserAnswers, BigDecimal)])] = Seq(
    "May Variable Weekly Scenarios" -> Seq(
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-09-05")
        .withFurloughEndDate("2021-05-14")
        .withPreviousFurloughedPeriodsAnswer(true)
        .withFirstFurloughDate("2020-11-02")
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-01".toLocalDate, "2021-05-07".toLocalDate),
              Period("2021-05-04".toLocalDate, "2021-05-07".toLocalDate)
            ),
            FullPeriod(Period("2021-05-08".toLocalDate, "2021-05-14".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(19564.4)
        .withFurloughStartDate("2021-05-04")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-04-30", "2021-05-07", "2021-05-14"))
        .withUsualHours(List(UsualHours("2021-05-07".toLocalDate, Hours(40.0)), UsualHours("2021-05-14".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-05-14".toLocalDate, Hours(15.0))))
        -> 613.53,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-08-04")
        .withFurloughEndDate("2021-05-14")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-01".toLocalDate, "2021-05-07".toLocalDate),
              Period("2021-05-04".toLocalDate, "2021-05-07".toLocalDate)
            ),
            FullPeriod(Period("2021-05-08".toLocalDate, "2021-05-14".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(26000)
        .withFurloughStartDate("2021-05-04")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-04-30", "2021-05-07", "2021-05-14"))
        .withUsualHours(List(UsualHours("2021-05-07".toLocalDate, Hours(40.0)), UsualHours("2021-05-14".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-05-14".toLocalDate, Hours(15.0))))
        -> 571.44,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-08-04")
        .withFurloughEndDate("2021-05-14")
        .withPreviousFurloughedPeriodsAnswer(true)
        .withFirstFurloughDate("2020-11-02")
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(true)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            PartialPeriod(
              Period("2021-05-01".toLocalDate, "2021-05-07".toLocalDate),
              Period("2021-05-04".toLocalDate, "2021-05-07".toLocalDate)
            ),
            FullPeriod(Period("2021-05-08".toLocalDate, "2021-05-14".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(1000)
        .withFurloughStartDate("2021-05-04")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-04-30", "2021-05-07", "2021-05-14"))
        .withUsualHours(List(UsualHours("2021-05-07".toLocalDate, Hours(40.0)), UsualHours("2021-05-14".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-05-14".toLocalDate, Hours(15.0))))
        -> 66.66,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-07")
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedOnOrBefore1Feb2019()
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List("2019-05-03" -> 420, "2019-05-10" -> 490))
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(List(FullPeriod(Period("2021-05-01".toLocalDate, "2021-05-07".toLocalDate))))
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(26000)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-04-30", "2021-05-07"))
        .withUsualHours(List(UsualHours("2021-05-07".toLocalDate, Hours(40.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-07".toLocalDate, Hours(14.0))))
        -> 258.58,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withFurloughEndDate("2021-05-24")
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedOnOrBefore1Feb2019()
        .withClaimPeriodStart("2021-05-03")
        .withLastYear(List("2019-05-10" -> 420, "2019-05-17" -> 490, "2019-05-24" -> 560))
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeNo)
        .withAnnualPayAmount(26000)
        .withFurloughStartDate("2021-05-04")
        .withClaimPeriodEnd("2021-05-28")
        .withPayDate(List("2021-04-30", "2021-05-07", "2021-05-14", "2021-05-21", "2021-05-28"))
        .withUsualHours(List())
        .withPartTimeHours(List())
        -> 1241.15,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-10-30")
        .withFurloughEndDate("2021-05-14")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(false)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-05-01".toLocalDate, "2021-05-07".toLocalDate)),
            FullPeriod(Period("2021-05-08".toLocalDate, "2021-05-14".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(10000)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-04-30", "2021-05-07", "2021-05-14"))
        .withUsualHours(List(UsualHours("2021-05-07".toLocalDate, Hours(40.0)), UsualHours("2021-05-14".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-05-14".toLocalDate, Hours(15.0))))
        -> 413.08,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-12-24")
        .withFurloughEndDate("2021-05-14")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(false)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-05-01".toLocalDate, "2021-05-07".toLocalDate)),
            FullPeriod(Period("2021-05-08".toLocalDate, "2021-05-14".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(10000)
        .withStatutoryLeaveData(days = 60, amount = 4000)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-04-30", "2021-05-07", "2021-05-14"))
        .withUsualHours(List(UsualHours("2021-05-07".toLocalDate, Hours(40.0)), UsualHours("2021-05-14".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-05-14".toLocalDate, Hours(15.0))))
        -> 667.09,
      emptyUserAnswers
        .withFurloughStatus(FurloughStatus.FurloughEnded)
        .withEmployeeStartDate("2020-12-24")
        .withFurloughEndDate("2021-05-14")
        .withPreviousFurloughedPeriodsAnswer(false)
        .withPaymentFrequency(Weekly)
        .withEmployeeStartedAfter1Feb2019()
        .withOnPayrollBefore30thOct2020(false)
        .withClaimPeriodStart("2021-05-01")
        .withLastYear(List())
        .withFurloughInLastTaxYear(false)
        .withPayPeriodsList(PayPeriodsList.Yes)
        .withPartTimePeriods(
          List(
            FullPeriod(Period("2021-05-01".toLocalDate, "2021-05-07".toLocalDate)),
            FullPeriod(Period("2021-05-08".toLocalDate, "2021-05-14".toLocalDate))
          )
        )
        .withPayMethod(PayMethod.Variable)
        .withPartTimeQuestion(PartTimeQuestion.PartTimeYes)
        .withAnnualPayAmount(10000)
        .withFurloughStartDate("2021-05-01")
        .withClaimPeriodEnd("2021-05-31")
        .withPayDate(List("2021-04-30", "2021-05-07", "2021-05-14"))
        .withUsualHours(List(UsualHours("2021-05-07".toLocalDate, Hours(40.0)), UsualHours("2021-05-14".toLocalDate, Hours(50.0))))
        .withPartTimeHours(List(PartTimeHours("2021-05-07".toLocalDate, Hours(14.0)), PartTimeHours("2021-05-14".toLocalDate, Hours(15.0))))
        -> 590.66
    )
  )
}
