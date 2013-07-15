import sbt._
import sbt.Keys._
import Dependencies._

object AudioBuild extends Build {
  lazy val utilAudio = Project("util-audio", file("."), settings = commonSettings)
    .settings(
    libraryDependencies ++= Seq(utilDep, scalaTest, jodaTime, jodaConvert, jAudioTagger),
    exportJars := true
  )

  lazy val commonSettings = Defaults.defaultSettings ++ Seq(
    scalaVersion := "2.10.2",
    retrieveManaged := false,
    sbt.Keys.fork in Test := true,
    resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
    resolvers += "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/"
  )
}