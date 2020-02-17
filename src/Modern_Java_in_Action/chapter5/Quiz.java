package Modern_Java_in_Action.chapter5;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


public class Quiz {

    public static void main(String[] args) {
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

        // p.161 5-1
        List<Dish> dishes = menu.stream()
                .filter(d -> d.getType() == Dish.Type.MEAT)
                .limit(2)
                .collect(toList());
        
        //System.out.println(dishes.toString());
        dishes.stream().forEach(System.out::println);
        System.out.println("");

        // p.165 5-2 (1)
        List<Integer> numbers = Arrays.asList(1,3,5,7,9);
        List<Integer> squares = numbers.stream()
                .map(number -> number*number)
                .collect(toList());
        
        System.out.println(squares);
        System.out.println("");

        // p. 166 5-2 (2)
        List<Integer> num1 = Arrays.asList(1,2,3);
        List<Integer> num2 = Arrays.asList(4,5);
        List<int[]> pairs = num1.stream()
        											.flatMap(i -> num2.stream().map(j -> new int[] {i,j}))
        											.collect(toList());
        
        /* for(int i=0; i<pairs.size();i++)
 	    System.out.println("(" + pairs.get(i)[0]+ "," + pairs.get(i)[1] + ")"); */
        pairs.stream().forEach(i -> { System.out.println("(" + i[0]+ "," + i[1] + ")"); });
        System.out.println("");
       
       // p.166 5-2 (3)
        List<int[]> pairs2 = pairs.stream()
        											.filter(i -> (i[0] + i[1]) % 3 == 0)
        											.collect(toList());
        
        pairs2.stream().forEach(i -> { System.out.println("(" + i[0]+ "," + i[1] + ")"); });
        System.out.println("");
        
        // p.173 5-3
       List<Integer> numbers2 = Arrays.asList(1,2,3,4,5,6);
       long count = numbers2.stream().count();						// count 사용
       int count2 = numbers2.stream()
    		   							   .map(i -> 1)
    		   							   .reduce(0, (a, b) -> a+1);				// map, reduce 사용
       
       System.out.println("count : " + count + ", count2 : " +count2 + "\n");
       
        // p.191 5-4
       Stream.iterate(new int[] {0, 1}, a -> new int[] {a[1], a[0]+a[1]})
       			   .limit(20)
       			   .forEach(t -> System.out.println("(" + t[0] + ", " + t[1] + ")"));
        
       
    }
}
