# scalades
A framework for Descrete Event Simulation written in Scala 3.

This library is work in progress in its early stages - contributions are welcome; contact bjorn.regnell@cs.lth.se 

This library follow the terminology and principles of on Stochastic Discrete Event Simulation taught in [this course](https://www.eit.lth.se/index.php?ciuid=1298&coursepage=9535)  

## Design exploration

The basic principles of this framework is currently under exploration and there are currently three alternative design proposals for discussion:

* Design 1: the bare minimal; many things left for 
* Design 2: processes now has both a receive method and a send method and the event handling in the simulation framework is less exposed
* Design 3: processes now has a signal type parameter and an abstract type State to model state transitions


## TODO + questions

* add Logger 
* should time be a Double or BigDecimal? Floating-point numbers are strange when comparing with >= etc... But BigDecimal is perhaps too slow
* is the optimization of using opaque type Time instead of a simple case class worth it?
* is a central que the right thing or should each process have its own queue? But then we need to find the next event by comparing all heads of ques which may take some time...  What's best from a modelling perspective
* Perhaps a more FP-oriented design where thunks are stored in a data structure and executed at the end of the world? Would this be good for modelling?
* is the LazyList-thing in Simulation really needed? (intended for traversing a copied sequence of all events)
* is the onNextEventCalllback useful/needed?

