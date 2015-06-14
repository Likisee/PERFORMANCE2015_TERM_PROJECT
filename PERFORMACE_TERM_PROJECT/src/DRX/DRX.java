package DRX;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.GeometricDistribution;

public class DRX {

	// Topic: Device Power Saving and Latency Optimization inLTE-A Networks Through DRX Configuration
	// http://ieeexplore.ieee.org/xpl/articleDetails.jsp?arnumber=6776592
	
	public static void main(String[] args) {
		
		/******************************
		 * Settings
		 ******************************/
		
		// eventType
		final int ACTIVE = 1;
		final int BACKGROUND = 3; 
		
		// bound
		int bound = 1000000;
		int round = 1000000;
		
		// Parameters
//		final int lteBandwidth = 10000000;		// 10MHz = 一千萬
//		final int subCarrierSpacing = 15000;	// 15KHz = 一萬五
		final int tti = 1;						// Sub-frame Length (ms)
//		final int ttiSymbols = 14;
//		final int ttiSymbolsData = 11;
//		final int ttiSymbolsControl = 3;
		final int Ton = 4;						// DRX ON Duration (ms)
		final int [] TiArray = {1, 2, 3, 4, 5, 6, 8, 10, 20, 30, 40, 50, 60, 80, 100, 200, 300, 500, 750, 1280, 1920, 2560};// DRX Inactivity Timer (ms)
		final int [] TscArray = {10, 16, 20, 32, 40, 64, 80, 128, 160, 256, 320, 512, 640};									// Short DRX Cycle TimeOn Duration (ms)
		final int [] NscArrayy = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};									// Number of Short Cycles
		final int [] TlcArray = {8, 10, 16, 20, 32, 40, 64, 80, 128, 160, 256, 320, 512, 640, 1024, 1280, 2048, 2560};		// Long DRX Cycle (ms)
		
		ExponentialDistribution Ts	= new ExponentialDistribution(60 * 1000); 	// Session inter-arrival time
		ExponentialDistribution Tpc	= new ExponentialDistribution(0.5 * 1000); 	// Packet call inter-arrival time
		ExponentialDistribution Tp	= new ExponentialDistribution(0.01 * 1000); // Packet inter-arrival time
		ExponentialDistribution TBpc= new ExponentialDistribution(10 * 1000);	// Packet call inter-arrival time
		GeometricDistribution Ns	= new GeometricDistribution(1.0/7.0); 		// Number of packet calls per session (Mean:6)
		GeometricDistribution Npc	= new GeometricDistribution(1.0/21.0);; 	// Number of packets per packet call (Mean:20)
//		System.out.println("Mean: " + Ns.getNumericalMean());
//		System.out.println("Sample: " + Ns.sample());
		
		/******************************
		 * Main
		 ******************************/
		int Ti = 30;
		int Tsc = 64;
		int Tlc = 2560;
		int Nsc = 2;
		
		
		/******************************
		 * Simulation
		 ******************************/
		
		// record the event
		TreeMap<Integer, Event> mapEvent = new TreeMap<Integer, Event>();
		
		// Generating
		PriorityQueue<Event> queue = new PriorityQueue<Event>(2, new EventComp());
		queue.add(new Event(ACTIVE, 0)); // FirstActiveEvent
		queue.add(new Event(BACKGROUND, 0)); // FirstBackgroundEvent
		
		int et;
		int ts;
		int tsOffset;
		int nextTs;
		while(mapEvent.size() == 0 || mapEvent.lastEntry().getKey() < bound) {
			Event event = queue.remove();
			et = event.eventType;
			ts = event.timeStamp;
			
			switch(et) {
			case ACTIVE:
				
				// Generating Next Event
				tsOffset = (int) Ts.sample();
				nextTs = ts + tsOffset;
				queue.add(new Event(ACTIVE, nextTs));
				
				int aNs = (int) Ns.sample();
				int aNpc = (int) Npc.sample();
				for(int i = 0; i < aNs; i++) {
					for(int j = 0; j < aNpc; j++) {
						mapEvent.put(ts, event);
						tsOffset = (int) Tp.sample();
						if(j < aNs - 1) {
							ts = ts + tti + tsOffset;
						} else {
							ts = ts + tti;
						}
					}
					tsOffset = (int) Tpc.sample();
					ts = ts + tsOffset;					
				}
				
				break;
			case BACKGROUND:
				
				// Generating Next Event
				tsOffset = (int) TBpc.sample();
				nextTs = ts + tsOffset;
				event = new Event(BACKGROUND, nextTs);
				queue.add(event);
				
				break;
			}
		}
					
		
		/******************************
		 * Evaluation: DRX Sleep Time
		 ******************************/
		int cnt = 0;
		int NscCnt = 0;
		int totalDrxSleepTime = 0; // (ms)
		int currentEventMs = 0;
		int nextEventMs = 0;
//		ArrayList<Event> lstWaitingEvent = new ArrayList<Event>();
		Iterator<Entry<Integer, Event>> entries = mapEvent.entrySet().iterator();
		while (entries.hasNext()) {
			if(cnt == 0) {
				Entry<Integer, Event> entry = entries.next();
				nextEventMs = entry.getKey();
				cnt ++;
				System.out.println("currentEventMs: " + currentEventMs + ", nextEventMs: " + nextEventMs);
			}
			if(NscCnt == 0 && ((nextEventMs - currentEventMs) > (Ti + Ton))) { // short sleep: enter
     			NscCnt++;
				totalDrxSleepTime = totalDrxSleepTime + Tsc - Ton;
				currentEventMs = currentEventMs + Ti + Tsc;
				System.out.println("short sleep, NscCnt = " + NscCnt + ", totalDrxSleepTime = " + totalDrxSleepTime);
				while(nextEventMs < currentEventMs && entries.hasNext()) {
					// TODO DROP when sleep
					Entry<Integer, Event> entry = entries.next();
					nextEventMs = entry.getKey();
					cnt ++;
					System.out.println("currentEventMs: " + currentEventMs + ", nextEventMs: " + nextEventMs + " (PASS EVENT)");
				}
			} else if(NscCnt != 0 && NscCnt < Nsc && ((nextEventMs - currentEventMs) > Ton)) { // short sleep: continue
				NscCnt++;
				totalDrxSleepTime = totalDrxSleepTime + Tsc - Ton;
				currentEventMs = currentEventMs + Tsc;
				System.out.println("short sleep, NscCnt = " + NscCnt + ", totalDrxSleepTime = " + totalDrxSleepTime);
				while(nextEventMs < currentEventMs && entries.hasNext()) {
					// TODO DROP when sleep
					Entry<Integer, Event> entry = entries.next();
					nextEventMs = entry.getKey();
					cnt ++;
					System.out.println("currentEventMs: " + currentEventMs + ", nextEventMs: " + nextEventMs + " (PASS EVENT)");
				}
			} else if(NscCnt >= Nsc && ((nextEventMs - currentEventMs) > Ton)) { // long sleep: enter
				NscCnt++;
				totalDrxSleepTime = totalDrxSleepTime + Tlc - Ton;
				currentEventMs = currentEventMs + Tlc;
				System.out.println("long sleep, NscCnt = " + NscCnt + ", totalDrxSleepTime = " + totalDrxSleepTime);
				while(nextEventMs < currentEventMs && entries.hasNext()) {
					// TODO DROP when sleep
					Entry<Integer, Event> entry = entries.next();
					nextEventMs = entry.getKey();
					cnt ++;
					System.out.println("currentEventMs: " + currentEventMs + ", nextEventMs: " + nextEventMs + " (PASS EVENT)");
				}
			} else {
				NscCnt = 0;
				currentEventMs = nextEventMs;
				if(entries.hasNext()) {
					Entry<Integer, Event> entry = entries.next();
					nextEventMs = entry.getKey();
					cnt ++;
				}
				System.out.println("currentEventMs: " + currentEventMs + ", nextEventMs: " + nextEventMs + " (PROCESS EVENT)");
			}

		}
		System.out.println("Total packets: " + cnt);
		System.out.println("Sleep %: " + (double) totalDrxSleepTime / (double) nextEventMs);

	}

}
