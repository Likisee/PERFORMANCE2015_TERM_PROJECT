package MMcc;

import java.util.PriorityQueue;

import org.apache.commons.math3.distribution.GammaDistribution;

public class Test {

	public static void main(String[] args) {
		
		
		PriorityQueue<Event> queue = new PriorityQueue<Event>(10, new EventComp());
		queue.add(new Event(1, 10));
		queue.add(new Event(1, 5));
		queue.add(new Event(1, 55));
		
		while(!queue.isEmpty()) {
			Event event = queue.remove();
			System.out.println(event.timeStamp);
		}
				
		
		GammaDistribution TEST = new GammaDistribution(1, 10);
		System.out.println(TEST.sample());
		System.out.println(TEST.sample());
		System.out.println(TEST.sample());
		System.out.println(TEST.sample());
		System.out.println(TEST.sample());
		System.out.println(TEST.sample());
		System.out.println(TEST.sample());
		System.out.println(TEST.sample());
		System.out.println(TEST.sample());
		System.out.println(TEST.sample());
		System.out.println(TEST.getAlpha() * TEST.getBeta()); // MEAN
		
	}

}
