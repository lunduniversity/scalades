package scalades

/** A process that can receive and send signals from/to other processes. 
 * 
 *  @param ctx the simulation context that handles signal event logic.
*/
abstract class Process(using ctx: Simulation):
  import scala.compiletime.uninitialized

  /** Implement init. You should call it before simulation is started. */
  def start(): Unit

  /** Implement behaviour when input signal is received. Called by ctx*/
  def receive(input: Signal): Unit  

  /** A process identity number that is unique for this process instance. */
  final val pid: Int = ctx.register(this)

  private var _lastSender: Process = uninitialized

  /** The process that sent the last signal, uninitialized until first receive.*/
  final def lastSender: Process = _lastSender

  /** The current simulation time. */
  final def now: Time.Stamp = ctx.now

  /** Send output to this or another process. Will happen at now + delay. */
  final def send(
    output: Signal, 
    dest: Process = this, 
    delay: Time.Duration = Time.NoDelay
  ): Unit =
    ctx.add(Simulation.Event(output, dest, sender = this, time = now + delay)) 

  /** Override this method if you want a custom name and not the class name.*/
  def name: String = getClass.getName

  /** A string representation of this instance including name and pid. */
  override def toString = s"name:pid=$pid"

  /** Called by ctx to update lastSender and call process. */
  final def update(signal: Signal, sender: Process): Unit = 
    _lastSender = sender
    receive(signal)
  

