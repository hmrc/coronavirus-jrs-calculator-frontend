import sbt.*

object AppDependencies {

  val playSuffix = "-play-28"
  val bootstrapVersion = "7.22.0"
  val hmrcMongoVersion = "1.3.0"

  val compile = Seq(
    "uk.gov.hmrc.mongo"             %% s"hmrc-mongo$playSuffix"             % hmrcMongoVersion,
    "uk.gov.hmrc"                   %% "play-conditional-form-mapping"      % s"1.13.0$playSuffix",
    "uk.gov.hmrc"                   %% s"bootstrap-frontend$playSuffix"     % bootstrapVersion,
    "uk.gov.hmrc"                   %% "play-frontend-hmrc"                 % s"7.27.0$playSuffix",
    "org.typelevel"                 %% "cats-core"                          % "2.9.0",
    "com.softwaremill.quicklens"    %% "quicklens"                          % "1.8.10",
    "com.github.pureconfig"         %% "pureconfig"                         % "0.17.4",
    "com.fasterxml.jackson.module"  %% "jackson-module-scala"               % "2.14.2",
    "org.playframework"             %% "play-json"                          % "3.0.1",
    "ch.qos.logback"                % "logback-classic"                     % "1.4.11",
    "com.typesafe.play"             %% "play-logback"                       % "2.8.21"
  )

  val scalatestVersion = "3.1.4"

  val test = Seq(
    "uk.gov.hmrc.mongo"             %% s"hmrc-mongo-test$playSuffix"        % hmrcMongoVersion,
    "uk.gov.hmrc"                   %% s"bootstrap-test$playSuffix"         % bootstrapVersion,
    "org.scalatest"                 %% "scalatest"                          % scalatestVersion,
    "com.ironcorelabs"              %% "cats-scalatest"                     % "3.1.1",
    "org.scalatestplus.play"        %% "scalatestplus-play"                 % "5.1.0",
    "org.pegdown"                   %  "pegdown"                            % "1.6.0",
    "org.jsoup"                     %  "jsoup"                              % "1.15.4",
    "org.playframework"             %% "play-test"                          % "3.0.0",
    "org.scalatestplus"             %% "mockito-3-12"                       % "3.2.10.0",
    "org.mockito"                   %  "mockito-core"                       % "5.2.0",
    "com.github.tomakehurst"        %  "wiremock-jre8"                      % "2.35.0",
    "org.scalatestplus"             %% "scalacheck-1-16"                    % "3.2.14.0",
    "org.scalacheck"                %% "scalacheck"                         % "1.17.0",
    "com.vladsch.flexmark"          %  "flexmark-all"                       % "0.64.6"
  ).map(_ % "test, it")

  def apply(): Seq[ModuleID] = compile ++ test

  val akkaVersion = "2.6.21"
  val akkaHttpVersion = "10.1.14"

  val overrides = Seq(
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-protobuf" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion
  )
}
