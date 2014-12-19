package com.mongodb;

import java.util.*;

interface Deque<E> extends Queue<E>
{
    boolean offerFirst(E p0);
    
    boolean offerLast(E p0);
    
    void addFirst(E p0);
    
    void addLast(E p0);
    
    E pollFirst();
    
    E pollLast();
    
    E removeFirst();
    
    E removeLast();
    
    E peekFirst();
    
    E peekLast();
    
    E getFirst();
    
    E getLast();
    
    boolean removeFirstOccurrence(Object p0);
    
    boolean removeLastOccurrence(Object p0);
    
    boolean offer(E p0);
    
    boolean add(E p0);
    
    E poll();
    
    E remove();
    
    E peek();
    
    E element();
    
    void push(E p0);
    
    E pop();
    
    Iterator<E> iterator();
}
