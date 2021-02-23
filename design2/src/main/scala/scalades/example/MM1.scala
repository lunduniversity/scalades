package scalades.example
import scalades.*

//sbt "runMain scalades.example.runMM1 10.0 8.0 100.0"
@main def runMM1(lambda: Double, mu: Double, endTime: Double): Unit =
  MM1(lambda, mu).run(end=Time.Stamp(endTime))

final class MM1(val lambda: Double, val mu: Double):
  def nextInterArrivalTime: Time.Duration = Time.Duration(RNG.negExp(lambda))
  def nextServiceTime: Time.Duration = Time.Duration(RNG.negExp(mu))

  given sim: Simulation = new Simulation

  enum Sig extends Signal:
    case Generate, Job, JobDone

  import Sig.*

  object Generator extends Process:
    def receive(sig: Signal, from: Process, now: Time.Stamp): Unit = sig match
      case Generate =>
        println(s"Generator activated at $now.")
        send(Job, to = Server)
        send(Generate, to = this, delay = nextInterArrivalTime)
 
  object Server extends Process:
    private var nbrOfJobsInQ = 0

    def receive(sig: Signal, from: Process, now: Time.Stamp): Unit = sig match
      case Job =>
        println(s"Server starts processing Job at $now. In queue: $nbrOfJobsInQ")
        if nbrOfJobsInQ == 0 then send(JobDone, to = this, delay = nextServiceTime)
        nbrOfJobsInQ += 1

      case JobDone =>
        nbrOfJobsInQ -= 1
        println(s"Server JobDone at $now. In queue: $nbrOfJobsInQ")
        if nbrOfJobsInQ > 0 then send(JobDone, to = this, delay = nextServiceTime)

  def run(end: Time.Stamp) = 
    sim.reset()
    sim.add(Event(signal = Generate, handler = Generator, from = Generator, Time.init)) 
    sim.simulate(until = end)

