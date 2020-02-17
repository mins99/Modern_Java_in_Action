package Modern_Java_in_Action.chapter2;

//녹색 사과 선택
public class AppleGreenColorPredicate implements ApplePredicate {
	public static final String GREEN = "GREEN";
	public static final String RED = "RED";
	
	public boolean test(Apple apple) {
		return GREEN.equals(apple.getColor());
	}
}
