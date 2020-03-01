package Modern_Java_in_Action.chapter9.observer;

public class NYTimes implements Observer {
	public void notify(String tweet) {
		if(tweet != null && tweet.contains("money")) {
			System.out.println("Breaking news in NY! " + tweet);
		}
	}
}
