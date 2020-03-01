package Modern_Java_in_Action.chapter9.observer;

public class Guardian implements Observer {
	public void notify(String tweet) {
		if(tweet != null && tweet.contains("queen")) {
			System.out.println("Yet more news from London... " + tweet);
		}
	}
}
