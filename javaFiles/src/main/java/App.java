

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        try {
            // Initialize database
            DatabaseInitializer.initializeDatabase();
            
            // Test database connection
            try (Connection conn = DatabaseConnection.getConnection()) {
                if (conn != null) {
                    LOGGER.info("Database connection successful!");
                }
            }
            
            // Initialize services
            GymService gymService = new GymService();
            
            // Add a test gym
            Gym newGym = new Gym(0, "123 Fitness Street", 49.99, 100, 4.5);
            gymService.addGym(newGym);
            LOGGER.log(Level.INFO, "Successfully added new gym: {0}", newGym);
            
            // List all gyms
            LOGGER.info("All Gyms:");
            gymService.getAllGyms().forEach(gym -> LOGGER.info(gym.toString()));
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
            System.exit(1);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Application error", e);
            System.exit(1);
        }
    }
}

