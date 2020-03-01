package Modern_Java_in_Action.chapter9.templeteMethod;
import java.util.function.Consumer;

public class OnlineBankingLambda {
	// 더미 Customer 클래스
	static private class Customer {}

	// 더미 Database 클래스
	static private class Database {
		static Customer getCustomerWithId(int id) {
		      return new Customer();
		}
	}
	
	public void processCustomer(int id, Consumer<Customer> makeCustomerHappy) {
		Customer c = Database.getCustomerWithId(id);
		makeCustomerHappy.accept(c);
	}
	
	public static void main(String[] args) {
	    new OnlineBankingLambda().processCustomer(1337, (Customer c) -> System.out.println("Hello!"));
	  }
}
