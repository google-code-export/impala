package wicket;

import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;


/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see wicket.myproject.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{    
    
    @Override
    public void init() {
        super.init();
        final SpringComponentInjector springComponentInjector = new SpringComponentInjector(this);
        addComponentInstantiationListener((IComponentInstantiationListener) springComponentInjector);
    }

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
