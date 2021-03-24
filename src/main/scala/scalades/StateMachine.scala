package scalades

/** A process with a state that is updated when receiving signals. */
abstract class StateMachine(using ctx: Simulation) extends Process(using ctx):
  import scala.compiletime.uninitialized

  /** Implement possible state values, typically by enum State: ... */
  type State

  /** Implement the state that is assigned when `start()` is called */
  def startState(): State 
  
  /** Implement state transitions when receiving input signals. */
  def nextState(input: Signal): State

  private var _state: State = uninitialized

  /** Return the current state. Uninitialized until first call of start(). */
  final def currentState: State = _state

  /** Initialize state to startState(). Call before start of simulation. */ 
  final override def start(): Unit = _state = startState()

  /** Update state using `nextState` when simulation context update. */
  final override def receive(input: Signal): Unit = _state = nextState(input)


