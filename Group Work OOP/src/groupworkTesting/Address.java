package groupworkTesting;

//By: Kadedra Mason
//ID: 2304879

public class Address {
	
	private String parish;
    private String street;
    private String city;
    
    
    public Address() {
          //not set
    }

    // Constructor with validation
    public Address(String street, String city, String parish) {
        if (parish == null || parish.isEmpty() || street == null || street.isEmpty() || city == null || city.isEmpty()) {
            throw new IllegalArgumentException("Parish, street, and city cannot be null or empty");
        }
        this.street = street;
        this.city = city;
        this.parish = parish;
        
    }
    
    public Address(Address adr, String city) {
    	this.street = adr.street;
    	this.city = city;
        this.parish = adr.parish;
    }

    // Getters
    public String getParish() {
        return parish;
    }

    public void setParish(String parish) {
    	this.parish = parish;
    }
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
    	this.street = street;
    }

    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
    	 this.city = city;
    }

    @Override
    public String toString() {
        return street + ", " + city + ", " + parish;
    }

}
