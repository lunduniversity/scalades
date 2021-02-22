package scalades

// TODO:
  // use receive instead of sim.add in generator of MM1 etc
  // add Logger 
  // let Simulation extend Process and handleNextEvent be handle??? and let an event to the simulation render a logging
  // should an event have a callback, default empty so that you can tell what to do when it is received? 
  

open abstract class Process(sim: Simulation):
  def handle(e: Event): Unit  
  def receive(msg: Message, from: Process, now: Time.Stamp): Unit = ???
  def send(msg: Message, to: Process, delay: Time.Duration): Unit =
    sim.add(Event(msg = msg, handler = to, from = this, time = sim.now + delay))

object NoProcess extends Process(null):
  def handle(e: Event): Unit = 
    throw new Simulation.EmptyHandlerException("illegal to call NoProcess.handle")

open class Message

final case class Event(msg: Message, handler: Process, from: Process, time: Time.Stamp)
object Event:
  given Ordering[Event] = 
    Ordering.fromLessThan[Event]((e1, e2) => e1.time.units > e2.time.units)

open class Simulation:
  import scala.collection.immutable.LazyList
  import scala.collection.mutable.PriorityQueue

  private val eventQueue = PriorityQueue.empty[Event]
  def events: LazyList[Event] = eventQueue.to(collection.immutable.LazyList)
  def queueLength = eventQueue.length
  def nonEmpty: Boolean = queueLength > 0
  def isEmpty: Boolean = queueLength == 0

  private var currentTime: Time.Stamp = Time.zero
  def now: Time.Stamp = currentTime
  def nextTimeStamp: Time.Stamp = eventQueue.head.time

  def add(event: Event): Unit =
    if (event.time.units >= currentTime.units) eventQueue.enqueue(event)
    else throw new Simulation.IllegalTimeException(s"$event is before $now")

  private var onNextEventCallback: Event => Unit = e => ()   

  def onNextEvent(callback: Event => Unit) = onNextEventCallback = callback

  private def handleNextEvent(): Unit = 
    val event = eventQueue.dequeue
    currentTime = event.time
    onNextEventCallback(event)
    event.handler.handle(event)

  def simulate(until: Time.Stamp): Unit =
    while nonEmpty && nextTimeStamp.units <= until.units 
    do handleNextEvent()

  final def reset(): Unit = 
    eventQueue.clear()
    currentTime = Time.zero

object Simulation:
  class EmptyHandlerException(msg: String) extends Exception(msg)
  class IllegalTimeException(msg: String)  extends Exception(msg)