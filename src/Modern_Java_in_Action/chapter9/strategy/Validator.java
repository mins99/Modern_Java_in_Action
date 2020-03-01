package Modern_Java_in_Action.chapter9.strategy;

public class Validator {
	private final ValidationStrategy strategy;
	
	public Validator(ValidationStrategy v) {
		this.strategy = v;
	}
	
	public boolean validate(String s) {
		return strategy.execute(s);
	}
	
	// 1. IsNumeric, IsAllLowerCase 구현
	Validator numericValidator = new Validator(new IsNumeric());
	boolean b1 = numericValidator.validate("aaaa");
	
	Validator lowerCaseValidator = new Validator(new IsAllLowerCase());
	boolean b2 = lowerCaseValidator.validate("bbbb");
	
	// 2. 람다 표현식을 직접 전달
	Validator numericValidator2 = new Validator((String s) -> s.matches("[a-z]+"));
	boolean b3 = numericValidator.validate("aaaa");
	
	Validator lowerCaseValidator2 = new Validator((String s) -> s.matches("\\d+"));
	boolean b4 = lowerCaseValidator.validate("bbbb");
}
