
import sbt._

object Resolvers {
  val ossReleases =  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases/"
  val ossCentral =  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/central/"
  val ossSnapshots =  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  val ossStaging =  "Sonatype OSS Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"

  val allResolvers = Seq(ossSnapshots,ossStaging,ossReleases, ossCentral)

}
