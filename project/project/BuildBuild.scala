import sbt._
import sbt.Keys._

/**
 *
 * @author mle
 */
object BuildBuild extends Build {
  // "build.sbt" goes here
  override lazy val settings = super.settings ++ Seq(
    scalaVersion := "2.10.2",
    resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
    scalacOptions ++= Seq("-unchecked", "-deprecation"),
    addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.1")
  )

  override lazy val projects = Seq(root)
  lazy val root = Project("plugins", file("."))
}


