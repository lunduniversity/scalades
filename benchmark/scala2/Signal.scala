// This class defines a signal. What can be seen here is a minimum. If one wants to add more
// information just do it here. 

class Signal(
  var destination: Proc = null,
  var arrivalTime: Double = 0.0,
  var signalType: Int = 0,
  var next: Signal = null 
)
