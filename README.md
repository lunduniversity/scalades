# scalades
`scalades` is a library for empirical investigation of real-world systems using stochastic [Descrete-Event Simulation (DES)](https://en.wikipedia.org/wiki/Discrete-event_simulation) models in the [Scala](https://www.scala-lang.org/) programming language. 

This library is work in progress in its early stages, so the [api](https://fileadmin.cs.lth.se/scalades/docs/api/) is likely to change. Contributions are welcome! Contact bjorn.regnell@cs.lth.se 

`scalades` follow the terminology and principles of on Stochastic Discrete Event Simulation taught in [this course on Simulation at Lund University](https://www.eit.lth.se/index.php?ciuid=1298&coursepage=9535).  

* Documentation: https://fileadmin.cs.lth.se/scalades/docs/api/
* Source code: https://github.com/lunduniversity/scalades/tree/main/src/main/scala/scalades
  
## How to use `scalades`

* Pre-requisites: install [sbt](https://www.scala-sbt.org/download.html) (requires [JDK](https://adoptopenjdk.net/))

* Clone this repo *or* download and extract [this zip](https://github.com/lunduniversity/scalades/archive/refs/heads/main.zip) 

* Navigate to the scalades folder and do this in terminal:

```
$ sbt
sbt:scalades> runMain scalades.example.runMM1 9 10 10000 
```
You should get an output similar to:
```
[info] running scalades.example.runMM1 9 10 10000
---- SIMULATION START -----
Number of samples: 9828
Average queue length: 10.33536833536831
Execution time: 0.543 seconds
Last event at time stamp: 9999.9100217340560922411402 time units
SIMULATION END AFTER 10000.0 time units
[success] Total time: 1 s, completed Mar 24, 2021, 11:30:21 PM
```

## Design approach

The main goal of `scalades` is to provide an easy-to-use library that supports the modelling of systems in the paradigm of stochastic Discrete Event Simulation. 

### Message passing

The world is modelled using events that are happening when processes send signals among each other. Each event has a decimal timestamp ordered in an event queue, with the event with the soonest time stamp first. The simulation time can directly jump to the time of the occurring next event, allowing efficient simulation of real-world systems in pseudo-time instead of real time.

### Non-parallell, co-operative concurrency
Although a process can model behaviour that happens concurrently in the real world, the simulation model itself is executed in *one single thread* with co-operative, non-parallell [concurrency](https://en.wikipedia.org/wiki/Concurrency_(computer_science)) through scheduling of process execution based on synchronous, sequential event dispatching. The [Simulation class](https://github.com/lunduniversity/scalades/blob/main/src/main/scala/scalades/Simulation.scala) is responsible for the event dispatching.

### Each simulation in a single thread executed in parallell
Multiple simulations can be executed independently of each other in different threads to allow efficient exploration of different simulation parameters in parallell using many CPU threads.   

### Utilizing the modelling power and execution efficiency of Scala 3
The `scalades` library is implemented in [Scala 3](https://docs.scala-lang.org/scala3/new-in-scala3.html), utilizing new language constructs such as [*enums*](https://docs.scala-lang.org/scala3/book/types-adts-gadts.html) for [enumerating signals and states](https://github.com/lunduniversity/scalades/blob/main/src/main/scala/scalades/example/MM1.scala#L42), [*opaque type aliases*](https://docs.scala-lang.org/scala3/book/types-opaque-types.html) for unboxed [Time](https://github.com/lunduniversity/scalades/blob/main/src/main/scala/scalades/Time.scala) values, [*extension methods*](https://dotty.epfl.ch/docs/reference/contextual/extension-methods.html) and [*export clauses*](https://dotty.epfl.ch/docs/reference/other-new-features/export.html) to shape the api, and [contextual abstraction](https://docs.scala-lang.org/scala3/book/ca-given-using-clauses.html) to provide a `given` simulation context to the implicit `using` by its processes.

We have conducted a series of [*benchmarks*](https://github.com/lunduniversity/scalades/tree/main/benchmark), indicating that [Scala Native]() is a very promising runtime for `scalades` that may be even faster than the JVM. When Scala Native is available for Scala 3 our intention is to publish this library also for bare metal execution.  

## TODO

* Should [Time](https://github.com/lunduniversity/scalades/blob/main/src/main/scala/scalades/Time.scala) be a Double or BigDecimal? Floating-point numbers are strange when comparing with >= etc so precision might be an issue especially in long simulations where durations are much smaller than the simulation time stamps. But BigDecimal is significantly slower than Double. This is a trade-off between precision and performance that is interesting to explore in benchmarks.
  
* What are the most important use cases left to implement for the [Simulation course at Lund university](https://www.eit.lth.se/index.php?ciuid=1298&coursepage=9535)?

* add help for contributors

* add more examples

* do more benchmarks for performance evaluation

* try out using Scala's Future to run several simulations in parallell on different CPU-threads

  
