import com.malliina.sbtutils.{SbtProjects, SbtUtils}

val malliinaGroup = "com.malliina"
val soundGroup = "com.googlecode.soundlibs"

lazy val utilAudio = SbtProjects.mavenPublishProject("util-audio")

organization := malliinaGroup
SbtUtils.gitUserName := "malliina"
SbtUtils.developerName := "Michael Skogberg"
version := "2.2.0"
scalaVersion := "2.12.2"
crossScalaVersions := Seq("2.11.11", scalaVersion.value)
libraryDependencies ++= Seq(
  malliinaGroup %% "util" % "2.6.0",
  "org" % "jaudiotagger" % "2.0.3",
  soundGroup % "tritonus-share" % "0.3.7-2",
  soundGroup % "jlayer" % "1.0.1-1",
  soundGroup % "mp3spi" % "1.9.5-1"
)
resolvers ++= Seq(
  "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
)