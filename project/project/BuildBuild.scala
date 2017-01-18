import sbt.Keys._
import sbt._

object BuildBuild {
  // "build.sbt" goes here
  lazy val settings = Seq(
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

  val mleGroup = "com.malliina"

  def sbtPlugins = Seq(
    mleGroup % "sbt-utils" % "0.5.0",
    "com.eed3si9n" % "sbt-assembly" % "0.11.2"
  ) map addSbtPlugin
}
