import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "org.reactivemongo"          %% "play2-reactivemongo"            % "0.19.7-play26",
    "uk.gov.hmrc"                %% "logback-json-logger"            % "5.1.0",
    "uk.gov.hmrc"                %% "play-health"                    % "3.16.0-play-26",
    "uk.gov.hmrc"                %% "play-conditional-form-mapping"  % "1.9.0-play-26",
    "uk.gov.hmrc"                %% "bootstrap-play-26"              % "4.0.0",
    "uk.gov.hmrc"                %% "play-whitelist-filter"          % "3.1.0-play-26",
    "uk.gov.hmrc"                %% "play-frontend-govuk"            % "0.71.0-play-26",
    "uk.gov.hmrc"                %% "play-frontend-hmrc"             % "0.59.0-play-26",
    "org.typelevel"              %% "cats-core"                      % "2.6.0",
    "com.softwaremill.quicklens" %% "quicklens"                      % "1.5.0",
    "com.github.pureconfig"      %% "pureconfig"                     % "0.13.0"
  )

  val test = Seq(
    "org.scalatest"               %% "scalatest"           % "3.0.9",
    "com.ironcorelabs"            %% "cats-scalatest"      % "3.0.0",
    "org.scalatestplus.play"      %% "scalatestplus-play"  % "3.1.2",
    "org.pegdown"                 %  "pegdown"             % "1.6.0",
    "org.jsoup"                   %  "jsoup"               % "1.13.1",
    "com.typesafe.play"           %% "play-test"           % PlayVersion.current,
    "org.mockito"                 %  "mockito-all"         % "1.10.19",
    "com.github.tomakehurst"      %  "wiremock-standalone" % "2.22.0",
    "org.scalacheck"              %% "scalacheck"          % "1.14.1",
    "uk.gov.hmrc"                 %% "hmrctest"            % "3.10.0-play-26"

  ).map(_ % "test, it")

  def apply(): Seq[ModuleID] = compile ++ test

  val akkaVersion = "2.5.23"
  val akkaHttpVersion = "10.0.15"

  val overrides = Seq(
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-protobuf" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion
  )
}
