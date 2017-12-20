import org.junit.*;

import java.util.*;

import static junit.framework.Assert.assertFalse;
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

    @Test
    public void remove(){
        splayints.addAll(ints);
        splayints.remove(14);
        assertFalse(splayints.contains(14));
        splayints.remove(10);
        assertFalse(splayints.contains(10));
    }

    @Test
    public void iteratorTest(){
        splayints.addAll(ints);
        Iterator iterator = splayints.iterator();
        iterator.hasNext();
        Assert.assertEquals(7, iterator.next());
        Assert.assertEquals(8, iterator.next());
        Assert.assertEquals(9, iterator.next());
        Assert.assertEquals(10, iterator.next());
        Assert.assertEquals(11, iterator.next());
        Assert.assertEquals(12, iterator.next());
        Assert.assertEquals(13, iterator.next());
        Assert.assertEquals(14, iterator.next());
        Assert.assertEquals(15, iterator.next());
        Assert.assertEquals(18, iterator.next());
        Assert.assertEquals(20, iterator.next());
    }

}
