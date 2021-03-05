object Global {
  val ARRIVAL = 1
  val READY = 2
  val MEASURE = 3

  var time = 0.0
  
  def send(tpe: Int, dest: Proc, arrtime: Double): Unit = 
    SignalList.sendSignal(tpe, dest, arrtime)
  
}
