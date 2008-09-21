package struts;

import java.io.Serializable;

public class SerializableValueHolder implements Serializable {

	private static final long serialVersionUID = 1L;
	
	int count;

	protected int getCount() {
		return count;
	}

	protected void setCount(int count) {
		this.count = count;
	}

	public void increment() {
		count++;
	}
	
}
