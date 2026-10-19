package groupworkTesting;
import java.util.Date;
 
//Kadedra Mason
//ID: 2304879

public abstract class Officer {
	
	
	public abstract void addTicket(Date issueDate, int offenceCode, 
            String offenceDescription, String vehicleLicensePlate, 
            Driver driver, Fine fine);
	public abstract void viewTicket(int ticketNumber);
	

}
