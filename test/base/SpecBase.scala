/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package base

import org.scalatest.{MustMatchers, OptionValues, TryValues, WordSpec}

trait SpecBase extends WordSpec with MustMatchers with TryValues with OptionValues
