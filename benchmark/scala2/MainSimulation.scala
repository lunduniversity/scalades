//The class inherits Proc so that we can use time and the signal names without dot notation

object MainSimulation {
    import Global._
    
    def main(args: Array[String]): Unit = {

      // The signal list is started and actSignal is declared. actSignal is the latest signal that has been fetched from the 
      // signal list in the main loop below.

      var actSignal: Signal  = null

      // Here process instances are created (two queues and one generator) and their parameters are given values. 

      val Q1 = new QS
      Q1.sendTo = null

      //Generator shall generate 9 customers per second
      // The generated customers shall be sent to Q1
      val Generator = new Gen(lambda = 9, sendTo = Q1)

      //To start the simulation the first signals are put in the signal list below

      send(READY, Generator, time);
      send(MEASURE, Q1, time);
      
      val startTime = System.currentTimeMillis() // To measure execution time in mail loop

      // This is the main loop

      while (time < 1000000) {
        actSignal = SignalList.fetchSignal()
        time = actSignal.arrivalTime
        actSignal.destination.treatSignal(actSignal)
      }
      
      // The execution time is measured and printed: 
      
      val stopTime = System.currentTimeMillis()
      System.out.println("Execution time: " + (stopTime - startTime)/1000.0 + " seconds")

      //Finally the result of the simulation is printed below:

      System.out.println("Mean number of customers in queuing system: " + 1.0*Q1.accumulated/Q1.noMeasurements)
    }
}