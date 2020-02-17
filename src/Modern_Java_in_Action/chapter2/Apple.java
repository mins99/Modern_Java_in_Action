package Modern_Java_in_Action.chapter2;

public class Apple {
	
	String color;
	Integer weight;
	
	
	public Apple() {
		this.color = "RED";
		this.weight = 130;
	}
	
	public Apple(Integer weight) {
		this.color = "RED";
		this.weight = weight;
	}
	
	public Apple(Integer weight, String color) {
		this.color = color;
		this.weight = weight;
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "Apple [color=" + color + ", weight=" + weight + "]";
	}
	
	
}
