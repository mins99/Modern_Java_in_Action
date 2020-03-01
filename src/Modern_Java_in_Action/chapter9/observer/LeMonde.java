package Modern_Java_in_Action.chapter9.observer;

public class LeMonde implements Observer {
	public void notify(String tweet) {
		if(tweet != null && tweet.contains("wine")) {
			System.out.println("Today cheese, wine and news! " + tweet);
		}
	}
}
