package scalades.example
import scalades.*

val logger = new Logger(isDebug = false)

def dbg = logger.dbg 

def out(s: String) = 
  logger.log(s)
  logger.show(s)

//sbt "runMain scalades.example.runMM1 0.1 0.125 100.0"
@main def runMM1(lambda: Double, mu: Double, endTime: Double): Unit =
  MM1(lambda, mu).run(end=Time.Stamp(endTime))

class MM1(val lambda: Double, val mu: Double) extends Simulation:
  def nextInterArrivalTime(): Time.Duration = RNG.duration(lambda)
  def nextServiceTime(): Time.Duration = RNG.duration(mu)  

  def nextTimeToMeasure(): Time.Duration = RNG.duration(1.0)
  
  given Simulation = this

  object JobGenerator extends Generator(Server.Job, Server):
    def nextDuration() =  nextInterArrivalTime()

  object Sampler extends Generator(Server.Measure, Server):
    var average: Double = 0.0 // cumulative moving average
    var n: Long = 0 // number of samples
    def addSample(x: Double): Unit = 
      // https://en.wikipedia.org/wiki/Moving_average#Cumulative_moving_average
      average += (x - average)/(n + 1)
      n += 1

    def nextDuration() =  nextTimeToMeasure()
  
  object Server extends StateMachine:
    enum State:
      case EmptyQueue, NonEmptyQueue

    enum Signals extends Signal:
      case Job, JobDone, Measure 

    export Signals.*

    import State.*

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
    out(s"Number of samples: ${Sampler.n}")
    out(s"Average queue length: ${Sampler.average}")
    out(s"Execution time: ${(t1 - t0)/1000.0} seconds")
    out(s"Last event at time stamp: $now time units")
    out(s"SIMULATION END AFTER $end time units")   
    
end MM1