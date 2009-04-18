package wicket;

import interfaces.MessageService;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class HomePage extends WebPage {

    private static final long serialVersionUID = 1L;

    @SpringBean(name = "messageService")
    private MessageService messageService;

    public HomePage(final PageParameters parameters) {
        add(new Label("message", messageService.getMessage()));
    }
    
}
