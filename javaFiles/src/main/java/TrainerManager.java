import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public final class TrainerManager extends BaseGymManager {
    private JTextField trainerNameField;
    private JTextField specialtyField;
    private JTextField hourlyRateField;
    private JTextField emailField;
    private final TrainerService trainerService;
    private final TrainerTableModel tableModel;
    private JTable trainerTable;
    private final Gym gym;

    public TrainerManager(Frame owner, Gym gym) throws SQLException {
        super(owner, "Manage Trainers", true);
        this.gym = gym;
        this.currentGym = gym;
        this.trainerService = new TrainerService();
        this.tableModel = new TrainerTableModel();
        
        initializeComponents();
        setupTableSelection();
        loadTrainers();
        validatePermissions();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(600, 400);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        trainerNameField = new JTextField(20);
        specialtyField = new JTextField(20);
        hourlyRateField = new JTextField(20);
        emailField = new JTextField(20);

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(trainerNameField);
        inputPanel.add(new JLabel("Specialization:"));
        inputPanel.add(specialtyField);
        inputPanel.add(new JLabel("Hourly Rate:"));
        inputPanel.add(hourlyRateField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);

        // Table
        trainerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(trainerTable);

        // Use buttons from base class
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        closeButton = new JButton("Close");

        closeButton.addActionListener(e -> dispose());

        // Button Panel
        JPanel buttonPanel = new JPanel();
        addButton.addActionListener(e -> addTrainer());
        updateButton.addActionListener(e -> updateTrainer());
        deleteButton.addActionListener(e -> deleteTrainer());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        // Add components
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadTrainers() {
        try {
            tableModel.setTrainers(trainerService.getTrainersByGym(gym.getGymId()));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading trainers: " + e.getMessage());
        }
    }

    private void addTrainer() {
        try {
            Trainer trainer = new Trainer(
                0,
                gym.getGymId(),
                trainerNameField.getText(),
                specialtyField.getText(),
                Double.parseDouble(hourlyRateField.getText()),
                emailField.getText()
            );
            trainerService.addTrainer(trainer);
            loadTrainers();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding trainer: " + e.getMessage());
        }
    }

    private void updateTrainer() {
        int selectedRow = trainerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a trainer to update");
            return;
        }

        try {
            Trainer trainer = new Trainer(
                tableModel.getTrainerAt(selectedRow).getTrainerId(),
                gym.getGymId(),
                trainerNameField.getText(),
                specialtyField.getText(),
                Double.parseDouble(hourlyRateField.getText()),
                emailField.getText()
            );
            
            trainerService.updateTrainer(trainer);
            loadTrainers();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating trainer: " + e.getMessage());
        }
    }

    private void deleteTrainer() {
        int selectedRow = trainerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a trainer to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this trainer?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int trainerId = tableModel.getTrainerAt(selectedRow).getTrainerId();
                trainerService.deleteTrainer(trainerId);
                loadTrainers();
                clearFields();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting trainer: " + e.getMessage());
            }
        }
    }

    private void clearFields() {
        trainerNameField.setText("");
        specialtyField.setText("");
        hourlyRateField.setText("");
        emailField.setText("");
    }

    // Add selection listener to populate fields when a row is selected
    private void setupTableSelection() {
        trainerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = trainerTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Trainer selected = tableModel.getTrainerAt(selectedRow);
                    trainerNameField.setText(selected.getName());
                    specialtyField.setText(selected.getSpecialization());
                    hourlyRateField.setText(String.valueOf(selected.getHourlyRate()));
                    emailField.setText(selected.getEmail());
                }
            }
        });
    }

    public JTextField getSpecializationField() {
        return specialtyField;
    }

    public void setSpecializationField(JTextField specializationField) {
        this.specialtyField = specializationField;
    }

    @Override
    public void validatePermissions() {
        boolean canEdit = checkPermissions();
                
                // Set button states
                addButton.setEnabled(canEdit);
                updateButton.setEnabled(canEdit);
                deleteButton.setEnabled(canEdit);
        
                // Make fields read-only for users without edit permissions
                trainerNameField.setEditable(canEdit);
                specialtyField.setEditable(canEdit);
                hourlyRateField.setEditable(canEdit);
                emailField.setEditable(canEdit);
                // ... set other fields
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
} 