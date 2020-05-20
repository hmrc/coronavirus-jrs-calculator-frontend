import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "org.reactivemongo" %% "play2-reactivemongo"            % "0.18.6-play26",
    "uk.gov.hmrc"       %% "logback-json-logger"            % "4.8.0",
    "uk.gov.hmrc"       %% "play-health"                    % "3.14.0-play-26",
    "uk.gov.hmrc"       %% "play-conditional-form-mapping"  % "1.2.0-play-26",
    "uk.gov.hmrc"       %% "bootstrap-play-26"              % "1.6.0",
    "uk.gov.hmrc"       %% "play-whitelist-filter"          % "3.1.0-play-26",
    "uk.gov.hmrc"       %% "play-frontend-govuk"            % "0.41.0-play-26",
    "org.typelevel"     %% "cats-core"                      % "2.1.1",
    "com.softwaremill.quicklens" %% "quicklens"             % "1.5.0"
  )

  val test = Seq(
    //"org.mockito"                 %  "mockito-all"        % "1.10.19",
    //"com.vladsch.flexmark"        % "flexmark-all"        % "0.50.30",
    "org.scalatest"               %% "scalatest"          % "3.1.0",
    "org.scalatestplus.play"      %% "scalatestplus-play" % "3.1.3",
    "org.pegdown"                 %  "pegdown"            % "1.6.0",
    "org.jsoup"                   %  "jsoup"              % "1.12.1",
    "com.typesafe.play"           %% "play-test"          % PlayVersion.current,
    "com.ironcorelabs"            %% "cats-scalatest"     % "3.0.5",
    "com.vladsch.flexmark"        % "flexmark-profile-pegdown" % "0.36.8",
    "org.scalatestplus"           %% "mockito-1-10"       % "3.1.0.0",
    "org.scalatestplus"           %% "scalacheck-1-14"    % "3.1.0.0",
    "org.scalacheck"              %% "scalacheck"         % "1.14.1"
  ).map(_ % Test)

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
