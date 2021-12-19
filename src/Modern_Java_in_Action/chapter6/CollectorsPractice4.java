package Modern_Java_in_Action.chapter6;

import java.util.*;

import static Modern_Java_in_Action.chapter6.Dish.menu;
import static java.util.stream.Collectors.*;

public class CollectorsPractice4 {

	public static void main(String[] args) {
        /* 6.4 분할*/
		// p.220 모든 요리 중 채식 요리와 채식이 아닌 요리로 분류
        Map<Boolean, List<Dish>> partitionedMenu = menu.stream().collect(partitioningBy(Dish::isVegetarian)); 		// partitioningBy : 분할 함수
		//System.out.println(partitionedMenu);
        List<Dish> vegetarianDishes = partitionedMenu.get(true);
		//System.out.println(vegetarianDishes);
		List<Dish> nonvegetarianDishes = partitionedMenu.get(false);
		//System.out.println(nonvegetarianDishes);

		// p.220 filter와 list로 해도 동일한 결과
        List<Dish> vegetarianDishes3 = menu.stream().filter(Dish::isVegetarian).collect(toList());

		/* 6.4.1 분할의 장점 */
		// p.220 partitioningBy의 두 번째 인수로 컬렉터를 전달 가능
        Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType 
        						= menu.stream().collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType)));
		//System.out.println(vegetarianDishesByType);

		// p.221 채식 요리와 채식이 아닌 요리 각각의 그룹에서 가장 칼로리가 높은 요리 찾기
        Map<Boolean, Dish> mostCaloricPartitionedByVegetarian
        						= menu.stream().collect(partitioningBy(Dish::isVegetarian, collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get)));
		//System.out.println(mostCaloricPartitionedByVegetarian);

		/* 6.4.2 숫자를 소수와 비소수로 분할하기 */
		Prime primeCheck = new Prime();
		//System.out.println("isPrime => " + primeCheck.isPrime(10));
		//System.out.println("isPrimeList => " + primeCheck.partitionPrimes(10));
        
        /* 6.5 Collector 인터페이스 */
        List<Dish> dishes = menu.stream().collect(new ToListCollector<Dish>());		// new로 인스턴스화
        //System.out.println(dishes);
        List<Dish> dishes2 = menu.stream().collect(toList());
        //System.out.println(dishes2);
	}

}
