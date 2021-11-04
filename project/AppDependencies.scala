import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val playSuffix = "-play-28"
  val bootstrapVersion = "5.16.0"

  val compile = Seq(
    "org.reactivemongo"             %% "play2-reactivemongo"            % "0.19.7-play28",
    "uk.gov.hmrc"                   %% "play-conditional-form-mapping"  % s"1.10.0$playSuffix",
    "uk.gov.hmrc"                   %% s"bootstrap-frontend$playSuffix" % bootstrapVersion,
    "uk.gov.hmrc"                   %% "play-frontend-hmrc"             % s"1.22.0$playSuffix",
    "org.typelevel"                 %% "cats-core"                      % "2.6.0",
    "com.softwaremill.quicklens"    %% "quicklens"                      % "1.5.0",
    "com.github.pureconfig"         %% "pureconfig"                     % "0.13.0"
  )

  val scalatestVersion = "3.2.10"

  val test = Seq(
    "uk.gov.hmrc"                   %% s"bootstrap-test$playSuffix"     % bootstrapVersion,
    "org.scalatest"                 %% "scalatest"                      % scalatestVersion,
    "com.ironcorelabs"              %% "cats-scalatest"                 % "3.0.0",
    "org.scalatestplus.play"        %% "scalatestplus-play"             % "5.1.0",
    "org.pegdown"                   %  "pegdown"                        % "1.6.0",
    "org.jsoup"                     %  "jsoup"                          % "1.14.3",
    "com.typesafe.play"             %% "play-test"                      % PlayVersion.current,
    "org.scalatestplus"             %% "mockito-3-12"                   % s"$scalatestVersion.0",
    "org.mockito"                   %  "mockito-core"                   % "3.12.4",
    "com.github.tomakehurst"        %  "wiremock-jre8"                  % "2.31.0",
    "org.scalatestplus"             %% "scalacheck-1-15"                % s"$scalatestVersion.0",
    "org.scalacheck"                %% "scalacheck"                     % "1.15.4"

  ).map(_ % "test, it")

  def apply(): Seq[ModuleID] = compile ++ test

  val akkaVersion = "2.6.14"
  val akkaHttpVersion = "10.1.14"

  val overrides = Seq(
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-protobuf" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion
  )
}
