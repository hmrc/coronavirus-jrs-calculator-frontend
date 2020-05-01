/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package generators

import models.UserAnswers
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.TryValues
import pages._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersGenerator extends TryValues {
  self: Generators =>

  val generators: Seq[Gen[(QuestionPage[_], JsValue)]] =
    arbitrary[(LastYearPayPage.type, JsValue)] ::
      arbitrary[(LastPayDatePage.type, JsValue)] ::
      arbitrary[(FurloughCalculationsPage.type, JsValue)] ::
      arbitrary[(PartialPayBeforeFurloughPage.type, JsValue)] ::
      arbitrary[(PartialPayBeforeFurloughPage.type, JsValue)] ::
      arbitrary[(PartialPayAfterFurloughPage.type, JsValue)] ::
      arbitrary[(VariableGrossPayPage.type, JsValue)] ::
      arbitrary[(EmployeeStartDatePage.type, JsValue)] ::
      arbitrary[(VariableLengthEmployedPage.type, JsValue)] ::
      arbitrary[(FurloughStartDatePage.type, JsValue)] ::
      arbitrary[(FurloughEndDatePage.type, JsValue)] ::
      arbitrary[(FurloughStatusPage.type, JsValue)] ::
      arbitrary[(ClaimPeriodEndPage.type, JsValue)] ::
      arbitrary[(ClaimPeriodStartPage.type, JsValue)] ::
      arbitrary[(PensionContributionPage.type, JsValue)] ::
      arbitrary[(NicCategoryPage.type, JsValue)] ::
      arbitrary[(SalaryQuestionPage.type, JsValue)] ::
      arbitrary[(PaymentFrequencyPage.type, JsValue)] ::
      arbitrary[(PayDatePage.type, JsValue)] ::
      arbitrary[(PayMethodPage.type, JsValue)] ::
      Nil

  implicit lazy val arbitraryUserData: Arbitrary[UserAnswers] = {

    import models._

    Arbitrary {
      for {
        id <- nonEmptyString
        data <- generators match {
                 case Nil => Gen.const(Map[QuestionPage[_], JsValue]())
                 case _   => Gen.mapOf(oneOf(generators))
               }
      } yield
        UserAnswers(
          id = id,
          data = data.foldLeft(Json.obj()) {
            case (obj, (path, value)) =>
              obj.setObject(path.path, value).get
          }
        )
    }
  }
}
