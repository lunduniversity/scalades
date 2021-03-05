public class Global{
	public static final int ARRIVAL = 1, READY = 2, MEASURE = 3;
	public static double time = 0;
	
	public static void send(int type, Proc dest, double arrtime) {
		SignalList.sendSignal(type, dest, arrtime);
	}
	
}
