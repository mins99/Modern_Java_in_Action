package Modern_Java_in_Action.chapter21;

import java.util.List;

public class localVarilable {
    public static void main(String[] args) {
        localVarilableTest1();
        localVarilableTest2();

        var numbers = List.of(1, 2, 3, 4, 5);
        localVarilableTest3(numbers);
        //localVarilableTest4(numbers);
    }

    public static void localVarilableTest1() {
        var numbers = List.of(1, 2, 3, 4, 5);
        for (var number : numbers) {
            System.out.println(number);
        }
    }

    public static void localVarilableTest2() {
        var numbers = List.of(1, 2, 3, 4, 5);
        for (var i = 0; i < numbers.size(); i++) {
            System.out.println(numbers.get(i));
        }
    }

    public static void localVarilableTest3(List<Integer> numbers) {
        for (var i = 0; i < numbers.size(); i++) {
            System.out.println(numbers.get(i));
        }
    }

    // error!
    /*public static void localVarilableTest4(var numbers) {
        for (var i = 0; i < numbers.size(); i++) {
            System.out.println(numbers.get(i));
        }
    }*/
}
