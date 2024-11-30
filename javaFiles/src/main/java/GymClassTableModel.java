
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class GymClassTableModel extends AbstractTableModel {
    private List<GymClass> gymClasses;
    private final String[] columnNames = {"Class ID", "Gym ID", "Trainer ID", "Class Name", "Schedule", "Capacity"};

    public GymClassTableModel(List<GymClass> gymClasses) {
        this.gymClasses = gymClasses;
    }

    @Override
    public int getRowCount() {
        return gymClasses.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        GymClass gymClass = gymClasses.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> gymClass.getClassId();
            case 1 -> gymClass.getGymId();
            case 2 -> gymClass.getTrainerId();
            case 3 -> gymClass.getClassName();
            case 4 -> gymClass.getSchedule();
            case 5 -> gymClass.getCapacity();
            default -> null;
        };
    }

    public void updateData(List<GymClass> newGymClasses) {
        this.gymClasses = newGymClasses;
        fireTableDataChanged();
    }

    public GymClass getGymClassAt(int rowIndex) {
        return gymClasses.get(rowIndex);
    }
}