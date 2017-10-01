/**
 *Реализация Splay-дерева (расширяющееся, "выворачивающееся" бинарное дерево)
 * */
public class SplayTree <Key extends Comparable<Key>, Value>{
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

    private Node rightRotate(Node parent){
        Node axis = parent.left; //Берем осевой узел - левый ребенок родителя
        parent.left = axis.right; //Правый ребенок осевого узла становится левым ребенком родителя
        axis.right = parent; //Осевой узел "выплывает" наверх, становясь новым родителем
        return axis;
    }

    //Левый поворот аналогично правому, но в зеркальном отражении
    private Node leftRotate(Node parent){
        Node axis = parent.right;
        parent.right = axis.left;
        axis.left = parent;
        return axis;
    }

    private void splay(){}


}
