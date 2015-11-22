organization  := "com.example"

version       := "0.1"

scalaVersion  := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val sprayV = "1.3.3"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test",
    "com.thenewmotion.akka" %% "akka-rabbitmq" % "1.2.4",
    "org.elasticsearch" % "elasticsearch" % "0.20.5",
    "net.debasishg" %% "redisclient" % "3.0",
    "com.sksamuel.elastic4s" % "elastic4s-core_2.11" % "1.6.4",
    "com.sksamuel.elastic4s" % "elastic4s-jackson_2.11" % "1.6.4",
    "com.sksamuel.elastic4s" % "elastic4s-streams_2.11" % "1.6.4",
    "com.sksamuel.elastic4s" % "elastic4s-examples_2.11" % "1.6.4",
    "com.sksamuel.elastic4s" % "elastic4s-testkit_2.11" % "1.6.4",
    "org.scalaj" %% "scalaj-http" % "1.1.5",
    "org.json4s" %% "json4s-native" % "3.2.11",
    "com.roundeights" %% "hasher" % "1.2.0",
    "org.json4s" %% "json4s-jackson" % "3.2.10",
    "ch.qos.logback" % "logback-classic" % "1.0.13",
    "joda-time" % "joda-time" % "2.8.2",
    "mysql" % "mysql-connector-java" % "5.1.25"
  )
}

Revolver.settings
