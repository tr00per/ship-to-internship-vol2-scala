scalaVersion := "2.13.6"

assembly / assemblyJarName := "idemiomat.jar"
assembly / mainClass := Some("com.idemia.idemiomat.MainWeb")

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.10"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test"

// Web
val akkaHttpVersion = "10.2.6"
val akkaVersion    = "2.6.17"

libraryDependencies += "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream"              % akkaVersion
libraryDependencies += "ch.qos.logback"    % "logback-classic"           % "1.2.3"
