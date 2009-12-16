import org.impalaframework.web.StartJetty;
import org.impalaframework.web.WebConstants;


public class StartServer {
    public static void main(String[] args) {
        System.setProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM, "classpath:impala-embedded.properties");
        StartJetty.main(new String[]{"8080", "../maven-host/src/main/webapp", "/maven-host"});
    }
}
