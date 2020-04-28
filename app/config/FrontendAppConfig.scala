/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package config

import java.time.LocalDate

import com.google.inject.{Inject, Singleton}
import play.api.Configuration

import scala.util.Try

@Singleton
class FrontendAppConfig @Inject()(val configuration: Configuration) {

  lazy val host: String = configuration.get[String]("host")
  lazy val appName: String = configuration.get[String]("appName")

  private val contactHost = configuration.get[String]("contact-frontend.host")
  private val contactFormServiceIdentifier = "jrsc"

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

  val calculationGuidance =
    "https://www.gov.uk/guidance/work-out-80-of-your-employees-wages-to-claim-through-the-coronavirus-job-retention-scheme"

  val ninoCatLetter = "https://www.gov.uk/national-insurance-rates-letters/category-letters"

  val workOutHowMuch =
    "https://www.gov.uk/guidance/work-out-80-of-your-employees-wages-to-claim-through-the-coronavirus-job-retention-scheme#work-out-how-much-you-can-claim-for-employer-national-insurance-contributions"

  val webchatHelpUrl = "https://www.tax.service.gov.uk/ask-hmrc/webchat/job-retention-scheme"

  val jobRetentionScheme = "https://www.gov.uk/guidance/claim-for-wages-through-the-coronavirus-job-retention-scheme"

}
