package servlet;

import interfaces.MessageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ServletControllerDelegate implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.getWriter().println("Entering servlet controller delegate");
		ApplicationContext applicationContext = (ApplicationContext) request.getAttribute("spring.context");
		
		MessageService bean = (MessageService) applicationContext.getBean("messageService");
		response.getWriter().write("A message: " + bean.getMessage() + ": " + bean);
		
		return null;
	}

}
