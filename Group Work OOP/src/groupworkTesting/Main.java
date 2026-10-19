package groupworkTesting;
import java.util.*;

//import java.time.LocalDate;
import java.text.SimpleDateFormat;
//import java.time.ZoneId;
import java.util.Date;
//Done by: Kadedra Mason
//ID: 2304879
public class Main {
	
    private static Jcf officer;
    private static Driver driver;
    private static Map<Integer, Ticket> tickets = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    // Define valid credentials for JCF Officers
    private static final String JCF_USERNAME = "jcf_officer";
    private static final String JCF_PASSWORD = "jcf_password";
    
    // Define valid credentials for Processing Officers
    private static final String OFFICER_USERNAME = "processing_officer";
    private static final String OFFICER_PASSWORD = "officer_password";
    
    // Method to handle JCF Officer login
    public static boolean jcfLogin(String username, String password) {
        return username.equals(JCF_USERNAME) && password.equals(JCF_PASSWORD);
    }
    
    // Method to handle Processing Officer login
    public static boolean officerLogin(String username, String password) {
        return username.equals(OFFICER_USERNAME) && password.equals(OFFICER_PASSWORD);
    }
    
    // General login method for any officer type (JCF or Processing)
    public static void officerlogin() {
        
        System.out.println("=== Officer Login ===");
        System.out.print("Enter your Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter your Password: ");
        String password = scanner.nextLine();

        // Attempt login for JCF Officer first
        if (jcfLogin(username, password)) {
            System.out.println("\nWelcome JCF Officer!\n");
            jcfOfficerMenu();  // Assuming you have a method for the JCF Officer menu
        }
        // If JCF login fails, attempt Processing Officer login
        else if (officerLogin(username, password)) {
            System.out.println("\nWelcome Processing Officer!\n");
            processingOfficerMenu();  // Assuming you have a method for the Processing Officer menu
        } else {
            System.out.println("Invalid Username or Password. Please try again.");
        }
    }
    
    
    
    public static void main(String[] args) {
    	
        officer = new Jcf("001", "Officer John", "Downtown Station");
        driver = new Driver();
     
        while (true) {
            System.out.println("\n=== Ticket Management System ===");
            System.out.println("Select your role:");
            System.out.println("1. Driver");
            System.out.println("2. Processing Officer");
            System.out.println("3. JCF Officer");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int roleChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.println("\n");
            
            switch (roleChoice) {
                case 1:
                	login();
                    break;
                case 2:
                    officerlogin();
                    break;
                case 3:
                    officerlogin();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }



    private static void login() {
        System.out.println("=== Driver Login ===");
        System.out.print("Enter your TRN: ");
        String trn = scanner.nextLine();

        // Check if the driver exists in the drivers map
        driver = officer.getDriverByTrn(trn);  // Assuming `officer` is your JCF instance with the map of drivers

        if (driver == null) {
            System.out.println("Invalid TRN. Please try again.");
        } else {
            System.out.println("Welcome " + driver.getDriverName() + "!");
            driverMenu();  // Once logged in, show the driver menu
        }
    }

    private static void driverMenu() {
        while (true) {
            System.out.println("\n=== Driver Menu ===");
            System.out.println("1. View My Tickets");
            System.out.println("2. Pay Ticket");
            System.out.println("3. Check Court Details");
            System.out.println("4. Check Non-Past Due Tickets");
            System.out.println("5. Check for Warrants");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            System.out.println("\n");

            switch (choice) {
                case 1:
                    viewTickets();
                    break;
                case 2:
                    payTicket();
                    break;
                case 3:
                	officer.assignCourtDetailsForOverdueTickets();
                    driver.checkCourtDetails(); // Checks and prints warrant/court info
                    break;
                case 4:
                	String driverTrn = driver.getTrn();
                    driver.viewTicketPayments(driverTrn);
                    break;
                case 5:
                	checkWarrantStatus(driver);
                    break;
                case 6:
                    return; // Exit back to the main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    private static void viewTickets() {
        System.out.println("\n=== My Tickets ===");
        List<Ticket> sortedTickets = driver.getSortedTickets();  // Assuming this method sorts tickets by date or other criteria
        
        if (sortedTickets.isEmpty()) {
            System.out.println("You have no tickets.");
        } else {
            for (Ticket ticket : sortedTickets) {
                System.out.println("Ticket Number: " + ticket.getTicketNumber());
                System.out.println("Offence Code: " + ticket.getOffenceCode());
                System.out.println("Offence Description: " + ticket.getOffenceDescription());
                System.out.println("Ticket Issue Date: " + ticket.getIssueDate());
                System.out.println("Payment Due Date: " + ticket.getPaymentDueDate());
                System.out.println("Fine Amount: " + ticket.getFineAmount());
                System.out.println("Ticket Status: " + ticket.getStatus()); // Paid, Unpaid, Warrant Outstanding

                // Check if the warrantInfo is not null before trying to access court details
                if (ticket.getWarrantInfo() != null) {
                    System.out.println("Court Date: " + ticket.getWarrantInfo().getCourtDate());
                    System.out.println("Court Location: " + ticket.getWarrantInfo().getPoliceStation());
                } else {
                    System.out.println("No court details assigned yet.");
                }
                System.out.println("\n");
            }
        }
    }



    private static void payTicket() {
        System.out.print("Enter ticket number to pay: ");
        int ticketNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Ticket ticket = driver.getTicketByNumber(ticketNumber);  // Assuming `getTicketByNumber` retrieves the ticket by number

        if (ticket != null) {
            if (!ticket.isPaid()) {  // Assuming `isPaid()` checks if the ticket is already paid
                double amount = ticket.getFineAmount();  // Assuming `getFineAmount()` returns the fine amount for the ticket
                ticket.payTicket();  // Mark ticket as paid

                System.out.println("Ticket " + ticketNumber + " has been paid. Amount: $" + amount);
            } else {
                System.out.println("This ticket has already been paid.");
            }
        } else {
            System.out.println("Ticket not found.");
        }
    }


    private static void checkWarrantStatus(Driver driver) {
        boolean foundWarrant = false;

        // Iterate through tickets of the driver
        for (Ticket ticket : tickets.values()) {
            // Check if this ticket belongs to the driver and if a warrant has been issued
            if (ticket.getDriver().getTrn().equals(driver.getTrn()) && ticket.isWarrantIssued()) {
                foundWarrant = true;
                CourtInfo courtInfo = ticket.getWarrantInfo(); // Get court information

                if (courtInfo != null) {
                    // Print details of the police station and court room
                    System.out.println("Warrant Issued for Ticket #" + ticket.getTicketNumber());
                    System.out.println("Police Station: " + courtInfo.getPoliceStation());
                    System.out.println("Court Date: " + courtInfo.getCourtDate());
                    System.out.println("Court Room: " + courtInfo.getCourtRoom());
                }
            }
        }

        if (!foundWarrant) {
            System.out.println("No warrants found for this driver.");
        }
    }
  

    public static void processingOfficerMenu() {
        // Assuming drivers and tickets are loaded elsewhere (you may load them from a database or file)
        Map<String, Driver> driverMap = new HashMap<>(); // Replace with actual loading of drivers
        Map<Integer, Ticket> ticketMap = new HashMap<>(); // Replace with actual loading of tickets

        // Create ProcessingOfficer instance with driverMap and ticketMap
        ProcessingOfficer processingOfficer = new ProcessingOfficer(driverMap, ticketMap);

        while (true) {
            System.out.println("=== Processing Officer Menu ===");
            System.out.println("1. Create Application");
            System.out.println("2. Check tickets");
            System.out.println("3. Update Application");
            System.out.println("4. Delete Application");
            System.out.println("5. Generate Applicant Report");
            System.out.println("6. Save and Exit");
            System.out.print("Select an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.println("\n");

            switch (choice) {
                case 1:
                    // Input details for a new applicant
                    System.out.print("Enter applicant's name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter applicant's TRN: ");
                    String trn = scanner.nextLine();

                    System.out.print("Enter applicant's date of birth (e.g., 01/01/1990): ");
                    String dateOfBirth = scanner.nextLine();

                    System.out.print("Enter applicant's address: ");
                    String address = scanner.nextLine();

                    System.out.print("Enter applicant's contact number: ");
                    String contactNumber = scanner.nextLine();

                    System.out.print("Enter applicant's email address: ");
                    String email = scanner.nextLine();

                    //System.out.print("Enter application ID: ");
                    //int applicationID = scanner.nextInt();
                    //scanner.nextLine(); // Consume the newline character

                    // Ask the required qualification questions
                    System.out.print("Has the driver caused any accidents in the last two years? (yes/no): ");
                    String accidentResponse = scanner.nextLine().trim().toLowerCase();
                    boolean hasAccidents = accidentResponse.equals("yes");

                    System.out.print("Does the driver have a negative police record? (yes/no): ");
                    String policeRecordResponse = scanner.nextLine().trim().toLowerCase();
                    boolean hasNegativePoliceRecord = policeRecordResponse.equals("yes");

                    System.out.print("Does the driver have any outstanding tickets? (yes/no): ");
                    String ticketsResponse = scanner.nextLine().trim().toLowerCase();
                    boolean hasOutstandingTickets = ticketsResponse.equals("yes");

                    // Try to retrieve the Driver object based on TRN
                    Applicant newApplicant = new Applicant(name, dateOfBirth, address, contactNumber, email, trn);
                    Driver driver = processingOfficer.getDriverByTrn(trn); // Method to get the driver details using TRN

                    if (driver != null) {
                        // Driver found, check for conditions and process application
                        processingOfficer.createApplication(newApplicant, hasAccidents, hasNegativePoliceRecord, hasOutstandingTickets);
                    } else {
                        // Driver not found, assume no issues for this applicant
                        processingOfficer.createApplication(newApplicant, hasAccidents, hasNegativePoliceRecord, hasOutstandingTickets);
                    }
                    break;

                case 2:
                    // Check tickets for a driver
                    System.out.print("Enter the TRN of the driver to check tickets: ");
                    String driverTrn = scanner.nextLine();

                    // Assuming you already have the method to view tickets for a driver
                    processingOfficer.viewDriverTickets(officer, driverTrn);
                    break;

                case 3:
                    // Prompt for TRN and new details, then call updateApplication
                    System.out.print("Enter applicant's TRN to update: ");
                    String updateTRN = scanner.nextLine();

                    System.out.print("Has accidents? (true/false): ");
                    boolean updateHasAccidents = scanner.nextBoolean();

                    System.out.print("Has negative police record? (true/false): ");
                    boolean updateHasNegativePoliceRecord = scanner.nextBoolean();
                    
                    System.out.print("Has outstanding Tickets? (true/false): ");
                    @SuppressWarnings("unused") boolean updateHasOutstandingTickets = scanner.nextBoolean();

                    // Update the application
                    System.out.println("\n");
                 // Assuming newTickets is a Map<String, Ticket>
                    processingOfficer.updateApplication(updateTRN, updateHasAccidents, updateHasNegativePoliceRecord, tickets);
                    System.out.println("\n");
                    break;

                case 4:
                    // Prompt for TRN and call deleteApplication
                    System.out.print("Enter applicant's TRN to delete: ");
                    String deleteTRN = scanner.nextLine();

                    processingOfficer.deleteApplication(deleteTRN);
                    break;

                case 5:
                    // Prompt for TRN and call generateApplicantReport
                    System.out.print("Enter applicant's TRN to generate report: ");
                    String reportTRN = scanner.nextLine();
                    
                    System.out.println("\n");
 
                    processingOfficer.generateApplicantReport(reportTRN);
                    
                    System.out.println("\n");
                    break;

                case 6:
                    processingOfficer.saveToFile();
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

	private static void jcfOfficerMenu() {
        while (true) {
            System.out.println("\n=== JCF Officer Menu ===");
            System.out.println("1. Add Ticket");
            System.out.println("2. View Ticket");
            System.out.println("3. Check Driver Status");
            System.out.println("4. View Outstanding Tickets by Parish");
            System.out.println("5. View All Outstanding Tickets");
            System.out.println("6. Outstanding Tickets Report");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.println("\n");
            
            switch (choice) {
                case 1:
                    addTicket();
                    break;
                case 2:
                    viewTicket();
                    break;
                case 3:
                    checkDriverStatus();
                    break;
                case 4:
                    viewOutstandingTicketsByParish();
                    break;
                case 5:
                    officer.viewAllOutstandingTickets();
                    break;
                case 6:
                    officer.generateOutstandingTicketsReport();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    private static void addTicket() {
        try {
        	
            System.out.print("Enter issue date (MM/dd/yyyy): ");
            String issueDateStr = scanner.nextLine();
            Date issueDate = new SimpleDateFormat("MM/dd/yyyy").parse(issueDateStr);

            System.out.print("Enter offense code: ");
            int offenceCode = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter offense description: ");
            String offenceDescription = scanner.nextLine();

            System.out.print("Enter vehicle license plate: ");
            String vehicleLicensePlate = scanner.nextLine();

            // Get the driver's details
            System.out.print("Enter driver's TRN: ");
            String trn = scanner.nextLine();

            System.out.print("Enter driver's name: ");
            String driverName = scanner.nextLine();

            // New fields added for driver's details
            System.out.print("Enter driver's date of birth (MM/dd/yyyy): ");
            String dob = scanner.nextLine();  // Driver's Date of Birth

            System.out.print("Enter driver's contact number: ");
            String contactNumber = scanner.nextLine();  // Driver's Contact Number

            System.out.print("Enter driver's street: ");
            String street = scanner.nextLine();

            System.out.print("Enter driver's city: ");
            String city = scanner.nextLine();

            System.out.print("Enter driver's parish: ");
            String parish = scanner.nextLine();
            
            System.out.println("\n");

            // Create a Driver object with all the details
            Address driverAddress = new Address(street, city, parish);
			Driver driver = new Driver(trn, driverName, dob, driverAddress, contactNumber);
            // Get the fine associated with the offense
            Fine fine = Fine.getFineByOffenceCode(offenceCode);

            new Ticket(issueDate, offenceCode, offenceDescription, vehicleLicensePlate, driver, officer, fine);

            // Now call addTicket with the correct arguments
            officer.addTicket(issueDate, offenceCode, offenceDescription, vehicleLicensePlate, driver, fine);


            System.out.println("Ticket added successfully for " + driver.getDriverName());

        } catch (Exception e) {
            System.out.println("Error adding ticket: " + e.getMessage());
        }
    }

    private static void viewTicket() {
        try {
            System.out.print("Enter ticket number to view: ");
            int ticketNumber = scanner.nextInt();
            System.out.println("\n");
            officer.viewTicket(ticketNumber);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void checkDriverStatus() {
        System.out.print("Enter driver TRN to check status: ");
        String trn = scanner.nextLine();
        System.out.println("\n");
        officer.checkDriverStatus(trn);
    }

    private static void viewOutstandingTicketsByParish() {
        System.out.print("Enter parish to view outstanding tickets: ");
        String parish = scanner.nextLine();
        System.out.println("\n");
        officer.viewOutstandingTicketsByParish(parish);
    }
    
    /*private static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }*/
}
