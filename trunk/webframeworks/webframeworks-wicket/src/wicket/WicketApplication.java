package wicket;


/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see wicket.myproject.Start#main(String[])
 */
public class WicketApplication extends org.apache.wicket.protocol.http.WebApplication
{    
    /**
     * Constructor
     */
	public WicketApplication()
	{
		mountBookmarkablePage("home", HomePage.class);
	}
	
	/**
	 * @see wicket.Application#getHomePage()
	 */
	public Class<?> getHomePage()
	{
		return HomePage.class;
	}

}
