name := """http-retarder"""

organization := "emicareofcell44"

version := "0.1.0"

version in ThisBuild := "0.1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-http-core" % "2.4.4",
			    "com.typesafe.akka" %% "akka-http-experimental" % "2.4.4",				  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.4")

enablePlugins(DockerPlugin)

dockerfile in docker := {
  // The assembly task generates a fat JAR file
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("java")
    add(artifact, artifactTargetPath)
    expose(9091)
    entryPoint("java", "-jar", artifactTargetPath)
  }
}


