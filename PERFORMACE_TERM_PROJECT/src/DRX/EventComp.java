package DRX;

import java.util.Comparator;

public class EventComp implements Comparator<Event> {
	
	@Override
	public int compare(Event arg0, Event arg1) {
		if(arg0.timeStamp < arg1.timeStamp) {
			return -1;
		} else if(arg0.timeStamp > arg1.timeStamp) {
			return 1;
		} else {
			return 0;	
		}
	}
	
}
