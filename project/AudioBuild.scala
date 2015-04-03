import com.mle.sbtutils.{SbtProjects, SbtUtils}
import sbt._
import sbt.Keys._
import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin._

object AudioBuild extends Build {
  lazy val utilAudio = SbtProjects.mavenPublishProject("util-audio").settings(commonSettings: _*)

  val mleGroup = "com.github.malliina"
  val soundGroup = "com.googlecode.soundlibs"

  lazy val commonSettings = assemblySettings ++ Seq(
    SbtUtils.gitUserName := "malliina",
    SbtUtils.developerName := "Michael Skogberg",
    version := "1.5.0",
    scalaVersion := "2.11.6",
    crossScalaVersions := Seq(scalaVersion.value, "2.10.4"),
    retrieveManaged := false,
    sbt.Keys.fork in Test := true,
    libraryDependencies ++= Seq(
      mleGroup %% "util" % "1.6.1",
      "org" % "jaudiotagger" % "2.0.3",
      soundGroup % "tritonus-share" % "0.3.7-2",
      soundGroup % "jlayer" % "1.0.1-1",
      soundGroup % "mp3spi" % "1.9.5-1",
      "io.reactivex" %% "rxscala" % "0.24.1"),
    libraryDependencies := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, scalaMajor)) if scalaMajor >= 11 =>
          libraryDependencies.value :+ "org.scala-lang.modules" %% "scala-xml" % "1.0.2"
        case _ =>
          libraryDependencies.value
      }
    },
    exportJars := true,
    resolvers ++= Seq(
      "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/",
      "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"),
    mainClass in assembly := Some("com.mle.audio.run.Main"),
    test in assembly := {}
  )
}