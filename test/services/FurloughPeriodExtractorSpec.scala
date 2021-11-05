/*
 * Copyright 2021 HM Revenue & Customs
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

package services

import java.time.LocalDate

import base.SpecBase
import cats.data.{Chain, Validated}
import cats.scalatest.{ValidatedMatchers, ValidatedValues}
import generators.Generators
import models.{FurloughEnded, FurloughOngoing, FurloughWithinClaim, UserAnswers}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import pages.FurloughStartDatePage
import utils.CoreTestData

class FurloughPeriodExtractorSpec
    extends SpecBase with CoreTestData with ScalaCheckPropertyChecks with ValidatedMatchers with ValidatedValues with Generators {

  "calling the .extractFurloughPeriod()" must {

    "span furlough start to furlough end if furlough start and end is set" in new FurloughPeriodExtractor {
      val userAnswers = emptyUserAnswers
        .withFurloughStartDate("2020, 4, 1")
        .withFurloughEndDate("2020, 5, 2")

      extractFurloughPeriodV(userAnswers).value mustBe
        FurloughEnded(
          LocalDate.of(2020, 4, 1),
          LocalDate.of(2020, 5, 2)
        )
    }

    "be ongoing if furlough start is set" in new FurloughPeriodExtractor {
      val userAnswers = emptyUserAnswers.withFurloughStartDate("2020, 4, 1")

      extractFurloughPeriodV(userAnswers).value mustBe FurloughOngoing(
        LocalDate.of(2020, 4, 1)
      )
    }

    "return none if furlough start is not set" in new FurloughPeriodExtractor {
      val userAnswers = emptyUserAnswers

      userAnswers.getV(FurloughStartDatePage) mustBe invalid
      extractFurloughPeriodV(userAnswers) mustBe invalid
    }
  }

  "calling the .extractFurloughWithinClaim()" must {

    val policyStart: LocalDate = LocalDate.of(2020, 3, 1)
    val policyEnd: LocalDate   = LocalDate.of(2020, 6, 30)

    "use claim period start if after furlough start" in new FurloughPeriodExtractor {

      val userAnswers = emptyUserAnswers
        .withFurloughStartDate("2020, 3, 1")
        .withClaimPeriodStart("2020, 3, 2")
        .withClaimPeriodEnd("2020, 3, 31")

      extractFurloughWithinClaimV(userAnswers).value mustBe FurloughWithinClaim(
        LocalDate.of(2020, 3, 2),
        LocalDate.of(2020, 3, 31)
      )
    }

    "use claim period start if after furlough start (generated)" in new FurloughPeriodExtractor {
      val gen = for {
        furloughStart <- datesBetween(policyStart, policyEnd.minusDays(22))
        claimStart    <- datesBetween(furloughStart, policyEnd.minusDays(21))
        claimEnd      <- datesBetween(claimStart.plusDays(21), policyEnd)
      } yield (furloughStart, claimStart, claimEnd)

      forAll(gen) {
        case (furloughStart: LocalDate, claimStart: LocalDate, claimEnd: LocalDate) =>
          val userAnswers = emptyUserAnswers
            .withFurloughStartDate(furloughStart.toString)
            .withClaimPeriodStart(claimStart.toString)
            .withClaimPeriodEnd(claimEnd.toString)

          extractFurloughWithinClaimV(userAnswers).value mustBe FurloughWithinClaim(claimStart, claimEnd)
      }
    }

    "use furlough start if after claim period start" in new FurloughPeriodExtractor {
      val userAnswers = emptyUserAnswers
        .withFurloughStartDate("2020, 3, 3")
        .withClaimPeriodStart("2020, 3, 2")
        .withClaimPeriodEnd("2020, 3, 31")

      extractFurloughWithinClaimV(userAnswers).value mustBe
        FurloughWithinClaim(
          LocalDate.of(2020, 3, 3),
          LocalDate.of(2020, 3, 31)
        )
    }

    "use furlough start if after claim period start (generated)" in new FurloughPeriodExtractor {
      val gen = for {
        claimStart    <- datesBetween(policyStart, policyEnd.minusDays(22))
        furloughStart <- datesBetween(claimStart, policyEnd.minusDays(21))
        claimEnd      <- datesBetween(furloughStart.plusDays(21), policyEnd)
      } yield (claimStart, furloughStart, claimEnd)

      forAll(gen) {
        case (claimStart: LocalDate, furloughStart: LocalDate, claimEnd: LocalDate) =>
          val userAnswers = emptyUserAnswers
            .withFurloughStartDate(furloughStart.toString)
            .withClaimPeriodStart(claimStart.toString)
            .withClaimPeriodEnd(claimEnd.toString)

          extractFurloughWithinClaimV(userAnswers).value mustBe FurloughWithinClaim(furloughStart, claimEnd)
      }
    }

    "use claim period end if furlough end is missing" in new FurloughPeriodExtractor {
      val userAnswers: UserAnswers = emptyUserAnswers
        .withFurloughStartDate("2020, 3, 1")
        .withClaimPeriodStart("2020, 3, 2")
        .withClaimPeriodEnd("2020, 3, 30")

      extractFurloughWithinClaimV(userAnswers).value mustBe
        FurloughWithinClaim(
          LocalDate.of(2020, 3, 2),
          LocalDate.of(2020, 3, 30)
        )
    }

    "use claim period end if furlough end is missing (generated)" in new FurloughPeriodExtractor {
      val gen = for {
        furloughStart <- datesBetween(policyStart, policyEnd.minusDays(22))
        claimStart    <- datesBetween(furloughStart, policyEnd.minusDays(21))
        claimEnd      <- datesBetween(claimStart.plusDays(21), policyEnd)
      } yield (furloughStart, claimStart, claimEnd)

      forAll(gen) {
        case (furloughStart: LocalDate, claimStart: LocalDate, claimEnd: LocalDate) =>
          val userAnswers = emptyUserAnswers
            .withFurloughStartDate(furloughStart.toString)
            .withClaimPeriodStart(claimStart.toString)
            .withClaimPeriodEnd(claimEnd.toString)

          extractFurloughWithinClaimV(userAnswers).value mustBe FurloughWithinClaim(claimStart, claimEnd)
      }
    }

    "use claim period end if before furlough end" in new FurloughPeriodExtractor {
      val userAnswers = emptyUserAnswers
        .withFurloughStartDate("2020, 3, 1")
        .withFurloughEndDate("2020, 3, 31")
        .withClaimPeriodStart("2020, 3, 2")
        .withClaimPeriodEnd("2020, 3, 30")

      extractFurloughWithinClaimV(userAnswers).value mustBe
        FurloughWithinClaim(
          LocalDate.of(2020, 3, 2),
          LocalDate.of(2020, 3, 30)
        )
    }

    "use claim period end if before furlough end (generated)" in new FurloughPeriodExtractor {
      val gen = for {
        furloughStart <- datesBetween(policyStart, policyEnd.minusDays(22))
        claimStart    <- datesBetween(furloughStart, policyEnd.minusDays(21))
        furloughEnd   <- datesBetween(claimStart.plusDays(21), policyEnd.plusDays(30))
        claimEnd      <- datesBetween(claimStart.plusDays(21), policyEnd) suchThat (_.isBefore(furloughEnd))
      } yield (furloughStart, furloughEnd, claimStart, claimEnd)

      forAll(gen) {
        case (furloughStart: LocalDate, furloughEnd: LocalDate, claimStart: LocalDate, claimEnd: LocalDate) =>
          val userAnswers = emptyUserAnswers
            .withFurloughStartDate(furloughStart.toString)
            .withFurloughEndDate(furloughEnd.toString)
            .withClaimPeriodStart(claimStart.toString)
            .withClaimPeriodEnd(claimEnd.toString)

          extractFurloughWithinClaimV(userAnswers).value mustBe FurloughWithinClaim(claimStart, claimEnd)
      }
    }

    "use furlough end if before claim period end" in new FurloughPeriodExtractor {
      val userAnswers: UserAnswers = emptyUserAnswers
        .withFurloughStartDate("2020, 3, 1")
        .withFurloughEndDate("2020, 3, 29")
        .withClaimPeriodStart("2020, 3, 2")
        .withClaimPeriodEnd("2020, 3, 30")

      extractFurloughWithinClaimV(userAnswers).value mustBe
        FurloughWithinClaim(
          LocalDate.of(2020, 3, 2),
          LocalDate.of(2020, 3, 29)
        )
    }

    "use furlough end if before claim period end (generated)" in new FurloughPeriodExtractor {
      val gen = for {
        furloughStart <- datesBetween(policyStart, policyEnd.minusDays(22))
        claimStart    <- datesBetween(furloughStart, policyEnd.minusDays(21))
        furloughEnd   <- datesBetween(claimStart.plusDays(21), policyEnd.minusDays(1))
        claimEnd      <- datesBetween(claimStart.plusDays(21), policyEnd) suchThat (_.isAfter(furloughEnd))
      } yield (furloughStart, furloughEnd, claimStart, claimEnd)

      forAll(gen) {
        case (furloughStart: LocalDate, furloughEnd: LocalDate, claimStart: LocalDate, claimEnd: LocalDate) =>
          val userAnswers = emptyUserAnswers
            .withFurloughStartDate(furloughStart.toString)
            .withFurloughEndDate(furloughEnd.toString)
            .withClaimPeriodStart(claimStart.toString)
            .withClaimPeriodEnd(claimEnd.toString)

          extractFurloughWithinClaimV(userAnswers).value mustBe FurloughWithinClaim(claimStart, furloughEnd)
      }
    }

    "return none" when {

      "claim period start is missing" in new FurloughPeriodExtractor {
        val userAnswers = emptyUserAnswers
          .withFurloughStartDate("2020, 3, 1")
          .withClaimPeriodEnd("2020, 3, 31")

        extractFurloughWithinClaimV(userAnswers) mustBe invalid
      }

      "claim period end is missing" in new FurloughPeriodExtractor {
        val userAnswers = emptyUserAnswers
          .withFurloughStartDate("2020, 3, 1")
          .withClaimPeriodStart("2020, 3, 2")

        extractFurloughWithinClaimV(userAnswers) mustBe invalid
      }

      "furlough start is missing" in new FurloughPeriodExtractor {
        val userAnswers = emptyUserAnswers
          .withClaimPeriodStart("2020, 3, 2")
          .withClaimPeriodEnd("2020, 3, 31")

        extractFurloughWithinClaimV(userAnswers) mustBe invalid
      }
    }

  }

  "calling the .extractFurloughPeriodDatesV()" when {

    "determining the furlough start date" must {

      "the furlough start date should just be the furlough start date" in new FurloughPeriodExtractor {

        val userAnswers = emptyUserAnswers
          .withClaimPeriodEnd("2020, 3, 31")
          .withFurloughStartDate("2020, 3, 1")

        extractFurloughPeriodDatesV(userAnswers).value.start mustBe LocalDate.of(2020, 3, 1)
      }

    }

    "determining the furlough end date" must {

      "user answers furlough is 'FurloughEnded', the furlough end date should be set to the answered furlough end date" in new FurloughPeriodExtractor {

        val userAnswers = emptyUserAnswers
          .withClaimPeriodEnd("2020, 3, 31")
          .withFurloughStartDate("2020, 3, 1")
          .withFurloughEndDate("2020, 3, 20")

        extractFurloughPeriodDatesV(userAnswers).value.`end` mustBe
          LocalDate.of(2020, 3, 20)

      }

      "user answers furlough is 'FurloughOngoing', the furlough end date should be set to the claim period end date" in new FurloughPeriodExtractor {

        val userAnswers = emptyUserAnswers
          .withClaimPeriodEnd("2020, 3, 31")
          .withFurloughStartDate("2020, 3, 1")

        extractFurloughPeriodDatesV(userAnswers).value.`end` mustBe
          LocalDate.of(2020, 3, 31)

      }
    }

    "supplied all answers should build a correct FurloughWithinClaim model" must {

      "user answers furlough is 'FurloughEnded', the furlough end date should be set to the answered furlough end date" in new FurloughPeriodExtractor {

        val userAnswers = emptyUserAnswers
          .withClaimPeriodEnd("2020, 3, 31")
          .withFurloughStartDate("2020, 3, 5")
          .withFurloughEndDate("2020, 3, 20")

        extractFurloughPeriodDatesV(userAnswers).value mustBe
          FurloughWithinClaim(
            start = LocalDate.of(2020, 3, 5),
            end = LocalDate.of(2020, 3, 20)
          )
      }

      "user answers furlough is 'FurloughOngoing', the furlough end date should be set to the claim period end date" in new FurloughPeriodExtractor {

        val userAnswers = emptyUserAnswers
          .withClaimPeriodEnd("2020, 3, 31")
          .withFurloughStartDate("2020, 3, 1")

        extractFurloughPeriodDatesV(userAnswers).value mustBe FurloughWithinClaim(
          start = LocalDate.of(2020, 3, 1),
          end = LocalDate.of(2020, 3, 31)
        )
      }
    }

    "not supplied any answers" must {

      "return invalid" in new FurloughPeriodExtractor {

        val userAnswers = emptyUserAnswers

        extractFurloughPeriodDatesV(userAnswers) mustBe invalid
      }
    }
  }

}
