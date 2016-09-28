import com.typesafe.sbt.SbtPgp.autoImportImpl._
import sbt.Keys._
import sbt._
import sbtassembly.AssemblyKeys._


object ProjectSettings {

  val sparkVersion = "2.0.0"
  val hadoopVersion = "2.2.0"
  val electricVersion = "0.0.5-SNAPSHOT"
  val organizationName="com.recipegrace"
  val username = System.getenv().get("SONATYPE_USERNAME")

  val password = System.getenv().get("SONATYPE_PASSWORD")

  val passphrase = System.getenv().get("PGP_PASSPHRASE") match {
      case x:String => x
      case null => ""
      }

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
    pgpPassphrase := Some( passphrase.toCharArray),
    pgpSecretRing := file("local.secring.gpg"),
    pgpPublicRing := file("local.pubring.gpg"),
    scalaVersion :=  "2.11.6",
    organization := organizationName,
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.5.0",
      "com.recipegrace" %% "core" % electricVersion
    ),
 //   dependencyOverrides ++= Set(
 //     "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.4"
 //   ),
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value) Some(Resolvers.ossSnapshots)
      else Some(Resolvers.ossStaging)
    },
    credentials += Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password),
    pomIncludeRepository := { _ => false },
    pomExtra := (
      <url>http://recipegrace.com/recipegrace</url>
        <licenses>
          <license>
            <name>BSD-style</name>
            <url>http://www.opensource.org/licenses/bsd-license.php</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:recipegrace/BigLibrary.git</url>
          <connection>scm:git:git@github.com:recipegrace/BigLibrary.git</connection>
        </scm>
        <developers>
          <developer>
            <id>feroshjacob</id>
            <name>Ferosh Jacob</name>
            <url>http://www.feroshjacob.com</url>
          </developer>
        </developers>),
    resolvers ++= Resolvers.allResolvers,
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
   "org.dbpedia.extraction" % "core" % "4.1",
     "org.scalaj" %% "scalaj-http" % "1.1.5"
    )
  )
}
