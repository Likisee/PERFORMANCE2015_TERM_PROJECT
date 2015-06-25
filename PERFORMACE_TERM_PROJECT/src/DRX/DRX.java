package DRX;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.GeometricDistribution;

import DRX.Event.EventType;

public class DRX {

	// Topic: Device Power Saving and Latency Optimization in LTE-A Networks Through DRX Configuration
	// http://ieeexplore.ieee.org/xpl/articleDetails.jsp?arnumber=6776592
	
	public static void main(String[] args) {
		
		/******************************
		 * Settings
		 ******************************/
		
		// bound
		int bound = 10000;
		int round = 100000;
		
		// Parameters
//		final int lteBandwidth = 10000000;		// 10MHz
//		final int subCarrierSpacing = 15000;	// 15KHz
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
		GeometricDistribution Ns	= new GeometricDistribution(1.0/7.0); 		// Number of packet calls per session (Mean:6) ((1-p)/p)
		GeometricDistribution Npc	= new GeometricDistribution(1.0/21.0);; 	// Number of packets per packet call (Mean:20) ((1-p)/p)
//		System.out.println("Mean: " + Ns.getNumericalMean());
//		System.out.println("Sample: " + Ns.sample());
		
		/******************************
		 * Main
		 ******************************/
		int Ti = 30;
		int Tsc = 20;
		int Tlc = 80;
//		int Tsc = 40;
//		int Tlc = 160;
//		int Tsc = 80;
//		int Tlc = 320;
//		int Tsc = 160;
//		int Tlc = 640;
//		int Tsc = 320;
//		int Tlc = 1280;
//		int Tsc = 640;
//		int Tlc = 2560;
		int Nsc = 2;
		
		// percent
		double percent;
		double sumPercent = 0;
		for(int i = 0 ; i < round; i++) {
			TreeMap<Integer, Event> mapEvent = getMapEvent(bound, tti, Ts, Tpc, Tp, TBpc, Ns, Npc);	// Simulation
			if(mapEvent.size() > 1) {
				percent = getEvaluationDRXSleepTime(mapEvent, Ton, Ti, Tsc, Tlc, Nsc);				// Evaluation: DRX Sleep Time
				sumPercent = sumPercent + percent;
				System.out.println("Round " + (i+1) + ", DRX Sleep Time (Percent): " + String.format("%.0f%%", percent * 100));
			} else {
				i--;
			}
		}
		System.out.println("Final, DRX Sleep Time (Percent): " + String.format("%.0f%%", sumPercent / round * 100));
		
		Nsc = 4;
		sumPercent = 0;
		for(int i = 0 ; i < round; i++) {
			TreeMap<Integer, Event> mapEvent = getMapEvent(bound, tti, Ts, Tpc, Tp, TBpc, Ns, Npc);	// Simulation
			if(mapEvent.size() > 1) {
				percent = getEvaluationDRXSleepTime(mapEvent, Ton, Ti, Tsc, Tlc, Nsc);				// Evaluation: DRX Sleep Time
				sumPercent = sumPercent + percent;
			} else {
				i--;
			}
		}
		System.out.println("Final, DRX Sleep Time (Percent): " + String.format("%.0f%%", sumPercent / round * 100));
		
		Nsc = 16;
		sumPercent = 0;
		for(int i = 0 ; i < round; i++) {
			TreeMap<Integer, Event> mapEvent = getMapEvent(bound, tti, Ts, Tpc, Tp, TBpc, Ns, Npc);	// Simulation
			if(mapEvent.size() > 1) {
				percent = getEvaluationDRXSleepTime(mapEvent, Ton, Ti, Tsc, Tlc, Nsc);				// Evaluation: DRX Sleep Time
				sumPercent = sumPercent + percent;
			} else {
				i--;
			}
		}
		System.out.println("Final, DRX Sleep Time (Percent): " + String.format("%.0f%%", sumPercent / round * 100));
		
		// delay
//		double delay;
//		double sumDelay = 0;
//		for(int i = 0 ; i < round; i++) {
//			TreeMap<Integer, Event> mapEvent = getMapEvent(bound, tti, Ts, Tpc, Tp, TBpc, Ns, Npc);	// Simulation
//			if(mapEvent.size() > 1) {
//				delay = getEvaluationDRXDelayTime(mapEvent, Ton, Ti, Tsc, Tlc, Nsc);				// Evaluation: DRX Delay Time
//				sumDelay = sumDelay + delay;
//				System.out.println("Round " + (i+1) + ", DRX Delay Time (s): " + (delay / 1000));
//			} else {
//				i--;
//			}
//		}
//		System.out.println("Final, DRX Delay Time (s): " + (sumDelay / round / 1000));
//		
//		Nsc = 4;
//		sumDelay = 0;
//		for(int i = 0 ; i < round; i++) {
//			TreeMap<Integer, Event> mapEvent = getMapEvent(bound, tti, Ts, Tpc, Tp, TBpc, Ns, Npc);	// Simulation
//			if(mapEvent.size() > 1) {
//				delay = getEvaluationDRXDelayTime(mapEvent, Ton, Ti, Tsc, Tlc, Nsc);				// Evaluation: DRX Delay Time
//				sumDelay = sumDelay + delay;
//			} else {
//				i--;
//			}
//		}
//		System.out.println("Final, DRX Delay Time (s): " + (sumDelay / round / 1000));
//		
//		Nsc = 16;
//		sumDelay = 0;
//		for(int i = 0 ; i < round; i++) {
//			TreeMap<Integer, Event> mapEvent = getMapEvent(bound, tti, Ts, Tpc, Tp, TBpc, Ns, Npc);	// Simulation
//			if(mapEvent.size() > 1) {
//				delay = getEvaluationDRXDelayTime(mapEvent, Ton, Ti, Tsc, Tlc, Nsc);				// Evaluation: DRX Delay Time
//				sumDelay = sumDelay + delay;
//			} else {
//				i--;
//			}
//		}
//		System.out.println("Final, DRX Delay Time (s): " + (sumDelay / round / 1000));

	}
	
	
	/*
	 * Simulation
	 */
	public static TreeMap<Integer, Event> getMapEvent(int bound, int tti,
			ExponentialDistribution Ts, ExponentialDistribution Tpc, ExponentialDistribution Tp, ExponentialDistribution TBpc,
			GeometricDistribution Ns, GeometricDistribution Npc) {
		TreeMap<Integer, Event> mapEvent = new TreeMap<Integer, Event>();
		// Generating function
		PriorityQueue<Event> queue = new PriorityQueue<Event>(10, new EventComp());
		queue.add(new Event(EventType.ACTIVE, 0));		// FirstActiveEvent
//		queue.add(new Event(EventType.BACKGROUND, 0));	// FirstBackgroundEvent
		EventType et;
		int ts;
		int tsOffset;
		int nextTs;
//		while(queue.size() > 0) {
		while(mapEvent.size() == 0 && queue.size()!=0) {
			Event event = queue.remove();
			et = event.eventType;
			ts = event.timeStamp;
			
			switch(et) {
			case ACTIVE:
				
				int aNs = Ns.sample();
				int aNpc = Npc.sample();
				for(int i = 0; i < aNs; i++) {
					for(int j = 0; j < aNpc; j++) {
						mapEvent.put(ts, event);
						tsOffset = (int) Tp.sample();
						if(j < aNpc - 1) {
							ts = ts + tti + tsOffset;
						} else {
							ts = ts + tti;
						}
					}
					tsOffset = (int) Tpc.sample();
					if(i < aNs - 1) {
						ts = ts + tsOffset;
					}
				}
				
				// Generating Next Event
				tsOffset = (int) Ts.sample();
				nextTs = ts + tsOffset;
				if(nextTs < bound) {
					queue.add(new Event(EventType.ACTIVE, nextTs));
				}
				
				break;
			case BACKGROUND:
				
				mapEvent.put(ts, event);
				
				// Generating Next Event
				tsOffset = (int) TBpc.sample();
				nextTs = ts + tsOffset;
				if(nextTs < bound) {
					queue.add(new Event(EventType.BACKGROUND, nextTs));
				}
				
				break;
			}
		}
		return mapEvent;
	}
	
	
	/*
	 * Evaluation: DRX Sleep Time
	 */
	public static double getEvaluationDRXSleepTime(TreeMap<Integer, Event> mapEvent, int Ton, int Ti, int Tsc, int Tlc, int Nsc) {
		int cnt = 0;
		int NscCnt = 0;
		int totalDrxSleepTime = 0; // (ms)
		int currentEventMs = 0;
		int nextEventMs = 0;
		Iterator<Entry<Integer, Event>> entries = mapEvent.entrySet().iterator();
		while (entries.hasNext()) {
			if(cnt == 0) {
				Entry<Integer, Event> entry = entries.next();
				nextEventMs = entry.getKey();
				cnt ++;
//				System.out.println("currentEventMs: " + currentEventMs + ", nextEventMs: " + nextEventMs);
			}
			if(Nsc != 0 && NscCnt == 0 && ((nextEventMs - currentEventMs) > (Ti + Ton))) { // short sleep: enter
     			NscCnt++;
				totalDrxSleepTime = totalDrxSleepTime + Tsc - Ton;
				currentEventMs = currentEventMs + Ti + Tsc;
//				System.out.println("short sleep, NscCnt = " + NscCnt + ", totalDrxSleepTime = " + totalDrxSleepTime);
				while(nextEventMs < currentEventMs && entries.hasNext()) {
					// TODO DROP when sleep
					Entry<Integer, Event> entry = entries.next();
					nextEventMs = entry.getKey();
					cnt ++;
//					System.out.println("currentEventMs: " + currentEventMs + ", nextEventMs: " + nextEventMs + " (PASS EVENT)");
				}
			} else if(Nsc != 0 && NscCnt != 0 && NscCnt < Nsc && ((nextEventMs - currentEventMs) > Ton)) { // short sleep: continue
				NscCnt++;
				totalDrxSleepTime = totalDrxSleepTime + Tsc - Ton;
				currentEventMs = currentEventMs + Tsc;
//				System.out.println("short sleep, NscCnt = " + NscCnt + ", totalDrxSleepTime = " + totalDrxSleepTime);
				while(nextEventMs < currentEventMs && entries.hasNext()) {
					// TODO DROP when sleep
					Entry<Integer, Event> entry = entries.next();
					nextEventMs = entry.getKey();
					cnt ++;
//					System.out.println("currentEventMs: " + currentEventMs + ", nextEventMs: " + nextEventMs + " (PASS EVENT)");
				}
			} else if(NscCnt >= Nsc && ((nextEventMs - currentEventMs) > Ton)) { // long sleep: enter
				NscCnt++;
				totalDrxSleepTime = totalDrxSleepTime + Tlc - Ton;
				currentEventMs = currentEventMs + Tlc;
//				System.out.println("long sleep, NscCnt = " + NscCnt + ", totalDrxSleepTime = " + totalDrxSleepTime);
				while(nextEventMs < currentEventMs && entries.hasNext()) {
					// TODO DROP when sleep
					Entry<Integer, Event> entry = entries.next();
					nextEventMs = entry.getKey();
					cnt ++;
//					System.out.println("currentEventMs: " + currentEventMs + ", nextEventMs: " + nextEventMs + " (PASS EVENT)");
				}
			} else {
				NscCnt = 0;
				currentEventMs = nextEventMs;
				if(entries.hasNext()) {
					Entry<Integer, Event> entry = entries.next();
					nextEventMs = entry.getKey();
					cnt ++;
				}
//				System.out.println("currentEventMs: " + currentEventMs + ", nextEventMs: " + nextEventMs + " (PROCESS EVENT)");
			}

		}
//		System.out.println("Total packets: " + cnt);
//		System.out.println("Sleep %: " + (double) totalDrxSleepTime / (double) nextEventMs);
		// percent
		double percent = (double) totalDrxSleepTime / (double) nextEventMs;
		if(percent > 1) {
			return 1;
		} else {
			return percent;
		}
	}
	
	
	/*
	 * Evaluation: DRX Delay Time
	 */
	public static double getEvaluationDRXDelayTime(TreeMap<Integer, Event> mapEvent, int Ton, int Ti, int Tsc, int Tlc, int Nsc) {
		int cnt = 0;
		int NscCnt = 0;
		int totalDrxSleepTime = 0; // (ms)
		int totalDrxDelayTime = 0; // (ms)
		int currentEventMs = 0;
		int nextEventMs = 0;
		Iterator<Entry<Integer, Event>> entries = mapEvent.entrySet().iterator();
		while (entries.hasNext()) {
			if(cnt == 0) {
				Entry<Integer, Event> entry = entries.next();
				nextEventMs = entry.getKey();
				cnt ++;
			}
			if(Nsc != 0 && NscCnt == 0 && ((nextEventMs - currentEventMs) > (Ti + Ton))) { // short sleep: enter
     			NscCnt++;
				totalDrxSleepTime = totalDrxSleepTime + Tsc - Ton;
				currentEventMs = currentEventMs + Ti + Tsc;
				while(nextEventMs < currentEventMs && entries.hasNext()) {
					Entry<Integer, Event> entry = entries.next();
					nextEventMs = entry.getKey();
					cnt ++;
					if(nextEventMs < currentEventMs) {
						totalDrxDelayTime = totalDrxDelayTime + (currentEventMs - nextEventMs);
					}
				}
			} else if(Nsc != 0 && NscCnt != 0 && NscCnt < Nsc && ((nextEventMs - currentEventMs) > Ton)) { // short sleep: continue
				NscCnt++;
				totalDrxSleepTime = totalDrxSleepTime + Tsc - Ton;
				currentEventMs = currentEventMs + Tsc;
				while(nextEventMs < currentEventMs && entries.hasNext()) {
					Entry<Integer, Event> entry = entries.next();
					nextEventMs = entry.getKey();
					cnt ++;
					if(nextEventMs < currentEventMs) {
						totalDrxDelayTime = totalDrxDelayTime + (currentEventMs - nextEventMs);
					}
				}
			} else if(NscCnt >= Nsc && ((nextEventMs - currentEventMs) > Ton)) { // long sleep: enter
				NscCnt++;
				totalDrxSleepTime = totalDrxSleepTime + Tlc - Ton;
				currentEventMs = currentEventMs + Tlc;
				while(nextEventMs < currentEventMs && entries.hasNext()) {
					Entry<Integer, Event> entry = entries.next();
					nextEventMs = entry.getKey();
					cnt ++;
					if(nextEventMs < currentEventMs) {
						totalDrxDelayTime = totalDrxDelayTime + (currentEventMs - nextEventMs);
					}
				}
			} else {
				NscCnt = 0;
				currentEventMs = nextEventMs;
				if(entries.hasNext()) {
					Entry<Integer, Event> entry = entries.next();
					nextEventMs = entry.getKey();
					cnt ++;
				}
			}

		}
//		System.out.println("Total packets: " + cnt);
//		System.out.println("Delay (ms): " + (double) totalDrxDelayTime / (double) cnt);
		// delay
		double delay = (double) totalDrxDelayTime / (double) cnt;
		return delay;
	}

}