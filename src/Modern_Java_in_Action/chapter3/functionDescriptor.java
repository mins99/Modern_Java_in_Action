package Modern_Java_in_Action.chapter3;

import java.util.*;

public class functionDescriptor {

	// 1. Predicate
	@FunctionalInterface
	public interface Predicate<T> {
		boolean test(T t);
	}
	
	public static<T> List<T> filter(List<T> list, Predicate<T> p) {
		List<T> results = new ArrayList<>();
		for(T t:list) {
			if(p.test(t)) {
				results.add(t);
			}
		}
		return results;
	}
	
	// 2. Consumer
	@FunctionalInterface
	public interface Consumer<T> {
		void accept(T t);
	}
	
	public static<T> void forEach(List<T> list, Consumer<T> c) {
		for(T t: list) {
			c.accept(t);
		}
	}
	
	// 3. Function
	@FunctionalInterface
	public interface Function<T, R> {
		R apply(T t);
	}
	
	public static<T,R> List<R> map(List<T> list, Function<T,R> f) {
		List<R> result = new ArrayList<>();
		for(T t:list) {
			result.add(f.apply(t));
		}
		return result;
	}
	
	public static void main(String[] args) {
		// 1. Predicate
		List<String> listOfStrings = Arrays.asList("apple", "banana", "cherry", "donut");
		
		Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
		List<String> nonEmpty = filter(listOfStrings, nonEmptyStringPredicate);
		
		// 2. Consumer
		forEach(
				Arrays.asList(1,2,3,4,5),
				(Integer i) -> System.out.println(i)
		);
		
		// 3. Function
		List<Integer> l = map(
			Arrays.asList("lambdas", "in", "action"),
			(String s) -> s.length()
		);
				
	}

}
