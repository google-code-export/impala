package web;

import java.util.HashMap;

import interfaces.MessageService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class MessageController  implements Controller {
	private MessageService messageService;

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("message", messageService.getMessage());

		ModelAndView mav = new ModelAndView("test", map);
		return mav;
	}
}