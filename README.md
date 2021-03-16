# scalades
`scalades` is a Scala library for modelling and empirical investigation of real-world systems using stochastic  [Descrete-Event Simulation (DES)](https://en.wikipedia.org/wiki/Discrete-event_simulation). 

This library is work in progress in its early stages - contributions are welcome; contact bjorn.regnell@cs.lth.se 

`scalades` follow the terminology and principles of on Stochastic Discrete Event Simulation taught in [this course on Simulation at Lund University](https://www.eit.lth.se/index.php?ciuid=1298&coursepage=9535).  

## Design exploration

The main goal of `scalades` is to provide an easy-to-use DSL that supports the modelling of systems in the paradigm of stochastic Discrete Event Simulation. The world is modelled using discrete events that each has a decimal timestamp ordered in an event queue. The simulation time can directly jump to the time of the occurring next event, allowing efficient simulation of "real" time.

In `scalades` systems are modelled in terms of **processes** and **signals**, and the events model that a signal is sent from one process to another at a specific point in time. Each process defines the actions that are taken when it receives a signal and which signals it sends after processing its incoming signals. Processes can have states and the processing of incoming signals can change the state of a process.

Although process can model behaviour that happens concurrently in the real world, the simulation model itself is executed in one single thread together with the event dispatching mechanism. Multiple simulation runs can be executed independently of each other in different threads to allow efficient exploration of different simulation parameters.   

The basic design principles are currently under exploration using some of the [new features of Scala 3](https://docs.scala-lang.org/scala3/new-in-scala3.html) and there are currently three alternative design proposals for discussion:

* [*Design 1*](https://github.com/lunduniversity/scalades/tree/main/design1/src/main/scala/scalades): the bare minimum OO design; many things left for the user
* [*Design 2*](https://github.com/lunduniversity/scalades/tree/main/design2/src/main/scala/scalades): processes now has both a receive method and a send method and the event handling in the simulation framework is less exposed
* [*Design 3*](https://github.com/lunduniversity/scalades/tree/main/design3/src/main/scala/scalades): processes now has some FP-stuff; there is a signal type parameter and an abstract type State to model state transitions

As a basis for design exploration we have conducted a series of [**benchmarks**](https://github.com/lunduniversity/scalades/tree/main/benchmark), indicating that [Scala Native]() is a very promising runtime for `scalades` if short simulation execution time is a priority.   



## Questions and discussion items

* should time be a Double or BigDecimal? Floating-point numbers are strange when comparing with >= etc... But BigDecimal is perhaps too slow. Well in long simulations time deltas could be too small for the precision. Interesting to test performance and compare.
  
* is the optimization of using opaque type Time instead of a simple case class worth it? needs performance test testing

* is a central queue the right thing or should each process have its own queue? But then we need to find the next event by comparing all heads of ques which may take some time...  What's best from a modelling perspective: BEST WITH CENTRAL QUEUE

* Perhaps a more FP-oriented design where thunks are stored in a data structure and executed at the end of the world? Would this be good for modelling? Perhaps intersection types can be used to make an Await[Sig1|Sig2]((sig) => body) description more type safe and nicer for modelling? Would Simula-like Suspend/Resume be possible in a description/interpreter-FP-style?

* is the LazyList-thing in Simulation really needed? (intended for traversing a copied sequence of all events) Yes good e.g. debuggen 

* is the onNextEventCalllback useful/needed? yes good for debugging and measurement

* what are the most important use cases left to implement for the Simulation course?

## Tentative TODO/Brainstorming

Some of these points will eventually become tickets/issues:

* add Logger trait with plugins for screen and file logging
*   log(s"tiden är $now") between name not to confuse with logarithm; out?

* add Sampler in MM1 example to measure e.g. queue length
  * make the distribution between measurement optional
  * measure at specific events

* add more examples

* do some bencharks for performance evaluation

* try out using Scala's Future to run several simulations in parallell on different CPU-threads

* good if you statically can limit the set of signals that a process can receive
  
* good with debugging features, e.g. print the event queue  (LazyList)