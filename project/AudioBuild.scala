import sbt._
import sbt.Keys._

object AudioBuild extends Build {
  lazy val utilAudio = Project("util-audio", file(".")).settings(commonSettings: _*)

  lazy val commonSettings = Seq(
    version := "1.0.1",
    scalaVersion := "2.10.3",
    retrieveManaged := false,
    sbt.Keys.fork in Test := true,
    libraryDependencies ++= Seq(
      "com.github.malliina" %% "util" % "0.7.1",
      "org.scalatest" %% "scalatest" % "2.0" % "test",
      "joda-time" % "joda-time" % "2.1",
      "org.joda" % "joda-convert" % "1.3",
      "org" % "jaudiotagger" % "2.0.3"),
    exportJars := true,
    resolvers ++= Seq(
      "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
      "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/")
  )
}