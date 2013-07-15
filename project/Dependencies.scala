import sbt._

object Dependencies {
  val utilVersion = "0.7.1"
  val utilGroup = "com.github.malliina"
  val utilDep = "com.github.malliina" %% "util" % "0.7.1"
  val scalaTest = "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  val jodaTime = "joda-time" % "joda-time" % "2.1"
  val jodaConvert = "org.joda" % "joda-convert" % "1.3"
  val jAudioTagger = "org" % "jaudiotagger" % "2.0.3"
}