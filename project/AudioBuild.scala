import com.mle.sbtutils.SbtUtils
import sbt._
import sbt.Keys._

object AudioBuild extends Build {
  lazy val utilAudio = Project("util-audio", file(".")).settings(commonSettings: _*)

  val mleGroup = "com.github.malliina"
  val soundGroup = "com.googlecode.soundlibs"

  lazy val commonSettings = SbtUtils.publishSettings ++ Seq(
    SbtUtils.gitUserName := "malliina",
    SbtUtils.developerName := "Michael Skogberg",
    version := "1.4.1",
    scalaVersion := "2.11.0",
    crossScalaVersions := Seq("2.11.0", "2.10.4"),
    retrieveManaged := false,
    sbt.Keys.fork in Test := true,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.1.3" % "test",
      mleGroup %% "util" % "1.3.1",
      "org" % "jaudiotagger" % "2.0.3",
      soundGroup % "tritonus-share" % "0.3.7-2",
      soundGroup % "jlayer" % "1.0.1-1",
      soundGroup % "mp3spi" % "1.9.5-1",
      "com.netflix.rxjava" % "rxjava-scala" % "0.18.1"),
    libraryDependencies := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, scalaMajor)) if scalaMajor >= 11 =>
          libraryDependencies.value :+ "org.scala-lang.modules" %% "scala-xml" % "1.0.1"
        case _ =>
          libraryDependencies.value
      }
    },
    exportJars := true,
    resolvers ++= Seq(
      "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
      "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/",
      "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/")
  )


}