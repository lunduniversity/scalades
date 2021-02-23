package scalades

open abstract class Process:
  def handle(e: Event): Unit  

open class Signal

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

  private var currentTime: Time.Stamp = Time.zero
  final def now: Time.Stamp = currentTime
  final def nextTimeStamp: Time.Stamp = eventQueue.head.time

  final def add(event: Event): Unit =
    if (event.time.units >= currentTime.units) eventQueue.enqueue(event)
    else throw new Simulation.IllegalTimeException(s"$event is before $now")

  private var onNextEventCallback: Event => Unit = e => ()   
  final def onNextEvent(callback: Event => Unit) = onNextEventCallback = callback

  private def handleNextEvent(): Unit = 
    val event = eventQueue.dequeue
    currentTime = event.time
    onNextEventCallback(event)
    event.handler.handle(event)

  final def simulate(until: Time.Stamp): Unit =
    while nonEmpty && nextTimeStamp.units <= until.units 
    do handleNextEvent()

  final def reset(): Unit = 
    eventQueue.clear()
    currentTime = Time.zero

object Simulation:
  final class IllegalTimeException(msg: String)  extends Exception(msg)