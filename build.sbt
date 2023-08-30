val projectName        = "gvolpe-errorhandling-http4s"
val projectDescription = "Tinkering with the source of Gabriel Volpe's blog post on error handling with http4s"

// turbo mode with ClassLoader layering
ThisBuild / turbo                  := true // default: false
// build definition source watch
Global / onChangedBuildSource      := ReloadOnSourceChanges
// include resolvers from the metabuild
ThisBuild / includePluginResolvers := true // default: false
// enable generation of SemanticDB
Global / semanticdbEnabled         := true // default: false
// run apps in a separate Java process
Global / fork                      := true // default: false

lazy val commonSettings =
  Seq(
    version                           := Versions.projectVersion,
    scalaVersion                      := Versions.scala2Version,
    scalacOptions ++= ScalacOptions.defaultScalacOptions,
    Compile / console / scalacOptions := ScalacOptions.consoleScalacOptions,
    Test / console / scalacOptions    := ScalacOptions.consoleScalacOptions,
    libraryDependencies ++= Dependencies.dependencies
  )

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    name        := projectName,
    description := projectDescription
  )
