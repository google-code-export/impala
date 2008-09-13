package servlet;

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
		
		response.getWriter().println("Entering servlet controller delegate");
		ApplicationContext applicationContext = (ApplicationContext) request.getAttribute("spring.context");
		
		/*
		Cannot execute this because WebApplicationContextUtils.getApplicationContext only gives us the root context
		SharedBean bean = (SharedBean) applicationContext.getBean("dao");
		bean.executeMe();
		response.getWriter().write("Just executed bean " + SharedBean.class.getName() + ": " + bean);
		*/
		
		response.getWriter().println("Can still access class, though: " + SharedBean.class.getName());
		new SharedBean().executeMe();
		
		EntryDAO entryDAO = (EntryDAO) applicationContext.getBean("entryDAO");
		int year = 1996;
		Collection<Entry> entries = entryDAO.getEntriesWithCount(year);
		response.getWriter().println("Just got " + entries.size() + " entries of count " + year);
		
		return null;
	}

}
