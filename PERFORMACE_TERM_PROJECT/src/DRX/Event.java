package DRX;

public class Event {

	enum EventType {
	    ACTIVE, BACKGROUND
	} 
	
	public EventType eventType;
	public int timeStamp;
	
	public Event() 
	{ 
		this.eventType = EventType.ACTIVE; 
	}

	public Event(EventType eventType, int timeStamp) 
	{
		this.eventType = eventType;
		this.timeStamp = timeStamp;
	}
}
