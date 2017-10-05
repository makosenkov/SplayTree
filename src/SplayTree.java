/**
 * Реализация Splay-дерева (расширяющееся, "выворачивающееся" бинарное дерево)
 */
public class SplayTree<Key extends Comparable<Key>, Value> {
    private class Node {
        private Node left, right, parent;
        private Key key;
        private Value value;

        Node(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;

    private boolean treeIsEmpty() {
        return root == null;
    }

    private boolean isRoot(Node vertex) {
        return vertex.parent == null;
    }

    public void split(Node vertex) {

    }

    private Node rightZig(Node parent) {
        Node axis = parent.left; //Берем осевой узел - левый ребенок родителя
        parent.left = axis.right; //Правый ребенок осевого узла становится левым ребенком родителя
        axis.right = parent; //Осевой узел "выплывает" наверх, становясь новым родителем
        return axis;
    }

    //Левый поворот аналогично правому, но в зеркальном отражении
    private Node leftZig(Node parent) {
        Node axis = parent.right;
        parent.right = axis.left;
        axis.left = parent;
        return axis;
    }

    private Node splay(Node vertex) {
        if (isRoot(vertex)) return vertex;
        Node parent = vertex.parent;
        Node gparent = parent.parent;
        do {
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
        } while (!isRoot(vertex));
        return vertex;
    }
}
