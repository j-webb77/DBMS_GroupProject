import java.time.LocalDate;

public class Membership {
    private final int membershipId;
    private String memberName;
    private String membershipType;
    private LocalDate startDate;
    private LocalDate endDate;
    private int gymId;
    private double price;
    private String status;

    public Membership(int membershipId, String memberName, String membershipType,
                     LocalDate startDate, LocalDate endDate, int gymId, 
                     double price, String status) {
        this.membershipId = membershipId;
        this.memberName = memberName;
        this.membershipType = membershipType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.gymId = gymId;
        this.price = price;
        this.status = status;
    }

    // Add this constructor for creating new memberships
    public Membership(String memberName, String membershipType,
                     LocalDate startDate, LocalDate endDate, 
                     double price, String status) {
        this(0, memberName, membershipType, startDate, endDate, 0, price, status);
    }

    // Getters and setters
    public int getMembershipId() { return membershipId; }
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public int getGymId() { return gymId; }
    public void setGymId(int gymId) { this.gymId = gymId; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Membership copy() {
        return new Membership(
            this.membershipId,
            this.memberName,
            this.membershipType,
            this.startDate,
            this.endDate,
            this.gymId,
            this.price,
            this.status
        );
    }
} 