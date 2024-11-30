import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class GymService {
    public List<Gym> getAllGyms() throws SQLException {
        List<Gym> gyms = new ArrayList<>();
        String query = "SELECT * FROM gyms";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                gyms.add(new Gym(
                    rs.getInt("gym_id"),
                    rs.getString("address"),
                    rs.getDouble("cost"),
                    rs.getInt("members"),
                    rs.getDouble("rating")
                ));
            }
        }
        return gyms;
    }

    public void addGym(Gym gym) throws SQLException {
        String query = "INSERT INTO gyms (gym_id, address, cost, members, rating) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, gym.getGymID());
            pstmt.setString(2, gym.getAddress());
            pstmt.setDouble(3, gym.getCost());
            pstmt.setInt(4, gym.getNumberOfMembers());
            pstmt.setDouble(5, gym.getRating());
            pstmt.executeUpdate();
        }
    }

    public void updateGym(Gym gym) throws SQLException {
        String query = "UPDATE gyms SET address=?, cost=?, members=?, rating=? WHERE gym_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, gym.getAddress());
            pstmt.setDouble(2, gym.getCost());
            pstmt.setInt(3, gym.getNumberOfMembers());
            pstmt.setDouble(4, gym.getRating());
            pstmt.setInt(5, gym.getGymID());
            pstmt.executeUpdate();
        }
    }

    public void deleteGym(int gymId) throws SQLException {
        String query = "DELETE FROM gyms WHERE gym_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, gymId);
            pstmt.executeUpdate();
        }
    }

    public List<Gym> searchGyms(String column, String value) throws SQLException {
        // Validate column name to prevent SQL injection
        List<String> validColumns = List.of("gymid", "address", "cost", "members", "rating");
        if (!validColumns.contains(column.toLowerCase())) {
            throw new IllegalArgumentException("Invalid column name");
        }
        
        List<Gym> gyms = new ArrayList<>();
        String query = "SELECT * FROM gyms WHERE " + column + " LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + value + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                gyms.add(new Gym(
                    rs.getInt("gym_id"),
                    rs.getString("address"),
                    rs.getDouble("cost"),
                    rs.getInt("members"),
                    rs.getDouble("rating")
                ));
            }
        }
        return gyms;
    }

	public Gym getGymById(int gymID) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getGymById'");
	}

	public void deleteGym(Point gymID) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'deleteGym'");
	}
}