scalaVersion := "2.12.4"
resolvers ++= Seq(
  // temporary hack
  // http://stackoverflow.com/a/42211230
  "JBoss" at "https://repository.jboss.org/",
  ivyRepo("bintray-sbt-plugin-releases",
    "http://dl.bintray.com/content/sbt/sbt-plugin-releases"),
  ivyRepo("malliina bintray sbt",
    "https://dl.bintray.com/malliina/sbt-plugins/"),
  Resolver.bintrayRepo("malliina", "maven")
)

def ivyRepo(name: String, urlString: String) =
  Resolver.url(name, url(urlString))(Resolver.ivyStylePatterns)

addSbtPlugin("com.malliina" %% "sbt-utils" % "0.8.0")
