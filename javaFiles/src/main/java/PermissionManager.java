import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;

public class PermissionManager {
    private final User currentUser;
    private final Gym currentGym;

    public PermissionManager(User currentUser, Gym currentGym) {
        this.currentUser = currentUser;
        this.currentGym = currentGym;
    }

    public boolean canEdit() {
        if (currentUser == null || currentGym == null) {
            return false;
        }

        boolean isAdmin = currentUser.getRole().equals("ADMIN");
        boolean isManager = currentUser.getRole().equals("MNGR");
        
        if (isManager) {
            return currentUser.getManagedGymId() != null && 
                   currentUser.getManagedGymId().equals(currentGym.getGymId());
        }
        
        return isAdmin;
    }

    public String getWhereClause() {
        if (currentUser.getRole().equals("MNGR")) {
            return " WHERE gym_id = " + currentUser.getManagedGymId();
        }
        return ""; // Admin and Users get no WHERE clause (see all)
    }

    public void setGymIdInStatement(PreparedStatement stmt, int paramIndex) throws SQLException {
        if (currentUser.getRole().equals("MNGR")) {
            stmt.setInt(paramIndex, currentUser.getManagedGymId());
        } else if (currentUser.getRole().equals("ADMIN")) {
            stmt.setInt(paramIndex, currentGym.getGymId());
        }
    }

    public void validateUIPermissions(JButton... buttons) {
        for (JButton button : buttons) {
            if (!canEdit()) {
                button.setEnabled(false);
            }
        }
    }
} 