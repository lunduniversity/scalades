package scalades.example
import scalades.*

//sbt "runMain scalades.example.runMM1 0.1 0.125 100.0"
@main def runMM1(lambda: Double, mu: Double, endTime: Double): Unit =
  MM1(lambda, mu).run(end=Time.Stamp(endTime))

class MM1(val lambda: Double, val mu: Double) extends Simulation:
  def nextInterArrivalTime(): Time.Duration = RNG.duration(lambda)
  def nextServiceTime(): Time.Duration = RNG.duration(mu)

  given Simulation = this

  object JobGenerator extends Generator(Server.Job, Server):
    def nextDuration() =  nextInterArrivalTime()
  
  object Server extends Process:
    enum State:
      case EmptyQueue, NonEmptyQueue

    enum Signals extends Signal:
      case Job, JobDone

    export Signals.*

    import State.*

    private var nbrOfJobsInQ = 0
    def startState() = EmptyQueue

    def nextState() = _ =>
        case Job =>
          println(s"Server starts processing Job at $now. In queue: $nbrOfJobsInQ")
          if nbrOfJobsInQ == 0 then send(JobDone, delay = nextServiceTime())
          nbrOfJobsInQ += 1  
          NonEmptyQueue
        
        case JobDone =>
          nbrOfJobsInQ -= 1
          println(s"Server JobDone at $now. In queue: $nbrOfJobsInQ")
          if nbrOfJobsInQ > 0 then 
            send(JobDone, delay = nextServiceTime())
            NonEmptyQueue
          else
            EmptyQueue
        case _ => currentState
  end Server
  
  def run(end: Time.Stamp) =   
    println(s"MM1 started\ngenerator:$JobGenerator\nserver:$Server")
    JobGenerator.start()
    Server.start()
    simulate(until = end)
    for (pid <- processIds) 
    do println(s"Process ${process(pid)} in state ${process(pid).get.currentState}")
    
end MM1