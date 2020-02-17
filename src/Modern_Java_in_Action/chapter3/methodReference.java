package Modern_Java_in_Action.chapter3;

import java.util.*;
//import java.util.Comparator.comparing;
import static java.util.Comparator.*;
import java.util.function.*;

import Modern_Java_in_Action.chapter2.Apple;

public class methodReference {
	
	private boolean isValidName(String string) {
		return Character.isUpperCase(string.charAt(0));
	}
	
	enum Color { RED, GREEN }
	public static final String GREEN = "GREEN";
	public static final String RED = "RED";
	
	// p.119 생성자 참조
	List<Integer> weights = Arrays.asList(7,3,4,10);
	List<Apple> apples = map(weights, Apple::new);
	public List<Apple> map(List<Integer> list, Function<Integer, Apple> f) {
		
		List<Apple> result = new ArrayList<>();
		for(Integer i : list) {
			result.add(f.apply(i));
		}
		return result;
	}


	public static void main(String[] args) {
		//public Color color;
		List<Apple> inventory =  Arrays.asList(new Apple(80, GREEN), new Apple(155, GREEN), new Apple(120, RED));

		// 1. 기존 코드
		inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
		
		// 2. 메서드 참조
		//inventory.sort(Comparator.comparing(Apple::getWeight));
		inventory.sort(comparing(Apple::getWeight));
		
		List<String> str = Arrays.asList("a","b","A","B");
		str.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
		str.sort(String::compareToIgnoreCase);
		
		ToIntFunction<String> stringToInt = (String s) -> Integer.parseInt(s);
		Function<String, Integer> stringToInt2 = Integer::parseInt;
		
		BiPredicate<List<String>, String> contains = (list, element) -> list.contains(element);
		BiPredicate<List<String>, String> contains2 = List::contains;
		
		//Predicate<String> startsWithNumber = (String string) -> this.startsWithNumber(string);
		//Predicate<String> startsWithNumber2 = this::startsWithNumber;
		
		// p.119 3. 생성자 참조
		Supplier<Apple> c1 = Apple::new;
		//Supplier<Apple> c1 = () -> new Apple();
		Apple a1 = c1.get();
		
		Function<Integer, Apple> c2 = Apple::new;
		//Function<Integer, Apple> c2 = (weight) -> new Apple(weight);
		Apple a2 = c2.apply(110);
		
	}

}
