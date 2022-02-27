package Modern_Java_in_Action.chapter21;

import java.util.Arrays;
import java.util.List;

public class toArray {
    public static void main(String[] args) {
        List<String> sampleList = Arrays.asList("Java", "Kotlin");
        String[] sampleArray = sampleList.toArray(String[]::new);
    }
}
