package Modern_Java_in_Action.chapter5;

import java.util.*;
import static java.util.stream.Collectors.toList;

public class Practice {

	public static void main(String[] args) {
		// p.177 5.6 실전연습
		
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
		
		// 1. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리하시오.
        List<Transaction> Q1 = transactions.stream()
        															.filter(i -> i.getYear() == 2011)
        															.sorted(Comparator.comparing(Transaction::getValue))
        															.collect(toList());
        for(Transaction transaction : Q1)
        		System.out.println(transaction.toString());
        System.out.println("\n" + "=====================================================" + "\n");
		
		// 2. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.
        List<String> Q2 = transactions.stream()
        													 .map(i -> i.getTrader())			// i.getTrader().getCity() 형태도 가능
        													 .map(j -> j.getCity())
        													 .distinct()
        													 .collect(toList());
        System.out.println(Q2.toString());
        System.out.println("\n" + "=====================================================" + "\n");
        													 
		
		// 3. 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오.
        List<Trader> Q3 = transactions.stream()
				 										 .filter(i -> i.getTrader().getCity().equals("Cambridge"))
				 										 .map(j -> j.getTrader())
				 										 .sorted(Comparator.comparing(Trader::getName))
				 										 .distinct()
				 										 .collect(toList());
        for(Trader trader : Q3)
    		System.out.println(trader.toString());
        System.out.println("\n" + "=====================================================" + "\n");
		
		// 4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오.
        List<String> Q4 = transactions.stream()
				 										  .map(i -> i.getTrader().getName())
				 										  .sorted()
				 										  .distinct()
				 										  .collect(toList());			// String type으로 .reduce("", (n1, n2) -> n1 + n2);	도 가능
        System.out.println(Q4.toString());
        System.out.println("\n" + "=====================================================" + "\n");
		
		// 5. 밀라노에 거래자가 있는가?
        Optional<Transaction> Q5 = transactions.stream()
        																	   .filter(i -> i.getTrader().getCity().equals("Milan"))
        																	   .findAny();
        
        /* boolean Q5_1 = transactions.stream()
        													.anyMatch(i -> i.getTrader().getCity().equals("Milan")); */
        
        if(Q5.isPresent()) {
        		System.out.println("There is a trader in Milan.");
        } else {
        		System.out.println("No traders in Milan");
        }
        System.out.println("\n" + "=====================================================" + "\n");
		
		// 6. 케임브리지에 거주하는 거래자의 모든 트랜잭션값을 출력하시오.
        List<Integer> Q6 = transactions.stream()
				 										   .filter(i -> i.getTrader().getCity().equals("Cambridge"))
				 										   .map(Transaction::getValue)
				 										   .collect(toList());
        System.out.println(Q6);
        System.out.println("\n" + "=====================================================" + "\n");
        
		// 7. 전체 트랜잭션 중 최댓값은 얼마인가?
        Optional<Integer> Q7 = transactions.stream()
				   													.map(i -> i.getValue())
				   													.reduce(Integer::max);
        System.out.println(Q7);
        System.out.println("\n" + "=====================================================" + "\n");
        
		// 8. 전체 트랜잭션 중 최솟값은 얼마인가?
        Optional<Integer> Q8 = transactions.stream()
																	.map(i -> i.getValue())
																	.reduce(Integer::min);
        System.out.println(Q8);
        System.out.println("\n" + "=====================================================" + "\n");

	}

}
