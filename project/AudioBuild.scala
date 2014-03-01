import sbt._
import sbt.Keys._
import Dependencies._

object AudioBuild extends Build {
  lazy val utilAudio = Project("util-audio", file(".")).settings(commonSettings: _*)

  lazy val commonSettings = Seq(
    version := "1.0.0",
    scalaVersion := "2.10.3",
    retrieveManaged := false,
    sbt.Keys.fork in Test := true,
    libraryDependencies ++= Seq(utilDep, scalaTest, jodaTime, jodaConvert, jAudioTagger),
    exportJars := true,
    resolvers ++= Seq(
      "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
      "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/")
  )
}