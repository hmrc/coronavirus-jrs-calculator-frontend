/*
 * Copyright 2020 HM Revenue & Customs
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

package config

import java.time.LocalDate

import com.google.inject.{Inject, Singleton}
import play.api.Configuration
import uk.gov.hmrc.play.bootstrap.binders.SafeRedirectUrl

import scala.util.Try

@Singleton
class FrontendAppConfig @Inject()(val configuration: Configuration) {

  lazy val host: String = configuration.get[String]("host")
  lazy val appName: String = configuration.get[String]("appName")

  private val contactHost = configuration.get[String]("contact-frontend.host")
  private val contactFormServiceIdentifier = "jrsc"

  def reportAccessibilityIssueUrl(problemPageUri: String): String =
    s"$contactHost/contact/accessibility-unauthenticated?service=$contactFormServiceIdentifier&userAction=${SafeRedirectUrl(host + problemPageUri).encodedUrl}"

  val gtmContainer: Option[String] = (Try {
    configuration.get[String]("gtm.container")
  } map {
    case "main"         => Some("GTM-NDJKHWK")
    case "transitional" => Some("GTM-TSFTCWZ")
  }) getOrElse (None)

  val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"

  val contactStandaloneForm = s"$contactHost/contact/contact-hmrc-unauthenticated?service=$contactFormServiceIdentifier"

  val feedbackUrl = s"$contactHost/contact/beta-feedback-unauthenticated?service=$contactFormServiceIdentifier"

  lazy val timeout: Int = configuration.get[Int]("timeout.timeout")
  lazy val countdown: Int = configuration.get[Int]("timeout.countdown")

  lazy val authUrl: String = configuration.get[Service]("auth").baseUrl
  lazy val loginUrl: String = configuration.get[String]("urls.login")
  lazy val loginContinueUrl: String = configuration.get[String]("urls.loginContinue")

  lazy val languageTranslationEnabled: Boolean = configuration.get[Boolean]("features.welsh-translation")

  lazy val cookies: String = host + configuration.get[String]("urls.footer.cookies")
  lazy val privacy: String = host + configuration.get[String]("urls.footer.privacy")
  lazy val termsConditions: String = host + configuration.get[String]("urls.footer.termsConditions")
  lazy val govukHelp: String = configuration.get[String]("urls.footer.govukHelp")

  lazy val schemeStartDate = LocalDate.parse(configuration.get[String]("scheme.startDate"))
  lazy val schemeEndDate = LocalDate.parse(configuration.get[String]("scheme.endDate"))

  lazy val calculatorVersion = configuration.get[String]("calculator.version")

  val variableJourneyEnabled = configuration.get[Boolean]("variable.journey.enabled")

  val topUpJourneyEnabled = configuration.get[Boolean]("topup.journey.enabled")

  val confirmationWithDetailedBreakdowns = configuration.get[Boolean]("confirmationWithDetailedBreakdowns.enabled")

  val calculationGuidance =
    "https://www.gov.uk/guidance/work-out-80-of-your-employees-wages-to-claim-through-the-coronavirus-job-retention-scheme"

  val ninoCatLetter = "https://www.gov.uk/national-insurance-rates-letters/category-letters"

  val workOutHowMuch =
    "https://www.gov.uk/guidance/work-out-80-of-your-employees-wages-to-claim-through-the-coronavirus-job-retention-scheme#work-out-how-much-you-can-claim-for-employer-national-insurance-contributions"

  val webchatHelpUrl = "https://www.tax.service.gov.uk/ask-hmrc/webchat/job-retention-scheme"

  val jobRetentionScheme = "https://www.gov.uk/guidance/claim-for-wages-through-the-coronavirus-job-retention-scheme"

}
