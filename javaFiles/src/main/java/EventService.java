import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.Timestamp;

public class EventService {
    private final Connection dbConnection;

    public EventService() throws SQLException {
        this.dbConnection = DatabaseConnection.getConnection();
    }

    public List<Event> getEventsForGym(int gymId) throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE gym_id = ?";
        
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setInt(1, gymId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                events.add(createEventFromResultSet(rs));
            }
        }
        return events;
    }

    // Add other CRUD operations
    // ... (implementation of addEvent, updateEvent, deleteEvent)
    private Event createEventFromResultSet(ResultSet rs) throws SQLException {
        Event event = new Event(
            rs.getInt("event_id"),
            rs.getString("event_name"),
            rs.getTimestamp("event_date").toLocalDateTime(),
            rs.getString("description"),
            rs.getInt("gym_id"),
            0, // Default capacity 
            "" // Default status
        );
        event.setDescription(rs.getString("description"));
        return event;
    }

    public void addEvent(Event event) throws SQLException {
        String sql = "INSERT INTO events (gym_id, event_name, event_date, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setInt(1, event.getGymId());
            stmt.setString(2, event.getName());
            stmt.setTimestamp(3, Timestamp.valueOf(event.getDateTime()));
            stmt.setString(4, event.getDescription());
            stmt.executeUpdate();
        }
    }

    public void updateEvent(Event event) throws SQLException {
        String sql = "UPDATE events SET event_name = ?, event_date = ?, description = ? WHERE event_id = ? AND gym_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setString(1, event.getName());
            stmt.setTimestamp(2, Timestamp.valueOf(event.getDateTime()));
            stmt.setString(3, event.getDescription());
            stmt.setInt(4, event.getEventId());
            stmt.setInt(5, event.getGymId());
            stmt.executeUpdate();
        }
    }

    public void deleteEvent(int eventId, int gymId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteEvent'");
    }
}