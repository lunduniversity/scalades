# Simulation Benchmarks 

Here you can find benchmarks of stochastic discrete event simulation using different runtime environments, currently including Java, Scala 2, and Scala Native. Scala Native is significantly faster than Java and Scala 2.

## How to run 
Navigate to the sub-folders of the different benchmarks (java, scala2, scala-native) and then fire up `sbt` and type `run`.

NOTE: [Scala native](http://www.scala-native.org) does not, at the time of writing, support Windows without WSL. You need Linux (e.g. [Ubuntu](https://ubuntu.com/download/desktop)), or macos, or you can use [WSL2](https://docs.microsoft.com/en-us/windows/wsl/) to run Scala Native in Windows. 

### Java:
```
sbt:java> run
[info] running (fork) MainSimulation 
Execution time: 1.042 seconds
Mean number of customers in queuing system: 4.040177420897283
```
### Scala 2:
```
sbt:scala2> run
[info] running (fork) MainSimulation 
Execution time: 1.365 seconds
Mean number of customers in queuing system: 4.000130556983663
```
### Scala Native 
```
sbt:scala-native> run
[info] Linking (523 ms)
[info] Discovered 678 classes and 4741 methods
[info] Optimizing (release-fast mode) (1379 ms)
[info] Generating intermediate code (677 ms)
[info] Produced 8 files
[info] Compiling to native code (835 ms)
[info] Linking native code (commix gc, thin lto) (3282 ms)
[info] Total (6730 ms)
Execution time: 0.905 seconds
Mean number of customers in queuing system: 3.9754686235154075
```

You can see a flame graph below of the above execution using Scala Native's super-fast garbage collector "commix".
To enable interactive zooming: download [this svg](https://github.com/lunduniversity/scalades/blob/main/benchmark/scala-native/kernel-GC-commix.svg) to your local machine by clicking on the *Raw* button, saving it, and then open the file using a web browser such as Firefox, and now you can click on boxes to zoom in on that stack allocation chain. 
<img src="./scala-native/kernel-GC-commix.svg">

## Impact of garbage collection

The garbage collection with `GC.commix` that is used by Scala Native is very fast. 

In order to investigate the impact of garbage collection, scala-native can be set to use a dommy garbage collector by using `GC.none` (instead of `GC.commix`) in [`build.sbt`](https://github.com/lunduniversity/scalades/blob/main/benchmark/scala-native/build.sbt)

When investigating this we found this issue https://github.com/scala-native/scala-native/issues/2202, resulting in this fix https://github.com/scala-native/scala-native/pull/2205 by @teodimoff.

[Here is a flamegraph with `GC.none`](https://github.com/lunduniversity/scalades/blob/main/benchmark/scala-native/kernel-GC-none.svg) (`GC.none` in [`build.sbt`](./scala-native/build.sbt) before the fix. The reason that `GC.commix` is significantly faster than `GC.none` is that memory before the fix was allocated in small chunks that resulted in page fault calls to the OS taking significant time. After #2205 the memory pre allocation can be set by an environment variable `export GC_NONE_PREALLOC_SIZE=4000M` and the execution time of `GC.none` is comparable to, or slightly faster tha `GC.commix`. 

If you want to set this environment variable in your `build.sbt` use:
```
ThisBuild / envVars := Map(  
  "GC_NONE_PREALLOC_SIZE" -> sys.env.getOrElse("GC_NONE_PREALLOC_SIZE", "4000M")
)
```

## Credit

The Java code was originally developed by Christian Nyberg. The Java version was ported to Scala 2 by Björn Regnell, keeping as close to the Java variant as possible enabling "fair" performance comparison. The Scala 2 is thus not idiomatic Scala, but just a direct translation of "java in scala".
