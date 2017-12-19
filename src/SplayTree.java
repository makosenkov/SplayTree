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
        splay(it);
        merge(it.left, it.right);
        it.left.parent = null;
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
        if (!isEmpty())
            for (V object : this) {
                remove(object);
            }
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
            if (value.compareTo(current.value) == 0)
                return current;
            else if (value.compareTo(root.value) < 0) {
                current = current.left;
            } else if (value.compareTo(root.value) > 0) {
                current = current.left;
            }
        }
        return null;

    }

    private void split(Node<V> vertex) {
       /*найти ключ, меньше либо равный ключу входящего узла
        *сделать для него splay
        */
        //TODO
    }

    private void merge(Node<V> tree1, Node<V> tree2) {
        splay(maxNode(tree1));
        Node<V> newRoot = root;
        newRoot.right = tree2;
    }

    private void rightRotate(Node<V> parent) {
            Node<V> axis = parent.left;
            parent.left = axis.right;
            if (axis.right != null)
                axis.right.parent = parent;
            transplant(parent, axis);
            axis.right = parent;
            axis.right.parent = axis;
    }

    //Левый поворот аналогично правому, но в зеркальном отражении
    private void leftRotate(Node<V> parent) {
            Node<V> axis = parent.right;
            parent.right = axis.left;
            if (axis.left != null)
                axis.left.parent = parent;
            transplant(parent, axis);
            axis.left = parent;
            axis.left.parent = axis;
    }

    private void transplant(Node<V> parent, Node<V> child) {
        if (parent.parent == null)
            root = child;
        else if (parent == parent.parent.left)
            parent.parent.left = child;
        else if (parent == parent.parent.right)
            parent.parent.right = child;
        if (child != null)
            child.parent = parent.parent;
    }

    private void splay(Node<V> element) {
        while (element.parent != null) {
            if (isRoot(element.parent)) {
                if (element == element.parent.left)
                    rightRotate(element);
                else
                    leftRotate(element);
            } else if (element == element.parent.left && element.parent == element.parent.parent.left) {
                rightRotate(element.parent.parent);
                rightRotate(element.parent);
            } else if (element == element.parent.right && element.parent == element.parent.parent.right) {
                leftRotate(element.parent.parent);
                leftRotate(element.parent);
            } else if (element == element.parent.right && element.parent == element.parent.parent.left) {
                leftRotate(element.parent);
                rightRotate(element.parent.parent);
            } else if (element == element.parent.left && element.parent == element.parent.parent.right) {
                rightRotate(element.parent);
                leftRotate(element.parent.parent);
            }
        }
        root = element;
    }

    private Node<V> maxNode(Node<V> vertex) {
        if (vertex.right != null) {
            return maxNode(vertex.right);
        } else return vertex;
    }

    @Override
    public Iterator<V> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
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
