package scalades

abstract class Generator(signal: Signal, receiver: Process)(using ctx: Simulation) 
extends Process(using ctx): 
  def nextDuration(): Time.Duration

  enum State:
    case Generating // Only one state

  object Generate extends Signal

  def startState(): State = 
    println("Gen started")
    send(Generate)  // to get things going
    State.Generating

  def nextState(): Transition = 
    case State.Generating =>
      case Generate =>
        println(s"Generator activated at $now.")
        send(signal, to = receiver)
        send(Generate, delay = nextDuration())
        currentState
