package groupworkTesting;

import java.util.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

//Kaif Brown
//ID: 2304843

public class Driver {
    private String trn;
    private String driverName;
    private String dob;
    private Address address;
    private String contactNumber;
    private boolean hasNegativePoliceRecord;
    private Map<Integer, Ticket> tickets;
    
    // Default constructor
    public Driver() {
        this.tickets = new HashMap<>(); // Initialize the tickets map
    }

    // Constructor with parameters
    public Driver(String trn, String driverName, String dob, Address address, String contactNumber) {
        this.trn = trn;
        this.driverName = driverName;
        this.dob = dob;
        this.address = address;
        this.contactNumber = contactNumber;
        this.tickets = new HashMap<>(); // Initialize the tickets map
        this.setHasNegativePoliceRecord(false); 
    }

    // Getters and Setters
    public String getTrn() { 
        return trn; 
    }

    public void setTrn(String trn) { 
        this.trn = trn; 
    }

    public String getDriverName() { 
        return driverName; 
    }

    public void setDriverName(String driverName) { 
        this.driverName = driverName; 
    }

    public String getDob() { 
        return dob; 
    }

    public void setDob(String dob) { 
        this.dob = dob; 
    }

    public Address getAddress() { 
        return address; 
    }

    public void setAddress(Address address) { 
        this.address = address; 
    }

    public String getContactNumber() { 
        return contactNumber; 
    }

    public void setContactNumber(String contactNumber) { 
        this.contactNumber = contactNumber; 
    }

    public void addTicket(Ticket ticket) {
        tickets.put(ticket.getTicketNumber(), ticket);
    }

    public Map<Integer, Ticket> getTickets() {
        return tickets;
    }
    
	public boolean isHasNegativePoliceRecord() {
		return hasNegativePoliceRecord;
	}

	public void setHasNegativePoliceRecord(boolean hasNegativePoliceRecord) {
		this.hasNegativePoliceRecord = hasNegativePoliceRecord;
	}
	
    // Method to retrieve a ticket by its number
    public Ticket getTicketByNumber(int ticketNumber) {
        return tickets.get(ticketNumber);  // Returns the ticket associated with the ticketNumber key, or null if not found
    }

    // Method to get sorted tickets based on issue date
    public List<Ticket> getSortedTickets() {
        List<Ticket> ticketList = new ArrayList<>(tickets.values()); // Convert Map to List
        ticketList.sort(Comparator.comparing(Ticket::getIssueDate)); // Sort based on issue date
        return ticketList;
    }
    
    public void viewTickets() {
        if (tickets.isEmpty()) {
            System.out.println("No tickets found for this driver.");
        } else {
            for (Ticket ticket : tickets.values()) {
                System.out.println(ticket);  // Assuming the Ticket class has a good `toString` method
            }
        }
    }

    // Method to make an online payment for an unpaid ticket
    public void makePayment(int ticketNumber) {
        for (Ticket ticket : tickets.values()) { // Iterate over Map values
            if (ticket.getTicketNumber() == ticketNumber && ticket.getStatus() == Ticket.Status.UNPAID) {
                ticket.setStatus(Ticket.Status.PAID);
                System.out.println("Payment successful for ticket " + ticketNumber);
                return;
            }
        }
        System.out.println("Ticket not found or already paid.");
    }

    // Method to check for past due tickets
    public List<Ticket> getPastDueTickets() {
        List<Ticket> pastDueTickets = new ArrayList<>();
        for (Ticket ticket : tickets.values()) { // Iterate over Map values
            if (ticket.isPastDue()) {
                pastDueTickets.add(ticket);
            }
        }
        return pastDueTickets;
    }

    // Method to check if the driver has recent accidents
    public boolean hasRecentAccidents() {
        LocalDate currentDate = LocalDate.now(); // Get the current date
        LocalDate twoYearsAgo = currentDate.minusYears(2); // Subtract 2 years from current date

        // Iterate over the tickets in the tickets map
        for (Ticket ticket : tickets.values()) {
            // 1. If ticket status is WARRANT, classify it as an accident
            if (ticket.isWarrant()) {
                return true; // If any WARRANT ticket exists, treat it as an accident
            }

            // 2. Check if the ticket is from an accident-related offence
            if (isAccidentOffence(ticket)) {
                // 3. Check if the ticket was issued within the last two years
                LocalDate ticketDate = ticket.getIssueDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                if (!ticketDate.isBefore(twoYearsAgo)) {
                    return true; // If it's an accident offence and within the last 2 years, it's a recent accident
                }
            }
        }
        return false; // No accidents found
    }

    // Helper method to classify offences as accidents
    private boolean isAccidentOffence(Ticket ticket) {
        String[] accidentOffences = {
            "Speeding", "Reckless driving", "Driving under the influence", "Hit and run"
        };

        for (String offence : accidentOffences) {
            if (ticket.getOffenceDescription().equalsIgnoreCase(offence)) {
                return true; // If the offence matches, return true
            }
        }
        return false; // No matching accident offence found
    }

    // Method to check if the driver has a negative police record (based on WARRANT tickets)
    public boolean hasNegativePoliceRecord() {
        for (Ticket ticket : tickets.values()) { // Iterate over Map values
            if (ticket.isWarrant()) {
                return true; // If there's a WARRANT ticket, return true for a negative record
            }
        }
        return false; // No WARRANT tickets found
    }

 // Method to check court details for tickets
    // Driver checks court details for tickets
    public void checkCourtDetails() {
        boolean found = false;

        for (Ticket ticket : tickets.values()) {
            CourtInfo courtInfo = ticket.getWarrantInfo();
            if (courtInfo != null) {
                System.out.println("Court Details for Ticket #" + ticket.getTicketNumber());
                System.out.println("Police Station: " + courtInfo.getPoliceStation());
                System.out.println("Court Date: " + courtInfo.getCourtDate());
                System.out.println("Court Room: " + courtInfo.getCourtRoom());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No court details available for any tickets.");
        }
    }
    
 // Method for Driver to View Unpaid Tickets Not Past 21 Days from Issue Date
    // Method to view tickets based on driver's TRN
    public void viewTicketPayments(String driverTrn) {
        boolean found = false;

        // Iterate over all tickets in the system
        for (Ticket ticket : tickets.values()) {
            // Ensure the ticket's driver TRN matches the one provided
            if (ticket.getDriver().getTrn().equals(driverTrn)) {
                Date issueDate = ticket.getIssueDate();
                LocalDate ticketIssueDate = issueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate currentDate = LocalDate.now();

                // Check if the ticket is within 21 days from the issue date
                if (ChronoUnit.DAYS.between(ticketIssueDate, currentDate) <= 21) {
                    // If the ticket is within 21 days, print ticket details
                    System.out.println("Ticket Number: " + ticket.getTicketNumber());
                    System.out.println("Issue Date: " + ticket.getIssueDate());

                    // Get the Fine object associated with the ticket
                    Fine fine = ticket.getFine();  // Access the Fine object
                    if (fine != null) {
                        System.out.println("Amount Due: " + fine.getFineAmount());  // Display fine amount
                    } else {
                        System.out.println("Amount Due: No fine assigned.");
                    }
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No tickets found or all tickets are past due.");
        }
    }

}
