import org.junit.*;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertEquals;

/**
 *
 */
public class Tests {
    private SortedSet<Integer> splayints = new SplayTree<>();
    private List ints = Arrays.asList(10, 11, 14, 18, 15, 20, 13, 9, 7, 8, 12);


    @Test
    public void add() {
        splayints.add(10);
        splayints.add(9);
        splayints.add(12);
        assertTrue(splayints.contains(10));
        assertTrue(splayints.contains(9));
        assertTrue(splayints.contains(12));
        splayints.clear();
    }

    @Test
    public void containsAll(){
        splayints.addAll(ints);
        boolean bool = splayints.containsAll(ints);
        assertTrue(bool);
    }
}
