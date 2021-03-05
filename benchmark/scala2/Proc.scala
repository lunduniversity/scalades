// This is an abstract class which all classes that are used for defining 
// process types inherit. The purpose is to make sure that they all define the 
// method treatSignal.

trait Proc {
  def treatSignal(x: Signal): Unit
}

