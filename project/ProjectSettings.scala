import sbt.Keys._
import sbt._
import sbtassembly.AssemblyKeys._

object ProjectSettings {

  val sparkVersion = "1.6.1"
  val hadoopVersion = "2.2.0"
  val electricVersion = "0.0.2"
  val currentVersion="0.0.1-SNAPSHOT"
  val organizationName="com.recipegrace"

  // sbt-assembly settings for building a fat jar
  lazy val sparkAssemblySettings = Seq(

    // Slightly cleaner jar name
    assemblyJarName in assembly := {
      name.value + "-" + version.value + ".jar"
    },

    // Drop these jars
    assemblyExcludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
      val excludes = Set(
        "jsp-api-2.1-6.1.14.jar",
        "jsp-2.1-6.1.14.jar",
        "jasper-compiler-5.5.12.jar",
        "commons-beanutils-core-1.8.0.jar",
        "commons-beanutils-1.7.0.jar",
        "servlet-api-2.5-20081211.jar",
        "servlet-api-2.5.jar",
        "scala-xml-2.11.0-M4.jar",
        "jsr311-api-1.1.1.jar"
      )
      cp filter { jar => excludes(jar.data.getName) }
    }

  )
  val coreSettings = Seq(
    version := currentVersion,
    scalaVersion := "2.10.6",
    organization := organizationName,
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.5.0",
      "com.recipegrace" %% "core" % electricVersion
    ),
 //   dependencyOverrides ++= Set(
 //     "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.4"
 //   ),
    resolvers ++= Seq("Recipegrace snapshots" at "http://recipegrace.com/nexus/content/repositories/snapshots/"),
    parallelExecution in Test := false
)

  val electricJobSettings = Seq(
    test in assembly := {},
    libraryDependencies ++= Seq(
      "org.apache.commons" % "commons-lang3" % "3.4",
      "com.recipegrace" %% "electric" % electricVersion,
      "org.apache.hadoop" % "hadoop-streaming" % hadoopVersion % "provided" ,
      "org.apache.spark" %% "spark-core" % sparkVersion % "provided"
    )

  )

  val wikispaSettings = Seq(
    test in assembly := {},
    libraryDependencies ++= Seq(
   "org.dbpedia.extraction" % "core" % "4.2-SNAPSHOT",
     "org.scalaj" %% "scalaj-http" % "1.1.5"
    )
  )
}
