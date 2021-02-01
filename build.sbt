name := "kinesis-consumer"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-kinesis" % "2.0.2",
  "org.mongodb.scala" %% "mongo-scala-driver" % "4.1.1",
  "com.typesafe" % "config" % "1.4.1",
  "com.typesafe.play" %% "play-json" % "2.9.2"
)

scalacOptions := Seq(
  "-deprecation",
  "-feature",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-Wdead-code",
  "-Wunused:imports",
  "-Ymacro-annotations"
)
