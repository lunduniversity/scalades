package scalades


/** A process with state. 
 * 
 * Initialized by calling start(), which sets currentState to startState().
 * The abstract methods startState and nextState defines the behaviour.
 * The states are modelled by the abstract type `State`.
 * The name Process is taken java.lang.Process, hence Proc to avoid ambiguity.
*/
abstract class Proc(using val ctx: Simulation):
  import scala.compiletime.uninitialized
  type State
  type Transition = State => Signal => State

  def nextState(): Transition

  def startState(): State 

  private var _state: State = uninitialized
  final def currentState: State = _state
  final def start(): Unit = _state = startState()
  final val pid: Int = ctx.register(this)

  override def toString = getClass.getName + s":pid=$pid"

  def now: Time.Stamp = ctx.now

  private var _lastSender: Option[Proc] = None
  final def from: Option[Proc] = _lastSender

  final def update(signal: Signal, from: Option[Proc]): Unit =
    if from.isDefined then _lastSender = from else _lastSender = None
    _state = nextState()(currentState)(signal)

  final def send(
    sig: Signal, 
    dest: Proc = this, 
    delay: Time.Duration = Time.NoDelay
  ): Unit =
    ctx.add(Event(sig, destination = dest, sender = this, time = now + delay)) 



abstract class SingleStateProc(using ctx: Simulation) extends Proc(using ctx):
  type State = Unit // Only one state
  def handleSignal(): Signal => Unit
  final override def nextState(): Transition = _ => handleSignal()
  final override def startState(): State = ()