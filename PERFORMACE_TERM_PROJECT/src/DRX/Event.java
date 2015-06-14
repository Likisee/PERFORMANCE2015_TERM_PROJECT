package DRX;

public class Event {

	public int eventType;
	public int timeStamp;
	
	public Event() 
	{ 
		this.eventType = 0; 
	}

	public Event(int eventType, int timeStamp) 
	{
		this.eventType = eventType;
		this.timeStamp = timeStamp;
	}
}
