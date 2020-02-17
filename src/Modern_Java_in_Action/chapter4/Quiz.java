package Modern_Java_in_Action.chapter4;

import java.util.*;
import java.util.stream.Collectors;

public class Quiz {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// p.149 4-1
		List<Dish> menu = Arrays.asList(
				new Dish("pork", false, 800, Dish.Type.MEAT),
				new Dish("beef", false, 700, Dish.Type.MEAT),
				new Dish("chicken", false, 400, Dish.Type.MEAT),
				new Dish("french fries", true, 530, Dish.Type.OTHER),
				new Dish("rice", true, 350, Dish.Type.OTHER),
				new Dish("season fruit", true, 120, Dish.Type.OTHER),
				new Dish("pizza", true, 550, Dish.Type.OTHER),
				new Dish("prawns", false, 300, Dish.Type.FISH),
				new Dish("salmon", false, 450, Dish.Type.FISH)
		);
				
		// 자바 8 이전
		List<String> highCaloricDishes = new ArrayList<>();
		Iterator<Dish> iterator = menu.iterator();
		while(iterator.hasNext()) {
			Dish dish = iterator.next();
			if(dish.getCalories() > 300) {
				highCaloricDishes.add(dish.getName());
			}
		}
		System.out.println(highCaloricDishes.toString());
		
		//자바 8 이후
		List<String> highCaloricDishes2 = menu.stream().
																filter(dish -> dish.getCalories() > 300).
																map(Dish::getName).
																collect(Collectors.toList());
		System.out.println(highCaloricDishes2.toString());

	}

}
