organization := "IBM"

name := "ESSparkSY"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.5"

//libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.0" % "provided"
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.6.0" % "provided",
  "org.apache.spark" % "spark-streaming_2.10" % "1.6.0" % "provided",	
  "org.elasticsearch" % "elasticsearch-hadoop" % "2.3.0" % "provided",
  "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.6.0" 
)


resolvers ++= Seq(
	"clojars" at "https://clojars.org/repo"
//	"conjars" at "http://conjars.org/repo"
)


mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
    case m if m.startsWith("META-INF") => MergeStrategy.discard
    case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
    case PathList("org", "apache", xs @ _*) => MergeStrategy.first
    case PathList("org", "jboss", xs @ _*) => MergeStrategy.first
    case PathList("org", "apache","hive", xs @ _*) => MergeStrategy.first
    case "about.html"  => MergeStrategy.rename
    case "reference.conf" => MergeStrategy.concat
    case _ => MergeStrategy.first
  }
}
