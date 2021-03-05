// This class defines the signal list. If one wants to send more information than here,
// one can add the extra information in the Signal class and write an extra sendSignal method 
// with more parameters. 

object SignalList {
  private var list: Signal = new Signal()
  private var last: Signal = new Signal()
  list.next = last;

  def sendSignal(tpe : Int, dest: Proc, arrtime: Double): Unit = {
    var dummy: Signal = list.next
    var predummy: Signal = list
    val newSignal: Signal = 
      new Signal(signalType = tpe, destination = dest, arrivalTime = arrtime)

    while ((dummy.arrivalTime < newSignal.arrivalTime) & (dummy != last)) {
      predummy = dummy
      dummy = dummy.next
    }

    predummy.next = newSignal
    newSignal.next = dummy
  }

  def fetchSignal(): Signal = {
    val dummy = list.next
    list.next = dummy.next
    dummy.next = null
    dummy
  }
}
