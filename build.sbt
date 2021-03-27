ThisBuild / scalaVersion := "3.0.0-RC1"
ThisBuild / organization := "se.lth.cs"

ThisBuild / initialCommands := "import scalades.*"

ThisBuild / version      := "0.1.1"

ThisBuild / scalacOptions ++= Seq(
  "-encoding", "utf8", 
  "-source", "future",
  "-Xfatal-warnings",  
  "-deprecation",
  "-unchecked",
)

lazy val scalades = (project in file("."))
  .settings(
    name := "scalades"
  )