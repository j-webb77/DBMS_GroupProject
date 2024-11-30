import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Gym {
    private int gymID;
    private String address;
    private double cost;
    private int numberOfMembers;
    private double rating;

    public Gym(int gymID, String address, double cost, int numberOfMembers, double rating) {
        this.gymID = gymID;
        this.address = address;
        this.cost = cost;
        this.numberOfMembers = numberOfMembers;
        this.rating = rating;
    }

    // Getters
    public int getGymID() { return gymID; }
    public String getAddress() { return address; }
    public double getCost() { return cost; }
    public int getNumberOfMembers() { return numberOfMembers; }
    public double getRating() { return rating; }

    // Setters
    public void setGymID(int gymID) { this.gymID = gymID; }
    public void setAddress(String address) { this.address = address; }
    public void setCost(double cost) { this.cost = cost; }
    public void setNumberOfMembers(int numberOfMembers) { this.numberOfMembers = numberOfMembers; }
    public void setRating(double rating) { this.rating = rating; }

    // Add toString() method
    @Override
    public String toString() {
        return "Gym{" +
               "gymID=" + gymID +
               ", address='" + address + '\'' +
               ", cost=" + cost +
               ", numberOfMembers=" + numberOfMembers +
               ", rating=" + rating +
               '}';
    }

	public List<GymClass> getClasses() {
		// Implement actual logic to fetch classes
		return new ArrayList<>();  // Temporary return
	}

	public void addClass(GymClass newClass) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'addClass'");
	}

	public void updateClass(int selectedRow, GymClass updatedClass) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'updateClass'");
	}

	public void deleteClass(int selectedRow) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'deleteClass'");
	}

	public int getId() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getId'");
	}

    public int getGymId() {
        return this.gymID;  // Assuming you have a gymID field
    }

	public void setGymID(Point gymID2) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setGymID'");
	}
}
