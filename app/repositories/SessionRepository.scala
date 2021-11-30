/*
 * Copyright 2021 HM Revenue & Customs
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

package repositories

import config.FrontendAppConfig
import models.UserAnswers
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Indexes.ascending
import org.mongodb.scala.model.{FindOneAndReplaceOptions, IndexModel, IndexOptions}
import play.api.libs.json._
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class DefaultSessionRepository @Inject()(mongo: MongoComponent, appConfig: FrontendAppConfig)(implicit ec: ExecutionContext)
    extends PlayMongoRepository[UserAnswers](
      mongo,
      "user-answers",
      Format(UserAnswers.reads, UserAnswers.writes),
      indexes = Seq(
        IndexModel(
          ascending("lastUpdated"),
          IndexOptions()
            .name("user-answers-last-updated-index")
            .expireAfter(appConfig.mongoConf.timeToLiveInSeconds, TimeUnit.SECONDS)
        )
      )
    ) with SessionRepository {

  override def get(id: String): Future[Option[UserAnswers]] =
    collection.find(equal("_id", id)).toFuture().map(_.headOption)

  override def set(userAnswers: UserAnswers): Future[Boolean] = {
    val selector = equal("_id", userAnswers.id)
    val newDoc   = userAnswers copy (lastUpdated = LocalDateTime.now)
    collection.findOneAndReplace(selector, newDoc, FindOneAndReplaceOptions().upsert(true)).toFuture().map(_ => true)
  }
}

trait SessionRepository {

  def get(id: String): Future[Option[UserAnswers]]

  def set(userAnswers: UserAnswers): Future[Boolean]
}
