// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation.

class QS extends Proc {
  var numberInQueue = 0
  var accumulated = 0
  var noMeasurements = 0
  var sendTo: Proc = null
  val slump = new java.util.Random()

  def treatSignal(x: Signal): Unit = {
    import Global._
    x.signalType match {
      case ARRIVAL =>
        numberInQueue += 1
        if (numberInQueue == 1) send(READY,this, time + 0.2*slump.nextDouble())

      case READY =>
        numberInQueue -= 1
        if (sendTo != null) send(ARRIVAL, sendTo, time)
        if (numberInQueue > 0) send(READY, this, time + 0.2*slump.nextDouble())

      case MEASURE =>
        noMeasurements += 1
        accumulated += numberInQueue
        send(MEASURE, this, time + 2*slump.nextDouble())
    }
  }
}