package scalades

export Simulation.Signal // Make Signal available by `import scalades.*`

object Simulation:
  /** Base trait for all signals sent between processes. */
  trait Signal

  /** A simulation event representing the sending of a signal. */
  final case class Event(
    signal: Signal, 
    receiver: Process, 
    sender: Process, 
    time: Time.Stamp
  )
  
  /** Event companion with implicit ordering of events according to time. */
  object Event:
    given Ordering[Event] = 
      Ordering.fromLessThan[Event]((e1, e2) => e1.time.units > e2.time.units)

/** A driver of simulations with an event queue and signal event logic. */
open class Simulation:

  import scala.collection.mutable.{PriorityQueue, ArrayBuffer}
  import Simulation.Event

  private val eventQueue: PriorityQueue[Event] = PriorityQueue.empty[Event]
  private val processes = ArrayBuffer.empty[Process]
  
  /** A range with all process identities. */
  final def processIds: Range = processes.indices

  /** Gives an optional reference to the process with identity number pid. */
  final def process(pid: Int): Option[Process] = processes.lift(pid)
  
  /** Object with methods to allow `for p <- allProcess ...`*/
  object allProcesses:
    def toSeq: Seq[Process] = processes.to(collection.immutable.ArraySeq)
    def foreach(action: Process => Unit): Unit = processes.foreach(action)
    def map[T](f: Process => T): Seq[T] = processes.map(f).toSeq
    def flatMap[T](f: Process => IterableOnce[T]): Seq[T] = processes.flatMap(f).toSeq
    def withFilter(f: Process => Boolean) = processes.withFilter(f)

  /** A list with a lazy copy of the event queue. */
  final def events: LazyList[Event] = eventQueue.to(LazyList)

  /** The number of remaining events in the event queue. */
  final def queueLength = eventQueue.length
  
  /** True if there are events left in the event queue. */
  final def nonEmpty: Boolean = queueLength > 0

  /** True if there are no event in the event queue. */
  final def isEmpty: Boolean = queueLength == 0

  private var currentTime: Time.Stamp = Time.Zero

  /** The current simulation time. */
  final def now: Time.Stamp = currentTime

  /** The simulation time of the next event. */
  final def nextTimeStamp: Time.Stamp = eventQueue.head.time

  /** Store a reference to a process and return its identity number. */
  final def register(p: Process): Int = 
    processes.append(p)
    processes.length - 1

  /** Add an event to the event queue. */
  final def add(event: Event): Unit =
    if (event.time.units >= now.units) eventQueue.enqueue(event)
    else throw new Time.IllegalTimeException(s"$event time is before now: $now")

  private var onNextEventCallback: Event => Unit = e => ()   
  
  /** Register a callback procedure that is called when each event is handled. */
  final def onNextEvent(callback: Event => Unit) = onNextEventCallback = callback

  /** Send a start signal that is enqueued into the event que at Time.Zero. */
  final def startSignal(signal: Signal, starter: Process): Unit =
    add(Event(signal, starter, starter, Time.Zero))

  private def handleNextEvent(): Unit = 
    val event = eventQueue.dequeue
    currentTime = event.time
    onNextEventCallback(event)
    event.receiver.update(event.signal, sender = event.sender)

  private def reset(): Unit = 
    eventQueue.clear()
    processes.foreach(_.start())
    currentTime = Time.Zero  

  /** Simulate while the condition `isTrue` is true. */  
  final def simulateWhile(isTrue: => Boolean): Unit = 
    while isTrue do handleNextEvent()

  /** Simulate until simulation time is `endTime` or the event queue is empty.*/
  final def simulateUntil(endTime: Time.Stamp): Unit = 
    simulateWhile(nonEmpty && nextTimeStamp.units <= endTime.units) 

