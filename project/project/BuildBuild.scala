import sbt.Keys._
import sbt._

/**
 *
 * @author mle
 */
object BuildBuild extends Build {
  // "build.sbt" goes here
  override lazy val settings = super.settings ++ Seq(
    scalaVersion := "2.10.6",
    resolvers ++= Seq(
      "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
      "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/",
      Resolver.url("bintray-sbt-plugin-releases",
        url("http://dl.bintray.com/content/sbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns),
      Resolver.url("malliina bintray sbt",
        url("https://dl.bintray.com/malliina/sbt-plugins/"))(Resolver.ivyStylePatterns)
    ),
    scalacOptions ++= Seq("-unchecked", "-deprecation")
  ) ++ sbtPlugins

  val mleGroup = "com.github.malliina"

  def sbtPlugins = Seq(
    mleGroup % "sbt-utils" % "0.2.1",
    "com.eed3si9n" % "sbt-assembly" % "0.11.2",
    "me.lessis" % "bintray-sbt" % "0.3.0"
  ) map addSbtPlugin

  override lazy val projects = Seq(root)
  lazy val root = Project("plugins", file("."))
}
