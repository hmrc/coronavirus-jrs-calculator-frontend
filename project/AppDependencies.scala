import sbt.*

object AppDependencies {

  val playSuffix = "-play-30"
  val bootstrapVersion = "8.4.0"
  val hmrcMongoVersion = "1.8.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc.mongo"             %% s"hmrc-mongo$playSuffix"                       % hmrcMongoVersion,
    "uk.gov.hmrc"                   %% s"play-conditional-form-mapping$playSuffix"    % "2.0.0",
    "uk.gov.hmrc"                   %% s"bootstrap-frontend$playSuffix"               % bootstrapVersion,
    "uk.gov.hmrc"                   %% s"play-frontend-hmrc$playSuffix"               % "8.4.0",
    "org.typelevel"                 %% "cats-core"                                    % "2.10.0",
    "com.softwaremill.quicklens"    %% "quicklens"                                    % "1.9.6",
    "com.github.pureconfig"         %% "pureconfig"                                   % "0.17.4",
    "com.fasterxml.jackson.module"  %% "jackson-module-scala"                         % "2.16.0",
    "org.playframework"             %% "play-json"                                    % "3.0.2",
    "ch.qos.logback"                % "logback-classic"                               % "1.4.14",
    "com.typesafe.play"             %% "play-logback"                                 % "2.8.21",
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc.mongo"             %% s"hmrc-mongo-test$playSuffix"        % hmrcMongoVersion,
    "uk.gov.hmrc"                   %% s"bootstrap-test$playSuffix"         % bootstrapVersion,
    "com.ironcorelabs"              %% "cats-scalatest"                     % "3.1.1",
    "org.pegdown"                   %  "pegdown"                            % "1.6.0",
    "org.jsoup"                     %  "jsoup"                              % "1.17.1",
    "org.playframework"             %% "play-test"                          % "3.0.0",
    "org.scalatestplus"             %% "mockito-3-12"                       % "3.2.10.0",
    "org.mockito"                   %  "mockito-core"                       % "5.8.0",
    "org.wiremock"                  %  "wiremock"                           % "3.3.1",
    "org.scalatestplus"             %% "scalacheck-1-16"                    % "3.2.14.0",
    "org.scalacheck"                %% "scalacheck"                         % "1.17.0",
    "com.vladsch.flexmark"          %  "flexmark-all"                       % "0.64.8",
  ).map(_ % "test, it")

  def apply(): Seq[ModuleID] = compile ++ test

  val pekkoHTTPVersion = "1.0.1"
  val pekkoVersion = "1.0.2"

  val overrides: Seq[ModuleID] = Seq(
    "org.apache.pekko" %% "pekko-stream" % pekkoVersion,
    "org.apache.pekko" %% "pekko-protobuf" % pekkoVersion,
    "org.apache.pekko" %% "pekko-slf4j" % pekkoVersion,
    "org.apache.pekko" %% "pekko-actor" % pekkoVersion,
    "org.apache.pekko" %% "pekko-actor-typed" % pekkoVersion,
    "org.apache.pekko" %% "pekko-serialization-jackson" % pekkoVersion,
    "org.apache.pekko" %% "pekko-http-core" % pekkoHTTPVersion
  )
}
