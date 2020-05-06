/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages

import java.time.LocalDate

import pages.behaviours.PageBehaviours

class TopupPeriodsPageSpec extends PageBehaviours {

  "TopupPeriodsPage" must {

    beRetrievable[Set[List[LocalDate]]](TopupPeriodsPage)

    beSettable[Set[List[LocalDate]]](TopupPeriodsPage)

    beRemovable[Set[List[LocalDate]]](TopupPeriodsPage)
  }
}
