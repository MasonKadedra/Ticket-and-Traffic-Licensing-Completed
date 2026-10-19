package groupworkTesting;

//Abigail Bembridge
//ID: 2305624

import java.util.*;
import java.io.*;
//import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProcessingOfficer extends Officer {

    private List<Applicant> applicants;
    private Map<String, Driver> driverMap;
    //private Map<Integer, Ticket> ticketMap;

    // Constructor to initialize driverMap and ticketMap
    public ProcessingOfficer(Map<String, Driver> driverMap, Map<Integer, Ticket> ticketMap) {
        this.driverMap = driverMap;
       // this.ticketMap = ticketMap;
        this.applicants = new ArrayList<>();
    }

    // Constructor for cases where you don't pass the driverMap and ticketMap
    public ProcessingOfficer() {
        this.applicants = new ArrayList<>();
    }

    // Method to get driver by TRN
    public Driver getDriverByTrn(String trn) {
        return driverMap.get(trn); // Returns the driver object if found, otherwise null
    }
    
    public void viewDriverTickets(Jcf jcfOfficer, String trn) {
        jcfOfficer.checkDriverStatus(trn);  // Call the Jcf officer's method to view tickets
    }

    // Method to create a new application
    public void createApplication(Applicant applicant, boolean hasAccidents, boolean hasNegativePoliceRecord, boolean hasOutstandingTickets) {
        // Check the conditions and provide feedback to the user
        if (hasAccidents) {
            System.out.println("\nApplication Denied: Applicant has had accidents in the last two years.");
            return; // Deny the application if there have been accidents
        }

        if (hasNegativePoliceRecord) {
            System.out.println("\nApplication Denied: Applicant has a negative police record.");
            return; // Deny the application if there's a negative police record
        }

        if (hasOutstandingTickets) {
            System.out.println("\nApplication Denied: Applicant has outstanding tickets.");
            return; // Deny the application if there are outstanding tickets
        }

        // If none of the conditions are met, approve the application
        System.out.println("\nApplication Approved for " + applicant.getName() + " (TRN: " + applicant.getTRN() + ")");
        System.out.println("Application Number: " + applicant.getApplicationID());
        System.out.println("\n");
        // Add the applicant to the applicants list after approval
        applicants.add(applicant);
    }

    public void updateApplication(String TRN, boolean hasAccidents, boolean hasNegativePoliceRecord, Map<Integer, Ticket> tickets) {
        Applicant applicant = findApplicantByTRN(TRN);
        if (applicant != null) {
            // Update the applicant's information
            applicant.setAccidentStatus(hasAccidents);
            applicant.setPoliceRecordStatus(hasNegativePoliceRecord);

            // Add the tickets from the map and sum their fines
            applicant.addOutstandingTickets(tickets);

            // Dynamically update the application status based on the new details
            applicant.updateStatus();

            // Print the updated status and confirmation
            System.out.println("Application updated for: " + applicant.getName());
            System.out.println("New Status: " + applicant.getApplicationStatus());
            System.out.println("Total Outstanding Tickets: " + applicant.getOutstandingTickets());
            System.out.println("Total Outstanding Amount: $" + applicant.getTotalOutstandingAmount());
        } else {
            System.out.println("Applicant not found.");
        }
    }



    // Method to delete an application
    public void deleteApplication(String TRN) {
        Applicant applicant = findApplicantByTRN(TRN);
        if (applicant != null) {
            applicants.remove(applicant);
            System.out.println("Application deleted for: " + applicant.getName());
        } else {
            System.out.println("Applicant not found.");
        }
    }

    // Method to generate a report for an applicant
    public void generateApplicantReport(String TRN) {
        Applicant applicant = findApplicantByTRN(TRN);
        if (applicant != null) {
            applicant.generateReport();
        } else {
            System.out.println("Applicant not found.");
        }
    }

    // Search for an applicant by TRN
    private Applicant findApplicantByTRN(String TRN) {
        // Iterate through the applicants list to find the matching TRN
        for (Applicant applicant : applicants) {
            if (applicant.getTRN().equals(TRN)) {
                return applicant;
            }
        }
        return null; // Return null if applicant with given TRN is not found
    }

    // Save applicant data to file
    public void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("applicants.dat"))) {
            out.writeObject(applicants);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    // Load applicant data from file
    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("applicants.dat"))) {
            applicants = (List<Applicant>) in.readObject();
            System.out.println("Data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
    
    @Override
    public void addTicket(Date issueDate, int offenceCode, 
                          String offenceDescription, String vehicleLicensePlate, 
                          Driver driver, Fine fine) {
        // You can leave this method unimplemented or do nothing
    }

    @Override
    public void viewTicket(int ticketNumber) {
        // Leave unimplemented or do nothing since ProcessingOfficer doesn't handle ticket viewing directly
    }
    
}

