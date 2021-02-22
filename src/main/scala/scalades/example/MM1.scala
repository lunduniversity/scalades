package scalades

class MM1(val lambda: Double, val mu: Double):
  def nextInterArrivalTime: Time.Duration = Time.Duration(RNG.negExp(lambda))
  def nextServiceTime: Time.Duration = Time.Duration(RNG.negExp(mu))

  val sim = new Simulation

  enum Msg extends Message:
    case Generate, Job, JobDone

  object generator extends Process(sim):
    def handle(e: Event) = e match
      case Event(Msg.Generate, handler, from, now) =>
        println(s"Generator activated at $now.")
        sim.add(Event(msg = Msg.Job, handler = server, from = this, time = now))
        sim.add(Event(msg = Msg.Generate, handler = this, from = this, time = now + nextInterArrivalTime))
 
  object server extends Process(sim):
    private var nbrOfJobsInQ = 0
    def n = nbrOfJobsInQ

    def handle(e: Event) = e match
      case Event(Msg.Job, handler, from, now) =>
        println(s"Server starts processing Job at $now. In queue: $nbrOfJobsInQ")
        if nbrOfJobsInQ == 0
        then sim.add(Event(msg=Msg.JobDone, handler = this, from = this, time = now + nextServiceTime))
        nbrOfJobsInQ += 1

      case Event(Msg.JobDone, handler, from, now) =>
        nbrOfJobsInQ -= 1
        println(s"Server JobDone at $now. In queue: $nbrOfJobsInQ")
        if nbrOfJobsInQ > 0
        then sim.add(Event(Msg.JobDone, handler = this, from = this, time = now + nextServiceTime))

  def until(end: Time.Stamp) = 
    sim.add(Event(msg = Msg.Generate, handler = generator, from = NoProcess, Time.zero))
    sim.simulate(until = end)

object MM1:
  @main def run(lambda: Double, mu: Double, endTime: Double): Unit =
    new MM1(lambda, mu).until(end=Time.Stamp(endTime))
