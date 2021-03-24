scalaVersion := "3.0.0-RC1"

initialCommands in console := """
  import scalades.{*, given}
"""

scalacOptions ++= Seq(
  "-encoding", "utf8", 
  "-source", "future",  // remove this if you want to allow old Scala 2 syntax
  "-Xfatal-warnings",  
  "-deprecation",
  "-unchecked",
)

fork                := true
connectInput        := true
outputStrategy      := Some(StdoutOutput)
