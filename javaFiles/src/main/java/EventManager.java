import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Font;

public final class EventManager extends BaseGymManager {
    private final Gym gym;
    private JTable eventTable;
    private JTextField eventNameField;
    private JDChooser dateChooser;
    private JTextArea descriptionArea;
    private final EventService eventService;


    private static class JDateChooser extends JTextField {
        public JDateChooser() {
            super();
        }
        
        public void setDateFormatString(String pattern) {
            setToolTipText("Date format: " + pattern);
        }
        
        public Date getDate() {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.parse(getText());
            } catch (java.text.ParseException e) {
                return null;
            }
        }
        
        public void setDate(Date date) {
            if (date == null) {
                setText("");
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                setText(sdf.format(date));
            }
        }
    }

    public class JDChooser extends JDateChooser {
        private SimpleDateFormat dateFormat;
        
        public JDChooser(String datePattern) {
            super();
            initialize();
            setDateFormat(new SimpleDateFormat(datePattern));
        }
        
        private void initialize() {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            setDateFormatString("yyyy-MM-dd");
            setFont(new Font("SansSerif", Font.PLAIN, 12));
            setPreferredSize(new Dimension(150, 25));
        }
        
        @SuppressWarnings("unused")
        public String getFormattedDate() {
            Date date = getDate();
            return date != null ? dateFormat.format(date) : "";
        }
        
        public final void setDateFormat(SimpleDateFormat format) {
            this.dateFormat = format;
            setDateFormatString(format.toPattern());
        }
        
        @SuppressWarnings("unused")
        public boolean isValidDate() {
            return getDate() != null;
        }
        
        @SuppressWarnings("unused")
        public void clearDate() {
            setDate(null);
        }
        
        @Override
        public void setDate(Date date) {
            super.setDate(date);
        }
        
        @Override
        public void setDateFormatString(String pattern) {
            super.setDateFormatString(pattern);
        }

        @Override
        public Date getDate() {
            return super.getDate();
        }
    }

    public EventManager(Frame owner, Gym gym) throws SQLException {
        super(owner, "Manage Events", true);
        this.gym = gym;
        this.currentGym = gym;
        this.eventService = new EventService();
        
        initializeComponents();
        loadEvents();
        validatePermissions();
    }
                    
                    
                        @Override
                        public void setFont(Font font) {
                            super.setFont(font);
                        }
            
            
                        private void initializeComponents() {
                setLayout(new BorderLayout(10, 10));
                setSize(600, 400);
        
                // Create input panel with matching dimensions
                JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
                
                eventNameField = new JTextField(60);  // Match the text field size of other managers
                dateChooser = new JDChooser("MM/dd/yyyy");
                descriptionArea = new JTextArea(2, 10);  // Match the width of other text fields
                JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        
                inputPanel.add(new JLabel("Event Name:"));
                inputPanel.add(eventNameField);
                inputPanel.add(new JLabel("Date:"));
                inputPanel.add(dateChooser);
                inputPanel.add(new JLabel("Description:"));
                inputPanel.add(descScrollPane);
        
                // Create button panel
                JPanel buttonPanel = new JPanel();
                addButton.addActionListener(e -> {
                    try {
                        addEvent();
                    } catch (SQLException e1) {
                    }
                });
                updateButton.addActionListener(e -> {
                    try {
                        updateEvent();
                    } catch (SQLException e1) {
                    }
                });
                deleteButton.addActionListener(e -> {
                    try {
                        deleteEvent();
                    } catch (SQLException e1) {
                    }
                });
                closeButton.addActionListener(e -> dispose());

                buttonPanel.add(addButton);
                buttonPanel.add(updateButton);
                buttonPanel.add(deleteButton);
                buttonPanel.add(closeButton);

                // Create table
                eventTable = new JTable(new EventTableModel());
                JScrollPane scrollPane = new JScrollPane(eventTable);
                
                // Add components
                add(inputPanel, BorderLayout.NORTH);
                add(scrollPane, BorderLayout.CENTER);
                add(buttonPanel, BorderLayout.SOUTH);

                // Setup table selection
                eventTable.getSelectionModel().addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()) {
                        int selectedRow = eventTable.getSelectedRow();
                        if (selectedRow >= 0) {
                            Event selected = ((EventTableModel)eventTable.getModel()).getEventAt(selectedRow);
                            populateFields(selected);
                        }
                    }
                });
            }
        
            private void populateFields(Event event) {
                eventNameField.setText(event.getName());
                dateChooser.setDate(Date.from(event.getDateTime().atZone(ZoneId.systemDefault()).toInstant()));
                descriptionArea.setText(event.getDescription());
            }
        
            @SuppressWarnings("unused")
            private void addButton(JPanel buttonPanel, String text, ActionListener listener) {
                JButton button = new JButton(text);
                button.addActionListener(listener);
                buttonPanel.add(button);
            }
        
            private void loadEvents() throws SQLException {
                EventTableModel model = (EventTableModel) eventTable.getModel();
                model.setEvents(eventService.getEventsForGym(gym.getGymId()));
            }
        
            private void addEvent() throws SQLException {
                if (!checkPermissions()) {
                                    JOptionPane.showMessageDialog(this, "You don't have permission to add events.");
                                    return;
                                }
                
                                Event event = new Event(0, eventNameField.getText(), 
                                    dateChooser.getDate().toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDateTime(),
                                    descriptionArea.getText(),
                                    gym.getGymId(),
                                    0,
                                    "");
                
                                eventService.addEvent(event);
                                loadEvents();
                                clearInputFields();
                            }
                        
                            private boolean checkPermissions() {
                                // TODO Auto-generated method stub
                                throw new UnsupportedOperationException("Unimplemented method 'checkPermissions'");
                            }
                
                
                            private void updateEvent() throws SQLException {
                int selectedRow = eventTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select an event to update", "No Selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }
        
                EventTableModel model = (EventTableModel) eventTable.getModel();
                Event event = model.getEventAt(selectedRow);
                event.setName(eventNameField.getText());
                event.setDateTime(dateChooser.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
                event.setDescription(descriptionArea.getText());
        
                eventService.updateEvent(event);
                loadEvents();
                clearInputFields();
            }
        
            private void deleteEvent() throws SQLException {
                int selectedRow = eventTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select an event to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }
        
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this event?", 
                    "Confirm Delete", 
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    EventTableModel model = (EventTableModel) eventTable.getModel();
                    Event event = model.getEventAt(selectedRow);
                    eventService.deleteEvent(event.getEventId(), event.getGymId());
                    loadEvents();
                    clearInputFields();
                }
            }
        
            private void clearInputFields() {
                eventNameField.setText("");
                dateChooser.setDate(null);
                descriptionArea.setText("");
            }

    public JDChooser getDateChooser() {
        return dateChooser;
    }

    public void setDateChooser(JDChooser dateChooser) {
        this.dateChooser = dateChooser;
    }
    public EventService getEventService() {
        return eventService;
    }

    @Override
    public void validatePermissions() {
        setButtonPermissions();
        boolean canEdit = ((PermissionManager)permissionManager).canEdit();
        eventNameField.setEditable(canEdit);
        dateChooser.setEditable(canEdit);
        descriptionArea.setEditable(canEdit);
    }

    @SuppressWarnings("unused")
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return button;
    }
} 