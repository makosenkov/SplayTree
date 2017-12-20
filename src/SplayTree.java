import java.util.*;

/**
 * Реализация Splay-дерева (расширяющееся, "выворачивающееся" бинарное дерево)
 */

public class SplayTree<V extends Comparable<V>> extends AbstractSet<V> implements SortedSet<V> {


    private static class Node<V> {
        final V value;
        Node<V> left = null;
        Node<V> right = null;
        Node<V> parent = null;

        Node(V value) {
            this.value = value;
        }
    }

    private Node<V> root;

    private int size = 0;

    private boolean isRoot(Node<V> vertex) {
        return vertex == root;
    }

    public boolean add(V value) {
        Node<V> preInsertPlace = null;
        Node<V> insertPlace = root;
        while (insertPlace != null) {
            preInsertPlace = insertPlace;
            if (value.compareTo(insertPlace.value) > 0)
                insertPlace = insertPlace.right;
            else if (value.compareTo(insertPlace.value) < 0)
                insertPlace = insertPlace.left;
        }
        Node<V> insertElement = new Node(value);
        insertElement.parent = preInsertPlace;
        if (preInsertPlace == null)
            root = insertElement;
        else if (insertElement.value.compareTo(preInsertPlace.value) < 0)
            preInsertPlace.left = insertElement;
        else if (insertElement.value.compareTo(preInsertPlace.value) > 0)
            preInsertPlace.right = insertElement;
        splay(insertElement);
        size++;
        return true;
    }

    private boolean checkInvariant(Node<V> node) {
        Node<V> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<V> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || root == null) return false;
        @SuppressWarnings("unchecked")
        Node<V> it = find((V) o);
        root = merge(it.left, it.right);
        size--;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        for (V element : c) {
            add(element);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object object : c) {
            if (!remove(object)) return false;
        }
        return true;
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) return false;
        }
        return true;
    }

    private Node<V> find(V value) {
        Node<V> current = root;
        while (current != null) {
            if (value.compareTo(current.value) == 0) {
                splay(current);
                return current;
            } else if (value.compareTo(current.value) < 0) {
                current = current.left;
            } else if (value.compareTo(current.value) > 0) {
                current = current.right;
            }
        }

        return null;
    }

    private Node<V> merge(Node<V> tree1, Node<V> tree2) {
        if (tree1 == null || tree2 == null) {
            if (tree1 == null)
                return tree2;
            return tree1;
        } else {
            tree1 = find((maxNode(tree1)).value);
            tree1.right = tree2;
            return tree1;
        }
    }

    private void splay(Node<V> element) {
        while (!isRoot(element)) {
            if (element.parent == null) return;

            Node<V> parent = element.parent;
            Node<V> gparent = parent.parent;

            if (gparent == null) {
                if (parent.left == element) {
                    rightMoveToRoot(parent, element);
                    return;
                } else {
                    leftMoveToRoot(parent, element);
                    return;
                }
            } else {
                if (gparent.left == parent && parent.left == element) {
                    rotateLeft(gparent, parent);
                    rotateLeft(parent, element);
                } else if (gparent.right == parent && parent.right == element) {
                    rotateRight(gparent, parent);
                    rotateRight(parent, element);
                } else if (gparent.left == parent && parent.right == element) {
                    rightZigZag(gparent, parent, element);
                } else if (gparent.right == parent && parent.left == element) {
                    leftZigZag(gparent, parent, element);
                }
            }

        }
    }

    private void rightMoveToRoot(Node<V> parent, Node<V> node) {
        Node<V> right = node.right;
        node.right = parent;
        parent.parent = node;
        parent.left = right;
        if (right != null) right.parent = parent;
        node.parent = null;
        root = node;

    }

    private void leftMoveToRoot(Node<V> parent, Node<V> node) {
        Node<V> left = node.left;
        node.left = parent;
        node.parent = null;
        root = node;
        parent.parent = node;
        parent.right = left;
        if (left != null)
            left.parent = parent;
    }

    private void rotateLeft(Node<V> parent, Node<V> node) {
        Node<V> right = node.right;
        node.right = parent;
        node.parent = parent.parent;
        setParent(parent, node);
        parent.parent = node;
        parent.left = right;
        if (right != null) right.parent = parent;
        if (node.parent == null) root = node;
    }

    private void rotateRight(Node<V> parent, Node<V> node) {
        Node<V> left = node.left;
        node.left = parent;
        node.parent = parent.parent;
        setParent(parent, node);
        parent.parent = node;
        parent.right = left;
        if (left != null) left.parent = parent;
        if (node.parent == null) root = node;
    }

    private void rightZigZag(Node<V> gparent, Node<V> parent, Node<V> node) {

        Node<V> left = node.left;
        gparent.left = node;
        node.parent = gparent;
        node.left = parent;
        parent.parent = node;
        parent.right = left;
        if (left != null) left.parent = parent;

        rotateLeft(gparent, node);
    }

    private void leftZigZag(Node<V> gparent, Node<V> parent, Node<V> node) {

        Node<V> right = node.right;

        gparent.right = node;
        node.parent = gparent;
        node.right = parent;
        parent.parent = node;
        parent.left = right;
        if (right != null) right.parent = parent;

        rotateRight(gparent, node);
    }

    private void setParent(Node<V> previousSon, Node<V> newSon) {
        if (newSon.parent != null) {
            if (previousSon.parent.left == previousSon) previousSon.parent.left = newSon;
            else previousSon.parent.right = newSon;
        }
    }

    private Node<V> maxNode(Node<V> vertex) {
        if (vertex.right != null) {
            return maxNode(vertex.right);
        } else return vertex;
    }

    @Override
    public Iterator<V> iterator() {
        return new SplayTreeIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Iterator<V> iterator = this.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            array[i] = iterator.next();
            i++;
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        V v = (V) o;
        Node<V> closest = find(v);
        return closest != null && closest.value.compareTo(v) == 0;
    }

    @Override
    public Comparator<? super V> comparator() {
        return null;
    }

    public class SplayTreeIterator implements Iterator<V> {

        private Node<V> current = null;

        private Node<V> findNext() {
            Node<V> next = current;

            if (next == null) {
                next = first();
                return next;
            }

            if (next.right != null) {
                next = next.right;
                while (next.left != null) next = next.left;
                return next;
            }

            while (next.parent != null) {
                if (next.parent.left == next) {
                    next = next.parent;
                    return next;
                }
                next = next.parent;
            }

            return null;

        }

        private Node<V> first() {
            if (root == null) throw new NoSuchElementException();
            Node<V> result = root;
            while (result.left != null) {
                result = result.left;
            }
            return result;
        }

        @Override
        public boolean hasNext() {
            return findNext() != null;
        }

        @Override
        public V next() {
            current = findNext();
            if (current == null) throw new NoSuchElementException();
            return current.value;
        }

        @Override
        public void remove() {
            SplayTree.this.remove(current);
        }
    }


    @Override
    public SortedSet<V> subSet(V fromElement, V toElement) {
        if (fromElement.compareTo(this.first()) < 0 || fromElement.compareTo(this.last()) > 0 ||
                toElement.compareTo(this.first()) < 0 || toElement.compareTo(this.last()) > 0 ||
                fromElement.compareTo(toElement) > 0)
            throw new IllegalArgumentException();
        return this.headSet(toElement).tailSet(fromElement);
    }

    @Override
    public SortedSet<V> headSet(V toElement) {
        if (toElement == null) throw new NullPointerException();
        if (root == null) throw new NoSuchElementException();
        Node<V> current = root;
        SplayTree<V> newSet = new SplayTree<>();
        while (toElement.compareTo(current.value) != 0) {
            if (toElement.compareTo(current.value) > 0) {
                newSet.add(current.value);
                if (current.left != null) addBranch(current.left, newSet);
                if (current.right != null) current = current.right;
                else break;
            } else {
                if (current.left != null) current = current.left;
                else break;
            }
        }
        if (toElement.compareTo(current.value) == 0) {
            if (current.left != null) addBranch(current.left, newSet);
        }
        return newSet;
    }

    @Override
    public SortedSet<V> tailSet(V fromElement) {
        if (fromElement == null) throw new NullPointerException();
        if (root == null) throw new NoSuchElementException();
        Node<V> current = root;
        SplayTree<V> newSet = new SplayTree<>();
        while (fromElement.compareTo(current.value) != 0) {
            if (fromElement.compareTo(current.value) < 0) {
                newSet.add(current.value);
                if (current.right != null) addBranch(current.right, newSet);
                if (current.left != null) current = current.left;
                else
                    break;
            } else {
                if (current.right != null) current = current.right;
                else break;
            }
        }
        if (fromElement.compareTo(current.value) == 0) {
            newSet.add(current.value);
            if (current.right != null) addBranch(current.right, newSet);
        }
        return newSet;
    }

    private void addBranch(Node<V> node, SplayTree<V> tree) {
        tree.add(node.value);
        if (node.left != null) addBranch(node.left, tree);
        if (node.right != null) addBranch(node.right, tree);
    }

    @Override
    public V first() {
        if (isEmpty()) throw new NoSuchElementException();
        Node<V> current = root;
        while (current.left != null) {
            current = current.left;
        }
        splay(current);
        return current.value;
    }

    @Override
    public V last() {
        if (isEmpty()) throw new NoSuchElementException();
        Node<V> current = root;
        while (current.right != null) {
            current = current.right;
        }
        splay(current);
        return current.value;
    }
}
