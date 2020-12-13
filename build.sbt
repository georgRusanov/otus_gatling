import Dependencies._

enablePlugins(GatlingPlugin)

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "tus.gatling",
      scalaVersion := "2.12.12",
      version := "0.1.0-SNAPSHOT"
    )),
    name := "tus",
    libraryDependencies ++= gatling
  )
