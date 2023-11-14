import sbt.*

object AppDependencies {
  import play.core.PlayVersion

  val playSuffix = "-play-28"
  val bootstrapVersion = "7.23.0"
  val hmrcMongoVersion = "1.3.0"

  val compile = Seq(
    "uk.gov.hmrc.mongo"             %% s"hmrc-mongo$playSuffix"             % hmrcMongoVersion,
    "uk.gov.hmrc"                   %% "play-conditional-form-mapping"      % s"1.13.0$playSuffix",
    "uk.gov.hmrc"                   %% s"bootstrap-frontend$playSuffix"     % bootstrapVersion,
    "uk.gov.hmrc"                   %% "play-frontend-hmrc"                 % s"7.27.0$playSuffix",
    "org.typelevel"                 %% "cats-core"                          % "2.10.0",
    "com.softwaremill.quicklens"    %% "quicklens"                          % "1.9.6",
    "com.github.pureconfig"         %% "pureconfig"                         % "0.17.4",
    "com.fasterxml.jackson.module"  %% "jackson-module-scala"               % "2.15.3",
    "com.typesafe.play"             %% "play-json"                          % "2.10.3",
    "org.commonmark"                % "commonmark"                          % "0.19.0",
    "ch.qos.logback"                % "logback-classic"                     % "1.4.11",
    "com.typesafe.play"             %% "play-logback"                       % "2.9.0"
  )

  val scalatestVersion = "3.2.17"

  val test = Seq(
    "uk.gov.hmrc.mongo"             %% s"hmrc-mongo-test$playSuffix"        % hmrcMongoVersion,
    "uk.gov.hmrc"                   %% s"bootstrap-test$playSuffix"         % bootstrapVersion,
    "org.scalatest"                 %% "scalatest"                          % "3.3.0-SNAP4",
    "com.ironcorelabs"              %% "cats-scalatest"                     % "3.1.1",
    "org.scalatestplus.play"        %% "scalatestplus-play"                 % "7.0.0",
    "org.pegdown"                   %  "pegdown"                            % "1.6.0",
    "org.jsoup"                     %  "jsoup"                              % "1.16.2",
    "com.typesafe.play"             %% "play-test"                          % "2.9.0",
    "org.scalatestplus"             %% "mockito-3-12"                       % "3.2.10.0",
    "org.mockito"                   %  "mockito-core"                       % "5.7.0",
    "com.github.tomakehurst"        %  "wiremock-jre8"                      % "3.0.1",
    "org.scalatestplus"             %% "scalacheck-1-16"                    % "3.2.14.0",
    "org.scalacheck"                %% "scalacheck"                         % "1.17.0",
    "com.vladsch.flexmark"          %  "flexmark-all"                       % "0.64.8"
  ).map(_ % "test, it")

  def apply(): Seq[ModuleID] = compile ++ test

  val akkaVersion = "2.9.0-M2"
  val akkaHttpVersion = "10.6.0-M1"

  val overrides = Seq(
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-protobuf" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-serialization-jackson" % akkaVersion,
  )
}
