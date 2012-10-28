name := "scala-redis-client"

organization := "com.top10"

scalaVersion := "2.9.1"

resolvers += "Local Maven Repository" at Path.userHome.asURL + "/.m2/repository"

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

releaseSettings

libraryDependencies ++= Seq(
  "org.scala-tools.time" %% "time" % "0.5",
  "redis.clients" % "jedis" % "2.0.0",
  "org.scalatest" %% "scalatest" % "1.6.1" % "test",
  "junit" % "junit" % "4.8.2" % "test",
  "org.mockito" % "mockito-core" % "1.8.5" % "test"
)

parallelExecution in Test := false

pomExtra := (
  <url>https://github.com/top10/scala-redis-client</url>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:top10/scala-redis-client.git</url>
    <connection>scm:git:git@github.com:top10/scala-redis-client.git</connection>
  </scm>
  <developers>
    <developer>
      <id>thesmith</id>
      <name>Ben Smith</name>
      <url>http://thesmith.co.uk</url>
    </developer>
  </developers>)