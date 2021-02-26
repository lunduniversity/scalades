# scalades
A library for Descrete Event Simulation written in Scala 3.

This is work in progress in its early stages - contributions are welcome; contact bjorn.regnell@cs.lth.se 

This library follows the terminology and principles of on Stochastic Discrete Event Simulation taught in [**this Lund University course on Simulation**](https://www.eit.lth.se/index.php?ciuid=1298&coursepage=9535)  

## Design exploration

The basic design principles are currently under exploration and there are currently three alternative design proposals for discussion:

* [*Design 1*](https://github.com/lunduniversity/scalades/tree/main/design1/src/main/scala/scalades): the bare minimum OO design; many things left for the user
* [*Design 2*](https://github.com/lunduniversity/scalades/tree/main/design2/src/main/scala/scalades): processes now has both a receive method and a send method and the event handling in the simulation framework is less exposed
* [*Design 3*](https://github.com/lunduniversity/scalades/tree/main/design3/src/main/scala/scalades): processes now has some FP-stuff; there is a signal type parameter and an abstract type State to model state transitions


## Questions

* what are the most important use cases left to implement for the Simulation course?

* should time be a Double or BigDecimal? Floating-point numbers are strange when comparing with >= etc... But BigDecimal is perhaps too slow. Well in long simulations time deltas could be too small for the precision. Interesting to test performance and compare.
  
* is the optimization of using opaque type Time instead of a simple case class worth it? needs performance test testing

* is a central queue the right thing or should each process have its own queue? But then we need to find the next event by comparing all heads of ques which may take some time...  What's best from a modelling perspective: BEST WITH CENTRAL QUEUE

* Perhaps a more FP-oriented design where thunks are stored in a data structure and executed at the end of the world? Would this be good for modelling? Perhaps intersection types can be used to make an Await[Sig1|Sig2]((sig) => body) description more type safe and nicer for modelling? Would Simula-like Suspend/Resume be possible in a description/interpreter-FP-style?

* is the LazyList-thing in Simulation really needed? (intended for traversing a copied sequence of all events) Yes good e.g. debuggen 

* is the onNextEventCalllback useful/needed? yes good for debugging and measurement

## Tentative TODO/Brainstorming

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