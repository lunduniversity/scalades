# Simulation Benchmarks 

Here you can find benchmarks of stochastic discrete event simulation using different runtime environments, currently including Java, Scala 2, and Scala Native.

Summary of results:
* The Java and Scala on JVM are performing equal. This is not surprising as the underlying runtime in both cases is the same JVM and the byte code generated is similar as the Scala code is a direct translation from Java.
* Scala Native is around 70% faster (sic!) in this use case. This is a significant speed-up and preliminary investigations gives a hint that the new commix garbage collector is part of the explanation of this extraordinary speedup.
* Disclaimer: The benchmark is still "naive" as it includes one one single data-point. Further simulations are needed to investigate the impact of JVM warmup including the run-time optimizations that JIT provides. Each run show a statistical variation that needs to be characterized using averaging over multiple runs. Etc Etc.  

## How to run 
Navigate to the sub-folders of the different benchmarks (java, scala2, scala-native) and then fire up `sbt` and type `run`.

Scala Native is more than 70% faster!  (0.7/0.4 = 1.749)

NOTE: [Scala native](http://www.scala-native.org) does not, at the time of writing, support Windows without WSL. You need Linux (e.g. [Ubuntu](https://ubuntu.com/download/desktop)), or macos, or you can use [WSL2](https://docs.microsoft.com/en-us/windows/wsl/) to run Scala Native in Windows. 

### Java:
```
sbt:java> run
[info] running (fork) MainSimulation 
Execution time: 0.693 seconds
Mean number of customers in queuing system: 3.5019126434482586
```
### Scala 2:
```
sbt:scala2> run
[info] running (fork) MainSimulation 
Execution time: 0.695 seconds
Mean number of customers in queuing system: 3.4850718010252866
```
### Scala Native 
```
sbt:scala-native> run
[info] Linking (662 ms)
[info] Discovered 969 classes and 7882 methods
[info] Optimizing (release-fast mode) (2947 ms)
[info] Generating intermediate code (809 ms)
[info] Produced 8 files
[info] Compiling to native code (1276 ms)
[info] Linking native code (commix gc, thin lto) (6400 ms)
[info] Total (12137 ms)
Execution time: 0.397 seconds
Mean number of customers in queuing system: 3.5100252092689312
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
