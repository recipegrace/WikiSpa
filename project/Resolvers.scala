
import sbt._
object Resolvers {
  val mvnrepository = "MVN Repo" at "http://mvnrepository.com/artifact"
  val recipegrace = "Recipegrace repo" at "http://recipegrace.com/nexus/content/repositories/releases/"
  val recipegraceSnapshots = "Recipegrace snapshots" at "http://recipegrace.com/nexus/content/repositories/snapshots/"
  val cloudera = "Cloudera" at "https://repository.cloudera.com/artifactory/cloudera-repos"
  val allResolvers = Seq(mvnrepository,recipegrace, recipegraceSnapshots)

}
