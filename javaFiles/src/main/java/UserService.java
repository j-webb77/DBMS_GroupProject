import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class UserService {
    @SuppressWarnings("unused")
	private final Connection dbConnection;

    public UserService() throws SQLException {
        this.dbConnection = DatabaseConnection.getConnection();
    }

    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("role"),
                    rs.getObject("managed_gym_id") != null ? rs.getInt("managed_gym_id") : null
                );
            }
        }
        return null;
    }

    private String hashPassword(String password) {
        // In a real application, use a proper password hashing algorithm
        return password;
    }

	public boolean validateLogin(String username, String password) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'validateLogin'");
	}

	public void createUser(String username, String password, String role, Integer managedGymId) throws SQLException {
        String sql = "INSERT INTO users (username, password, role, managed_gym_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));
            stmt.setString(3, role);
            if (managedGymId != null) {
                stmt.setInt(4, managedGymId);
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            stmt.executeUpdate();
        }
	}
} 