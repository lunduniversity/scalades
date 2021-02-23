package scalades

open abstract class Process[Signal](using sim: Simulation[Signal]):
  import scala.compiletime.uninitialized
  type State
  def nextState: State => Signal => State
  def initState: State 

  private var _state: State = uninitialized
  final def currentState: State = _state
  final def init(): Unit = _state = initState
  sim.register(this)

  export sim.now
  
  private var _lastSender: Option[Process[Signal]] = None
  final def from: Option[Process[Signal]] = _lastSender

  final def update(signal: Signal, from: Option[Process[Signal]]): Unit =
    if from.isDefined then _lastSender = from else _lastSender = None
    _state = nextState(currentState)(signal)

  final def send(
    signal: Signal, 
    to: Process[Signal] = this, 
    delay: Time.Duration = Time.noDelay
  ): Unit =
    sim.add(sim.Event(signal, handler = to, from = this, time = now + delay)) 
