
public class MainWithException {

	public static void main(String[] args) {
		Exception e = new Exception("A root exception occurred");
		RuntimeException re = new RuntimeException(e);
		throw re;
	}
	
}
