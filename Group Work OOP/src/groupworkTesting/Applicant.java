package groupworkTesting;

import java.io.Serializable;
import java.util.*;

//Abigail Bembridge
//ID: 2305624

@SuppressWarnings("unused")
public class Applicant  {
	private static int applicantCounter = 1111;
    private String name;
    private String dateOfBirth;
    private String address;
    private String contactNumber;
    private String email;
    private String TRN;
    private int applicationID;
    private boolean hasAccidents;
    private boolean hasNegativePoliceRecord;
    private int outstandingTickets;
    private double totalOutstandingAmount;
    private String applicationStatus;

    
    public Applicant() {
    	 this.applicationID = applicantCounter + 50;
    }
    // Constructor
    public Applicant(String name, String dateOfBirth, String address, String contactNumber,
                     String email, String TRN) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
        this.TRN = TRN;
        this.applicationID = applicantCounter + 50;
        this.hasAccidents = false;
        this.hasNegativePoliceRecord = false;
        this.applicationStatus = "Pending";
        this.outstandingTickets = 0;
        this.totalOutstandingAmount = 0.0;
    }

    // Setters
    public void setAccidentStatus(boolean hasAccidents) {
    	this.hasAccidents = hasAccidents; 
    }
    
    public void setPoliceRecordStatus(boolean hasNegativePoliceRecord) { 
    	this.hasNegativePoliceRecord = hasNegativePoliceRecord; 
    }
    
    public void addOutstandingTickets(Ticket ticket) {
        if (ticket != null && ticket.getFine() != null) {
            this.outstandingTickets += 1; // Increment the count of outstanding tickets
            this.totalOutstandingAmount += ticket.getFine().getFineAmount(); // Add the fine amount
        } else {
            System.out.println("Invalid ticket or fine. Cannot add to outstanding tickets.");
        }
    }
    
    public void addOutstandingTickets(Map<Integer, Ticket> ticketMap) {
        for (Ticket ticket : ticketMap.values()) { // Iterate through the map's values
            this.outstandingTickets += 1; // Increment the ticket count
            this.totalOutstandingAmount += ticket.getFine().getFineAmount(); // Sum the fine amounts
        }
    }


    // Check eligibility for the PPV license
    public boolean isEligible() {
        return !hasAccidents && !hasNegativePoliceRecord && outstandingTickets == 0;
    }

    // Update application status based on eligibility
    public void updateStatus() {
        if (isEligible()) {
            applicationStatus = "Approved";
        } else {
            applicationStatus = "Denied";
        }
    }
    
 // Method to retrieve the application status
    public String getApplicationStatus() {
        return applicationStatus;
    }
    
    // Generate a report for the applicant
    public void generateReport() {
        System.out.println("=== Applicant Report ===");
        System.out.println("Name: " + name);
        System.out.println("Date of Birth: " + dateOfBirth);
        System.out.println("Address: " + address);
        System.out.println("Contact Number: " + contactNumber);
        System.out.println("Email: " + email);
        System.out.println("TRN: " + TRN);
        System.out.println("Application ID: " + applicationID);
        System.out.println("Outstanding Tickets: " + outstandingTickets);
        System.out.println("Total Outstanding Amount: $" + totalOutstandingAmount);
        System.out.println("Application Status: " + applicationStatus);
    }

    // Getters
    public String getTRN() { 
    	return TRN; 
    }
    
    public String getName() { 
    	return name; 
    }
    
    public int getApplicationID() {
    	return applicationID;
    }
    
    public int getOutstandingTickets() {
        return outstandingTickets;
    }

    // Getter for totalOutstandingAmount
    public double getTotalOutstandingAmount() {
        return totalOutstandingAmount;
    }
}