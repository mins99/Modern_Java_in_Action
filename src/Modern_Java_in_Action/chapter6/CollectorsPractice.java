package Modern_Java_in_Action.chapter6;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.*;

public class CollectorsPractice {
	
	public static enum CaloricLevel { DIET, NORMAL, FAT }
	
	public static void main(String[] args) {
		Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );
        
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
        
        Map<String, List<String>> dishTags = new HashMap<>();
        dishTags.put("pork", Arrays.asList("greasy", "salty"));
        dishTags.put("beef", Arrays.asList("salty", "roasted"));
        dishTags.put("chicken", Arrays.asList("fried", "crisp"));
        dishTags.put("french fries", Arrays.asList("greasy", "fried"));
        dishTags.put("rice", Arrays.asList("light", "natural"));
        dishTags.put("season fruit", Arrays.asList("fresh", "natural"));
        dishTags.put("pizza", Arrays.asList("tasty", "salty"));
        dishTags.put("prawns", Arrays.asList("tasty", "roasted"));
        dishTags.put("salmon", Arrays.asList("delicious", "fresh"));
        
        // 트랜잭션의 리스트를 구하는 toList
        List<Transaction> transactions2 = transactions.stream().collect(toList());
        
        // menu의 갯수를 구하는 counting
        long howManyDishes = menu.stream().collect(counting());
        long howManyDishes2 = menu.stream().count();
        
        // menu 중 칼로리가 가장 높은 요리를 구하는 maxBy(minBy)
        Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
        Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(dishCaloriesComparator));
        
        // menu의 총 칼로리를 계산
        int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
        
        // menu의 평균 칼로리를 계산
        double avgCalories = menu.stream().collect(averagingInt(Dish::getCalories));
        
        // menu의 갯수, 칼로리 합, 평균 등등
        IntSummaryStatistics menuStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));
        
        // 문자열 연결 joining
        String shortMenu = menu.stream().map(Dish::getName).collect(joining(", "));
        //String shortMenu2 = menu.stream().collect(joining());		//Dish에 name을 반환하는 toString 메서드가 있으면 map(Dish::getName) 생략 가능
        
        
        /* 6.3 그룹화 */
        // 그룹화 Grouping
        Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType));
        
        // Grouping의 분류 함수로는 메서드 참조와 람다 표현식이 가능
        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(groupingBy(dish -> {
        		if(dish.getCalories() <= 400) return CaloricLevel.DIET;
        		else if(dish.getCalories() <= 700) return CaloricLevel.NORMAL;
        		else return CaloricLevel.FAT;
        }));
        
        // Grouping & filter
        // 전자로 하면 조건에 해당하지 않는 분류가 나타나지 않는 문제가 발생
        Map<Dish.Type, List<Dish>> caloricDishesByType = menu.stream().filter(dish -> dish.getCalories() > 500).collect(groupingBy(Dish::getType));
        //Map<Dish.Type, List<Dish>> caloricDishesByType2 = menu.stream().collect(groupingBy(Dish::getType, filtering(dish -> dish.getCalories() > 500, toList())));
        
        // mapping으로 그룹의 각 요리를 관련 이름 목록으로 변환
        Map<Dish.Type, List<String>> dishNamesByType = menu.stream().collect(groupingBy(Dish::getType, mapping(Dish::getName, toList())));
        
        // flatMapping로 요리의 태그를 추출
        //Map<Dish.Type, Set<String>> dishNamesByType2 = menu.stream().collect(groupingBy(Dish::getType, flatmapping(dish -> dishTags.get(dish.getName()).stream(), toSet())));
        
        // 다수준 그룹화
        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel
        = menu.stream().collect(groupingBy(Dish::getType, 
        												groupingBy(dish -> {
        													if(dish.getCalories() <= 400)
        														return CaloricLevel.DIET;
        													else if(dish.getCalories() <= 700)
        														return CaloricLevel.NORMAL;
        													else
        														return CaloricLevel.FAT;
        												})));
        
        // 서브그룹으로 데이터 수집
        // 메뉴에서 요리의 수를 종류별로 계산
        Map<Dish.Type, Long> typesCount = menu.stream().collect(groupingBy(Dish::getType, counting()));
        
        // 메뉴에서 가장 높은 칼로리를 가진 요리를 계산
        Map<Dish.Type, Optional<Dish>> mostCaloricByType = menu.stream().collect(groupingBy(Dish::getType, maxBy(Comparator.comparingInt(Dish::getCalories))));
        // Map의 값에 Optional 사용안하기
        Map<Dish.Type, Dish> mostCaloricByType2 = menu.stream().collect(groupingBy(Dish::getType, 
        																														collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get)));
        
        // groupingBy의 두 번째 인수로 전달할 컬렉터 사용
        // 메뉴들의 칼로리 합
        Map<Dish.Type, Integer> totalCaloriesByType = menu.stream().collect(groupingBy(Dish::getType, summingInt(Dish::getCalories)));
        
        // 요리 형식에 존재하는 모든 CaloricLevel값 계산
        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType = menu.stream().collect(groupingBy(Dish::getType, mapping(dish -> {
																			        															if(dish.getCalories() <= 400)
																			                														return CaloricLevel.DIET;
																			                													else if(dish.getCalories() <= 700)
																			                														return CaloricLevel.NORMAL;
																			                													else
																			                														return CaloricLevel.FAT;
																			        														}, toSet())));
        
        //toCollection 으로 형식 정하기
        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType2 = menu.stream().collect(groupingBy(Dish::getType, mapping(dish -> {
																																					if(dish.getCalories() <= 400)
																																						return CaloricLevel.DIET;
																																					else if(dish.getCalories() <= 700)
																																						return CaloricLevel.NORMAL;
																																					else
																																						return CaloricLevel.FAT;
																																				}, toCollection(HashSet::new))));
        
        
        /* 6.4 분할*/
        Map<Boolean, List<Dish>> partitionedMenu = menu.stream().collect(partitioningBy(Dish::isVegetarian)); 		// partitioningBy : 분할 함수
        List<Dish> vegetarianDishes = partitionedMenu.get(true);
        List<Dish> vegetarianDishes2 = menu.stream().filter(Dish::isVegetarian).collect(toList());
        
        Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType 
        						= menu.stream().collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType)));		// partitioningBy의 두 번째 인수로 컬렉터를 전달 가능
        
        Map<Boolean, Dish> mostCaloricPartitionedByVegetarian
        						= menu.stream().collect(partitioningBy(Dish::isVegetarian, collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get)));
        
        
        /* 6.5 Collector 인터페이스 */
        List<Dish> dishes = menu.stream().collect(new ToListCollector<Dish>());		// new로 인스턴스화
        List<Dish> dishes2 = menu.stream().collect(toList());
        
	}

}
