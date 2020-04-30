package handlers

import base.{CoreTestDataBuilder, SpecBase}
import models.JourneyCoreData
import services.PeriodHelper

class JourneyCoreDataBuilderSpec extends SpecBase with CoreTestDataBuilder {

  private val periodHelper: PeriodHelper = new PeriodHelper {}

  "build a journey core data" in new JourneyCoreDataBuilder {
    val periodGenerator = periodHelper.generatePeriods _
    val assignPayDates = periodHelper.assignPayDates _

    build(defaultedMandatoryData, periodGenerator, assignPayDates) must matchPattern {
      case JourneyCoreData(_, _, _, _, _) =>
    }
  }

}
