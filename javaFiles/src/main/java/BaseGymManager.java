import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Frame;

public abstract class BaseGymManager extends JDialog {
    protected User currentUser;
    protected Gym currentGym;
    protected JButton addButton;
    protected JButton updateButton;
    protected JButton deleteButton;
    protected JButton closeButton;
    protected PermissionManager permissionManager;

    public BaseGymManager(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        if (owner instanceof GymManagerGUI gymManagerGUI) {
            this.currentUser = gymManagerGUI.getCurrentUser();
            this.currentGym = gymManagerGUI.getCurrentGym();
            this.permissionManager = new PermissionManager(currentUser, currentGym);
        }
        
        // Initialize the buttons
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        closeButton = new JButton("Close");
    }

    protected void initializePermissionManager(Gym gym) {
        this.currentGym = gym;
        this.permissionManager = new PermissionManager(currentUser, currentGym);
    }

    protected void setButtonPermissions() {
        if (permissionManager != null) {
            boolean canEdit = permissionManager.canEdit();
            addButton.setEnabled(canEdit);
            updateButton.setEnabled(canEdit);
            deleteButton.setEnabled(canEdit);
        }
    }

    public abstract void validatePermissions();

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }
} 