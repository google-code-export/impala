package tapestry5.application.pages;

import java.util.Date;

/**
 * Start page of application.
 */
public class Home {
	
	public Date getCurrentTime() {
		return new Date();
	}
	
	public String getMessage(){
		return "Hello World";
	}
	
}