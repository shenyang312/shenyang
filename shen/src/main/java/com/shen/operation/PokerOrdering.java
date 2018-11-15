package com.shen.operation;

import java.util.Arrays;

/**
 * 题目：一副从1到n的牌，每次从牌堆顶取一张放桌子上，再取一张放牌堆底，直到手上没牌，最后桌子上的牌是从1到n有序，设计程序，输入n，输出牌堆的顺序数组
 */
public class PokerOrdering {


    public static void main(String[] args) {
        //逆向，把1～n顺序，转换成 b 1～n无序
        int[] a = calculate(8);
        //再把 1～n无序转换成 有序
        calculateTwo(a);
    }


    public static int[] calculate(int n){
        int [] a = new int[0]; // a是原数组，初始长度为0
        int [] b = new int[n]; // b是输出数组，按照题意按照1~n顺序

        // 初始化b数组，按照题意按照1~n顺序
        for (int i = 0; i < n; i++) {
            b[i] = i+1;
        }

        //执行逆向过程处理
        return reverse(a,b);

    }
    /**
     * 逆向的过程为：从b中堆顶取一张牌，放入a的堆顶，然后将a的堆底的牌，放到a的堆顶
     * @param a
     * @param b
     */
    private static int[] reverse(int[] a, int[] b) {
        //首先要将b中堆底牌放入a的堆顶，a需要先扩容
        a = Arrays.copyOf(a, a.length+1);
        //将a的数组的元素全部后移一位
        System.arraycopy(a, 0, a, 1, a.length-1);
        //给a的堆顶赋值
        a[0] = b[b.length-1];
        //b的长度减一
        b = Arrays.copyOf(b, b.length-1);
        //如果b的长度为零，则整个过程结束，并输出数组a
        if(0==b.length){
            System.out.println(Arrays.toString(a));
            return a;
        }
        //将a的堆底的牌，放到a的堆顶
        int cmp = a[a.length-1];
        System.arraycopy(a, 0, a, 1, a.length-1);
        a[0] = cmp;
        //递归
        return reverse(a, b);
    }


    public static void calculateTwo(int [] a){
//        a = new int[]{1,5,2,7,3,6,4,8}; // a是原数组，初始长度为0
        int [] b = new int[0]; // b是输出数组，按照题意按照1~n顺序
        //执行逆向过程处理
        reverseTwo(a,b,0);

    }

    /**
     * 正向向的过程为：从a中堆顶取一张牌，放入b的堆顶，然后将a的堆定的牌，放到a的堆底
     * @param a
     * @param b
     */
    private static void reverseTwo(int[] a, int[] b,int num) {
        int bLen = num;
        int excessiveLen = 0;
        int[] excessive = new int[a.length/2];
        //循环全部1～n基数挑出，偶数组成新的数组，把之前顺序倒叙
        for (int i = 0; i < a.length; i++) {
            if(i%2 != 0){
                excessive[excessiveLen] = a[i];
                excessiveLen++;
                continue;
            }
            b = Arrays.copyOf(b, b.length+1);
            b[bLen] = a[i];
            bLen++;
        }
        if(a.length != 0){
            reverseTwo(excessive,b,b.length);
            return;
        }
        System.out.println(Arrays.toString(b));
    }
}
