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
            left = null;
            right = null;
            parent = null;
        }
    }

    private Node root;

    private boolean treeIsEmpty() {
        return root == null;
    }

    private boolean isRoot(Node vertex) {
        return vertex.parent == null;
    }

    public void put(Key key, Value value) {
        //Положить по принципам обычного BST, сделать splay
        if (treeIsEmpty()) {
            root = new Node(key, value);
        }

        int comparison = key.compareTo(root.key);

        if (comparison < 0){

        }
    }

    private void split(Node vertex){
       /*найти ключ, меньше либо равный ключу входящего узла
        *сделать для него splay
        *
        */
    }

    private void merge(Node tree1, Node tree2){
        Node newRoot = splay(maxNode(tree1));
        newRoot.right = tree2;
    }

    private void rightZig(Node parent) {
        Node axis = parent.left; //Берем осевой узел - левый ребенок родителя
        parent.left = axis.right; //Правый ребенок осевого узла становится левым ребенком родителя
        axis.right = parent; //Осевой узел "выплывает" наверх, становясь новым родителем
    }

    //Левый поворот аналогично правому, но в зеркальном отражении
    private void leftZig(Node parent) {
        Node axis = parent.right;
        parent.right = axis.left;
        axis.left = parent;
    }

    private Node splay(Node vertex) {
        if (isRoot(vertex)) return vertex;
        Node parent = vertex.parent;
        Node gparent = parent.parent;
        while (!isRoot(vertex)){
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
        }
        return vertex;
    }

    private Node maxNode(Node vertex){
        if (vertex.right != null){
            return maxNode(vertex.right);
        }
        else return vertex;
    }
}
