import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;



public class GymManagerGUI extends JFrame {
    private final User currentUser;
    private Gym selectedGym;
    private final GymService gymService;
    private JTable gymTable;
    protected JButton addButton;
    protected JButton updateButton;
    protected JButton deleteButton;
    protected JButton closeButton;
    protected PermissionManager permissionManager;
    private JTextField addressField;
    private JTextField costField;
    private JTextField membersField;
    private JTextField ratingField;

    public GymManagerGUI(User currentUser) throws SQLException {
        this.currentUser = currentUser;
        this.gymService = new GymService();
        this.permissionManager = new PermissionManager(currentUser, selectedGym);
        
        // Initialize the buttons
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        closeButton = new JButton("Close");
        
        initializeComponents();
        setupMenuBar();
        loadGyms();
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu manageMenu = new JMenu("Manage");

        JMenuItem eventsItem = new JMenuItem("Events");
        eventsItem.addActionListener(e -> manageEvents());

        JMenuItem trainersItem = new JMenuItem("Trainers");
        trainersItem.addActionListener(e -> manageTrainers());

        JMenuItem membershipsItem = new JMenuItem("Memberships");
        membershipsItem.addActionListener(e -> manageMemberships());

        manageMenu.add(eventsItem);
        manageMenu.add(trainersItem);
        manageMenu.add(membershipsItem);
        menuBar.add(manageMenu);
        setJMenuBar(menuBar);
    }

    private void manageEvents() {
        handleGymSelection();
        if (selectedGym != null) {
            try {
                EventManager manager = new EventManager(this, selectedGym);
                manager.setLocationRelativeTo(this);
                manager.setVisible(true);
            } catch (SQLException e) {
                showError("Error opening Event Manager", e);
            }
        } else {
            showError("Please select a gym first", null);
        }
    }

    private void manageTrainers() {
        handleGymSelection();
        if (selectedGym != null) {
            try {
                TrainerManager manager = new TrainerManager(this, selectedGym);
                manager.setLocationRelativeTo(this);
                manager.setVisible(true);
            } catch (SQLException e) {
                showError("Error opening Trainer Manager", e);
            }
        } else {
            showError("Please select a gym first", null);
        }
    }

    private void manageMemberships() {
        handleGymSelection();
        if (selectedGym != null) {
            try {
                MembershipManager manager = new MembershipManager(this, selectedGym, currentUser);
                manager.setLocationRelativeTo(this);
                manager.setVisible(true);
            } catch (SQLException e) {
                showError("Error opening Membership Manager", e);
            }
        } else {
            showError("Please select a gym first", null);
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private void showError(String message, Exception e) {
        String fullMessage = e != null ? message + ": " + e.getMessage() : message;
        JOptionPane.showMessageDialog(this, fullMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(800, 600);
        setTitle("Gym Management System");

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create gym input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Gym"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize input fields
        addressField = new JTextField(20);
        costField = new JTextField(10);
        membersField = new JTextField(10);
        ratingField = new JTextField(10);

        // Add components to input panel
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(addressField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Cost:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(costField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Members:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(membersField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Rating:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(ratingField, gbc);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JPanel managePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // Style buttons
        addButton = createStyledButton("Add Gym");
        updateButton = createStyledButton("Update Gym");
        deleteButton = createStyledButton("Delete Gym");
        
        JButton eventsButton = createStyledButton("Manage Events");
        JButton membershipsButton = createStyledButton("Manage Memberships");
        JButton trainersButton = createStyledButton("Manage Trainers");
        JButton classesButton = createStyledButton("Manage Classes");

        // Add action listeners
        addButton.addActionListener(e -> addGym());
        updateButton.addActionListener(e -> updateGym());
        deleteButton.addActionListener(e -> deleteGym());
        eventsButton.addActionListener(e -> manageEvents());
        membershipsButton.addActionListener(e -> manageMemberships());
        trainersButton.addActionListener(e -> manageTrainers());
        classesButton.addActionListener(e -> manageClasses());

        // In initializeComponents(), add to your buttons panel
        JButton clearButton = createStyledButton("Clear");
        clearButton.addActionListener(e -> clearInputFields());
        crudPanel.add(clearButton);  // Add to your existing crudPanel with other buttons

        // Add buttons to panels
        crudPanel.add(addButton);
        crudPanel.add(updateButton);
        crudPanel.add(deleteButton);

        managePanel.add(eventsButton);
        managePanel.add(membershipsButton);
        managePanel.add(trainersButton);
        managePanel.add(classesButton);

        // Add panels to buttons panel
        buttonsPanel.add(crudPanel);
        buttonsPanel.add(managePanel);

        // Create top section panel (input + buttons)
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.add(inputPanel, BorderLayout.NORTH);
        topSection.add(buttonsPanel, BorderLayout.CENTER);

        // Create table panel with proper selection mode
        gymTable = new JTable(new GymTableModel()) {
            @Override
            public void changeSelection(int row, int column, boolean toggle, boolean extend) {
                super.changeSelection(row, 0, toggle, extend);
                super.setColumnSelectionInterval(0, getColumnCount() - 1);
            }
        };
        
        // Set selection behavior
        gymTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gymTable.setCellSelectionEnabled(false);
        gymTable.setRowSelectionAllowed(true);
        gymTable.setColumnSelectionAllowed(false);
        
        // Force row selection
        gymTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add this line to ensure the entire row is selected
        gymTable.getTableHeader().setReorderingAllowed(false);
        
        // Selection and grid appearance
        gymTable.setShowGrid(true);
        gymTable.setGridColor(Color.GRAY);
        gymTable.setSelectionBackground(new Color(184, 207, 229));
        gymTable.setSelectionForeground(Color.BLACK);
        gymTable.setShowHorizontalLines(true);
        gymTable.setShowVerticalLines(true);
        
        // Modify the selection listener to handle full row selection
        gymTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = gymTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Ensure the entire row is selected
                    gymTable.setRowSelectionInterval(selectedRow, selectedRow);
                    
                    selectedGym = ((GymTableModel) gymTable.getModel()).getGymAt(selectedRow);
                    addressField.setText(selectedGym.getAddress());
                    costField.setText(String.valueOf(selectedGym.getCost()));
                    membersField.setText(String.valueOf(selectedGym.getNumberOfMembers()));
                    ratingField.setText(String.valueOf(selectedGym.getRating()));
                    initializePermissionManager(selectedGym);
                    setButtonPermissions();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(gymTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Gyms"));

        // Add all components to main panel
        mainPanel.add(topSection, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);
    }

    private void loadGyms() throws SQLException {
        GymTableModel model = (GymTableModel) gymTable.getModel();
        model.setGyms(gymService.getAllGyms());
    }

    protected void initializePermissionManager(Gym gym) {
        this.selectedGym = gym;
        this.permissionManager = new PermissionManager(currentUser, selectedGym);
    }

    protected void setButtonPermissions() {
        if (permissionManager != null) {
            permissionManager.validateUIPermissions(addButton, updateButton, deleteButton);
        }
    }

    @SuppressWarnings("unused")
    private boolean validatePermissions(User user) {
        if (user == null) {
            JOptionPane.showMessageDialog(this, "You must be logged in to access this feature.");
            return false;
        }
        
        if (!user.canManageGym()) {
            JOptionPane.showMessageDialog(this, "You don't have permission to access gym management features.");
            return false;
        }
        
        return true;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public Gym getCurrentGym() {
        return selectedGym;
    }

    private void manageClasses() {
        handleGymSelection();
        if (selectedGym != null) {
            GymClassManager manager = new GymClassManager(this, selectedGym, currentUser);
            manager.setLocationRelativeTo(this);
            manager.setVisible(true);
        } else {
            showError("Please select a gym first", null);
        }
    }

    private void handleGymSelection() {
        if (!gymTable.getSelectionModel().isSelectionEmpty()) {
            int selectedRow = gymTable.getSelectedRow();
            if (selectedRow != -1) {
                // Get the selected gym from the table model
                selectedGym = ((GymTableModel) gymTable.getModel()).getGymAt(selectedRow);
                
                // Update the input fields with the selected gym's data
                addressField.setText(selectedGym.getAddress());
                costField.setText(String.valueOf(selectedGym.getCost()));
                membersField.setText(String.valueOf(selectedGym.getNumberOfMembers()));
                ratingField.setText(String.valueOf(selectedGym.getRating()));
                
                // Update permissions for the selected gym
                initializePermissionManager(selectedGym);
                setButtonPermissions();
            }
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 30));  // Match login button size
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setFocusPainted(false);
        
        // Add subtle border and background
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        button.setBackground(new Color(240, 240, 240));
        button.setForeground(Color.BLACK);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 240, 240));
            }
        });

        return button;
    }

    private void addGym() {
        try {
            // Validate input fields
            String address = addressField.getText().trim();
            double cost = Double.parseDouble(costField.getText().trim());
            int members = Integer.parseInt(membersField.getText().trim());
            double rating = Double.parseDouble(ratingField.getText().trim());

            // Create new gym without an ID (database will generate it)
            Gym newGym = new Gym(0, address, cost, members, rating); // Use 0 or -1 as temporary ID
            gymService.addGym(newGym);

            // Refresh table and clear fields
            loadGyms();
            clearInputFields();
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for cost, members, and rating", null);
        } catch (SQLException e) {
            showError("Error adding gym", e);
        }
    }

    private void updateGym() {
        if (selectedGym == null) {
            showError("Please select a gym to update", null);
            return;
        }

        try {
            // Update gym with new values
            selectedGym.setAddress(addressField.getText().trim());
            selectedGym.setCost(Double.parseDouble(costField.getText().trim()));
            selectedGym.setNumberOfMembers(Integer.parseInt(membersField.getText().trim()));
            selectedGym.setRating(Double.parseDouble(ratingField.getText().trim()));

            // Update in database
            gymService.updateGym(selectedGym);

            // Refresh table
            loadGyms();
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for cost, members, and rating", null);
        } catch (SQLException e) {
            showError("Error updating gym", e);
        }
    }

    private void deleteGym() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void clearInputFields() {
        addressField.setText("");
        costField.setText("");
        membersField.setText("");
        ratingField.setText("");
        gymTable.clearSelection();
        selectedGym = null;
    }
}

@SuppressWarnings("unused")
class GymTableModel extends AbstractTableModel {
    private List<Gym> gyms = new ArrayList<>();
    private final String[] columnNames = {"ID", "Address", "Cost", "Members", "Rating"};

    public void setGyms(List<Gym> gyms) {
        this.gyms = gyms;
        fireTableDataChanged();
    }

    public Gym getGymAt(int row) {
        return gyms.get(row);
    }

    @Override
    public int getRowCount() { return gyms.size(); }

    @Override
    public int getColumnCount() { return columnNames.length; }

    @Override
    public String getColumnName(int column) { return columnNames[column]; }

    @Override
    public Object getValueAt(int row, int column) {
        Gym gym = gyms.get(row);
        return switch (column) {
            case 0 -> gym.getGymID();
            case 1 -> gym.getAddress();
            case 2 -> gym.getCost();
            case 3 -> gym.getNumberOfMembers();
            case 4 -> gym.getRating();
            default -> null;
        };
    }
}
