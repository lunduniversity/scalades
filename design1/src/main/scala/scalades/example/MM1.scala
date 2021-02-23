package scalades.example
import scalades.*

//sbt "runMain scalades.example.runMM1 10.0 8.0 100.0"
@main def runMM1(lambda: Double, mu: Double, endTime: Double): Unit =
  MM1(lambda, mu).run(end=Time.Stamp(endTime))

final class MM1(val lambda: Double, val mu: Double):
  def nextInterArrivalTime: Time.Duration = Time.Duration(RNG.negExp(lambda))
  def nextServiceTime: Time.Duration = Time.Duration(RNG.negExp(mu))

  val sim = new Simulation

  enum Sig extends Signal:
    case Generate, Job, JobDone

  import Sig.*

  object generator extends Process:
    def handle(e: Event) = e match
      case Event(Generate, handler, from, now) =>
        println(s"Generator activated at $now.")
        sim.add(Event(Job, handler = server, from = this, time = now))
        sim.add(Event(Generate, handler = this, from = this, time = now + nextInterArrivalTime))
 
  object server extends Process:
    private var nbrOfJobsInQ = 0
    def n = nbrOfJobsInQ

    def handle(e: Event) = e match
      case Event(Job, handler, from, now) =>
        println(s"Server starts processing Job at $now. In queue: $nbrOfJobsInQ")
        if nbrOfJobsInQ == 0
        then sim.add(Event(JobDone, handler = this, from = this, time = now + nextServiceTime))
        nbrOfJobsInQ += 1

      case Event(JobDone, handler, from, now) =>
        nbrOfJobsInQ -= 1
        println(s"Server JobDone at $now. In queue: $nbrOfJobsInQ")
        if nbrOfJobsInQ > 0
        then sim.add(Event(JobDone, handler = this, from = this, time = now + nextServiceTime))

  def run(end: Time.Stamp) = 
    sim.reset()
    sim.add(Event(signal = Generate, handler = generator, from = null, Time.zero)) // null is ugly
    sim.simulate(until = end)

