// This class defines a signal. What can be seen here is a minimum. If one wants to add more
// information just do it here. 

class Signal(
  val destination: Proc = null,
  val arrivalTime: Double = 0.0,
  val signalType: Int = 0,
  var next: Signal = null 
)
