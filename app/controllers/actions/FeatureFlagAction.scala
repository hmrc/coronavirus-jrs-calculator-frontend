/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.actions

import com.google.inject.Inject
import controllers.routes
import models.requests.IdentifierRequest
import play.api.Configuration
import play.api.mvc.Results.Redirect
import play.api.mvc.{ActionFilter, Call, Result}

import scala.concurrent.{ExecutionContext, Future}

class FeatureFlagAction(
  maybeFlag: Option[FeatureFlagKey],
  configuration: Configuration,
  implicit protected val executionContext: ExecutionContext
) extends ActionFilter[IdentifierRequest] {
  override protected def filter[A](request: IdentifierRequest[A]): Future[Option[Result]] =
    maybeFlag
      .map(flag =>
        Future.successful(if (configuration.getOptional[Boolean](flag.key).getOrElse(false)) {
          None
        } else {
          Some(Redirect(flag.redirectRoute))
        }))
      .getOrElse(Future.successful(None))
}

trait FeatureFlagActionProvider {
  def apply(flag: Option[FeatureFlagKey]): ActionFilter[IdentifierRequest]
}

class FeatureFlagActionProviderImpl @Inject()(configuration: Configuration, ec: ExecutionContext) extends FeatureFlagActionProvider {
  override def apply(flag: Option[FeatureFlagKey] = None): ActionFilter[IdentifierRequest] =
    new FeatureFlagAction(flag, configuration, ec)
}

sealed trait FeatureFlagKey {
  def key: String
  def redirectRoute: Call
}

object FeatureFlagKey {

  case object VariableJourneyFlag extends FeatureFlagKey {
    val key = "variable.journey.enabled"
    val redirectRoute = routes.ComingSoonController.onPageLoad()
  }

  case object TopupJourneyFlag extends FeatureFlagKey {
    val key = "topup.journey.enabled"
    val redirectRoute = routes.ComingSoonController.onPageLoad(showCalculateTopupsLink = true)
  }

}
