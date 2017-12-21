import java.util.*;

/**
 *
 */
public class SubSet<V extends Comparable<V>> extends AbstractSet<V> implements SortedSet<V> {
    private V fromElement; //нижняя граница
    private V toElement; //верхняя граница
    private SplayTree<V> delegate;

    SubSet(V fromElement, V toElement, SplayTree<V> delegate) {
        this.fromElement = fromElement;
        this.toElement = toElement;
        this.delegate = delegate;
    }

    @Override
    public Iterator<V> iterator() {
        return new SubSetIterator();
    }

    @Override
    public boolean remove(Object o){
        @SuppressWarnings("unchecked")
        V value = (V) o;
        if ((toElement.compareTo(value) > 0 && fromElement.compareTo(value) <= 0) && contains(value))
            delegate.remove(value);
        else throw new IllegalArgumentException();
        return true;
    }

    @Override
    public boolean add(V value){
        delegate.add(value);
        return true;
    }

    @Override
    public boolean contains(Object o) {
        V value = (V) o;
        Iterator<V> iterator = this.iterator();
        while (iterator.hasNext()){
            if (value.compareTo(iterator.next()) == 0) return true;
        }
        return false;
    }

    public class SubSetIterator implements Iterator<V> {

        Iterator<V> iterator = delegate.iterator();
        V current = null;
        V next = findNext();

        private V findNext() {
            while (iterator.hasNext()) {
                V nextElement = iterator.next();
                if (nextElement.compareTo(fromElement) >= 0 &&
                        nextElement.compareTo(toElement) < 0) {
                    next = nextElement;
                    return nextElement;
                }
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public V next() {
            if (next == null) throw new NoSuchElementException();
            current = next;
            next = findNext();
            return current;
        }

    }

    @Override
    public int size() {
        int size = 0;
        for (V next : delegate) {
            if (next.compareTo(fromElement) >= 0 && next.compareTo(toElement) < 0) {
                size++;
            }
        }
        return size;
    }

    @Override
    public Comparator<? super V> comparator() {
        return null;
    }

    @Override
    public SortedSet<V> subSet(V fromElement, V toElement) {
        if (fromElement.compareTo(this.fromElement) >= 0 &&
                toElement.compareTo(this.toElement) < 0)
            return new SubSet<>(fromElement,toElement,delegate);
        else throw new IndexOutOfBoundsException();
    }

    @Override
    public SortedSet<V> headSet(V toElement) {
        if (toElement.compareTo(this.fromElement) >= 0 &&
                toElement.compareTo(this.toElement) < 0)
            return new SubSet<>(this.fromElement,toElement,delegate);
        else throw new IndexOutOfBoundsException();
    }

    @Override
    public SortedSet<V> tailSet(V fromElement) {
        if (fromElement.compareTo(this.fromElement) >= 0 &&
                fromElement.compareTo(this.toElement) < 0) {
            return new TailSet<>(fromElement, delegate);
        }
        else throw new IndexOutOfBoundsException();
    }

    @Override
    public V first() {
        return fromElement;
    }

    @Override
    public V last() {
        return toElement;
    }
}
