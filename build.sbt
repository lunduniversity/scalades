scalaVersion := "3.0.0-RC1"

initialCommands := "import scalades.*"

scalacOptions ++= Seq(
  "-encoding", "utf8", 
  "-source", "future",
  "-Xfatal-warnings",  
  "-deprecation",
  "-unchecked",
)
