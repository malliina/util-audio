import com.malliina.sbtutils.{SbtProjects, SbtUtils}

val malliinaGroup = "com.malliina"
val soundGroup = "com.googlecode.soundlibs"

lazy val utilAudio = SbtProjects.mavenPublishProject("util-audio")

organization := malliinaGroup
SbtUtils.gitUserName := "malliina"
SbtUtils.developerName := "Michael Skogberg"
scalaVersion := "2.12.4"
releaseCrossBuild := true
libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.6",
  "org.slf4j" % "slf4j-api" % "1.7.25",
  malliinaGroup %% "util-base" % "1.5.0",
  "org" % "jaudiotagger" % "2.0.3",
  soundGroup % "tritonus-share" % "0.3.7.4",
  soundGroup % "jlayer" % "1.0.1.4",
  soundGroup % "mp3spi" % "1.9.5.4"
)
resolvers ++= Seq(
  "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
  Resolver.bintrayRepo("malliina", "maven")
)
