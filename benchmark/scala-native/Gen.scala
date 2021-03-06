//This class inherits Proc so that we can use time and the signal names without dot notation 


  //There are two parameters:
    //Where to send customers
    //How many to generate per second

class Gen(sendTo: Proc, lambda: Double) extends Proc {

  //The random number generator is started:
  val rnd = new java.util.Random()

  //What to do when a signal arrives
  @inline def treatSignal(x: Signal): Unit = {
    import Global._
    if (x.signalType ==  READY) {
        send(ARRIVAL, sendTo, time)
        send(READY, this, time + (2.0/lambda)*rnd.nextDouble())
    }
  }
}