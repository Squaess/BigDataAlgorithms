ThisBuild / scalaVersion := "2.12.8"
ThisBuild / organization := "com.example"

lazy val wordCount =  (project in file("."))
    .settings(
        name := "WordCount",
        // libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.4",
        libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.4",
        libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.4.0"
    )
