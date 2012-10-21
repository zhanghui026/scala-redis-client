name := "scala-redis-client"

organization := "com.top10"

version := "1.0"

scalaVersion := "2.9.1"

resolvers += "Local Maven Repository" at Path.userHome.asURL + "/.m2/repository"

libraryDependencies ++= Seq(
  "org.scala-tools.time" %% "time" % "0.5",
  "redis.clients" % "jedis" % "2.0.1-TOP10-0.2",
  "org.scalatest" %% "scalatest" % "1.6.1" % "test",
  "junit" % "junit" % "4.8.2" % "test",
  "org.mockito" % "mockito-core" % "1.8.5" % "test"
)

parallelExecution in Test := false
