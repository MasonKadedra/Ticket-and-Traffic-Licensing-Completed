package groupworkTesting;
import java.time.LocalDate;
import java.util.Date;
import java.time.ZoneId;
//Kadedra Mason
// ID: 2304879

public class Ticket {
	
    public enum Status {
        UNPAID, PAID, WARRANT
    }
    
    private static int ticketCounter = 1000;  // Static counter to generate unique ticket numbers
    private int ticketNumber;
    private Date issueDate;
    private int offenceCode;
    private String offenceDescription;
    private String vehicleLicensePlate;
    private Driver driver;
    private Jcf officer;
    private Fine fine; // Fine class to assign fine based on offence
    private boolean isPaid;
    private Status status; 
    private LocalDate paymentDueDate;
    private CourtInfo warrantInfo; 
    private boolean warrantIssued;

    // Default constructor
    public Ticket() {
        // Automatically generate the ticket number using the static counter
        this.ticketNumber = ticketCounter++;
        this.isPaid = false;
    }
    
    // Primary Constructor
    public Ticket(Date issueDate, int offenceCode, String offenceDescription,
                  String vehicleLicensePlate, Driver driver, Jcf officer, Fine fine) {
        this.ticketNumber = ticketCounter++;  // Assign a new ticket number from the counter
        this.issueDate = issueDate;
        this.setOffenceCode(offenceCode);
        this.offenceDescription = offenceDescription;
        this.setVehicleLicensePlate(vehicleLicensePlate);
        this.setDriver(driver);
        this.setOfficer(officer);
        this.fine = fine; // Ensure the fine is assigned here
        this.isPaid = false;

        
        if (fine.getFineCode() == 4) {
            this.status = Status.WARRANT;
        } else {
            this.status = Status.UNPAID;
        }
        
        calculatePaymentDueDate();
    }

    // Copy Constructor
    public Ticket(Ticket ticket) {
        this.ticketNumber = ticket.ticketNumber;
        this.issueDate = ticket.issueDate;
        this.setOffenceCode(ticket.offenceCode);
        this.offenceDescription = ticket.offenceDescription;
        this.setVehicleLicensePlate(ticket.vehicleLicensePlate);
        this.setDriver(ticket.driver);
        this.setOfficer(ticket.officer);
        this.fine = ticket.fine;
        this.isPaid = ticket.isPaid;
    }
    
    // Getter for ticket status
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
 // Method to mark ticket as paid
    public void markAsPaid() {
        if (status != Status.WARRANT) {
            this.status = Status.PAID;
        } else {
            System.out.println("Cannot pay a ticket with a WARRANT status directly.");
        }
    }

    // Method to escalate to a warrant
    public void escalateToWarrant() {
        if (status == Status.UNPAID) {
            this.status = Status.WARRANT;
        }
    }
    
 // Method to check if the ticket has a WARRANT status
    public boolean isWarrant() {
        return this.status == Status.WARRANT;
    }

    // Getter for fine amount
    public double getFineAmount() {
        return fine != null ? fine.getFineAmount() : 0.0;
    }
    
    public Ticket(Driver driver, Status status) {
        this.driver = driver;
        this.status = status;
    }

    // Other getters
    public int getTicketNumber() { 
        return ticketNumber; 
    }
    
    public Date getIssueDate() { 
        return issueDate; 
    }
    
    public void setIssueDate(Date issueDate) {
    	this.issueDate = issueDate;
    }
    
    public String getOffenceDescription() { 
        return offenceDescription; 
    }

    public int getOffenceCode() {
        return offenceCode;
    }

    public void setOffenceCode(int offenceCode) {
        this.offenceCode = offenceCode;
    }

    public String getVehicleLicensePlate() {
        return vehicleLicensePlate;
    }

    public void setVehicleLicensePlate(String vehicleLicensePlate) {
        this.vehicleLicensePlate = vehicleLicensePlate;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Jcf getOfficer() {
        return officer;
    }

    public void setOfficer(Jcf officer) {
        this.officer = officer;
    }
    
    public Fine getFine() {
        return fine;
    }
    
    public boolean isPaid() {
        return isPaid;
    }

    // Setter for isPaid
    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }
    
    public boolean isOutstanding() {
        return !isPaid;  // Implement logic as needed
    }
    
    public LocalDate getPaymentDueDate() {
        return paymentDueDate;
    }
    
    public void setPaymentDueDate(LocalDate paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }
    
    public boolean isPastDue() {
        LocalDate currentDate = LocalDate.now(); // Get the current date
        return paymentDueDate != null && currentDate.isAfter(paymentDueDate); // Check if the current date is after the payment due date
    }
    
    public CourtInfo getWarrantInfo() {
        return warrantInfo;
    }

    public void setWarrantInfo(CourtInfo warrantInfo) {
        this.warrantInfo = warrantInfo;
    }
    
 // Getter and Setter for Warrant Issued
    public boolean isWarrantIssued() {
        return warrantIssued;
    }

    public void setWarrantIssued(boolean warrantIssued) {
        this.warrantIssued = warrantIssued;
    }
    
    // Method to calculate payment due date
    public void calculatePaymentDueDate() {
        if (this.issueDate != null) {
            // Convert issueDate (Date) to LocalDate
            LocalDate issueLocalDate = this.issueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Add 9 days to the issueDate
            LocalDate dueDate = issueLocalDate.plusDays(9);

            // Set the calculated due date
            this.paymentDueDate = dueDate;
        } else {
            System.out.println("Issue date is null, cannot calculate payment due date.");
        }
    }
    
    @Override
    public String toString() {
        String driverName = (driver != null) ? driver.getDriverName() : "N/A";
        String trn = (driver != null) ? driver.getTrn() : "N/A";
        Address address = (driver != null) ? driver.getAddress() : null;
        String badgeNumber = (officer != null) ? officer.getBadgeNumber()  : "N/A";
        String assignedPoliceStation = (officer != null) ? officer.getAssignedPoliceStation()  : "N/A";
        String officerName = (officer != null) ? officer.getFullName() : "N/A";
        double fineAmount = getFineAmount();

        return "Ticket Number: " + ticketNumber +
               "\nIssue Date: " + issueDate +
               "\nOffense Code: " + offenceCode +
               "\nOffense Description: " + offenceDescription +
               "\nVehicle License Plate: " + vehicleLicensePlate +
               "\nDriver: " + driverName +
               "\nAddress: " + address +
               "\nTrn: " + trn +
               "\nOfficer: " + officerName +
               "\nBadge Number: " + badgeNumber +
               "\nAssigned Station: " + assignedPoliceStation +
               "\nFine: " + fineAmount+
               "\nStatus: " + status;
    }

	public void payTicket() {
		 this.isPaid = true;
		
	}


}
