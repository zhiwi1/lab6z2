package com.company;

public class Node {

    public int data;                    //Хранимые данные (ключ)
    public boolean leftIsThread;        //Истина, если ссылка на левое поддерево ЯВЛЯЕТСЯ нитью
    public Node left;                   //Ссылка на левое поддерево
    public Node right;                  //Ссылка на правое поддерево

    public Node(int data){              //Конструтор для инициализации
        this.data = data;
        leftIsThread = false;
        left  = null;
        right = null;
    }

    //Метод для вывода ключа
    public void show() { System.out.print(data + " "); }

}
