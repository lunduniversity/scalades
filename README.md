# scalades
A library for Descrete Event Simulation written in Scala 3.

This is work in progress in its early stages - contributions are welcome; contact bjorn.regnell@cs.lth.se 

This library follows the terminology and principles of on Stochastic Discrete Event Simulation taught in [**this Lund University course on Simulation**](https://www.eit.lth.se/index.php?ciuid=1298&coursepage=9535)  

## Design exploration

The basic design principles are currently under exploration and there are currently three alternative design proposals for discussion:

* Design 1: the bare minimum OO design; many things left for 
* Design 2: processes now has both a receive method and a send method and the event handling in the simulation framework is less exposed
* Design 3: processes now has some FP-stuff; there is a signal type parameter and an abstract type State to model state transitions


## TODO + questions

* add Logger trait with plugins for screen and file logging
* add Sampler in MM1 example to measure e.g. queue length
* add more examples

* should time be a Double or BigDecimal? Floating-point numbers are strange when comparing with >= etc... But BigDecimal is perhaps too slow
* is the optimization of using opaque type Time instead of a simple case class worth it?
* is a central que the right thing or should each process have its own queue? But then we need to find the next event by comparing all heads of ques which may take some time...  What's best from a modelling perspective
* Perhaps a more FP-oriented design where thunks are stored in a data structure and executed at the end of the world? Would this be good for modelling?
* is the LazyList-thing in Simulation really needed? (intended for traversing a copied sequence of all events)
* is the onNextEventCalllback useful/needed?

