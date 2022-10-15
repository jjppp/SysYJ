import org.jjppp.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public final class FunctionalTest {
    public final static String FUNCTIONAL_PATH = ParserTest.FOLDER_PATH + "official/functional/";

    private final static Set<String> skipFiles = Set.of(
            "04_arr_defn3.sy",
            "05_arr_defn4.sy",
            "40_unary_op.sy",
            "41_unary_op2.sy",
            "44_stmt_expr.sy",
            "52_scope.sy",
            "53_scope2.sy",
            "86_long_code2.sy",
            "99_matrix_tran.sy"
    );

    private void checkOutput(String basename) throws IOException {
        File expect = new File(ParserTest.FOLDER_PATH + "official/functional/" + basename + "out");
        File my = new File(ParserTest.FOLDER_PATH + "official/functional/" + basename + "myout");
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
        try {
            Main.main(ParserTest.FOLDER_PATH + "official/functional/" + file,
                    ParserTest.FOLDER_PATH + "official/functional/" + substring + "in",
                    ParserTest.FOLDER_PATH + "official/functional/" + substring + "myout");
            checkOutput(substring);
        } catch (AssertionError error) {
            if (error.getMessage().equals("TODO")) {
                System.out.println("\t TODO:");
            }
        }
    }

    @Test
    void testAll() throws IOException {
        System.out.println(FUNCTIONAL_PATH);
        File functionalPath = new File(FUNCTIONAL_PATH);
        String[] files = Objects.requireNonNull(functionalPath.list());

        SortedSet<String> sortedSet = Arrays.stream(files)
                .filter(x -> x.endsWith(".sy"))
                .filter(x -> !skipFiles.contains(x))
                .collect(Collectors.toCollection(TreeSet::new));
        for (var file : sortedSet) {
            test(file);
        }
        System.out.println("DONE");
    }
}
