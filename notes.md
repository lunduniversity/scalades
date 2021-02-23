# Notes 

## Design questions

* use receive instead of sim.add in generator of MM1 etc
* add Logger 
* let Simulation extend Process and handleNextEvent be handle??? and let an event to the simulation render a logging
* should an event have a callback, default empty so that you can tell what to do when it is received? 
* should we model State, e.g. as an abstract type in Process?
* should time be a Double or a Long or BigDecimal? Floating-point numbers are strange when comparing with >= etc... But time and probability distributions are continuous and not discrete in the real world