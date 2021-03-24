//requires `sbt.version=1.5.0-RC2` or later in `project/build.properties`

scalaVersion := "3.0.0-RC1"

initialCommands := "import scalades.*"

Global / onChangedBuildSource := ReloadOnSourceChanges

scalacOptions ++= Seq(
  "-encoding", "utf8", 
  "-source", "future",  // remove if you want to allow old Scala 2 syntax
  "-Xfatal-warnings",  
  "-deprecation",
  "-unchecked",
)

fork                := true
connectInput        := true
outputStrategy      := Some(StdoutOutput)
