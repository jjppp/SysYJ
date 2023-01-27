import org.jjppp.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public final class FunctionalTest {
    public final static String FUNCTIONAL_PATH = "official/functional/";
    public final static String FUNCTIONAL_HIDDEN_PATH = "official/hidden_functional/";

    private final static Set<String> skipFiles = Set.of(
            "04_arr_defn3.sy",
            "05_arr_defn4.sy",
            "86_long_code2.sy",
            "95_float.sy",

            "07_arr_init_nd.sy",
            "08_global_arr_init.sy"
    );

    private void checkOutput(String basename) throws IOException {
        File expect = new File(ParserTest.FOLDER_PATH + basename + "out");
        File my = new File(ParserTest.FOLDER_PATH + basename + "myout");
        try (FileInputStream expectOut = new FileInputStream(expect);
             FileInputStream myOut = new FileInputStream(my)) {
            Assertions.assertArrayEquals(
                    expectOut.readAllBytes(),
                    myOut.readAllBytes());
        }
    }

    private void test(String file) throws IOException {
        System.out.println(file + ": ");
        String substring = file.substring(0, file.length() - 2);
        Main.main(ParserTest.FOLDER_PATH + file,
                ParserTest.FOLDER_PATH + substring + "in",
                ParserTest.FOLDER_PATH + substring + "myout");
        checkOutput(substring);
    }

    @Test
    void testBasics() throws IOException {
        System.out.println(ParserTest.FOLDER_PATH + FUNCTIONAL_PATH);
        File functionalPath = new File(ParserTest.FOLDER_PATH + FUNCTIONAL_PATH);
        String[] files = Objects.requireNonNull(functionalPath.list());

        SortedSet<String> sortedSet = Arrays.stream(files)
                .filter(x -> x.endsWith(".sy"))
                .filter(x -> !skipFiles.contains(x))
                .collect(Collectors.toCollection(TreeSet::new));
        for (var file : sortedSet) {
            test(FUNCTIONAL_PATH + file);
        }
        System.out.println("DONE");
    }

    @Test
    void testHidden() throws IOException {
        System.out.println(ParserTest.FOLDER_PATH + FUNCTIONAL_HIDDEN_PATH);
        File functionalPath = new File(ParserTest.FOLDER_PATH + FUNCTIONAL_HIDDEN_PATH);
        String[] files = Objects.requireNonNull(functionalPath.list());

        SortedSet<String> sortedSet = Arrays.stream(files)
                .filter(x -> x.endsWith(".sy"))
                .filter(x -> !skipFiles.contains(x))
                .collect(Collectors.toCollection(TreeSet::new));
        for (var file : sortedSet) {
            test(FUNCTIONAL_HIDDEN_PATH + file);
        }
        System.out.println("DONE");
    }
}
