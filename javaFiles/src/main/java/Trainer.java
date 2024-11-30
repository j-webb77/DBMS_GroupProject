public class Trainer {
    private int trainerId;
    private int gymId;
    private String name;
    private String specialization;
    private double hourlyRate;
    private String email;

    public Trainer(int trainerId, int gymId, String name, String specialization, double hourlyRate, String email) {
        this.trainerId = trainerId;
        this.gymId = gymId;
        this.name = name;
        this.specialization = specialization;
        this.hourlyRate = hourlyRate;
        this.email = email;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    public int getGymId() {
        return gymId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
} 
