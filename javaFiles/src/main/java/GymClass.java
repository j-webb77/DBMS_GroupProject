public class GymClass {
    private int classId;
    private final String className;
    private final String instructor;
    private final String schedule;
    private final int gymId = 0;
    private final int trainerId = 0;
    private final int capacity;
    private final double price;

    // Constructor for new classes (no ID)
    public GymClass(String className, String instructor, String schedule, int capacity, double price) {
        this.classId = 0;
        this.className = className;
        this.instructor = instructor;
        this.schedule = schedule;
        this.capacity = capacity;
        this.price = price;
    }

    // Constructor for existing classes (with ID)
    public GymClass(int classId, String className, String instructor, String schedule, int capacity, double price) {
        this.classId = classId;
        this.className = className;
        this.instructor = instructor;
        this.schedule = schedule;
        this.capacity = capacity;
        this.price = price;
    }

    // Getters and setters
    public int getClassId() { return classId; }
    public int getGymId() { return gymId; }
    public int getTrainerId() { return trainerId; }
    public void setClassId(int classId) { this.classId = classId; }
    public String getClassName() { return className; }
    public String getInstructor() { return instructor; }
    public String getSchedule() { return schedule; }
    public int getCapacity() { return capacity; }
    public double getPrice() { return price; }
} 