package MMcc;

public class Event {

	public int eventType;
	public double timeStamp;
	
	public Event() 
	{ 
		this.eventType = 0; 
	}

	public Event(int eventType, double timeStamp) 
	{
		this.eventType = eventType;
		this.timeStamp = timeStamp;
	}
}
