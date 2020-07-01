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

import pureconfig.{CamelCase, ConfigFieldMapping}
import pureconfig.generic.ProductHint

//TODO do not need this application.conf is changed from camel case to kebab-case
trait CamelCaseConf {
  implicit def hint[A]: ProductHint[A] = ProductHint[A](ConfigFieldMapping(CamelCase, CamelCase))
}
