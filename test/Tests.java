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
    private SortedSet<Integer> set = new TreeSet<>();
    private List ints = Arrays.asList(10, 11, 14, 18, 15, 20, 13, 9, 7, 8, 12);
    private List ints1 = Arrays.asList(20, 13, 9, 7, 8, 12);


    @Test
    public void add() {
        splayints.add(10);
        splayints.add(9);
        splayints.add(12);
        assertTrue(splayints.contains(10));
        assertTrue(splayints.contains(9));
        assertTrue(splayints.contains(12));
    }

    @Test
    public void containsAll() {
        splayints.addAll(ints);
        boolean bool = splayints.containsAll(ints);
        assertTrue(bool);
    }

    @Test
    public void remove() {
        splayints.addAll(ints);
        splayints.remove(14);
        assertFalse(splayints.contains(14));
        assertEquals(ints.size() - 1, splayints.size());
        splayints.remove(10);
        assertFalse(splayints.contains(10));
        assertEquals(ints.size() - 2, splayints.size());
    }

    @Test
    public void iteratorTest() {
        splayints.addAll(ints);
        Iterator iterator = splayints.iterator();
        iterator.hasNext();
        assertEquals(7, iterator.next());
        assertEquals(8, iterator.next());
        assertEquals(9, iterator.next());
        assertEquals(10, iterator.next());
        assertEquals(11, iterator.next());
        assertEquals(12, iterator.next());
        assertEquals(13, iterator.next());
        assertEquals(14, iterator.next());
        assertEquals(15, iterator.next());
        assertEquals(18, iterator.next());
        assertEquals(20, iterator.next());
    }

    @Test
    public void retainAll() {
        splayints.addAll(ints);
        splayints.retainAll(ints1);
        assertTrue(splayints.containsAll(ints1));
        assertFalse(splayints.contains(10));
        assertFalse(splayints.contains(11));
        assertFalse(splayints.contains(14));
        assertFalse(splayints.contains(18));
        assertFalse(splayints.contains(15));
        splayints.clear();
        splayints.addAll(ints1);
        assertFalse(splayints.retainAll(ints));
    }

    @Test
    public void subSet() {
        splayints.addAll(ints);
        SortedSet<Integer> subset = splayints.subSet(9, 12);
        assertEquals(Optional.of(11), Optional.of(subset.last()));
        assertEquals(Optional.of(9), Optional.of(subset.first()));
        assertTrue(subset.size() == 3);
        //Inclusive
        assertTrue(subset.contains(9));
        //Exclusive
        assertFalse(subset.contains(12));

        assertFalse(subset.contains(14));
        assertFalse(subset.contains(18));
        assertFalse(subset.contains(15));
        assertFalse(subset.contains(20));
        assertFalse(subset.contains(13));
        assertTrue(subset.contains(10));
        assertTrue(subset.contains(11));
        assertFalse(subset.contains(7));
        assertFalse(subset.contains(8));

        //Сравнение со стандартным классом
        set.addAll(ints);
        SortedSet<Integer> standart = set.subSet(9, 12);
        assertEquals(standart, subset);

        try {
            subset.remove(7);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        subset.remove(9);
        assertFalse(splayints.contains(9));

        try {
            SortedSet<Integer> sset = subset.subSet(8, 11);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void headSet() {
        splayints.addAll(ints);
        SortedSet<Integer> subset = splayints.headSet(12);
        assertTrue(subset.size() == 5);
        //Exclusive
        assertFalse(subset.contains(12));

        assertFalse(subset.contains(14));
        assertFalse(subset.contains(18));
        assertFalse(subset.contains(15));
        assertFalse(subset.contains(20));
        assertFalse(subset.contains(13));
        assertTrue(subset.contains(9));
        assertTrue(subset.contains(10));
        assertTrue(subset.contains(11));
        assertTrue(subset.contains(7));
        assertTrue(subset.contains(8));

        //Сравнение со стандартным классом
        set.addAll(ints);
        SortedSet<Integer> standart = set.headSet(12);
        assertEquals(standart, subset);

        try {
            subset.remove(15);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        subset.remove(9);
        assertFalse(splayints.contains(9));

        try {
            SortedSet<Integer> headset = subset.headSet(15);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void tailSet() {
        splayints.addAll(ints);
        SortedSet<Integer> subset = splayints.tailSet(12);
        //Inclusive
        assertTrue(subset.contains(12));
        //Inclusive
        assertTrue(subset.contains(20));

        assertTrue(subset.contains(14));
        assertTrue(subset.contains(18));
        assertTrue(subset.contains(15));
        assertTrue(subset.contains(13));
        assertFalse(subset.contains(9));
        assertFalse(subset.contains(10));
        assertFalse(subset.contains(11));
        assertFalse(subset.contains(7));
        assertFalse(subset.contains(8));

        //Сравнение со стандартным классом
        set.addAll(ints);
        SortedSet<Integer> standart = set.tailSet(12);
        assertEquals(standart, subset);

        try {
            subset.remove(7);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        subset.remove(15);
        assertFalse(splayints.contains(15));

        try {
            SortedSet<Integer> tailset = subset.tailSet(7);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }
}
