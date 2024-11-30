import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainerService extends DatabaseService {
    public List<Trainer> getTrainersByGym(int gymId) throws SQLException {
        List<Trainer> trainers = new ArrayList<>();
        String sql = "SELECT * FROM trainers WHERE gym_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, gymId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                trainers.add(new Trainer(
                    rs.getInt("trainer_id"),
                    rs.getInt("gym_id"),
                    rs.getString("name"),
                    rs.getString("specialization"),
                    rs.getDouble("hourly_rate"),
                    rs.getString("email")
                ));
            }
        }
        return trainers;
    }

    public void addTrainer(Trainer trainer) throws SQLException {
        String sql = "INSERT INTO trainers (gym_id, name, specialization, hourly_rate, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, trainer.getGymId());
            stmt.setString(2, trainer.getName());
            stmt.setString(3, trainer.getSpecialization());
            stmt.setDouble(4, trainer.getHourlyRate());
            stmt.setString(5, trainer.getEmail());
            stmt.executeUpdate();
        }
    }

    public void updateTrainer(Trainer trainer) throws SQLException {
        String sql = "UPDATE trainers SET name = ?, specialization = ?, hourly_rate = ?, email = ? WHERE trainer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, trainer.getName());
            stmt.setString(2, trainer.getSpecialization());
            stmt.setDouble(3, trainer.getHourlyRate());
            stmt.setString(4, trainer.getEmail());
            stmt.setInt(5, trainer.getTrainerId());
            stmt.executeUpdate();
        }
    }

    public void deleteTrainer(int trainerId) throws SQLException {
        String sql = "DELETE FROM trainers WHERE trainer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, trainerId);
            stmt.executeUpdate();
        }
    }
} 