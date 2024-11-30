import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GymClassService {
    public List<GymClass> getClassesForGym(int gymId) throws SQLException {
        List<GymClass> classes = new ArrayList<>();
        String query = "SELECT * FROM GymClasses WHERE gym_id = ?";
        
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query)) {
            stmt.setInt(1, gymId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                GymClass gymClass = new GymClass(
                    rs.getInt("class_id"),
                    rs.getString("class_name"),
                    rs.getString("instructor"),
                    rs.getString("schedule"),
                    rs.getInt("capacity"),
                    rs.getDouble("price")
                );
                classes.add(gymClass);
            }
        }
        return classes;
    }

    public int addClass(GymClass gymClass, int gymId) throws SQLException {
        String sql = "INSERT INTO gymclasses (class_id, gym_id, class_name, instructor, schedule, capacity, price) " +
                    "SELECT COALESCE(MAX(class_id), 0) + 1, ?, ?, ?, ?, ?, ? " +
                    "FROM gymclasses WHERE gym_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, gymId);
            stmt.setString(2, gymClass.getClassName());
            stmt.setString(3, gymClass.getInstructor());
            stmt.setString(4, gymClass.getSchedule());
            stmt.setInt(5, gymClass.getCapacity());
            stmt.setDouble(6, gymClass.getPrice());
            stmt.setInt(7, gymId);
            
            stmt.executeUpdate();
            
            // Get the generated ID
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public void updateClass(GymClass gymClass) throws SQLException {
        String query = "UPDATE gymclasses SET class_name = ?, instructor = ?, schedule = ?, capacity = ?, price = ? WHERE class_id = ?";
        
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, gymClass.getClassName());
            stmt.setString(2, gymClass.getInstructor());
            stmt.setString(3, gymClass.getSchedule());
            stmt.setInt(4, gymClass.getCapacity());
            stmt.setDouble(5, gymClass.getPrice());
            stmt.setInt(6, gymClass.getClassId());
            stmt.executeUpdate();
        }
    }

    public void deleteClass(int classId) throws SQLException {
        String query = "DELETE FROM gymclasses WHERE class_id = ?";
        
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query)) {
            stmt.setInt(1, classId);
            stmt.executeUpdate();
        }
    }
} 