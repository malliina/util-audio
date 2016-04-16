import com.mle.sbtutils.{SbtProjects, SbtUtils}
import sbt._
import sbt.Keys._
import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin._

object AudioBuild extends Build {
  lazy val utilAudio = SbtProjects.testableProject("util-audio")
    .enablePlugins(bintray.BintrayPlugin)
    .settings(commonSettings: _*)

  val malliinaGroup = "com.malliina"
  val soundGroup = "com.googlecode.soundlibs"

  lazy val commonSettings = assemblySettings ++ Seq(
    organization := malliinaGroup,
    SbtUtils.gitUserName := "malliina",
    SbtUtils.developerName := "Michael Skogberg",
    version := "2.0.0",
    scalaVersion := "2.11.7",
    crossScalaVersions := Seq(scalaVersion.value, "2.10.6"),
    retrieveManaged := false,
    sbt.Keys.fork in Test := true,
    libraryDependencies ++= Seq(
      malliinaGroup %% "util" % "2.4.1",
      "org" % "jaudiotagger" % "2.0.3",
      soundGroup % "tritonus-share" % "0.3.7-2",
      soundGroup % "jlayer" % "1.0.1-1",
      soundGroup % "mp3spi" % "1.9.5-1"),
    updateOptions := updateOptions.value.withCachedResolution(true),
    resolvers ++= Seq(
      "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/",
      "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"),
    mainClass in assembly := Some("com.mle.audio.run.Main"),
    test in assembly := {},
    licenses +=("MIT", url("http://opensource.org/licenses/MIT"))
  )
}
