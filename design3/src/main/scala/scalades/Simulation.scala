package scalades

class Simulation[Signal]:

  import scala.collection.immutable.LazyList
  import scala.collection.mutable.{PriorityQueue, ArrayBuffer}

  final case class Event(signal: Signal, handler: Process[Signal], from: Process[Signal], time: Time.Stamp)
  object Event:
    given Ordering[Event] = 
      Ordering.fromLessThan[Event]((e1, e2) => e1.time.units > e2.time.units)

  private val eventQueue = PriorityQueue.empty[Event]
  private val processes = ArrayBuffer.empty[Process[Signal]]
  def processIds: Range = processes.indices
  def process(pid: Int): Process[Signal] = processes(pid)
  final def events: LazyList[Event] = eventQueue.to(collection.immutable.LazyList)
  final def queueLength = eventQueue.length
  final def nonEmpty: Boolean = queueLength > 0
  final def isEmpty: Boolean = queueLength == 0

  private var currentTime: Time.Stamp = Time.init
  final def now: Time.Stamp = currentTime
  final def nextTimeStamp: Time.Stamp = eventQueue.head.time

  final def register(p: Process[Signal]): Unit = processes.append(p)

  final def add(event: Event): Unit =
    if (event.time.units >= currentTime.units) eventQueue.enqueue(event)
    else throw new Time.IllegalTimeException(s"$event time is before now: $now")

  private var onNextEventCallback: Event => Unit = e => ()   
  final def onNextEvent(callback: Event => Unit) = onNextEventCallback = callback

  private def handleNextEvent(): Unit = 
    val event = eventQueue.dequeue
    currentTime = event.time
    onNextEventCallback(event)
    event.handler.update(event.signal, Some(event.from))

  final def simulate(until: Time.Stamp): Unit =
    while nonEmpty && nextTimeStamp.units <= until.units 
    do handleNextEvent()

  final def reset(): Unit = 
    eventQueue.clear()
    processes.foreach(_.init())
    currentTime = Time.init  
