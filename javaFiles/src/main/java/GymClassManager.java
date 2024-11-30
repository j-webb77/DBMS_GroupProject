import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.List;

public class GymClassManager extends JDialog {
    private final Gym gym;
    private JTable classTable;
    private JTextField classNameField;
    private JTextField instructorField;
    private JTextField scheduleField;
    private JTextField capacityField;
    private JTextField priceField;
    private DefaultTableModel tableModel;
    private final GymClassService gymClassService;
    private List<GymClass> currentClasses;
    private JButton addButton, updateButton, deleteButton;
    private final User currentUser;
    
    public GymClassManager(Frame owner, Gym gym, User currentUser) {
        super(owner, "Manage Classes", true);
        this.gym = gym;
        this.currentUser = currentUser;
        this.gymClassService = new GymClassService();
        initializeComponents();
        loadClasses();
        validatePermissions();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(600, 400);

        // Create input panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        classNameField = new JTextField(20);
        instructorField = new JTextField(20);
        scheduleField = new JTextField(20);
        capacityField = new JTextField(20);
        priceField = new JTextField(20);

        inputPanel.add(new JLabel("Class Name:"));
        inputPanel.add(classNameField);
        inputPanel.add(new JLabel("Instructor:"));
        inputPanel.add(instructorField);
        inputPanel.add(new JLabel("Schedule:"));
        inputPanel.add(scheduleField);
        inputPanel.add(new JLabel("Capacity:"));
        inputPanel.add(capacityField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        
        addButton.addActionListener(e -> addClass());
        updateButton.addActionListener(e -> updateClass());
        deleteButton.addActionListener(e -> deleteClass());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Create table with columns
        String[] columns = {"Class ID", "Class Name", "Instructor", "Schedule", "Capacity", "Price"};
        tableModel = new DefaultTableModel(columns, 0);
        classTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(classTable);

        // Add selection listener
        classTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && classTable.getSelectedRow() != -1) {
                int row = classTable.getSelectedRow();
                classNameField.setText((String) tableModel.getValueAt(row, 1));
                instructorField.setText((String) tableModel.getValueAt(row, 2));
                scheduleField.setText((String) tableModel.getValueAt(row, 3));
                capacityField.setText(tableModel.getValueAt(row, 4).toString());
                priceField.setText(tableModel.getValueAt(row, 5).toString());
            }
        });

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadClasses() {
        try {
            tableModel.setRowCount(0);
            currentClasses = gymClassService.getClassesForGym(gym.getGymID());
            for (GymClass gymClass : currentClasses) {
                Object[] row = {
                    gymClass.getClassId(),
                    gymClass.getClassName(),
                    gymClass.getInstructor(),
                    gymClass.getSchedule(),
                    gymClass.getCapacity(),
                    gymClass.getPrice()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading classes: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addClass() {
        try {
            validateInputs();
            GymClass newClass = createClassFromInputs();
            gymClassService.addClass(newClass, gym.getGymID());
            loadClasses();
            clearFields();
            JOptionPane.showMessageDialog(this, "Class added successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error adding class: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateClass() {
        int selectedRow = classTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a class to update");
            return;
        }

        try {
            validateInputs();
            GymClass updatedClass = createClassFromInputs();
            updatedClass.setClassId(currentClasses.get(selectedRow).getClassId());
            gymClassService.updateClass(updatedClass);
            loadClasses();
            clearFields();
            JOptionPane.showMessageDialog(this, "Class updated successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error updating class: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteClass() {
        int selectedRow = classTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a class to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this class?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                gymClassService.deleteClass(currentClasses.get(selectedRow).getClassId());
                loadClasses();
                clearFields();
                JOptionPane.showMessageDialog(this, "Class deleted successfully!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error deleting class: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void validateInputs() {
        if (classNameField.getText().trim().isEmpty()) 
            throw new IllegalArgumentException("Class name cannot be empty");
        if (instructorField.getText().trim().isEmpty()) 
            throw new IllegalArgumentException("Instructor cannot be empty");
        if (scheduleField.getText().trim().isEmpty()) 
            throw new IllegalArgumentException("Schedule cannot be empty");
        try {
            Integer.valueOf(capacityField.getText());
            Double.valueOf(priceField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid capacity or price value");
        }
    }

    private GymClass createClassFromInputs() {
        return new GymClass(
            classNameField.getText().trim(),
            instructorField.getText().trim(),
            scheduleField.getText().trim(),
            Integer.parseInt(capacityField.getText()),
            Double.parseDouble(priceField.getText())
        );
    }

    private void clearFields() {
        classNameField.setText("");
        instructorField.setText("");
        scheduleField.setText("");
        capacityField.setText("");
        priceField.setText("");
        classTable.clearSelection();
    }

    private void validatePermissions() {
        boolean isAdmin = currentUser.getRole().equals("ADMIN");
        boolean isManager = currentUser.getRole().equals("MNGR") && 
            currentUser.getManagedGymId() != null && 
            currentUser.getManagedGymId().equals(this.gym.getGymID());
        
        boolean canEdit = isAdmin || isManager;

        // Set button states
        addButton.setEnabled(canEdit);
        updateButton.setEnabled(canEdit);
        deleteButton.setEnabled(canEdit);

        // Make fields read-only for users without edit permissions
        classNameField.setEditable(canEdit);
        instructorField.setEditable(canEdit);
        scheduleField.setEditable(canEdit);
        capacityField.setEditable(canEdit);
        priceField.setEditable(canEdit);
    }
} 