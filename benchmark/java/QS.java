import java.util.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation.

class QS extends Proc{
	public int numberInQueue = 0, accumulated, noMeasurements;
	public Proc sendTo;
	Random rnd = new Random();

	public void treatSignal(Signal x){
		switch (x.signalType){

			case ARRIVAL:{
				numberInQueue++;
				if (numberInQueue == 1){
					send(READY,this, time + 0.2*  rnd.nextDouble());
				}
			} break;

			case READY:{
				numberInQueue--;
				if (sendTo != null){
					send(ARRIVAL, sendTo, time);
				}
				if (numberInQueue > 0){
					send(READY, this, time + 0.2*  rnd.nextDouble());
				}
			} break;

			case MEASURE:{
				noMeasurements++;
				accumulated = accumulated + numberInQueue;
				send(MEASURE, this, time + 2*  rnd.nextDouble());
			} break;
		}
	}
}