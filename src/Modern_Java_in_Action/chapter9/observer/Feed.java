package Modern_Java_in_Action.chapter9.observer;

import java.util.ArrayList;
import java.util.List;

public class Feed {
	private final List<Observer> observers = new ArrayList<>();
	
	public void registerObserer(Observer o) {
		this.observers.add(o);
	}
	
	public void notifyObservers(String tweet) {
		observers.forEach(o -> o.notify(tweet));
	}
}
