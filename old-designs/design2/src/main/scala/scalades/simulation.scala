package scalades

open class Signal

open abstract class Process:
  def receive(sig: Signal, from: Process, now: Time.Stamp): Unit  

  final def send(sig: Signal, to: Process, 
    delay: Time.Duration = Time.noDelay)(using sim: Simulation): Unit = 
      sim.add(Event(sig, handler = to, from = this, time = sim.now + delay))

final case class Event(signal: Signal, handler: Process, from: Process, time: Time.Stamp)
object Event:
  given Ordering[Event] = 
    Ordering.fromLessThan[Event]((e1, e2) => e1.time.units > e2.time.units)

open class Simulation:
  import scala.collection.immutable.LazyList
  import scala.collection.mutable.PriorityQueue

  private val eventQueue = PriorityQueue.empty[Event]
  final def events: LazyList[Event] = eventQueue.to(collection.immutable.LazyList)
  final def queueLength = eventQueue.length
  final def nonEmpty: Boolean = queueLength > 0
  final def isEmpty: Boolean = queueLength == 0

  private var currentTime: Time.Stamp = Time.init
  final def now: Time.Stamp = currentTime
  final def nextTimeStamp: Time.Stamp = eventQueue.head.time

  final def add(event: Event): Unit =
    if (event.time.units >= currentTime.units) eventQueue.enqueue(event)
    else throw new Time.IllegalTimeException(s"$event time is before now: $now")

  private var onNextEventCallback: Event => Unit = e => ()   
  final def onNextEvent(callback: Event => Unit) = onNextEventCallback = callback

  private def handleNextEvent(): Unit = 
    val event = eventQueue.dequeue
    currentTime = event.time
    onNextEventCallback(event)
    event.handler.receive(event.signal, event.from, now)

  final def simulate(until: Time.Stamp): Unit =
    while nonEmpty && nextTimeStamp.units <= until.units 
    do handleNextEvent()

  final def reset(): Unit = 
    eventQueue.clear()
    currentTime = Time.init  