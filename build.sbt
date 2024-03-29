import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "rovers",
    libraryDependencies ++= Seq(cats, catsEffect, decline, attoCore, scalaTest % Test, scalaCheck % Test),
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDS")
  )

