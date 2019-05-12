import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class Tests {
    private SortedSet<Integer> splayints;
    private SortedSet<Integer> set;
    private List ints;
    private List ints1;

    @BeforeEach
    void init() {
        splayints = new SplayTree<>();
        set = new TreeSet<>();
        ints = Arrays.asList(10, 11, 14, 18, 15, 20, 13, 9, 7, 8, 12);
        ints1 = Arrays.asList(20, 13, 9, 7, 8, 12);
        splayints.addAll(ints);
    }

    @Test
    public void add() {
        splayints.add(19);
        splayints.add(45);
        splayints.add(0);
        assertTrue(splayints.contains(9));
        assertTrue(splayints.contains(45));
        assertTrue(splayints.contains(0));
    }

    @Test
    public void containsAll() {
        boolean bool = splayints.containsAll(ints);
        assertTrue(bool);
    }

    @Test
    public void remove() {
        splayints.remove(14);
        assertFalse(splayints.contains(14));
        assertEquals(ints.size() - 1, splayints.size());
        splayints.remove(10);
        assertFalse(splayints.contains(10));
        assertEquals(ints.size() - 2, splayints.size());
    }

    @Test
    public void iteratorTest() {
        Iterator iterator = splayints.iterator();
        assertTrue(iterator.hasNext());
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
        SortedSet<Integer> subset = splayints.subSet(9, 12);
        assertEquals(Optional.of(11), Optional.of(subset.last()));
        assertEquals(Optional.of(9), Optional.of(subset.first()));
        assertEquals(3, subset.size());
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

        assertThrows(IllegalArgumentException.class,
            () -> subset.remove(7)
        );

        subset.remove(9);
        assertFalse(splayints.contains(9));

        assertThrows(IndexOutOfBoundsException.class,
            () -> subset.subSet(8, 11)
        );
    }

    @Test
    void headSet() {
        SortedSet<Integer> subset = splayints.headSet(12);
        assertEquals(5, subset.size());
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

        assertThrows(IllegalArgumentException.class,
            () -> subset.remove(15)
        );

        subset.remove(9);
        assertFalse(splayints.contains(9));

        assertThrows(IndexOutOfBoundsException.class,
            () -> subset.headSet(15)
        );
    }

    @Test
    public void tailSet() {
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

        assertThrows(IllegalArgumentException.class,
            () -> subset.remove(7)
        );

        subset.remove(15);
        assertFalse(splayints.contains(15));

        assertThrows(IndexOutOfBoundsException.class,
            () -> subset.tailSet(7)
        );
    }

    @Test
    public void stringImplemention() {
        assertEquals(
            "7, 8, 9, 10, 11, 12, 13, 14, 15, 18, 20",
            splayints.toString()
        );
    }

    @Test
    void benchmark() {
        splayints.clear();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            splayints.add(i);
        }
        long end = System.currentTimeMillis();
        System.out.println("Add splay: " + (end - begin) + " ms");

        SortedSet<Integer> defaultTree = new TreeSet<>();
        begin = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            defaultTree.add(i);
        }
        end = System.currentTimeMillis();
        System.out.println("Add default: " + (end - begin) + " ms");

        SortedSet<Integer> subset = splayints.subSet(4560, 4600);
        begin = System.currentTimeMillis();
        for(int i: subset) {
            i++;
        }
        end = System.currentTimeMillis();
        System.out.println("Foreach subsplay: " + (end - begin) + " ms");

        SortedSet<Integer> subsetTree = splayints.subSet(4560, 4600);
        begin = System.currentTimeMillis();
        for(int i: subsetTree) {
            i++;
        }
        end = System.currentTimeMillis();
        System.out.println("Foreach default: " + (end - begin) + " ms");
    }
}
