import sbt.Keys._
import sbt._

/**
 *
 * @author mle
 */
object BuildBuild extends Build {
  // "build.sbt" goes here
  override lazy val settings = super.settings ++ Seq(
    scalaVersion := "2.10.4",
    resolvers ++= Seq("Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"),
    scalacOptions ++= Seq("-unchecked", "-deprecation")
  ) ++ sbtPlugins

  val mleGroup = "com.github.malliina"

  def sbtPlugins = Seq(
    mleGroup % "sbt-utils" % "0.0.5",
    mleGroup %% "ssh-client" % "0.0.4",
    "com.eed3si9n" % "sbt-assembly" % "0.11.2"
  ) map addSbtPlugin

  override lazy val projects = Seq(root)
  lazy val root = Project("plugins", file("."))
}