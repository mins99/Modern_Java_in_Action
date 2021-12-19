package Modern_Java_in_Action.chapter6;

import java.util.*;

import static Modern_Java_in_Action.chapter6.Dish.menu;
import static java.util.stream.Collectors.*;

public class CollectorsPractice3 {

	public static void main(String[] args) {
        /* 6.3 그룹화 */
        // p.210 그룹화 Grouping
        Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType));
		//System.out.println(dishesByType);
        
        // p.211 Grouping의 분류 함수로 람다 표현식이 가능
        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(groupingBy(dish -> {
        		if(dish.getCalories() <= 400) return CaloricLevel.DIET;
        		else if(dish.getCalories() <= 700) return CaloricLevel.NORMAL;
        		else return CaloricLevel.FAT;
        }));
		//System.out.println(dishesByCaloricLevel);
        
        /* 6.3.1 그룹화된 요소 조작 */
        // p.212 전자로 하면 조건에 해당하지 않는 분류가 나타나지 않는 문제가 발생
        Map<Dish.Type, List<Dish>> caloricDishesByType = menu.stream().filter(dish -> dish.getCalories() > 500).collect(groupingBy(Dish::getType));
        //System.out.println(caloricDishesByType);
        //Map<Dish.Type, List<Dish>> caloricDishesByType2 = menu.stream().collect(groupingBy(Dish::getType, filtering(dish -> dish.getCalories() > 500, toList())));
        
        // p.212 mapping으로 그룹의 각 요리를 관련 이름 목록으로 변환
        Map<Dish.Type, List<String>> dishNamesByType = menu.stream().collect(groupingBy(Dish::getType, mapping(Dish::getName, toList())));
		//System.out.println(dishNamesByType);
		//Map<Dish.Type, Set<String>> dishNamesByType2 = menu.stream().collect(groupingBy(Dish::getType, flatmapping(dish -> dishTags.get(dish.getName()).stream(), toSet())));

		/* 6.3.2 다수준 그룹화 */
        // p.214 type -> calories 다수준 그룹화
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
		//System.out.println(dishesByTypeCaloricLevel);
        
        /* 6.3.3 서브그룹으로 데이터 수집 */
        // p.215 메뉴에서 요리의 수를 종류별로 계산
        Map<Dish.Type, Long> typesCount = menu.stream().collect(groupingBy(Dish::getType, counting()));
		//System.out.println(typesCount);
        
        // p.216 메뉴에서 가장 높은 칼로리를 가진 요리를 계산
        Map<Dish.Type, Optional<Dish>> mostCaloricByType = menu.stream().collect(groupingBy(Dish::getType, maxBy(Comparator.comparingInt(Dish::getCalories))));
		//System.out.println(mostCaloricByType);

        // p.216 Map의 값에 Optional 사용안하기
        Map<Dish.Type, Dish> mostCaloricByType2 = menu.stream().collect(groupingBy(Dish::getType, collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get)));
		//System.out.println(mostCaloricByType2);
        
        /* groupingBy의 두 번째 인수로 전달할 컬렉터 사용 */
        // p.218 메뉴들의 칼로리 합(summingInt)
        Map<Dish.Type, Integer> totalCaloriesByType = menu.stream().collect(groupingBy(Dish::getType, summingInt(Dish::getCalories)));
		//System.out.println(totalCaloriesByType);
        
        // p.218 요리 형식에 존재하는 모든 CaloricLevel값 계산
        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType = menu.stream().collect(groupingBy(Dish::getType, mapping(dish -> {
																																	if(dish.getCalories() <= 400)
																																		return CaloricLevel.DIET;
																																	else if(dish.getCalories() <= 700)
																																		return CaloricLevel.NORMAL;
																																	else
																																		return CaloricLevel.FAT;
																																}, toSet())));
		//System.out.println(caloricLevelsByType);
        
        // p.219 toSet 대신 toCollection 으로 형식 정하기
        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType2 = menu.stream().collect(groupingBy(Dish::getType, mapping(dish -> {
																																	if(dish.getCalories() <= 400)
																																		return CaloricLevel.DIET;
																																	else if(dish.getCalories() <= 700)
																																		return CaloricLevel.NORMAL;
																																	else
																																		return CaloricLevel.FAT;
																																}, toCollection(HashSet::new))));
		//System.out.println(caloricLevelsByType2);

	}

}
