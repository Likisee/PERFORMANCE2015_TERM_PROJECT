package MMcc;

import java.util.PriorityQueue;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;

public class MMcc {

	public static void main(String[] args) {
		
		final int ARRIVAL = 1;
		final int DEPARTURE = 2;
		
		// Initialize
		int et = 0;
		double ts = 0.0;
		double bound = 1000000;
		double nArrival = 0;
		double nBlocking = 0;
		
		int nRound = 1000000;
		double totalExecuteTime = 0.0;
		double totalBlockRate = 0.0;
		double totalElaspsedSeconds = 0.0;
		
		// Setting
//		GammaDistribution M1 = new GammaDistribution(1, 1.0 / 50.0); // Arrival Rate
//		GammaDistribution M2 = new GammaDistribution(1, 1.0 / 10.0); // Service Rate
		ExponentialDistribution M1 = new ExponentialDistribution(1.0 / 50.0);
		ExponentialDistribution M2 = new ExponentialDistribution(1.0 / 10.0);
		int c1 = 8; // # of channels
		int c2 = 8; // system capacity
		
		// Generating First Event
		PriorityQueue<Event> queue = new PriorityQueue<Event>(c2, new EventComp());
		Event firstEvent = new Event(1, 0.0);
		queue.add(firstEvent);
		
		for(int i = 0; i < nRound; i++) {
			// Start
			long timeStart = System.currentTimeMillis();
			double maxTs = 0;
			while(nArrival <= bound) {
				
				Event event = queue.remove();
				et = event.eventType;
				ts = event.timeStamp;
				
				switch(et) {
				case ARRIVAL:
					
					// Generating Next Event
					nArrival++;
					event = new Event(1, ts + M1.sample());
					queue.add(event);
					
					if(c1 > 0) {
						// Do Service
						c1--;
						event = new Event(2, ts + M2.sample());
						queue.add(event);	
					} else {
						// Skip
						nBlocking++;
					}
					break;
				case DEPARTURE:
					c1++;
					
					if(ts > maxTs) maxTs = ts;
					
					break;
					
				}
				
			}
			long timeEnd = System.currentTimeMillis();
			
			// A Round
			System.out.println("A Execute Time: " + (timeEnd - timeStart) + " milliseconds");
			System.out.println("A Blocking Rate: " + nBlocking / nArrival);
			System.out.println("A Elaspsed seconds: " + maxTs);
			
			totalExecuteTime += timeEnd - timeStart;
			totalBlockRate += nBlocking / nArrival;
			totalElaspsedSeconds += maxTs;
		}
		
		// Final
		System.out.println("Avg Execute Time: " + totalExecuteTime/1000 + " seconds");
		System.out.println("Avg Blocking Rate: " + totalBlockRate / nRound);
		System.out.println("Avg Elaspsed seconds: " + totalElaspsedSeconds / nRound);
	}

}
