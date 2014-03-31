import sbt._
import sbt.Keys._

/**
 *
 * @author mle
 */
object BuildBuild extends Build {
  // "build.sbt" goes here
  override lazy val settings = super.settings ++ Seq(
    scalaVersion := "2.10.3",
    resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
    scalacOptions ++= Seq("-unchecked", "-deprecation")
  ) ++ sbtPlugins

  def sbtPlugins = Seq(
    "com.github.malliina" % "sbt-utils" % "0.0.2"
  ) map addSbtPlugin

  override lazy val projects = Seq(root)
  lazy val root = Project("plugins", file("."))
}


