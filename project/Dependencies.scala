import sbt._
import Versions._

object Dependencies {

  lazy val libraries =
    Seq(
      "org.scalameta" %% "munit"               % munitVersion, // % Test,
      "io.circe"      %% "circe-core"          % circeVersion,
      "io.circe"      %% "circe-generic"       % circeVersion,
      "io.circe"      %% "circe-parser"        % circeVersion,
      "io.circe"      %% "circe-literal"       % circeVersion,
      "org.http4s"    %% "http4s-dsl"          % http4sVersion,
      "org.http4s"    %% "http4s-ember-server" % http4sVersion,
      "org.http4s"    %% "http4s-circe"        % http4sVersion,
      // "com.beachape"                  %% "enumeratum"        % enumeratumVersion,
      // "com.softwaremill.sttp.client3" %% "core"              % sttpVersion,
      // "com.softwaremill.sttp.client3" %% "circe"             % sttpVersion,
      // "com.softwaremill.sttp.client3" %% "akka-http-backend" % sttpVersion,
      "dev.optics"    %% "monocle-core"        % monocleVersion,
      "dev.optics"    %% "monocle-macro"       % monocleVersion,
      // "com.softwaremill.quicklens"    %% "quicklens"         % quicklensVersion,
      "org.typelevel" %% "cats-effect"         % catsEffectVersion,
      "com.olegpy"    %% "meow-mtl-core"       % meowMtlVersion,
      // "com.olegpy"    %% "meow-mtl-effects"    % meowMtlVersion,
      // "io.estatico"                   %% "newtype"           % newtypeVersion,
      // "tf.tofu"                       %% "derevo-core"       % derevoVersion,
      // "org.typelevel"                 %% "simulacrum"        % simulacrumVersion % "provided",
      "org.slf4j"      % "slf4j-api"           % slf4jVersion,
      "org.slf4j"      % "slf4j-simple"        % slf4jVersion
    )

  lazy val compilerPlugins =
    Seq(
      compilerPlugin("org.typelevel" % "kind-projector"     % kindProjectorVersion cross CrossVersion.full),
      compilerPlugin("com.olegpy"   %% "better-monadic-for" % betterMonadicForVersion)
    )

  lazy val dependencies = libraries ++ compilerPlugins
}
