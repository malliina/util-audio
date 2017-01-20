import sbt.Keys._
import sbt._

object BuildBuild {
  // "build.sbt" goes here
  lazy val settings = Seq(
    scalaVersion := "2.10.6",
    resolvers ++= Seq(
      "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
      "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/",
      ivyResolver("bintray-sbt-plugin-releases", "http://dl.bintray.com/content/sbt/sbt-plugin-releases"),
      ivyResolver("malliina bintray sbt", "https://dl.bintray.com/malliina/sbt-plugins/")
    ),
    scalacOptions ++= Seq("-unchecked", "-deprecation")
  ) ++ sbtPlugins

  def ivyResolver(name: String, urlStr: String) =
    Resolver.url(name, url(urlStr))(Resolver.ivyStylePatterns)

  def sbtPlugins = Seq(
    "com.malliina" % "sbt-utils" % "0.5.0",
    "com.eed3si9n" % "sbt-assembly" % "0.11.2"
  ) map addSbtPlugin
}
