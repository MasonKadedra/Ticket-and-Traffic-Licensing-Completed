package groupworkTesting;

// By: Kadedra Mason
// ID: 2304879

public class Fine {

    private int fineCode;       // Code for the fine (e.g., 1, 2, 3, 4)
    private String fineClass;   // Class of the fine (e.g., CLASS_A, CLASS_B)
    private double fineAmount;  // The amount of the fine

    // Constructor
    public Fine(int fineCode, String fineClass, double fineAmount) {
        this.fineCode = fineCode;
        this.fineClass = fineClass;
        this.fineAmount = fineAmount;
    }

    // Getter for fine code
    public int getFineCode() {
        return fineCode;
    }

    // Getter for fine class
    public String getFineClass() {
        return fineClass;
    }

    // Getter for fine amount
    public double getFineAmount() {
        return fineAmount;
    }

    // Static method to create a Fine object by offense code
    public static Fine getFineByOffenceCode(int offenceCode) {
        switch (offenceCode) {
            case 1: return new Fine(1, "CLASS_A", 5000.0); // Example: Speeding
            case 2: return new Fine(2, "CLASS_B", 10000.0); // Example: Illegal Parking
            case 3: return new Fine(3, "CLASS_C", 15000.0); // Example: Drunk Driving
            case 4: return new Fine(4, "CLASS_D", 20000.0); // Example: Dangerous Driving
            default: return new Fine(0, "UNKNOWN", 0.0);    // Default: Unknown offense
        }
    }
}
