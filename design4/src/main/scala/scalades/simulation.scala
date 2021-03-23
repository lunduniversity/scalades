package scalades

/** Base type for signals sent between processes. */
trait Signal

/** A simulation event representing a signal sent to destination from sender. */
final case class Event(signal: Signal, destination: Proc, sender: Proc, time: Time.Stamp)
object Event:
  given Ordering[Event] = 
    Ordering.fromLessThan[Event]((e1, e2) => e1.time.units > e2.time.units)

/** Simulation controller. */
open class Simulation:

  import scala.collection.immutable.LazyList
  import scala.collection.mutable.{PriorityQueue, ArrayBuffer}


  /*private*/ val eventQueue: PriorityQueue[Event] = PriorityQueue.empty[Event]
  private val processes = ArrayBuffer.empty[Proc]
  
  final def processIds: Range = processes.indices
  final def process(pid: Int): Option[Proc] = processes.lift(pid)

  object allProcesses:
    def toSeq: Seq[Proc] = processes.to(collection.immutable.ArraySeq)
    def foreach(action: Proc => Unit): Unit = processes.foreach(action)
    def map[T](f: Proc => T): Seq[T] = processes.map(f).toSeq
    def flatMap[T](f: Proc => IterableOnce[T]): Seq[T] = processes.flatMap(f).toSeq
    def withFilter(f: Proc => Boolean) = processes.withFilter(f)

  final def events: LazyList[Event] = eventQueue.to(collection.immutable.LazyList)
  final def queueLength = eventQueue.length
  final def nonEmpty: Boolean = queueLength > 0
  final def isEmpty: Boolean = queueLength == 0

  private var currentTime: Time.Stamp = Time.Zero
  final def now: Time.Stamp = currentTime
  final def nextTimeStamp: Time.Stamp = eventQueue.head.time

  final def register(p: Proc): Int = 
    processes.append(p)
    processes.length - 1

  final def add(event: Event): Unit =
    if (event.time.units >= currentTime.units) eventQueue.enqueue(event)
    else throw new Time.IllegalTimeException(s"$event time is before now: $now")

  private var onNextEventCallback: Event => Unit = e => ()   
  final def onNextEvent(callback: Event => Unit) = onNextEventCallback = callback

  def startSignal(signal: Signal, starter: Proc): Unit =
    add(Event(signal, starter, starter, Time.Zero))

  private def handleNextEvent(): Unit = 
    val event = eventQueue.dequeue
    currentTime = event.time
    onNextEventCallback(event)
    event.destination.update(event.signal, from = Some(event.sender))

  private def reset(): Unit = 
    eventQueue.clear()
    processes.foreach(_.start())
    currentTime = Time.Zero  

  final def simulateWhile(isTrue: => Boolean): Unit = 
    while isTrue do handleNextEvent()

  final def simulateUntil(endTime: Time.Stamp): Unit = 
    simulateWhile(nonEmpty && nextTimeStamp.units <= endTime.units) 

