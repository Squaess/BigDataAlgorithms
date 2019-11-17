ThisBuild / scalaVersion := "2.13.1"
ThisBuild / organization := "com.example"

lazy val wordCount =  (project in file("."))
    .settings(
        name := "WordCount"
    )
