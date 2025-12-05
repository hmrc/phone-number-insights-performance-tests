import sbt._

object Dependencies {

  private val gatlingVersion = "3.5.1"

  val test = Seq(
    "com.typesafe"          % "config"                    % "1.4.2"        % Test,
    "uk.gov.hmrc"          %% "performance-test-runner"   % "6.3.0"         % Test,
    "io.gatling"            % "gatling-test-framework"    % gatlingVersion % Test
  )

}
