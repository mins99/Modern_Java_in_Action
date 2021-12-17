package Modern_Java_in_Action.chapter6;

public class Transaction {

	private final Currency currency;
	private final double value;

	public Transaction(Currency currency, double value) {
		this.currency = currency;
		this.value = value;
	}

	public Currency getCurrency() {
		return currency;
	}

	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return currency + " " + value;
	}

}
