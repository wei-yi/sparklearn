import sbt.Keys._

lazy val commonSetting = Seq(
  version := "1.0",
  name := "sparklearn",
  scalaVersion := "2.10.4",
  resolvers ++= Seq(
    "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    "Sonatype Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/",
    "Spring Plugin Release" at "http://repo.springsource.org/plugins-release/",
    "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository",
    "Maven Repository" at "http://repo1.maven.org/maven2",
    "Akka Repository" at "http://repo.akka.io/releases/",
    "Spray Repository" at "http://repo.spray.cc/"
  )
)

commonSetting

libraryDependencies += "com.github.seratch" %% "awscala" % "0.5.+"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "1.5.1"

libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.5.1"

libraryDependencies += "org.apache.kafka" % "kafka-clients" % "0.8.2.2"

libraryDependencies += "org.apache.spark" % "spark-streaming-flume-assembly_2.10" % "1.5.1"

libraryDependencies += "org.apache.spark" % "spark-sql_2.10" % "1.5.1"

libraryDependencies += "log4j" % "log4j" % "1.2.17"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.12"

libraryDependencies += "org.yaml" % "snakeyaml" % "1.16"

libraryDependencies += "com.typesafe" % "scalalogging-log4j_2.10" % "1.1.0"



//val sparkStreaming = "org.apache.spark" % "spark-streaming_2.10" % "1.4.0"
//val sparkStreaming_kafka = "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.4.0"
//val kafkaClients = "org.apache.kafka" % "kafka-clients" % "0.8.2.1"
//val ssf = "org.apache.spark" % "spark-streaming-flume-assembly_2.10" % "1.5.0"
//val ssql = "org.apache.spark" % "spark-sql_2.10" % "1.5.0"
//val log4j = "log4j" % "log4j" % "1.2.17"
//val sl4j = "org.slf4j" % "slf4j-api" % "1.7.12"
//val sankeyaml = "org.yaml" % "snakeyaml" % "1.16"

//val lib = Seq(libraryDependencies ++= Seq(sparkStreaming,sparkStreaming_kafka,kafkaClients,ssf,ssql,sl4j,sankeyaml))
//lazy val sparkSetting = commonSetting ++ lib
//   //.dependsOn("otherProject")
 //lazy val sparkLearn = project.in(file("learn")).setting(commonSetting)