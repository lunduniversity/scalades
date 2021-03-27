scalaVersion := "2.13.4"

// Set to false or remove if you want to show stubs as linking errors
nativeLinkStubs := true

enablePlugins(ScalaNativePlugin)

//// add this resolver to include other repos that are publishLocal 
// Global / resolvers += "Local Maven Repository" at "file://" + Path.userHome + "/.m2/repository"


//// Setting when GC.none https://github.com/scala-native/scala-native/pull/2205
//// PR not yet merged...
import scala.scalanative.build._
//ThisBuild / envVars := Map("GC_NONE_PREALLOC_SIZE" -> sys.env.getOrElse("GC_NONE_PREALLOC_SIZE", "4000M"))

nativeConfig ~= { 
  _.withLTO(LTO.thin)
    .withMode(Mode.releaseFast) // change to releaseFull for optimized binary
    .withGC(GC.commix) // change to GC.none to get dummy GC
}