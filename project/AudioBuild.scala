import com.malliina.sbtutils.{SbtProjects, SbtUtils}
import sbt.Keys._
import sbt._
import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin._

object AudioBuild {
  lazy val utilAudio = SbtProjects.mavenPublishProject("util-audio")
    .settings(commonSettings: _*)

  val malliinaGroup = "com.malliina"
  val soundGroup = "com.googlecode.soundlibs"

  lazy val commonSettings = assemblySettings ++ Seq(
    organization := malliinaGroup,
    SbtUtils.gitUserName := "malliina",
    SbtUtils.developerName := "Michael Skogberg",
    version := "2.1.0",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      malliinaGroup %% "util" % "2.5.0",
      "org" % "jaudiotagger" % "2.0.3",
      soundGroup % "tritonus-share" % "0.3.7-2",
      soundGroup % "jlayer" % "1.0.1-1",
      soundGroup % "mp3spi" % "1.9.5-1"
    ),
    resolvers ++= Seq(
      "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/",
      "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
    ),
    mainClass in assembly := Some("com.mle.audio.run.Main"),
    test in assembly := {}
  )
}
