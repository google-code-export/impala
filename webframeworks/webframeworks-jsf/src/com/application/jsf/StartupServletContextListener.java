package com.application.jsf;

import javax.servlet.ServletContextEvent;

public class StartupServletContextListener extends org.apache.myfaces.webapp.StartupServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        super.contextDestroyed(event);
        event.getServletContext().removeAttribute(org.apache.myfaces.webapp.StartupServletContextListener.class.getName() + ".FACES_INIT_DONE");
    }

}
