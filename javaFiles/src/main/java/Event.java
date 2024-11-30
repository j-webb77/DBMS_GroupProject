import java.sql.Date;
import java.time.LocalDateTime;

public class Event {
    private final int eventId;
    private String name;
    private LocalDateTime dateTime;
    private String description;
    private int gymId;
    private int capacity;
    private String instructor;

    public Event(int eventId, String name, LocalDateTime dateTime, String description, 
                 int gymId, int capacity, String instructor) {
        this.eventId = eventId;
        this.name = name;
        this.dateTime = dateTime;
        this.description = description;
        this.gymId = gymId;
        this.capacity = capacity;
        this.instructor = instructor;
    }

    // Getters and setters
    public int getEventId() { return eventId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getGymId() { return gymId; }
    public void setGymId(int gymId) { this.gymId = gymId; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public String getEventName() {
        return getName();
    }

    public LocalDateTime getEventDate() {
        return getDateTime();
    }

	public void setEventId(int int1) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setEventId'");
	}

	public void setEventName(String string) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setEventName'");
	}

	public void setEventDate(Date date) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setEventDate'");
	}
} 