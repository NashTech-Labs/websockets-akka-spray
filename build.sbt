organization  := "com.knoldus"

version       := "0.1"

scalaVersion  := "2.11.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Spray repository" at "http://repo.spray.io/"
)

libraryDependencies ++= {
  val akkaV = "2.3.6"
  val sprayV = "1.3.1"
  Seq(
//  "org.java-websocket"  %   "Java-WebSocket"  % "1.3.1",
    "io.spray"            %%  "spray-json"      % sprayV,
    "io.spray"            %%   "spray-can"      % sprayV,
    "io.spray"            %%   "spray-routing"  % sprayV,
    "com.typesafe.akka"   %%  "akka-actor"      % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"    % akkaV   % "test",
    "io.spray"            %%   "spray-testkit"  % sprayV  % "test",
    "org.scalatest"       %%  "scalatest"       % "2.2.1" % "test",
    "junit"               %   "junit"           % "4.11"  % "test",
    "org.specs2"          %%  "specs2"          % "2.4.9" % "test",
    "ch.qos.logback"      %   "logback-classic" % "1.1.2"
  )
}

seq(Revolver.settings: _*)
