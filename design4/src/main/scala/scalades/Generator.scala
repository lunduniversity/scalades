package scalades

// abstract class Generator(val sig: Signal, val dest: Proc)(using ctx: Simulation) 
// extends Proc(using ctx): 

//   def nextDuration(): Time.Duration

//   enum State:
//     case Generating // Only one state

//   case object Generate extends Signal //Only one sig

//   def startState(): State = State.Generating

//   def nextState(): Transition = 
//     case State.Generating =>
//       case Generate =>
//         send(sig, dest)
//         send(Generate, delay = nextDuration())
//         currentState

abstract class Generator(val sig: Signal, val dest: Proc)(using ctx: Simulation) 
extends SingleStateProc(using ctx): 

  def nextDuration(): Time.Duration

  case object Generate extends Signal //Only one sig

  def handleSignal(): Signal => Unit = 
      case Generate =>
        send(sig, dest)
        send(Generate, delay = nextDuration())

