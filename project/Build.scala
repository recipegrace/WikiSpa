import sbt._

object Build extends Build {

  import ProjectSettings._

  
  lazy val wikispaCore = (project in file("wikispa-core"))
    .settings(coreSettings ++ wikispaSettings: _*)

  lazy val wikispaSpark = (project in file("wikispa-spark") dependsOn(wikispaCore))
    .settings(coreSettings ++ electricJobSettings ++ sparkAssemblySettings: _*)

  lazy val wikispa = (project in file("."))
    .settings(coreSettings)  
    .aggregate(wikispaCore,wikispaSpark)
}



