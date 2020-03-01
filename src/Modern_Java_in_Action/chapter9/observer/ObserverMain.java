package Modern_Java_in_Action.chapter9.observer;

public class ObserverMain {

	public static void main(String[] args) {
		Feed f = new Feed();
		
		//f.registerObserer(new NYTimes());
		//f.registerObserer(new Guardian());
		f.registerObserer(new LeMonde());
		
		f.notifyObservers("The queen said her favourite book is Modern Java in Action!");
		
		// 람다 표현식 사용(옵저버를 명시적으로 인스턴스화하지 않고 직접 전달)
		f.registerObserer((String tweet) -> {
			if(tweet != null && tweet.contains("money")) {
				System.out.println("Breaking news in NY! " + tweet);
			}
		});
		
		f.registerObserer((String tweet) -> {
			if(tweet != null && tweet.contains("queen")) {
				System.out.println("Yet more news from London... " + tweet);
			}
		});
		
		f.notifyObservers("The queen said her favourite book is Modern Java in Action!");

	}

}
