package scalades.example
import scalades.*

//sbt "runMain scalades.example.runMM1 10.0 8.0 100.0"
@main def runMM1(lambda: Double, mu: Double, endTime: Double): Unit =
  MM1(lambda, mu).run(end=Time.Stamp(endTime))

class MM1(val lambda: Double, val mu: Double):
  def nextInterArrivalTime = Time.Duration(RNG.negExp(lambda))
  def nextServiceTime      = Time.Duration(RNG.negExp(mu))

  enum Signal:
    case Generate, Job, JobDone

  import Signal.*

  given sim: Simulation[Signal] = new Simulation[Signal]

  object Generator extends Process[Signal]:
    enum State { case Generating } // Only one state

    def initState = 
      send(Generate, this)  // to get things going
      State.Generating

    def nextState = 
      case State.Generating =>
        case Generate =>
          println(s"Generator activated at $now.")
          send(Job, to = Server)
          send(Generate, delay = nextInterArrivalTime)
          currentState
        case _ => currentState
  end Generator
  
  object Server extends Process[Signal]:
    enum State:
      case EmptyQueue, NonEmptyQueue
    
    import State.*

    private var nbrOfJobsInQ = 0
    def initState = EmptyQueue

    def nextState = _ =>
        case Job =>
          println(s"Server starts processing Job at $now. In queue: $nbrOfJobsInQ")
          if nbrOfJobsInQ == 0 then send(JobDone, delay = nextServiceTime)
          nbrOfJobsInQ += 1  
          NonEmptyQueue
        
        case JobDone =>
          nbrOfJobsInQ -= 1
          println(s"Server JobDone at $now. In queue: $nbrOfJobsInQ")
          if nbrOfJobsInQ > 0 then 
            send(JobDone, delay = nextServiceTime)
            NonEmptyQueue
          else
            EmptyQueue
        case _ => currentState
  end Server
  
  def run(end: Time.Stamp) = 
    Generator.init()
    Server.init()
    sim.simulate(until = end)
    for (pid <- sim.processIds) 
    do println(s"Process ${sim.process(pid)} in state ${sim.process(pid).currentState}")
    