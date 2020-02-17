package Modern_Java_in_Action.chapter2;

//무거운 사과 선택
class AppleHeavyWeightPredicate implements ApplePredicate {
	public boolean test(Apple apple) {
		return apple.getWeight() > 150;
	}
}
