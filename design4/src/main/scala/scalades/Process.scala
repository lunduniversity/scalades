package scalades

abstract class Process(using ctx: Simulation):
  import scala.compiletime.uninitialized
  type State
  type Transition = State => Signal => State

  def nextState(): Transition

  def startState(): State 

  private var _state: State = uninitialized
  final def currentState: State = _state
  final def start(): Unit = _state = startState()
  final val pid: Int = ctx.register(this)

  override def toString = super.toString + s":PID=$pid"

  def now: Time.Stamp = ctx.now

  private var _lastSender: Option[Process] = None
  final def from: Option[Process] = _lastSender

  final def update(signal: Signal, from: Option[Process]): Unit =
    if from.isDefined then _lastSender = from else _lastSender = None
    _state = nextState()(currentState)(signal)

  final def send(
    signal: Signal, 
    to: Process = this, 
    delay: Time.Duration = Time.NoDelay
  ): Unit =
    ctx.add(ctx.Event(signal, handler = to, from = this, time = now + delay)) 
