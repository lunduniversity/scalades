//The class inherits Proc so that we can use time and the signal names without dot notation

object MainSimulation {
    import Global._
    
    def main(args: Array[String]): Unit = {

      val qs = new QS  // Create a Queueing System 

      // The generator shall generate 9 customers per second
      // The generated customers shall be sent to qs
      val generator = new Gen(lambda = 9, sendTo = qs)

      //To start the simulation the first signals are put in the signal list below

      send(READY, generator, time);
      send(MEASURE, qs, time);
      
      val startTime = System.currentTimeMillis() // To measure execution time in mail loop

      // This is the main loop
			// actSignal is the latest signal that has been fetched from the 
      // signal list in the main loop below.

      while (time < 1000000) {
        val actSignal = SignalList.fetchSignal()
        time = actSignal.arrivalTime
        actSignal.destination.treatSignal(actSignal)
      }
      
      // The execution time is measured and printed: 
      
      val stopTime = System.currentTimeMillis()
      System.out.println("Execution time: " + (stopTime - startTime)/1000.0 + " seconds")

      //Finally the result of the simulation is printed below:

      System.out.println("Mean number of customers in queuing system: " + 1.0*qs.accumulated/qs.noMeasurements)
    }
}