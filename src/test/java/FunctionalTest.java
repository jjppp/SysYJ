import org.jjppp.Main;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public final class FunctionalTest {
    public final static String FUNCTIONAL_PATH = ParserTest.FOLDER_PATH + "official/functional/";

    private final static Set<String> skipFiles = Set.of(
            "04_arr_defn3.sy",
            "05_arr_defn4.sy",
            "40_unary_op.sy",
            "41_unary_op2.sy"
    );

    @Test
    void testAll() {
        System.out.println(FUNCTIONAL_PATH);
        File functionalPath = new File(FUNCTIONAL_PATH);
        String[] files = Objects.requireNonNull(functionalPath.list());

        SortedSet<String> sortedSet = Arrays.stream(files)
                .filter(x -> x.endsWith(".sy"))
                .filter(x -> !skipFiles.contains(x))
                .collect(Collectors.toCollection(TreeSet::new));
        for (var file : sortedSet) {
            System.out.println(file);
            Main.main(ParserTest.FOLDER_PATH + "official/functional/" + file);
        }
    }
}
