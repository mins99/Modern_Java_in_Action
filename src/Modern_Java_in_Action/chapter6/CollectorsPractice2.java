package Modern_Java_in_Action.chapter6;

import java.util.*;

import static java.util.stream.Collectors.*;
import static Modern_Java_in_Action.chapter6.Dish.menu;

public class CollectorsPractice2 {

    public static void main(String[] args) {

        /* p.202 6.2 리듀싱과 요약 */
        // menu의 갯수를 구하는 counting
        long howManyDishes = menu.stream().collect(counting());
        //System.out.println("howManyDishes = " + howManyDishes);

        long howManyDishes2 = menu.stream().count();
        //System.out.println("howManyDishes2 = " + howManyDishes2);

        /* p.202 6.2.1 스트림값에서 최댓값과 최솟값 검색 */
        // menu 중 칼로리가 가장 높은 요리를 구하는 maxBy(minBy)
        Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
        Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(dishCaloriesComparator));
        //System.out.println("mostCalorieDish = " + mostCalorieDish);

        /* p.204 6.2.2 요약 연산 */
        // menu의 총 칼로리를 계산
        int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
        //System.out.println("totalCalories = " + totalCalories);

        // menu의 평균 칼로리를 계산
        double avgCalories = menu.stream().collect(averagingInt(Dish::getCalories));
        //System.out.println("avgCalories = " + avgCalories);

        // menu의 갯수, 칼로리 합, 평균, 최댓값, 최솟값
        IntSummaryStatistics menuStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));
        //System.out.println(menuStatistics);

        /* 6.2.3 문자열 연결 */
        // 문자열 연결 joining
        String shortMenu = menu.stream().map(Dish::getName).collect(joining());             // map 생략시 오류
        //System.out.println(shortMenu);

        // 문자열 연결 joining - 구분자 사용
        String shortMenu2 = menu.stream().map(Dish::getName).collect(joining(", "));
        //System.out.println(shortMenu2);

        /* 6.2.4 범용 리듀싱 요약 연산 -> 컬렉션 프레임워크의 유연성 */
        // menu의 총 칼로리를 계산(람다표현식)
        int totalCalories2 = menu.stream().collect(reducing(0, Dish::getCalories, (i, j) -> i + j));
        //System.out.println(totalCalories2);

        // menu의 총 칼로리를 계산(변환 함수 sum)
        int totalCalories3 = menu.stream().collect(reducing(0, Dish::getCalories, Integer::sum));
        //System.out.println(totalCalories3);

        // menu의 총 칼로리를 계산(매핑 후 메서드 참조로 결과 스트림을 리듀싱)
        int totalCalories4 = menu.stream().map(Dish::getCalories).reduce(Integer::sum).get();
        //System.out.println(totalCalories4);

        // menu의 총 칼로리를 계산(변환 함수 IntStream 이용한 뒤 sum 호출)
        int totalCalories5 = menu.stream().mapToInt(Dish::getCalories).sum();
        //System.out.println(totalCalories5);
    }
}
