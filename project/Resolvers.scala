
import sbt._
object Resolvers {
  val mvnrepository = "MVN Repo" at "http://mvnrepository.com/artifact"
  val recipegrace = "Recipegrace repo" at "http://recipegrace.com:8080/nexus/content/repositories/releases/"
  val recipegraceSnapshots = "Recipegrace snapshots" at "http://recipegrace.com:8080/nexus/content/repositories/snapshots/"
  val aksw="AKSW Maven2 Repository" at "http://maven.aksw.org/repository/internal"
  val cloudera = "Cloudera" at "https://repository.cloudera.com/artifactory/cloudera-repos"
  val allResolvers = Seq(Resolver.mavenLocal,mvnrepository,aksw,recipegrace)

}
