package scalades

abstract class Generator(val output: Signal, val dest: Process)(using ctx: Simulation) 
extends Process(using ctx): 

  def nextDuration(): Time.Duration

  case object Generate extends Signal 

  def start(): Unit = ctx.startSignal(Generate, this) // to get things going

  def process(sig: Signal): Unit = sig match 
      case Generate =>
        send(output, dest)
        send(Generate, delay = nextDuration())
