/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package generators

import org.scalacheck.Arbitrary
import pages._

trait PageGenerators {

  implicit lazy val arbitraryFurloughCalculationsPage: Arbitrary[FurloughCalculationsPage.type] =
    Arbitrary(FurloughCalculationsPage)

  implicit lazy val arbitraryVariableLengthPartialPayPage: Arbitrary[VariableLengthPartialPayPage.type] =
    Arbitrary(VariableLengthPartialPayPage)

  implicit lazy val arbitraryVariableGrossPayPage: Arbitrary[VariableGrossPayPage.type] =
    Arbitrary(VariableGrossPayPage)

  implicit lazy val arbitraryEmployeeStartDatePage: Arbitrary[EmployeeStartDatePage.type] =
    Arbitrary(EmployeeStartDatePage)

  implicit lazy val arbitraryVariableLengthEmployedPage: Arbitrary[VariableLengthEmployedPage.type] =
    Arbitrary(VariableLengthEmployedPage)

  implicit lazy val arbitraryFurloughStartDatePage: Arbitrary[FurloughStartDatePage.type] =
    Arbitrary(FurloughStartDatePage)

  implicit lazy val arbitraryFurloughEndDatePage: Arbitrary[FurloughEndDatePage.type] =
    Arbitrary(FurloughEndDatePage)

  implicit lazy val arbitraryFurloughDatesPage: Arbitrary[FurloughDatesPage.type] =
    Arbitrary(FurloughDatesPage)

  implicit lazy val arbitraryTaxYearPayDatePage: Arbitrary[TaxYearPayDatePage.type] =
    Arbitrary(TaxYearPayDatePage)

  implicit lazy val arbitraryClaimPeriodStartPage: Arbitrary[ClaimPeriodStartPage.type] =
    Arbitrary(ClaimPeriodStartPage)

  implicit lazy val arbitraryClaimPeriodEndPage: Arbitrary[ClaimPeriodEndPage.type] =
    Arbitrary(ClaimPeriodEndPage)

  implicit lazy val arbitraryFurloughQuestionPage: Arbitrary[FurloughQuestionPage.type] =
    Arbitrary(FurloughQuestionPage)

  implicit lazy val arbitraryPensionAutoEnrolmentPage: Arbitrary[PensionAutoEnrolmentPage.type] =
    Arbitrary(PensionAutoEnrolmentPage)

  implicit lazy val arbitraryNicCategoryPage: Arbitrary[NicCategoryPage.type] =
    Arbitrary(NicCategoryPage)

  implicit lazy val arbitrarySalaryQuestionPage: Arbitrary[SalaryQuestionPage.type] =
    Arbitrary(SalaryQuestionPage)

  implicit lazy val arbitraryPaymentFrequencyPage: Arbitrary[PaymentFrequencyPage.type] =
    Arbitrary(PaymentFrequencyPage)

  implicit lazy val arbitraryPayQuestionPage: Arbitrary[PayQuestionPage.type] =
    Arbitrary(PayQuestionPage)

  implicit lazy val arbitraryPayDatePage: Arbitrary[PayDatePage.type] =
    Arbitrary(PayDatePage)

}
