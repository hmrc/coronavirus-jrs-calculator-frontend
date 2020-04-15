/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import java.time.LocalDate

import base.SpecBase
import models.Calculation.NicCalculationResult
import models.CalculationResult._
import play.api.libs.json.Json

class CalculationResultSpec extends SpecBase {

  "serialize/deserialize from/to json" in {
    val payPeriod =
      PayPeriodWithPayDay(PayPeriod(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31)), PaymentDate(LocalDate.of(2020, 3, 20)))
    val nicCalculationResult = CalculationResult(NicCalculationResult, 0.10, Seq(PayPeriodBreakdown(123.00, payPeriod)))
    val expectedJsValue =
      Json.parse(s"""
                    |{
                    |  "calculation": "nic",
                    |  "total": 0.1,
                    |  "payPeriodBreakdowns": [
                    |    {
                    |      "amount": 123,
                    |      "payPeriodWithPayDay": {
                    |        "payPeriod": {
                    |          "start": "2020-03-01",
                    |          "end": "2020-03-31"
                    |        },
                    |        "paymentDate": "2020-03-20"
                    |      }
                    |    }
                    |  ]
                    |}""".stripMargin)

    Json.toJson(nicCalculationResult) mustBe expectedJsValue
  }
}
