package Modern_Java_in_Action.chapter9.observer;

public interface Subject {
	void registerObserver(Observer o);
	void notifyObservers(String tweet);
}
