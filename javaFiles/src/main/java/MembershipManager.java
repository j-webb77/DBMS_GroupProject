import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;

public class MembershipManager extends BaseGymManager {
    private JTextField memberNameField;
    private JTextField membershipTypeField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField priceField;
    private JTextField statusField;
    private final MembershipService membershipService;
    private final MembershipTableModel tableModel;
    private JTable membershipTable;
    private final Gym gym;

    public MembershipManager(Frame owner, Gym gym, User currentUser) throws SQLException {
        super(owner, "Manage Memberships", true);
        this.gym = gym;
        this.currentGym = gym;
        this.membershipService = new MembershipService();
        this.tableModel = new MembershipTableModel();
        
        initializeComponents();
        setupTableSelection();
        loadMemberships();
        this.currentUser = currentUser;
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(600, 400);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        memberNameField = new JTextField(20);
        membershipTypeField = new JTextField(20);
        startDateField = new JTextField(20);
        endDateField = new JTextField(20);
        priceField = new JTextField(20);
        statusField = new JTextField(20);

        inputPanel.add(new JLabel("Member Name:"));
        inputPanel.add(memberNameField);
        inputPanel.add(new JLabel("Membership Type:"));
        inputPanel.add(membershipTypeField);
        inputPanel.add(new JLabel("Start Date:"));
        inputPanel.add(startDateField);
        inputPanel.add(new JLabel("End Date:"));
        inputPanel.add(endDateField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Status:"));
        inputPanel.add(statusField);

        // Table
        membershipTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(membershipTable);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        closeButton = new JButton("Close");

        addButton.addActionListener(e -> addMembership());
        updateButton.addActionListener(e -> updateMembership());
        deleteButton.addActionListener(e -> deleteMembership());
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        // Add components
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupTableSelection() {
        membershipTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = membershipTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Membership selected = tableModel.getMembershipAt(selectedRow);
                    memberNameField.setText(selected.getMemberName());
                    membershipTypeField.setText(selected.getMembershipType());
                    startDateField.setText(selected.getStartDate().toString());
                    endDateField.setText(selected.getEndDate().toString());
                    priceField.setText(String.valueOf(selected.getPrice()));
                    statusField.setText(selected.getStatus());
                }
            }
        });
    }

    private void loadMemberships() {
        try {
            tableModel.setMemberships(membershipService.getMembershipsByGym(gym.getGymId()));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading memberships: " + e.getMessage());
        }
    }

    
    @Override
    public void validatePermissions() {
        boolean canEdit = checkPermissions();
        
        // Set button states
        addButton.setEnabled(canEdit);
        updateButton.setEnabled(canEdit);
        deleteButton.setEnabled(canEdit);

        // Make fields read-only for users without edit permissions
        memberNameField.setEditable(canEdit);
        membershipTypeField.setEditable(canEdit);
        startDateField.setEditable(canEdit);
        endDateField.setEditable(canEdit);
        priceField.setEditable(canEdit);
        statusField.setEditable(canEdit);
    }

                private boolean checkPermissions() {
                String userRole = currentUser.getRole();
                if (userRole.equals("ADMIN")) {
                    return true;
                } else if (userRole.equals("MNGR")) {
                    return currentUser.getManagedGymId() != null && 
                           currentUser.getManagedGymId().equals(gym.getGymId());
                }
                return false;
            }



    private void addMembership() {
        try {
            validateInputs();
            Membership membership = new Membership(
                memberNameField.getText(),
                membershipTypeField.getText(),
                LocalDate.parse(startDateField.getText()),
                LocalDate.parse(endDateField.getText()),
                Double.parseDouble(priceField.getText()),
                statusField.getText()
            );
            membershipService.addMembership(membership, gym.getGymId());
            loadMemberships();
            clearFields();
            JOptionPane.showMessageDialog(this, "Membership added successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding membership: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid input: Please check date format (YYYY-MM-DD) and price");
        }
    }

    private void updateMembership() {
        int selectedRow = membershipTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a membership to update");
            return;
        }

        try {
            validateInputs();
            Membership membership = tableModel.getMembershipAt(selectedRow).copy();
            membership.setMemberName(memberNameField.getText());
            membership.setMembershipType(membershipTypeField.getText());
            membership.setStartDate(LocalDate.parse(startDateField.getText()));
            membership.setEndDate(LocalDate.parse(endDateField.getText()));
            membership.setPrice(Double.parseDouble(priceField.getText()));
            membership.setStatus(statusField.getText());
            membershipService.updateMembership(membership);
            loadMemberships();
            clearFields();
            JOptionPane.showMessageDialog(this, "Membership updated successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating membership: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid input: Please check date format (YYYY-MM-DD) and price");
        }
    }

    private void deleteMembership() {
        int selectedRow = membershipTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a membership to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this membership?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int membershipId = tableModel.getMembershipAt(selectedRow).getMembershipId();
                membershipService.deleteMembership(membershipId);
                loadMemberships();
                clearFields();
                JOptionPane.showMessageDialog(this, "Membership deleted successfully!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting membership: " + e.getMessage());
            }
        }
    }

    private void validateInputs() {
        if (memberNameField.getText().trim().isEmpty()) 
            throw new IllegalArgumentException("Member name cannot be empty");
        if (membershipTypeField.getText().trim().isEmpty()) 
            throw new IllegalArgumentException("Membership type cannot be empty");
        if (startDateField.getText().trim().isEmpty() || endDateField.getText().trim().isEmpty()) 
            throw new IllegalArgumentException("Dates cannot be empty");
        try {
            Double.valueOf(priceField.getText());
            // Validate date format
            LocalDate.parse(startDateField.getText());
            LocalDate.parse(endDateField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid price value");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
        }
    }

    private void clearFields() {
        memberNameField.setText("");
        membershipTypeField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        priceField.setText("");
        statusField.setText("");
    }
} 