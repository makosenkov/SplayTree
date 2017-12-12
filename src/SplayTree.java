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

    private Node<V> root = null;

    private int size = 0;

    private boolean isRoot(Node<V> vertex) {
        return vertex.parent == null;
    }

    public boolean add(V value) {
        //Добавление по принципам обычного BST, только
        // в конце делается splay для добавленного узла
        Node<V> closest = find(value);
        int comparison = closest == null ? -1 : value.compareTo(closest.value);
        if (comparison == 0) return false;
        Node<V> newNode = new Node<>(value);
        if (closest == null) root = newNode;
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        } else {
            assert closest.right == null;
            closest.right = newNode;
        }
        splay(newNode);
        size++;
        return true;
    }

    boolean checkInvariant() {
        return root == null || checkInvariant(root);
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
        assert it != null;
        Node<V> parent = it.parent;

        //Нет потомков
        if (it.left == it.right) {
            if (parent != null) {
                if (parent.left == it) parent.left = null;
                else parent.right = null;
            } else root = null;
        }

        //Один потомок
        else if (it.left == null || it.right == null) {
            if (parent != null) {
                if (it.left != null) {
                    if (parent.left == it) parent.left = it.left;
                    else parent.right = it.left;
                } else {
                    if (parent.left == it) parent.left = it.right;
                    else parent.right = it.right;
                }
            } else {
                if (it.left != null) root = it.left;
                else root = it.right;
            }
        }

        //Два потомка
        else {
            //TODO
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        for (V element : c) {
            if (!add(element)) return false;
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
            if (!this.contains(element)) return false;
        }
        return true;
    }

    private Node<V> find(V value) {
        if (isEmpty()) return null;
        return find(root, value);
    }

    private Node<V> find(Node<V> start, V value) {
        int comparison = value.compareTo(start.value);
        Node<V> current = root;
        while (current != null) {
            if (comparison > 0) {
                if (start.right == null) return start;
                current = current.right;
            } else if (comparison < 0) {
                if (start.left == null) return start;
                current = current.left;
            } else {
                splay(current);
                return current;
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
        Node<V> newRoot = splay(maxNode(tree1));
        newRoot.right = tree2;
    }

    private void rightZig(Node<V> parent) {
        Node<V> axis = parent.left; //Берем осевой узел - левый ребенок родителя
        parent.left = axis.right; //Правый ребенок осевого узла становится левым ребенком родителя
        axis.right = parent; //Осевой узел "выплывает" наверх, становясь новым родителем
    }

    //Левый поворот аналогично правому, но в зеркальном отражении
    private void leftZig(Node<V> parent) {
        Node<V> axis = parent.right;
        parent.right = axis.left;
        axis.left = parent;
    }

    private Node<V> splay(Node<V> vertex) {
        if (isRoot(vertex)) return vertex;
        Node<V> parent = vertex.parent;
        Node<V> gparent = parent.parent;
        while (!isRoot(vertex)) {
            //Zig-Zig
            if (parent == gparent.left && vertex == parent.left) {
                rightZig(parent);
                rightZig(vertex);
            }
            if (parent == gparent.right && vertex == parent.right) {
                leftZig(parent);
                leftZig(vertex);
            }
            //Zig-Zag
            if (parent == gparent.left && vertex == parent.right) {
                leftZig(vertex);
                rightZig(parent);
            }
            if (parent == gparent.left && vertex == parent.right) {
                rightZig(vertex);
                leftZig(parent);
            }
            if (vertex.parent == null) root = vertex;
        }
        return vertex;
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
