

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {
    @SuppressWarnings("CallToPrintStackTrace")
    public static void initializeDatabase() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create gyms table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS gyms (
                    gymID INT AUTO_INCREMENT PRIMARY KEY,
                    address VARCHAR(255),
                    cost DECIMAL(10, 2),
                    number_of_members INT,
                    rating DECIMAL(3, 2)
                )
            """);
            
            // Create memberships table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS memberships (
                    membershipID INT AUTO_INCREMENT PRIMARY KEY,
                    gymID INT,
                    memberName VARCHAR(255),
                    membershipType VARCHAR(50),
                    startDate DATE,
                    endDate DATE,
                    FOREIGN KEY (gymID) REFERENCES gyms(gymID)
                )
            """);
            
            // Create trainers table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS trainers (
                    trainerID INT AUTO_INCREMENT PRIMARY KEY,
                    gymID INT,
                    trainerName VARCHAR(255),
                    specialization VARCHAR(100),
                    rating DECIMAL(3, 2),
                    FOREIGN KEY (gymID) REFERENCES gyms(gymID)
                )
            """);
            
            // Create classes table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS classes (
                    classID INT AUTO_INCREMENT PRIMARY KEY,
                    gymID INT,
                    className VARCHAR(255),
                    schedule VARCHAR(255),
                    trainerID INT,
                    FOREIGN KEY (gymID) REFERENCES gyms(gymID),
                    FOREIGN KEY (trainerID) REFERENCES trainers(trainerID)
                )
            """);
            
            // Create events table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS events (
                    eventID INT AUTO_INCREMENT PRIMARY KEY,
                    gymID INT,
                    eventName VARCHAR(255),
                    eventDate DATE,
                    description TEXT,
                    FOREIGN KEY (gymID) REFERENCES gyms(gymID) ON DELETE CASCADE
                )
            """);
            
            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace(); // Added stack trace for better error debugging
        }
    }
} 