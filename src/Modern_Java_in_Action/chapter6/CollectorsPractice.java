package Modern_Java_in_Action.chapter6;

import java.util.*;

import static java.util.stream.Collectors.*;
import static Modern_Java_in_Action.chapter6.Dish.menu;

public class CollectorsPractice {
	
	public static enum CaloricLevel { DIET, NORMAL, FAT }

    public static List<Transaction> transactions = Arrays.asList(
            new Transaction(Currency.EUR, 1500.0),
            new Transaction(Currency.USD, 2300.0),
            new Transaction(Currency.GBP, 9900.0),
            new Transaction(Currency.EUR, 1100.0),
            new Transaction(Currency.JPY, 7800.0),
            new Transaction(Currency.CHF, 6700.0),
            new Transaction(Currency.EUR, 5600.0),
            new Transaction(Currency.USD, 4500.0),
            new Transaction(Currency.CHF, 3400.0),
            new Transaction(Currency.GBP, 3200.0),
            new Transaction(Currency.USD, 4600.0),
            new Transaction(Currency.JPY, 5700.0),
            new Transaction(Currency.EUR, 6800.0)
    );
	
	public static void main(String[] args) {

        /* 6.1을 시작하기 전에 */

        /*
        * 1. 통화별로 트랜잭션을 그룹화한 다음에 해당 통화로 일어난 모든 트랜잭션 합계를 계산하시오(Map<Currency, Integer>)
        * 2. 트랜잭션을 비싼 트랜잭션과 저렴한 트랜잭션 두 그룹으로 분류하시오(Map<Boolean, List<Transaction>>)
        * 3. 트랜잭션을 도시 등 다수준으로 그룹화하시오. 그리고 각 트랜잭션이 비싼지 저렴한지 구분하시오(Map<String, Map<Boolean, List<Transaction>>)
        * */

        // p.198 예제 6-1 통화별로 트랜잭션을 그룹화(명령형 프로그래밍 버전)
        Map<Currency, List<Transaction>> transactionsByCurrencies = new HashMap<>();
        for (Transaction transaction : transactions) {
            Currency currency = transaction.getCurrency();
            List<Transaction> transactionsForCurrency = transactionsByCurrencies.get(currency);
            if (transactionsForCurrency == null) {
                transactionsForCurrency = new ArrayList<>();
                transactionsByCurrencies.put(currency, transactionsForCurrency);
            }
            transactionsForCurrency.add(transaction);
        }
        //System.out.println(transactionsByCurrencies);

        // Stream(함수형 프로그래밍 버전)
        Map<Currency, List<Transaction>> transactionsByCurrencies2 = transactions.stream().collect(groupingBy(Transaction::getCurrency));
        //System.out.println(transactionsByCurrencies2);

        

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
