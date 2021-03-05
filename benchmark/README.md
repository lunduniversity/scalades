# Simulation Benchmark Java, Scala 2, Scala Native

## How to run 
Navigate to the sub-folders of the different benchmarks (java, scala2, scala-native) and then fire up `sbt` and type `run`.

As you can see Scala Native is rocking the boat, being more than 70% faster:

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