package Modern_Java_in_Action.chapter10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class StreamAPI {
    public static void main(String[] args) {
        simpleRead("test.txt");
        functionalRead("test.txt");
    }

    public static void simpleRead(String fileName) {
        try {
            List<String> errors = new ArrayList<>();
            int errorCount = 0;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line = bufferedReader.readLine();
            while (errorCount < 40 && line != null) {
                if (line.startsWith("ERROR")) {
                    errors.add(line);
                    errorCount++;
                }
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void functionalRead(String fileName) {
        try {
            List<String> errors = Files.lines(Paths.get(fileName))
                                        .filter(line -> line.startsWith("ERROR"))
                                        .limit(40)
                                        .collect(toList());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
