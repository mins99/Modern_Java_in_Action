package Modern_Java_in_Action.chapter9.strategy;

public class IsAllLowerCase implements ValidationStrategy {
	public boolean execute(String s) {
		return s.matches("[a-z]+");
	}
}
