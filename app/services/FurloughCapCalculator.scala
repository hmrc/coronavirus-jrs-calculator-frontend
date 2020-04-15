/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import java.time.Month
import java.time.temporal.ChronoUnit

import models.PaymentFrequency.Monthly
import models.{PayPeriod, PaymentFrequency}
import play.api.Logger

import scala.math.BigDecimal.RoundingMode.{RoundingMode, _}

trait FurloughCapCalculator {

  def furloughCap(paymentFrequency: PaymentFrequency, payPeriod: PayPeriod): Double = {
    val furloughCap = FurloughCapMapping.mappings
      .get(paymentFrequency)
      .fold {
        Logger.warn(s"Unable to find a Furlough Cap for $paymentFrequency")
        0.00
      } { cap =>
        cap.value
      }

    paymentFrequency match {
      case Monthly if (payPeriod.start.getMonth != payPeriod.end.getMonth) =>
        calculateFurloughCapNonSimplified(payPeriod)
      case _ => furloughCap
    }
  }

  def partialFurloughCap(payPeriod: PayPeriod): Double = calculateFurloughCapNonSimplified(payPeriod)

  protected def dailyMax(month: Month): Double =
    roundWithMode(2500.00 / month.maxLength, UP)

  private def calculateFurloughCapNonSimplified(payPeriod: PayPeriod): Double = {
    val startMonthDays: Long = ChronoUnit.DAYS.between(payPeriod.start, payPeriod.start.withDayOfMonth(payPeriod.start.getMonth.maxLength))
    val endMonthDays: Long = ChronoUnit.DAYS.between(payPeriod.start.withDayOfMonth(payPeriod.start.getMonth.maxLength), payPeriod.end)
    val startMonthDailyMax: Double = dailyMax(payPeriod.start.getMonth)
    val endMonthDailyMax: Double = dailyMax(payPeriod.end.getMonth)

    roundWithMode((startMonthDays * startMonthDailyMax) + (endMonthDays * endMonthDailyMax), HALF_UP)
  }

  val roundWithMode: (Double, RoundingMode) => Double = (value, mode) => BigDecimal(value).setScale(2, mode).toDouble

}
