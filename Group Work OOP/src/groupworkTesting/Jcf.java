package groupworkTesting;
import java.util.*;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Date;
import java.time.LocalDate;

//By: Kadedra Mason
//ID: 2304879

public class Jcf extends Officer {
	
	private String badgeNumber;
    private String fullName;
    private String assignedPoliceStation;
    private Map<Integer, Ticket> tickets = new HashMap<>(); // Keyed by ticket number
    private Map<String, Driver> drivers = new HashMap<>(); // Keyed by TRN 

    // Constructor
    public Jcf(String badgeNumber, String fullName, String assignedPoliceStation) {
        this.setBadgeNumber(badgeNumber);
        this.setFullName(fullName);
        this.setAssignedPoliceStation(assignedPoliceStation);
    }
    
    //getters and setters
	public String getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAssignedPoliceStation() {
		return assignedPoliceStation;
	}

	public void setAssignedPoliceStation(String assignedPoliceStation) {
		this.assignedPoliceStation = assignedPoliceStation;
	}

	public String getAssignedStation() {
		return assignedPoliceStation;
	}
	
    // Method to retrieve a driver based on TRN (for authentication)
    public Driver getDriverByTrn(String trn) {
        return drivers.get(trn);  // Returns the driver associated with the given TRN
    }
    
    public void addDriver(Driver driver) {
        drivers.put(driver.getTrn(), driver);  // Add the driver to the map
    }
	
	// Add a ticket
	@Override
	public void addTicket(Date issueDate, int offenceCode, String offenceDescription,
	                      String vehicleLicensePlate, Driver driver, Fine fine) {
	    // Create a new ticket; the ticket number is auto-generated in the Ticket constructor
	    Ticket ticket = new Ticket(issueDate, offenceCode, offenceDescription,
	                                vehicleLicensePlate, driver, this, fine);

	    // Add the ticket to the map with its generated ticket number as the key
	    tickets.put(ticket.getTicketNumber(), ticket); 
	    // Also add the ticket to the driver's tickets map
	    driver.addTicket(ticket);
	    
	    // Add driver to the drivers map if not already present
	    if (!drivers.containsKey(driver.getTrn())) {
	        drivers.put(driver.getTrn(), driver);
	    }
	    
	    // Save the ticket as text to a .dat file
	    try (FileWriter fw = new FileWriter("tickets.dat", true);
	         BufferedWriter bw = new BufferedWriter(fw);
	         PrintWriter out = new PrintWriter(bw)) {

	        out.println(ticket.toString()); // Write ticket details as a line in the file
	        System.out.println("Ticket saved to file.");

	    } catch (IOException e) {
	        System.err.println("Error saving ticket to file: " + e.getMessage());
	    }

	    // Print a confirmation message including the ticket number
	    System.out.println("Ticket Number: " + ticket.getTicketNumber());
	}


    // View ticket by ticket number
	@Override
    public void viewTicket(int ticketNumber) {
        Ticket ticket = tickets.get(ticketNumber);
        if (ticket != null) {
            System.out.println(ticket);
        } else {
            System.out.println("Ticket not found!");
        }
    }

    // 3. Check outstanding tickets and unpaid status for drivers
    public void checkDriverStatus(String trn) {
        Driver driver = drivers.get(trn);
        if (driver == null) {
            System.out.println("No driver found with TRN: " + trn);
            return;
        }
        
        System.out.println("Outstanding tickets for driver: " + driver.getDriverName());
        System.out.println();
        System.out.println("TRN: " + driver.getTrn());
        System.out.println("Address: " + driver.getAddress());
        
        boolean hasOutstandingTickets = false;

        for (Ticket ticket : tickets.values()) {
            if (ticket.getDriver().getTrn().equals(trn) && !ticket.isPaid()) {
                // Print only the required details for each outstanding ticket
                System.out.println("Ticket Number: " + ticket.getTicketNumber());
                System.out.println("Fine Amount: " + ticket.getFine().getFineAmount());
                System.out.println("Offense Description: " + ticket.getOffenceDescription());
                System.out.println("-----------------------------\n");
                hasOutstandingTickets = true;
            }
        }

        if (!hasOutstandingTickets) {
            System.out.println("No outstanding tickets found for this driver.");
        }
    }

    
    // 3. Check outstanding tickets and unpaid status for all offenders
    public void viewAllOutstandingTickets() {
        System.out.println("Outstanding Tickets for All Offenders:");
        
        boolean foundOutstanding = false;
        for (Ticket ticket : tickets.values()) {
            if (!ticket.isPaid()) {  // Check if the ticket is outstanding
                foundOutstanding = true;
                Driver driver = ticket.getDriver(); // Assuming getDriver() returns the driver associated with the ticket
                System.out.println("\nTicket #" + ticket.getTicketNumber() + "\nOffense: " 
                                    + ticket.getOffenceDescription());
                System.out.println("Driver Name: " + driver.getDriverName() + ", \nTRN: " 
                                    + driver.getTrn() + ", \nAddress: " + driver.getAddress());
                System.out.println("Issue Date: " + ticket.getIssueDate() + ", \nFine: " 
                                    + ticket.getFine().getFineAmount());
                System.out.println("--------------------------------");
            }
        }

        if (!foundOutstanding) {
            System.out.println("No outstanding tickets found.");
        }
    } 

    // 4. View all outstanding tickets by parish
    public void viewOutstandingTicketsByParish(String parish) {
        System.out.println("Outstanding tickets in " + parish + ":");
        System.out.println();
        boolean hasOutstandingTickets = false;

        for (Ticket ticket : tickets.values()) {
            // Check if the ticket is unpaid and if the driver's parish matches the input
            if (!ticket.isPaid() && ticket.getDriver().getAddress().getParish().equalsIgnoreCase(parish)) {
                Driver driver = ticket.getDriver();
                System.out.println("Driver Name: " + driver.getDriverName());
                System.out.println("Address: " + driver.getAddress());
                System.out.println("Ticket Number: " + ticket.getTicketNumber());
                System.out.println("Fine Amount: " + ticket.getFine().getFineAmount());
                System.out.println("Offense Description: " + ticket.getOffenceDescription());
                System.out.println("Ticket Status: " + ticket.getStatus());
                System.out.println("-----------------------------");
                hasOutstandingTickets = true;
            }
        }

        if (!hasOutstandingTickets) {
            System.out.println("No outstanding tickets found in " + parish + ".");
        }
    }


    // 5. Generate report showing all outstanding tickets sorted by parish
    public void generateOutstandingTicketsReport() {
        Map<String, List<Ticket>> ticketsByParish = new HashMap<>();
        
        for (Ticket ticket : tickets.values()) {
            if (ticket.isOutstanding()) {
                String parish = ticket.getDriver().getAddress().getParish();
                ticketsByParish.putIfAbsent(parish, new ArrayList<>());
                ticketsByParish.get(parish).add(ticket);
            }
        }

        for (String parish : ticketsByParish.keySet()) {
            System.out.println("Outstanding tickets in " + parish + ":");
            System.out.println();
            List<Ticket> ticketsInParish = ticketsByParish.get(parish);
            ticketsInParish.sort(Comparator.comparing(Ticket::getIssueDate));
            
            for (Ticket ticket : ticketsInParish) {
                System.out.println(ticket);
            }
        }

     }
    
    public void assignCourtDetailsForOverdueTickets() {
        Date currentDate = new Date(); // Current date

        for (Ticket ticket : tickets.values()) {
            if (!ticket.isPaid() && ticket.getWarrantInfo() == null) {
                // Calculate the difference in days between issueDate and currentDate
                long daysSinceIssue = (currentDate.getTime() - ticket.getIssueDate().getTime()) / (1000 * 60 * 60 * 24);

                if (daysSinceIssue > 21) {
                    // Convert the court date to LocalDate
                    LocalDate courtDate = LocalDate.now().plusDays(7); // Court date: 7 days from today
                    
                    CourtInfo courtInfo = new CourtInfo(
                        "Kingston Traffic Court", // Police station
                        courtDate,                // LocalDate court date
                        "Room 5"                  // Courtroom
                    );
                    ticket.setWarrantInfo(courtInfo);
                }

            }
        }
    }
    
    public void issueWarrantsForNoShows() {
        LocalDate currentDate = LocalDate.now();
        for (Ticket ticket : tickets.values()) {
            if (ticket.getWarrantInfo() != null && !ticket.isPaid() && !ticket.isWarrantIssued()) {
                if (currentDate.isAfter(ticket.getWarrantInfo().getCourtDate())) {
                    // Issue warrant
                    ticket.setWarrantIssued(true);
                    System.out.println("Warrant issued for driver: " + ticket.getDriver().getDriverName() +
                                       ", Ticket #" + ticket.getTicketNumber());
                }
            }
        }
    } 

}
