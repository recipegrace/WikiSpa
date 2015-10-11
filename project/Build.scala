import sbt._

object Build extends Build {

  import ProjectSettings._

  lazy val core = (project in file("core")).
    settings(coreSettings ++ wikispaSettings: _*)

  lazy val spark = (project in file("spark") dependsOn(core))
    .settings(coreSettings ++ electricJobSettings ++ sparkAssemblySettings: _*)

}



