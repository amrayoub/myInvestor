resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
resolvers += Resolver.sonatypeRepo("releases")

addSbtPlugin("org.typelevel" % "sbt-typelevel" % "0.3.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")

addSbtPlugin("com.scalapenos" % "sbt-prompt" % "1.0.0")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.7.0-SNAPSHOT")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")

