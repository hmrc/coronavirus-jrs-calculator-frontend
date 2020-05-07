/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages

import models.TopUpPeriod
import pages.behaviours.PageBehaviours

class TopUpPeriodsPageSpec extends PageBehaviours {

  "TopupPeriodsPage" must {

    beRetrievable[List[TopUpPeriod]](TopUpPeriodsPage)

    beSettable[List[TopUpPeriod]](TopUpPeriodsPage)

    beRemovable[List[TopUpPeriod]](TopUpPeriodsPage)
  }
}
