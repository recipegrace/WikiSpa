 name := "WikiSpa"
 
 version := "0.0.1"
 
 organization := "com.recipegrace"


scalaVersion := "2.11.5"
 


libraryDependencies ++= Seq(
   "org.dbpedia.extraction" % "core" % "4.0",
     "org.scalaj" %% "scalaj-http" % "1.1.5",
    "org.scalatest" %% "scalatest" % "2.2.5" % "test")


resolvers ++= Seq("sonatype releases" at "http://oss.sonatype.org/content/repositories/releases",
         "AKSW Maven2 Repository" at "http://maven.aksw.org/repository/internal"
)


resourceDirectory in Compile := baseDirectory.value / "files"

publishMavenStyle := true


