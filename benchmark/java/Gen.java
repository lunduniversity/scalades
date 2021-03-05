import java.util.*;


//This class inherits Proc so that we can use time and the signal names without dot notation 

class Gen extends Proc{

	//The random number generator is started:
	Random rnd = new Random();

	//There are two parameters:
	public Proc sendTo;    //Where to send customers
	public double lambda;  //How many to generate per second

	//What to do when a signal arrives
	public void treatSignal(Signal x){
		switch (x.signalType){
			case READY:{
				send(ARRIVAL, sendTo, time);
				send(READY, this, time + (2.0/lambda)*rnd.nextDouble());}
				break;
		}
	}
}