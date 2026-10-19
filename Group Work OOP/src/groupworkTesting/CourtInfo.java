package groupworkTesting;
import java.time.LocalDate;

//Kadedra Mason
//ID: 2304879

public class CourtInfo {
	
	    private String policeStation;
	    private LocalDate courtDate;
	    private String courtRoom;

	    // Constructor
	    public CourtInfo(String policeStation, LocalDate courtDate, String courtRoom) {
	        this.policeStation = policeStation;
	        this.courtDate = courtDate;
	        this.courtRoom = courtRoom;
	    }

	    // Getters and Setters
	    public String getPoliceStation() {
	        return policeStation;
	    }

	    public void setPoliceStation(String policeStation) {
	        this.policeStation = policeStation;
	    }

	    public LocalDate getCourtDate() {
	        return courtDate;
	    }

	    public void setCourtDate(LocalDate courtDate) {
	        this.courtDate = courtDate;
	    }

	    public String getCourtRoom() {
	        return courtRoom;
	    }

	    public void setCourtRoom(String courtRoom) {
	        this.courtRoom = courtRoom;
	    }

	    @Override
	    public String toString() {
	        return "Court Location: " + policeStation + ", Court Date: " + courtDate + ", Court Room: " + courtRoom;
	    }

}
