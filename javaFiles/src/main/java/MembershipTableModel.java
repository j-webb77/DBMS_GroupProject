import java.util.List;

import javax.swing.table.AbstractTableModel;

public class MembershipTableModel extends AbstractTableModel {
    private List<Membership> memberships;
    private final String[] columnNames = {
        "ID", "Member Name", "Type", "Start Date", "End Date", "Price", "Status"
    };

    public MembershipTableModel() {
        this.memberships = null;
    }

    @Override
    public int getRowCount() {
        return memberships.size();
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
        Membership membership = memberships.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> membership.getMembershipId();
            case 1 -> membership.getMemberName();
            case 2 -> membership.getMembershipType();
            case 3 -> membership.getStartDate();
            case 4 -> membership.getEndDate();
            case 5 -> membership.getPrice();
            case 6 -> membership.getStatus();
            default -> null;
        };
    }

    public void updateData(List<Membership> newMemberships) {
        this.memberships = newMemberships;
        fireTableDataChanged();
    }

    public Membership getMembershipAt(int rowIndex) {
        return memberships.get(rowIndex);
    }

	public void setMemberships(List<Membership> membershipsByGym) {
		this.memberships = membershipsByGym;
		fireTableDataChanged();
	}
}