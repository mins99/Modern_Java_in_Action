package Modern_Java_in_Action.chapter9.chainOfResponsibility;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class chainOfResponsibilityMain {

	public static void main(String[] args) {
		ProcessingObject<String> p1 = new HeaderTextProcessing();
		ProcessingObject<String> p2 = new SpellCheckerProcessing();
		
		p1.setSuccessor(p2);
		
		String result = p1.handle("Aren't labdas really sexy?!");
		System.out.println(result);
		
		// 람다 표현식 사용 -> UnaryOperator<String> 형식의 인스턴스와 andThen 메서드로 함수를 조합하여 체인 생성
		UnaryOperator<String> headerProcessing = (String text) -> "From Raoul, Mario and Alan : " + text;
		UnaryOperator<String> spellCheckerProcessing = (String text) -> text.replaceAll("labda", "lambda");
		
		Function<String, String> pipeline = headerProcessing.andThen(spellCheckerProcessing);
		
		String result2 = pipeline.apply("Aren't labdas really sexy?!");
		System.out.println(result2);
	}

}
