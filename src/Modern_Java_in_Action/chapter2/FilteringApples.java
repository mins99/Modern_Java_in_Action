package Modern_Java_in_Action.chapter2;

import java.util.*;


public class FilteringApples {
	
	public static final String GREEN = "GREEN";
	public static final String RED = "RED";
	
	private boolean isValidName(String string) {
		return Character.isUpperCase(string.charAt(0));
	}

	public static void main(String[] args) {
		
		List<Apple> inventory =  Arrays.asList(new Apple(80, GREEN), new Apple(155, GREEN), new Apple(120, RED));
		
		// 1. 동작 파라미터화
		List<Apple> heavyApples = filterApples(inventory, new AppleHeavyWeightPredicate());
		List<Apple> greenApples = filterApples(inventory, new AppleGreenColorPredicate());

		System.out.println(heavyApples.toString());
		System.out.println(greenApples.toString());
		System.out.println("================================");
		
		// 2. 람다 표현식 사용
		List<Apple> redApples = filterApples(inventory, (Apple apple) -> RED.equals(apple.getColor()));
		List<Apple> heavyApples2 = filterApples(inventory, (Apple apple) -> apple.getWeight() > 150);
		
		System.out.println(heavyApples2.toString());
		System.out.println(redApples.toString());
		System.out.println("================================");
		
		// 3. 리스트 형식으로 추상화
		List<Integer> numbers = Arrays.asList(1,2,3,4,5);
		List<Apple> redApples2 = filter(inventory, (Apple apple) -> RED.equals(apple.getColor()));
		List<Integer> evenNumbers = filter(numbers, (Integer i) -> i % 2 == 0);
		
		System.out.println(redApples2.toString());
		System.out.println(evenNumbers.toString());
		System.out.println("================================");
		
		// 4. Comparator로 정렬하기
		inventory.sort(new Comparator<Apple>() {
			public int compare(Apple a1, Apple a2) {
				return a1.getWeight().compareTo(a2.getWeight());
			}
		});
		
		inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
	}

	// 1. 동작 파라미터화
	public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
		List<Apple> result = new ArrayList<>();
		
		for(Apple apple : inventory) {
			if(p.test(apple)) {
				result.add(apple);
			}
		}
		
		return result;
	}
	
	// 2. 람다 표현식 사용
	public static <T> List<T> filter(List<T> list, Predicate<T> p) {
		List<T> result = new ArrayList<>();
		for(T e: list) {
			if(p.test(e)) {
				result.add(e);
			}
		}
		return result;
	}

}
