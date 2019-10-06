import Dependencies._

ThisBuild / scalaVersion     := "2.13.0"
ThisBuild / organization     := "com.sgit"

lazy val root = (project in file("."))
  .settings(
    name := "sgit",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.0-RC2",
    libraryDependencies += "com.github.pathikrit" %% "better-files" % "3.8.0"
  )

import sbtassembly.AssemblyPlugin.defaultUniversalScript

assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultUniversalScript(shebang = false)))

assemblyJarName in assembly := s"${name.value}"
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.