package com.shen.my_j_u;

import org.omg.CORBA.Any;

import java.nio.BufferUnderflowException;

public class MyAVLTree<AnyType extends Comparable<? super AnyType>> {
    /**
     * 平衡节点内部类
     * 注：与普通二叉树的区别在于，他需要记录每个节点的高度
     * @param <AnyType>
     */
    private static class AvlNode<AnyType>{
        AvlNode( AnyType theElement){
            this(theElement,null,null);
        }
        AvlNode( AnyType theElement,AvlNode<AnyType> lt, AvlNode<AnyType> rt){
            element = theElement;
            left = lt;
            right = rt;
            height = 0;
        }

        AnyType element;
        AvlNode<AnyType> left;
        AvlNode<AnyType> right;
        int height;
    }

    private AvlNode<AnyType> root;
    private static final int ALLOWED_IMBALANCE = 1;

    /**
     * 获取节点高度私有方法
     * @param t
     * @return
     */
    private int height(AvlNode<AnyType> t){
        return t ==null ?-1:t.height;
    }

    //既然大家都是了解二叉树的查找等结构的，那么我着重讲解一下平衡二叉树的---- 插入与删除的旋转

    private AvlNode<AnyType> insert(AnyType x,AvlNode<AnyType> t){
        if(t == null)
            return new AvlNode<>(x,null,null);
        int compareResult = x.compareTo(t.element);
        if(compareResult<0)
            t.left = insert(x,t.left);
        else if(compareResult > 0)
            t.right = insert(x,t.right);
        else
            ;
        return balance(t);
    }

    private  AvlNode<AnyType> balance(AvlNode<AnyType> t){
        if(t==null)
            return t;
        //如果左侧比右侧高一
        if(height(t.left) - height(t.right)>ALLOWED_IMBALANCE)
            //如果时左左情况，单侧旋转
            if(height(t.left.left)>=height(t.left.right))
                //单侧-右旋转
                t = rotateWithLeftChild(t);
            else
                t = doubleWithLeftChild(t);
        else
            //如果右侧比左侧高一
            if(height(t.right)-height(t.left)>ALLOWED_IMBALANCE)
                //如果是右右情况，单侧旋转
                if(height(t.right.right)>=height(t.right.left))
                    //单侧-左旋转
                    t = rotateWithRightChild(t);
                else
                    t = doubleWithRightChild(t);
        t.height = Math.max(height(t.left),height(t.right))+1;
        return t;
    }

    /**
     * 单侧-右旋转
     * @param k2
     * @return
     */
    private AvlNode<AnyType> rotateWithLeftChild(AvlNode<AnyType> k2){
        //先取出左侧的参数，这是想办法，把k1转到右侧
        AvlNode<AnyType> k1 = k2.left;
        //把当前节点的左侧子节点的右节点，赋值给当前节点的左节点，实现旋转节点的左侧右侧节点，右向左做旋转
        k2.left = k1.right;
        //然后把把旋转节点右移动，把之前的根节点k2变成其右侧节点，实现k2左侧移动
        k1.right = k2;
        //重新赋值k2高度
        k2.height = Math.max(height(k2.left),height(k2.right))+1;
        //重新赋值k1高度
        k1.height = Math.max(height(k1.left),k2.height) + 1;
        //旋转后，返回新的节点
        return k1;
    }

    private AvlNode<AnyType> rotateWithRightChild(AvlNode<AnyType> k2){
        AvlNode<AnyType> k1 = k2.right;
        k2.right = k1.left;
        k1.left = k2;
        k2.height = Math.max(height(k2.right),height(k2.left))+1;
        k1.height = Math.max(height(k1.right),k2.height) + 1;
        return k1;
    }

    private AvlNode<AnyType> doubleWithLeftChild(AvlNode<AnyType> k3){
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    private AvlNode<AnyType> doubleWithRightChild(AvlNode<AnyType> k3){
        k3.right = rotateWithRightChild(k3.right);
        return rotateWithRightChild(k3);
    }

    /**
     * 传入参数是删除数据和 节点t
     * 默认节点t为root跟节点
     * @param x
     * @param t
     * @return
     */
    private AvlNode<AnyType> remove(AnyType x,AvlNode<AnyType> t){
        if(t==null)
            return t;
        //compareTo方法，相当返回0，小于返回-1，大于返回1
        //先查找当前删除参数是否属于当前节点
        int compareResult = x.compareTo(t.element);
        //未查询到即继续递归查询
        //即未折半查找法
        if(compareResult<0)
            t.left = remove(x,t.left);
        if(compareResult<0)
            t.left = remove(x,t.left);
        else if(compareResult>0)
            t.right = remove(x,t.right);
        else if(t.left != null && t.right != null){
            t.element = findMin(t.right).element;
            t.right = remove(t.element,t.right);
        }
        else
            t = (t.left != null) ? t.left :t.right;
        return balance(t);
    }

    public AvlNode<AnyType> findMin(){
        if(isEmty())
            throw new BufferUnderflowException();
        return findMin(root);
    }

    private AvlNode<AnyType> findMin(AvlNode<AnyType> t){
        if(t ==null)
            return null;
        else if(t.left == null)
            return t;
        return findMin(t.left);
    }

    public boolean isEmty(){
        return root == null;
    }
}
