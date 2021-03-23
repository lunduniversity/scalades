package scalades


// The name Process is clashing with java.lang.Process, perhaps Proc to avoid ambiguity.

abstract class Process(using ctx: Simulation):
  def start(): Unit
  def process(signal: Signal): Unit

  private var _lastSender: Option[Process] = None

  final def from: Option[Process] = _lastSender

  final def update(signal: Signal, from: Option[Process]): Unit = 
    if from.isDefined then _lastSender = from else _lastSender = None
    process(signal)
  
  def simulation: Simulation = ctx  // exposed for debugging purposes

  final def send(sig: Signal, dest: Process = this, delay: Time.Duration = Time.NoDelay): Unit =
    ctx.add(Event(sig, destination = dest, sender = this, time = now + delay)) 

  final val pid: Int = ctx.register(this)

  override def toString = getClass.getName + s":pid=$pid"

  def now: Time.Stamp = ctx.now
  

/** A process with state. 
 * 
 * Initialized by calling start(), which sets currentState to startState().
 * The abstract methods startState and nextState defines the behaviour.
 * The states are modelled by the abstract type `State`.
*/
abstract class StateMachine(using ctx: Simulation) extends Process(using ctx):
  import scala.compiletime.uninitialized

  type State

  def nextState(input: Signal): State

  def startState(): State 

  private var _state: State = uninitialized
  final def currentState: State = _state
  final def start(): Unit = _state = startState()

  final def process(signal: Signal): Unit =
    _state = nextState(signal)

