/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages

import java.time.LocalDate

import pages.behaviours.PageBehaviours

class TopupPeriodsPageSpec extends PageBehaviours {

  "TopupPeriodsPage" must {

    beRetrievable[List[LocalDate]](TopupPeriodsPage)

    beSettable[List[LocalDate]](TopupPeriodsPage)

    beRemovable[List[LocalDate]](TopupPeriodsPage)
  }
}
