import java.io.*;


//The class inherits Global so that we can use time and the signal names without dot notation

public class MainSimulation extends Global{

    public static void main(String[] args) throws IOException {

    	// The signal list is started and actSignal is declared. actSignal is the latest signal that has been fetched from the 
    	// signal list in the main loop below.

    	Signal actSignal;
    	new SignalList();

    	// Here process instances are created (two queues and one generator) and their parameters are given values. 

    	QS Q1 = new QS();
    	Q1.sendTo = null;

    	Gen Generator = new Gen();
    	//Generator.lambda = 0.8 ; //Generator shall generate 9 customers per second
    	Generator.sendTo = Q1; // The generated customers shall be sent to Q1

    	//To start the simulation the first signals are put in the signal list below

    	send(READY, Generator, time);
    	send(MEASURE, Q1, time);
    	
    	long startTime = System.currentTimeMillis(); // To measure execution time in mail loop

    	// This is the main loop

    	while (time < 12345678){
    		actSignal = SignalList.fetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.treatSignal(actSignal);
    	}
    	
    	// The execution time is measured and printed: 
    	
    	long stopTime = System.currentTimeMillis();
    	System.out.println("Execution time: " + (stopTime - startTime)/1000.0 + " seconds");

    	//Finally the result of the simulation is printed below:

    	System.out.println("Mean number of customers in queuing system: " + 1.0*Q1.accumulated/Q1.noMeasurements);

    }
}