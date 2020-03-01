package Modern_Java_in_Action.chapter9.chainOfResponsibility;

public class SpellCheckerProcessing extends ProcessingObject<String> {
	public String handleWork(String text) {
		return text.replaceAll("labda", "lambda");
	}
}
