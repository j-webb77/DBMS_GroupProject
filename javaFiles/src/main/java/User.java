public class User {
    private final int userId;
    private final String username;
    private final String role;
    private final Integer managedGymId;

    public User(int userId, String username, String role, Integer managedGymId) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.managedGymId = managedGymId;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public Integer getManagedGymId() { return managedGymId; }

    public boolean canManageGym() {
        return role != null && (role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("MANAGER"));
    }

    public boolean canManageMemberships() {
        return role != null && (role.equalsIgnoreCase("ADMIN") || 
                              role.equalsIgnoreCase("MANAGER"));
    }
} 