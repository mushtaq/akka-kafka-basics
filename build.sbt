name := "akka-kafka-basics"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.5.8"
libraryDependencies += "org.scala-lang.modules" %% "scala-async" % "0.9.6-RC5"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.8"
libraryDependencies += "com.typesafe.akka" %% "akka-persistence" % "2.4.8"
libraryDependencies += "org.iq80.leveldb"            % "leveldb"          % "0.7"
libraryDependencies += "org.fusesource.leveldbjni"   % "leveldbjni-all"   % "1.8"
libraryDependencies += "com.typesafe.akka" %% "akka-persistence-query-experimental" % "2.4.8"

transitiveClassifiers in Global := Seq(Artifact.SourceClassifier)

updateOptions := updateOptions.value.withCachedResolution(true)

resolvers ++= Seq(
  Resolver.bintrayRepo("stanch", "maven"),
  Resolver.bintrayRepo("drdozer", "maven")
)

libraryDependencies += "org.stanch" %% "reftree" % "0.5.0"
