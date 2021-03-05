ThisBuild / scalaVersion := "2.13.5"

scalacOptions ++= Seq(
  "-optimize",
  //"-opt:l:inline","-opt-inline-from:**",
  "-encoding", "utf8",
  "-J-Xmx1G", 
  //"-Xfatal-warnings",  
  "-deprecation",
  "-unchecked",
)

fork                := true
connectInput        := true
outputStrategy      := Some(StdoutOutput)
