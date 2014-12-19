package org.apache.commons.pool2.impl;

import java.util.concurrent.locks.*;
import java.util.concurrent.*;
import java.util.*;
import java.lang.reflect.*;
import java.io.*;

class LinkedBlockingDeque<E> extends AbstractQueue<E> implements Deque<E>, Serializable
{
    private static final long serialVersionUID = -387911632671998426L;
    private transient Node<E> first;
    private transient Node<E> last;
    private transient int count;
    private final int capacity;
    private final InterruptibleReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;
    
    public LinkedBlockingDeque() {
        this(Integer.MAX_VALUE);
    }
    
    public LinkedBlockingDeque(final int capacity) {
        super();
        this.lock = new InterruptibleReentrantLock();
        this.notEmpty = this.lock.newCondition();
        this.notFull = this.lock.newCondition();
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
    }
    
    public LinkedBlockingDeque(final Collection<? extends E> c) {
        this(Integer.MAX_VALUE);
        this.lock.lock();
        try {
            for (final E e : c) {
                if (e == null) {
                    throw new NullPointerException();
                }
                if (!this.linkLast(e)) {
                    throw new IllegalStateException("Deque full");
                }
            }
        }
        finally {
            this.lock.unlock();
        }
    }
    
    private boolean linkFirst(final E e) {
        if (this.count >= this.capacity) {
            return false;
        }
        final Node<E> f = this.first;
        final Node<E> x = new Node<E>(e, null, f);
        this.first = x;
        if (this.last == null) {
            this.last = x;
        }
        else {
            f.prev = x;
        }
        ++this.count;
        this.notEmpty.signal();
        return true;
    }
    
    private boolean linkLast(final E e) {
        if (this.count >= this.capacity) {
            return false;
        }
        final Node<E> l = this.last;
        final Node<E> x = new Node<E>(e, l, null);
        this.last = x;
        if (this.first == null) {
            this.first = x;
        }
        else {
            l.next = x;
        }
        ++this.count;
        this.notEmpty.signal();
        return true;
    }
    
    private E unlinkFirst() {
        final Node<E> f = this.first;
        if (f == null) {
            return null;
        }
        final Node<E> n = f.next;
        final E item = f.item;
        f.item = null;
        f.next = f;
        if ((this.first = n) == null) {
            this.last = null;
        }
        else {
            n.prev = null;
        }
        --this.count;
        this.notFull.signal();
        return item;
    }
    
    private E unlinkLast() {
        final Node<E> l = this.last;
        if (l == null) {
            return null;
        }
        final Node<E> p = l.prev;
        final E item = l.item;
        l.item = null;
        l.prev = l;
        if ((this.last = p) == null) {
            this.first = null;
        }
        else {
            p.next = null;
        }
        --this.count;
        this.notFull.signal();
        return item;
    }
    
    private void unlink(final Node<E> x) {
        final Node<E> p = x.prev;
        final Node<E> n = x.next;
        if (p == null) {
            this.unlinkFirst();
        }
        else if (n == null) {
            this.unlinkLast();
        }
        else {
            p.next = n;
            n.prev = p;
            x.item = null;
            --this.count;
            this.notFull.signal();
        }
    }
    
    @Override
    public void addFirst(final E e) {
        if (!this.offerFirst(e)) {
            throw new IllegalStateException("Deque full");
        }
    }
    
    @Override
    public void addLast(final E e) {
        if (!this.offerLast(e)) {
            throw new IllegalStateException("Deque full");
        }
    }
    
    @Override
    public boolean offerFirst(final E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        this.lock.lock();
        try {
            return this.linkFirst(e);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public boolean offerLast(final E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        this.lock.lock();
        try {
            return this.linkLast(e);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public void putFirst(final E e) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        this.lock.lock();
        try {
            while (!this.linkFirst(e)) {
                this.notFull.await();
            }
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public void putLast(final E e) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        this.lock.lock();
        try {
            while (!this.linkLast(e)) {
                this.notFull.await();
            }
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public boolean offerFirst(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        long nanos = unit.toNanos(timeout);
        this.lock.lockInterruptibly();
        try {
            while (!this.linkFirst(e)) {
                if (nanos <= 0L) {
                    return false;
                }
                nanos = this.notFull.awaitNanos(nanos);
            }
            return true;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public boolean offerLast(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        long nanos = unit.toNanos(timeout);
        this.lock.lockInterruptibly();
        try {
            while (!this.linkLast(e)) {
                if (nanos <= 0L) {
                    return false;
                }
                nanos = this.notFull.awaitNanos(nanos);
            }
            return true;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public E removeFirst() {
        final E x = this.pollFirst();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }
    
    @Override
    public E removeLast() {
        final E x = this.pollLast();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }
    
    @Override
    public E pollFirst() {
        this.lock.lock();
        try {
            return this.unlinkFirst();
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public E pollLast() {
        this.lock.lock();
        try {
            return this.unlinkLast();
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public E takeFirst() throws InterruptedException {
        this.lock.lock();
        try {
            E x;
            while ((x = this.unlinkFirst()) == null) {
                this.notEmpty.await();
            }
            return x;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public E takeLast() throws InterruptedException {
        this.lock.lock();
        try {
            E x;
            while ((x = this.unlinkLast()) == null) {
                this.notEmpty.await();
            }
            return x;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public E pollFirst(final long timeout, final TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        this.lock.lockInterruptibly();
        try {
            E x;
            while ((x = this.unlinkFirst()) == null) {
                if (nanos <= 0L) {
                    return null;
                }
                nanos = this.notEmpty.awaitNanos(nanos);
            }
            return x;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public E pollLast(final long timeout, final TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        this.lock.lockInterruptibly();
        try {
            E x;
            while ((x = this.unlinkLast()) == null) {
                if (nanos <= 0L) {
                    return null;
                }
                nanos = this.notEmpty.awaitNanos(nanos);
            }
            return x;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public E getFirst() {
        final E x = this.peekFirst();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }
    
    @Override
    public E getLast() {
        final E x = this.peekLast();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }
    
    @Override
    public E peekFirst() {
        this.lock.lock();
        try {
            return (this.first == null) ? null : this.first.item;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public E peekLast() {
        this.lock.lock();
        try {
            return (this.last == null) ? null : this.last.item;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public boolean removeFirstOccurrence(final Object o) {
        if (o == null) {
            return false;
        }
        this.lock.lock();
        try {
            for (Node<E> p = this.first; p != null; p = p.next) {
                if (o.equals(p.item)) {
                    this.unlink(p);
                    return true;
                }
            }
            return false;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public boolean removeLastOccurrence(final Object o) {
        if (o == null) {
            return false;
        }
        this.lock.lock();
        try {
            for (Node<E> p = this.last; p != null; p = p.prev) {
                if (o.equals(p.item)) {
                    this.unlink(p);
                    return true;
                }
            }
            return false;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public boolean add(final E e) {
        this.addLast(e);
        return true;
    }
    
    @Override
    public boolean offer(final E e) {
        return this.offerLast(e);
    }
    
    public void put(final E e) throws InterruptedException {
        this.putLast(e);
    }
    
    public boolean offer(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.offerLast(e, timeout, unit);
    }
    
    @Override
    public E remove() {
        return this.removeFirst();
    }
    
    @Override
    public E poll() {
        return this.pollFirst();
    }
    
    public E take() throws InterruptedException {
        return this.takeFirst();
    }
    
    public E poll(final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.pollFirst(timeout, unit);
    }
    
    @Override
    public E element() {
        return this.getFirst();
    }
    
    @Override
    public E peek() {
        return this.peekFirst();
    }
    
    public int remainingCapacity() {
        this.lock.lock();
        try {
            return this.capacity - this.count;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public int drainTo(final Collection<? super E> c) {
        return this.drainTo(c, Integer.MAX_VALUE);
    }
    
    public int drainTo(final Collection<? super E> c, final int maxElements) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            throw new IllegalArgumentException();
        }
        this.lock.lock();
        try {
            final int n = Math.min(maxElements, this.count);
            for (int i = 0; i < n; ++i) {
                c.add((Object)this.first.item);
                this.unlinkFirst();
            }
            return n;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public void push(final E e) {
        this.addFirst(e);
    }
    
    @Override
    public E pop() {
        return this.removeFirst();
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.removeFirstOccurrence(o);
    }
    
    @Override
    public int size() {
        this.lock.lock();
        try {
            return this.count;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public boolean contains(final Object o) {
        if (o == null) {
            return false;
        }
        this.lock.lock();
        try {
            for (Node<E> p = this.first; p != null; p = p.next) {
                if (o.equals(p.item)) {
                    return true;
                }
            }
            return false;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public Object[] toArray() {
        this.lock.lock();
        try {
            final Object[] a = new Object[this.count];
            int k = 0;
            for (Node<E> p = this.first; p != null; p = p.next) {
                a[k++] = p.item;
            }
            return a;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public <T> T[] toArray(T[] a) {
        this.lock.lock();
        try {
            if (a.length < this.count) {
                a = (T[])Array.newInstance(a.getClass().getComponentType(), this.count);
            }
            int k = 0;
            for (Node<E> p = this.first; p != null; p = p.next) {
                a[k++] = (T)p.item;
            }
            if (a.length > k) {
                a[k] = null;
            }
            return a;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public String toString() {
        this.lock.lock();
        try {
            return super.toString();
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public void clear() {
        this.lock.lock();
        try {
            Node<E> n;
            for (Node<E> f = this.first; f != null; f = n) {
                f.item = null;
                n = f.next;
                f.prev = null;
                f.next = null;
            }
            final Node<E> node = null;
            this.last = node;
            this.first = node;
            this.count = 0;
            this.notFull.signalAll();
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }
    
    @Override
    public Iterator<E> descendingIterator() {
        return new DescendingItr();
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        this.lock.lock();
        try {
            s.defaultWriteObject();
            for (Node<E> p = this.first; p != null; p = p.next) {
                s.writeObject(p.item);
            }
            s.writeObject(null);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.count = 0;
        this.first = null;
        this.last = null;
        while (true) {
            final E item = (E)s.readObject();
            if (item == null) {
                break;
            }
            this.add(item);
        }
    }
    
    public boolean hasTakeWaiters() {
        this.lock.lock();
        try {
            return this.lock.hasWaiters(this.notEmpty);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public int getTakeQueueLength() {
        this.lock.lock();
        try {
            return this.lock.getWaitQueueLength(this.notEmpty);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public void interuptTakeWaiters() {
        this.lock.lock();
        try {
            this.lock.interruptWaiters(this.notEmpty);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    private static final class Node<E>
    {
        E item;
        Node<E> prev;
        Node<E> next;
        
        Node(final E x, final Node<E> p, final Node<E> n) {
            super();
            this.item = x;
            this.prev = p;
            this.next = n;
        }
    }
    
    private abstract class AbstractItr implements Iterator<E>
    {
        Node<E> next;
        E nextItem;
        private Node<E> lastRet;
        
        abstract Node<E> firstNode();
        
        abstract Node<E> nextNode(final Node<E> p0);
        
        AbstractItr() {
            super();
            LinkedBlockingDeque.this.lock.lock();
            try {
                this.next = this.firstNode();
                this.nextItem = ((this.next == null) ? null : this.next.item);
            }
            finally {
                LinkedBlockingDeque.this.lock.unlock();
            }
        }
        
        void advance() {
            LinkedBlockingDeque.this.lock.lock();
            try {
                Node<E> s = this.nextNode(this.next);
                if (s == this.next) {
                    this.next = this.firstNode();
                }
                else {
                    while (s != null && s.item == null) {
                        s = this.nextNode(s);
                    }
                    this.next = s;
                }
                this.nextItem = ((this.next == null) ? null : this.next.item);
            }
            finally {
                LinkedBlockingDeque.this.lock.unlock();
            }
        }
        
        @Override
        public boolean hasNext() {
            return this.next != null;
        }
        
        @Override
        public E next() {
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            this.lastRet = this.next;
            final E x = this.nextItem;
            this.advance();
            return x;
        }
        
        @Override
        public void remove() {
            final Node<E> n = this.lastRet;
            if (n == null) {
                throw new IllegalStateException();
            }
            this.lastRet = null;
            LinkedBlockingDeque.this.lock.lock();
            try {
                if (n.item != null) {
                    LinkedBlockingDeque.this.unlink(n);
                }
            }
            finally {
                LinkedBlockingDeque.this.lock.unlock();
            }
        }
    }
    
    private class Itr extends AbstractItr
    {
        @Override
        Node<E> firstNode() {
            return LinkedBlockingDeque.this.first;
        }
        
        @Override
        Node<E> nextNode(final Node<E> n) {
            return n.next;
        }
    }
    
    private class DescendingItr extends AbstractItr
    {
        @Override
        Node<E> firstNode() {
            return LinkedBlockingDeque.this.last;
        }
        
        @Override
        Node<E> nextNode(final Node<E> n) {
            return n.prev;
        }
    }
}
