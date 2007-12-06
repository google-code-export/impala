package classes;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(objectName = "wineorder:name=JMXHelper", description = "Example MBean")
public class JMXHelper {

	@ManagedOperation(description = "Example operation")
	public String exampleOperation() {
		String message = ">>>>>>>>>>>>>>>>>>>>>>>>>>>> Executing managed operation";
		System.out.println(message);
		return message;
	}
}
