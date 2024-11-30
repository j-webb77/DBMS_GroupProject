import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembershipService {
    private static final String INSERT_MEMBERSHIP = 
        "INSERT INTO memberships (member_name, membership_type, start_date, end_date, gym_id, price, status) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_MEMBERSHIP = 
        "UPDATE memberships SET member_name=?, membership_type=?, start_date=?, end_date=?, " +
        "price=?, status=? WHERE membership_id=?";
    
    private static final String DELETE_MEMBERSHIP = 
        "DELETE FROM memberships WHERE membership_id=?";
    
    private static final String SELECT_MEMBERSHIPS_BY_GYM = 
        "SELECT * FROM memberships WHERE gym_id=?";

    public void addMembership(Membership membership, int gymId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_MEMBERSHIP)) {
            
            stmt.setString(1, membership.getMemberName());
            stmt.setString(2, membership.getMembershipType());
            stmt.setDate(3, Date.valueOf(membership.getStartDate()));
            stmt.setDate(4, Date.valueOf(membership.getEndDate()));
            stmt.setInt(5, gymId);
            stmt.setDouble(6, membership.getPrice());
            stmt.setString(7, membership.getStatus());
            
            stmt.executeUpdate();
        }
    }

    public void updateMembership(Membership membership) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_MEMBERSHIP)) {
            
            stmt.setString(1, membership.getMemberName());
            stmt.setString(2, membership.getMembershipType());
            stmt.setDate(3, Date.valueOf(membership.getStartDate()));
            stmt.setDate(4, Date.valueOf(membership.getEndDate()));
            stmt.setDouble(5, membership.getPrice());
            stmt.setString(6, membership.getStatus());
            stmt.setInt(7, membership.getMembershipId());
            
            stmt.executeUpdate();
        }
    }

    public void deleteMembership(int membershipId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_MEMBERSHIP)) {
            
            stmt.setInt(1, membershipId);
            stmt.executeUpdate();
        }
    }

    public List<Membership> getMembershipsByGym(int gymId) throws SQLException {
        List<Membership> memberships = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_MEMBERSHIPS_BY_GYM)) {
            
            stmt.setInt(1, gymId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Membership membership = new Membership(
                    rs.getInt("membership_id"),
                    rs.getString("member_name"),
                    rs.getString("membership_type"),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("end_date").toLocalDate(),
                    rs.getInt("gym_id"),
                    rs.getDouble("price"),
                    rs.getString("status")
                );
                membership.setEndDate(rs.getDate("end_date").toLocalDate());
                membership.setPrice(rs.getDouble("price"));
                membership.setStatus(rs.getString("status"));
                memberships.add(membership);
            }
        }
        
        return memberships;
    }
} 