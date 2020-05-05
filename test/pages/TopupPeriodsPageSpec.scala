/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages

import models.TopupPeriods
import pages.behaviours.PageBehaviours

class TopupPeriodsPageSpec extends PageBehaviours {

  "TopupPeriodsPage" must {

    beRetrievable[Set[TopupPeriods]](TopupPeriodsPage)

    beSettable[Set[TopupPeriods]](TopupPeriodsPage)

    beRemovable[Set[TopupPeriods]](TopupPeriodsPage)
  }
}
