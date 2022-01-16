package Modern_Java_in_Action.chapter10;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class innerDSL {
	
	public static void main(String[] args) {
		// forEach를 이용하여 문자열 목록 출력하기
		List<String> numbers = Arrays.asList("one", "two", "three");

		// 1. 익명 내부 클래스
		numbers.forEach(new Consumer<String>() {    // 코드의 잡음1
			@Override
			public void accept(String s) {      	// 코드의 잡음2
				System.out.println(s);      		// 코드의 잡음3
			}
		});

		// 2. 람다 표현식
		//numbers.forEach(s -> System.out.println(s));

		// 3. 메서드 참조
		//numbers.forEach(System.out::println);
	}
	
}
