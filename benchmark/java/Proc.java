// This is an abstract class which all classes that are used for defining 
// process types inherit. The purpose is to make sure that they all define the 
// method treatSignal.


public abstract class Proc extends Global{
	public abstract void treatSignal(Signal x);
}

