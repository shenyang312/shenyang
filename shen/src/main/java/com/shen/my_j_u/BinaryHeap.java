package com.shen.my_j_u;

/**
 *  优先队列
 */
public class BinaryHeap<T extends Comparable<? super T>> {

    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 堆中元素个数
     */
    private int currentSize;


    private T[] array;

    public BinaryHeap() {
        super();
        currentSize = 0;
        array = (T[]) new Comparable[ DEFAULT_CAPACITY ];
    }

    public BinaryHeap( T[] items ){
        currentSize = items.length;
        array = (T[]) new Comparable[ (currentSize + 2) * 11 / 10 ];

        int i=1;
        for( T item : items ){
            array[ i++ ] = item;
        }
        buildHeap();
    }

    private void buildHeap(){
        for( int i = currentSize/2; i>0; i-- )
            percolateDown( i );
    }

    private void enlargeArray( int size ){
        Comparable[] newArray = new Comparable[size];
        for( int i=1 ; i<array.length ; i++ ){
            newArray[i] = array[i];
        }

        array = (T[]) newArray;
    }

    public void insert( T x ){
        if( currentSize == array.length - 1 ){
            enlargeArray( array.length*2 + 1 );
        }

        //上滤
        int hole = ++currentSize;
        //如果插入的值比堆中间位置得值小，即上滤，把上节点位置下移动留一个位置给x，如果再小，继续上滤
        for( array[0] = x; hole>1 && x.compareTo( array[ hole/2 ])<0; hole/=2 ){
            array[ hole ] = array[ hole/2 ];
        }
        array[ hole ] = x;
    }

    public boolean isEmpty(){
        return currentSize == 0;
    }

    /**
     * 删除最小
     * @return
     */
    public T deleteMin(){
        if( isEmpty() )
            throw new UnderflowException();

        T minItem = array[1];
        array[1] = array[ currentSize-- ];
        percolateDown( 1 );

        return minItem;
    }

    /**
     * 下滤
     * @param hole
     */
    private void percolateDown( int hole ){
        int child;
        T tmp = array[ hole ];

        for( ; hole*2 <= currentSize; hole=child ){
            child = hole*2;

            if( child != currentSize && array[child+1].compareTo( array[child] ) <0 ){
                child++;
            }

            if( array[child].compareTo( tmp ) < 0 ){
                array[hole] = array[child];
            }
            else{
                break;
            }
        }
        array[hole] = tmp;
    }
}

class UnderflowException extends RuntimeException{

}
