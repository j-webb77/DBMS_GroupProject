import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TrainerTableModel extends AbstractTableModel {
    private List<Trainer> trainers = new ArrayList<>();
    private final String[] columnNames = {"Name", "Specialization", "Hourly Rate", "Email"};

    @Override
    public int getRowCount() {
        return trainers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Trainer trainer = trainers.get(row);
        return switch (col) {
            case 0 -> trainer.getName();
            case 1 -> trainer.getSpecialization();
            case 2 -> trainer.getHourlyRate();
            case 3 -> trainer.getEmail();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public void setTrainers(List<Trainer> trainers) {
        this.trainers = trainers;
        fireTableDataChanged();
    }

    public Trainer getTrainerAt(int row) {
        return trainers.get(row);
    }
}