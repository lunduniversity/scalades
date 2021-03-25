package scalades.example
import scalades.*

//sbt "runMain scalades.example.runMM1 9 10 10000"
@main 
def runMM1(lambda: Double, mu: Double, endTime: Double): Unit =
  MM1(lambda, mu, sampleFreq = 1.0)
    .run(end = Time.Stamp(endTime))

class MM1(val lambda: Double, val mu: Double, sampleFreq: Double) extends Simulation:

  def nextInterArrivalTime(): Time.Duration = RNG.duration(lambda)
  def nextServiceTime(): Time.Duration = RNG.duration(mu)  
  def nextTimeToMeasure(): Time.Duration = RNG.duration(sampleFreq)
  
  given Simulation = this // provide implicit context to processes

  val logger = new Logger(isDebug = false)
  import logger.dbg   // for debugging: dbg("msg") prints if isDebug is true
  def out(s: String) = // log both to file and console
    logger.log(s)
    logger.show(s)

  object JobGenerator extends Generator(Server.Job, Server):
    def nextDuration() =  nextInterArrivalTime()

  object Sampler extends Generator(Server.Measure, Server):
    private var avg: Double = 0.0 
    def average = avg

    private var n: Long = 0 // number of samples
    def nbrOfSamples = n 

    def addSample(x: Double): Unit = 
    // https://en.wikipedia.org/wiki/Moving_average#Cumulative_moving_average
      avg += (x - avg)/(n + 1)
      n += 1

    def nextDuration() =  nextTimeToMeasure()
  end Sampler

  object Server extends StateMachine:
    enum State:
      case EmptyQueue, NonEmptyQueue
    import State.*

    enum Sig extends Signal:
      case Job, JobDone, Measure 
    export Sig.*  //Users can write Server.Job instead of Server.Sig.Job

    private var nbrOfJobsInQ = 0

    def startState() = EmptyQueue

    def nextState(input: Signal): State = input match
        case Job =>
          dbg(s"Server starts processing Job at $now. In queue: $nbrOfJobsInQ")
          if nbrOfJobsInQ == 0 then send(JobDone, delay = nextServiceTime())
          nbrOfJobsInQ += 1  
          NonEmptyQueue
        
        case JobDone =>
          nbrOfJobsInQ -= 1
          dbg(s"Server JobDone at $now. In queue: $nbrOfJobsInQ")
          if nbrOfJobsInQ > 0 then 
            send(JobDone, delay = nextServiceTime())
            NonEmptyQueue
          else
            EmptyQueue
        
        case Measure =>
          Sampler.addSample(nbrOfJobsInQ)
          currentState

  end Server
  
  def run(end: Time.Stamp) =
    logger.clearLog()
    out("---- SIMULATION START -----")   
    JobGenerator.start()
    Server.start()
    Sampler.start()
    val t0 = System.currentTimeMillis
    simulateUntil(end)
    val t1 = System.currentTimeMillis
    out(s"Number of samples: ${Sampler.nbrOfSamples}")
    out(s"Average queue length: ${Sampler.average}")
    out(s"Execution time: ${(t1 - t0)/1000.0} seconds")
    out(s"Last event at time stamp: $now time units")
    out(s"SIMULATION END AFTER $end time units")   
    
end MM1