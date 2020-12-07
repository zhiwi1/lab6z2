package com.company;

import java.util.Stack;

public class BinTree {
    private Node root;                              //Корень дерева

    //Конструктор для инициализации
    public BinTree(){
        root = null;
    }

    //Вставка нового ключа в дерево
    public void insert(int data){
        //Создаем узел
        Node newNode = new Node(data);

        //Делаем новый узел корневым
        if (root == null) {
            root = newNode;
        } else {
            //Становимся в начало дерево
            Node curr = root, prev;

            while (true) {
                //Запоминаем предыдущее положение
                prev = curr;
                //Если то, что мы хотим вставить, меньше того, что находится в текущем узле
                if (data < curr.data) {
                    curr = curr.left;
                    //Нашли место вставки - запоминаем ссылку
                    if (curr == null) {
                        prev.left = newNode;
                        return;
                    }
                } else {
                    //Иначе идем вправо
                    curr = curr.right;
                    //Нашли место вставки - запоминаем ссылку
                    if (curr == null) {
                        prev.right = newNode;
                        return;
                    }
                }
            }
        }
    }

    public void insertToThreadedBinaryTree(int data){
        getThreadingUpdated(root);
        insert(data);
        doThreading();
    }

    public void doTraversing(){
        Node curr = root;

        while (curr != null){
            System.out.print(curr.data + "->");
            if (curr.left != null && !curr.leftIsThread) curr = curr.left;
            else if (curr.left == null) curr = curr.right;
            else if (curr.leftIsThread) curr = curr.left;
        }

        System.out.println("Конец");
    }

    public void doThreading(){
        Stack<Node> stack = new Stack<Node>();
        stack.push(root);

        while (!stack.empty()){
            Node extracted = stack.pop();

            if (extracted.right != null) stack.push(extracted.right);
            if (extracted.left  != null && !extracted.leftIsThread) stack.push(extracted.left);

            if (stack.empty()) return;

            if (extracted.right == null && (extracted.leftIsThread || extracted.left == null)){

                if (stack.peek() == root){
                    extracted.left = null;
                    extracted.leftIsThread = false;
                } else {
                    extracted.left = stack.peek();
                    extracted.leftIsThread = true;
                }
            }
        }
    }

    private void getThreadingUpdated(Node n){
        if (n != null){
            if (n.leftIsThread){
                n.leftIsThread = false;
                n.left = null;
            }

            getThreadingUpdated(n.left);
            getThreadingUpdated(n.right);
        }
    }


    //Метод для удаления некоторого ключа из дерева.
    public boolean delete(int key) {
        //Становимся в начало дерева
        Node current = root;
        Node parent = root;
        //Пока предполагаем, что удаляемый узел будет являться
        //левым потомком узла-родителя
        boolean isLeftChild = true;
        //и обновляем, является ли найденный удаляемый узел левым
        // потомком либо нет
        while (current.data != key) {
            parent = current;
            if (key < current.data) {
                isLeftChild = true;
                current = current.left;
            } else {
                isLeftChild = false;
                current = current.right;
            }
            if (current == null)
                return false;
        }
        //Удаляемый узел все-таки найден.

        //Рассматриваем случай, когда удаляемый узел - лист
        if (current.left == null &&
                current.right == null) {
            if (current == root)
                root = null;
            else if (isLeftChild)
                parent.left = null;
            else
                parent.right = null;        //Удаляемый узел - правый потомок своего родителя. Обнуляем правую ссылку родителя
        }
        else
            //Рассматриваем случай, когда у удаляемого узла есть только левый потомок
            if (current.right == null) {
                if (current == root)
                    root = current.left;       //Удаляемый узел - корень. Правого поддерева нет. Просто перезапоминаем корень.
                else
                if (isLeftChild)          //Удаляемый узел - не корень. При этом удаляемый узел - левый потомок. Правого поддерева нет.
                    parent.left = current.left;         //Исключаем удаляемый узел из цепочки. Теперь родитель будет ссылаться на нового левого потомка.
                else
                    parent.right = current.left;        //Исключаем удаляемый узел из цепочки. Теперь родитель будет ссылаться на нового правого потомка.
            }
            else
                //Рассматриваем аналогичный случай, когда у удаляемого узла есть только правый потомок
                if (current.left == null) {
                    if (current == root)
                        root = current.right;
                    else
                    if (isLeftChild)
                        parent.left = current.right;
                    else
                        parent.right = current.right;
                }
                else
                //Рассматриваем случай, когда у удаляемого узла есть и левый, и правфй потомок
                {
                    //Находим преемника. Преемник - это узел со следующим по величине ключом.
                    Node successor = getSuccessor(current);
                    if (current == root)
                        root = successor;               //Если удаляемый узел - корень, то перезапоминаем его
                    else if (isLeftChild)
                        parent.left = successor;        //Удаляемый узел - левый потомок. Заменяем его преемником.
                    else
                        parent.right = successor;       //Аналогично в случае правого потомка.

                    successor.left = current.left;      //Переносим левое поддерево удаляемого узла.
                }
        //Сообщаем об удачном удалении
        return true;
    }



    //Вспомогательный метод для поиска преемника узлу delNode.
    //Преемник - либо (1) правый потомок delNode
    //           либо (2) "самый левый" потомок правого потомка delNode
    private Node getSuccessor(Node delNode) {
        //Становимся в правое поддерево удаляемого узла
        Node successorParent = delNode;
        Node successor = delNode;
        Node current = delNode.right;
        //Находим "самого левого"
        while(current != null) {
            successorParent = successor;
            successor = current;
            current = current.left;
        }
        //Если "самый левый" не является (1), а является (2), то
        //правый потомком преемника должно стать правое поддерево delNode
        if(successor != delNode.right) {
            successorParent.left = successor.right;
            successor.right = delNode.right;
        }
        //Возвращаем преемника
        return successor;
    }

    public boolean deleteFromThreadedBinaryTree(int key){
        getThreadingUpdated(root);
        boolean res = delete(key);
        doThreading();
        return res;
    }
}