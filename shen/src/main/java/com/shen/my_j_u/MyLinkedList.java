package com.shen.my_j_u;

import java.util.Iterator;

public class MyLinkedList <AnyType> implements Iterable<AnyType>{
    //链表长度
    private int theSize;
    //链表操作计数器，当清空，新增，删除时自增
    private int modCount = 0 ;
    //特殊节点：头节点
    private Node<AnyType> beginMarker;
    //特殊节点：尾节点
    private Node<AnyType> endMarker;

    public MyLinkedList(){
        doClear();
    }

    @Override
    public Iterator<AnyType> iterator() {
        return null;
    }

    /**
     * 节点对象
     * @param <AnyType>
     */
    private static class Node<AnyType>{
        /**
         * 节点构造器
         * @param d 节点数据对象
         * @param p 当前节点前节点
         * @param n 当前节点后节点
         */
        public Node(AnyType d,Node<AnyType> p,Node<AnyType> n){
            data = d;
            prev = p;
            next = n;
        }
        public AnyType data;
        public Node<AnyType> prev;
        public Node<AnyType> next;
    }

    /**
     * 链表初始化方法
     */
    private void doClear(){
        beginMarker = new Node<>(null,null,null);
        endMarker = new Node<>(null,beginMarker,null);
        beginMarker.next = endMarker;

        theSize = 0;
        modCount++;
    }

    /**
     * 获取当前链表长度
     * @return
     */
    public int size(){
        return theSize;
    }

    //插入对象方法，默认位置插入链表尾部
    public boolean add(AnyType x){
        add(size(),x);
        return true;
    }

    //按位置插入对象方法
    public boolean add(int idx,AnyType x){
        addBefore(getNode(idx,0,size()),x);
        return true;
    }

    //按位置修改方法
    public AnyType set(int idx,AnyType newVal){
        Node<AnyType> p = getNode(idx);
        AnyType oldVal = p.data;
        p.data = newVal;
        return oldVal;
    }

    /**
     * 按位置取出对象
     * @param idx 取出对象位置
     * @return
     */
    private Node<AnyType> getNode(int idx){
        return getNode(idx,0,size()-1);
    }

    /**
     * 获取插入位置之前的对象
     * @param idx 插入位置
     * @param lower 0
     * @param upper 现有长度
     * @return 插入位置现有元素
     */
    private Node<AnyType> getNode(int idx,int lower,int upper){
        Node<AnyType> p;
        //如果插入位置小于0，或者大于链表现有长度，抛出异常
        if(idx<lower || idx >upper)
            throw new IndexOutOfBoundsException();
        //如果插入位置在链表的前半部分
        if(idx<size()/2){
            //先赋值头节点，因为当插入位置为第一位时直接弹出
            p = beginMarker.next;
            //从头节点开始寻找插入位置
            for (int i = 0;i<idx;i++)
                //取出循环位置的下一个，即为需要插入的位置
                p = p.next;
        }else {
            //当插入位置在链表后半部
            //先赋值尾节点，因为当插入位置为最后一位位时直接弹出
            p =endMarker;
            //从尾位置查询
            for (int i= size();i>idx;i--)
                //取出循环位置的上一个，即为需要插入的位置
                p = p.prev;
        }
        return p;
    }

    /**
     * 新增节点方法
     * @param p 插入位置现有对象数据
     * @param x 新增对象数据
     */
    private void addBefore(Node<AnyType> p,AnyType x){
        //创建新增对象节点，p.prev为之前位置节点的母节点，子节点为插入前前当前位置节点
        Node<AnyType> newNode = new Node<>(x,p.prev,p);
        //修改父节点子节点，新创建对象节点复制给，之前父节点的自阶段
        newNode.prev.next = newNode;
        //修改位置节点的父节点
        p.prev = newNode;
        //长度加1
        theSize++;
        //操作计数加1
        modCount++;
    }

}
