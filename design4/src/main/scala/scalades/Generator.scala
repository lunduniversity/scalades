package scalades

/** A process that repeats sending `output` to `dest` at each `nextDuration`.*/
abstract class Generator(
  val output: Signal, 
  val dest: Process
)(using ctx: Simulation) 
extends Process(using ctx): 
  
  /** Implement this method to define the time to next output signal. */
  def nextDuration(): Time.Duration

  /** The signal sent to self each time a new output is generated. */
  case object Generate extends Signal 
  
  /** Send Generate to get things going. Call it before simulation starts. */
  override def start(): Unit = ctx.startSignal(Generate, this) 

  /** Called by simulation context to send output at nextDuration. */
  override def receive(input: Signal): Unit = input match 
      case Generate =>
        send(output, dest)
        send(Generate, delay = nextDuration())
