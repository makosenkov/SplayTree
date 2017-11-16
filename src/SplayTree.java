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

    private boolean treeIsEmpty() {
        return root == null;
    }

    private boolean isRoot(Node<V> vertex) {
        return vertex.parent == null;
    }

    public boolean add(V value) {
        //Добавление по принципам обычного BST, только
        // в конце делается splay для добавленного узла
        Node<V> closest = find(value);
        int comparison = closest == null ? -1 : value.compareTo(closest.value);
        if (comparison == 0) return false;
        Node<V> newNode = new Node<V>(value);
        if (closest == null) root = newNode;
        else if (comparison < 0){
            assert closest.left == null;
            closest.left = newNode;
        }else{
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    private Node<V> find(V value) {
        if (treeIsEmpty()) return null;
        return find(root, value);
    }

    private Node<V> find(Node<V> start, V value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) return start;
        else if (comparison > 0) {
            if (start.right != null) return start;
            return find(start.right, value);
        } else {
            if (start.left != null) return start;
            return find(start.left, value);
        }

    }

    private void split(Node<V> vertex) {
       /*найти ключ, меньше либо равный ключу входящего узла
        *сделать для него splay
        */
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
    public int size() {
        return size;
    }

    @Override
    public Comparator<? super V> comparator() {
        return null;
    }

    @Override
    public SortedSet<V> subSet(V fromElement, V toElement) {
        return null;
    }

    @Override
    public SortedSet<V> headSet(V toElement) {
        return null;
    }

    @Override
    public SortedSet<V> tailSet(V fromElement) {
        return null;
    }

    @Override
    public V first() {
        if (treeIsEmpty()) throw new NoSuchElementException();
        Node<V> current = root;
        while (current.left != null){
            current = current.left;
        }
        splay(current);
        return current.value;
    }

    @Override
    public V last() {
        if (treeIsEmpty()) throw new NoSuchElementException();
        Node<V> current = root;
        while (current.right != null){
            current = current.right;
        }
        splay(current);
        return current.value;
    }
}
