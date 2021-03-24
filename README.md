# scalades
`scalades` is a library for empirical investigation of real-world systems using stochastic [Descrete-Event Simulation (DES)](https://en.wikipedia.org/wiki/Discrete-event_simulation) models in the [Scala](https://www.scala-lang.org/) programming language. 

This library is work in progress in its early stages, so the api is likely to change. Contributions are welcome! Contact bjorn.regnell@cs.lth.se 

`scalades` follow the terminology and principles of on Stochastic Discrete Event Simulation taught in [this course on Simulation at Lund University](https://www.eit.lth.se/index.php?ciuid=1298&coursepage=9535).  

* Documentation: https://fileadmin.cs.lth.se/scalades/docs/api/
* Source code: https://github.com/lunduniversity/scalades/tree/main/src/main/scala/scalades
  
## Design approach

The main goal of `scalades` is to provide an easy-to-use library that supports the modelling of systems in the paradigm of stochastic Discrete Event Simulation. 

The world is modelled using events that are happening when processes send signals among each other. Each event has a decimal timestamp ordered in an event queue, with the event with the soonest time stamp first. The simulation time can directly jump to the time of the occurring next event, allowing efficient simulation of real-world systems in pseudo-time instead of real time.

Although process can model behaviour that happens concurrently in the real world, the simulation model itself is executed in one single thread with pseudo-concurrent precess scheduled based on sequential event dispatching. 

Multiple simulations can be executed independently of each other in different threads to allow efficient exploration of different simulation parameters in parallell using many CPU threads.   

We have conducted a series of [*benchmarks*](https://github.com/lunduniversity/scalades/tree/main/benchmark), indicating that [Scala Native]() is a very promising runtime for `scalades` that may be even faster than the JVM. When Scala Native is available for Scala 3 our intention is to publish this library also for bare metal execution.  

## Questions

* Should time be a Double or BigDecimal? Floating-point numbers are strange when comparing with >= etc so precision might be an issue especially in long simulations where delta time is smaller than total simulation time. But BigDecimal is significantly slower than Double. This is a trade-off between precision and performance that is interesting to explore in benchmarks.
  
* What are the most important use cases left to implement for the Simulation course?

## TODO

* add help for contributors

* add more examples

* do more benchmarks for performance evaluation

* try out using Scala's Future to run several simulations in parallell on different CPU-threads

  
