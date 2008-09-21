package struts;


public class ValueHolder {
	
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
