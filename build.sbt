ThisBuild / scalaVersion     := "2.13.0"
ThisBuild / organization     := "com.sgit"

scalacOptions ++= Seq("-Xlint:unused")

lazy val root = (project in file("."))
  .settings(
    name := "sgit",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8",
    libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.0-RC2",
    libraryDependencies += "com.github.pathikrit" %% "better-files" % "3.8.0",
    libraryDependencies += "commons-codec" % "commons-codec" % "1.13",
    libraryDependencies += "commons-io" % "commons-io" % "2.6",
    Test / parallelExecution := false
  )

import sbtassembly.AssemblyPlugin.defaultUniversalScript

assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultUniversalScript(shebang = false)))
assemblyJarName in assembly := s"${name.value}"
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.