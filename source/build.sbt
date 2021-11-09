scalaVersion := "2.13.6"

assembly / assemblyJarName := "idemiomat.jar"
assembly / mainClass := Some("com.idemia.idemiomat.MainCli")

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.10"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test"
