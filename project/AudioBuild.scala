import sbt._
import sbt.Keys._
import sbtassembly.Plugin.AssemblyKeys._

object AudioBuild extends Build {
  lazy val utilAudio = Project("util-audio", file(".")).settings(commonSettings: _*)

  lazy val commonSettings = sbtassembly.Plugin.assemblySettings ++ Seq(
    test in assembly := {},
    organization := "com.github.malliina",
    version := "1.1.1",
    scalaVersion := "2.10.3",
    retrieveManaged := false,
    sbt.Keys.fork in Test := true,
    libraryDependencies ++= Seq(
      "com.github.malliina" %% "util" % "1.2.3",
      "org.scalatest" %% "scalatest" % "2.0" % "test",
      "org" % "jaudiotagger" % "2.0.3"),
    exportJars := true,
    resolvers ++= Seq(
      "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
      "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/")
  )
}