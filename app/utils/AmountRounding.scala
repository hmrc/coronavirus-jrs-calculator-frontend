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

package utils

import models.Amount

import scala.math.BigDecimal.RoundingMode.RoundingMode

object AmountRounding {

  val roundWithMode: (BigDecimal, RoundingMode) => BigDecimal = (value, mode) => value.setScale(2, mode)

  val roundWithModeReturnAmount: (BigDecimal, RoundingMode) => Amount = (value, mode) => Amount(value.setScale(2, mode))

  val roundAmountWithMode: (Amount, RoundingMode) => Amount =
    (value, mode) => Amount(value.value.setScale(2, mode))

}
