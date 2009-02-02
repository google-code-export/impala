package example4.servlet;

import interfaces.EntryDAO;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import shared.SharedBean;

import classes.Entry;

public class ServletControllerDelegate implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.getWriter().println("Servlet controller delegate for example-servlet4");
		ApplicationContext applicationContext = (ApplicationContext) request.getAttribute("spring.context");
		
		response.getWriter().println("Accessed class for shared bean: " + SharedBean.class.getName());
		new SharedBean().executeMe();
		
		EntryDAO entryDAO = (EntryDAO) applicationContext.getBean("entryDAO");
		int count = 1996;
		Collection<Entry> entries = entryDAO.getEntriesWithCount(count);
		response.getWriter().println("Retrieved " + entries.size() + " entries of count " + count);
		
		return null;
	}

}
