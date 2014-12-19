package com.mongodb;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

class ConcurrentLinkedDeque<E> extends AbstractCollection<E> implements Deque<E>, Serializable
{
    private static final long serialVersionUID = 876323262645176354L;
    private final Node<E> header;
    private final Node<E> trailer;
    
    private static boolean usable(final Node<?> n) {
        return n != null && !n.isSpecial();
    }
    
    private static void checkNotNull(final Object v) {
        if (v == null) {
            throw new NullPointerException();
        }
    }
    
    private E screenNullResult(final E v) {
        if (v == null) {
            throw new NoSuchElementException();
        }
        return v;
    }
    
    private ArrayList<E> toArrayList() {
        final ArrayList<E> c = new ArrayList<E>();
        for (Node<E> n = this.header.forward(); n != null; n = n.forward()) {
            c.add(n.element);
        }
        return c;
    }
    
    public ConcurrentLinkedDeque() {
        super();
        final Node<E> h = new Node<E>(null, null, null);
        final Node<E> t = new Node<E>(null, null, h);
        h.setNext(t);
        this.header = h;
        this.trailer = t;
    }
    
    public ConcurrentLinkedDeque(final Collection<? extends E> c) {
        this();
        this.addAll(c);
    }
    
    public void addFirst(final E e) {
        checkNotNull(e);
        while (this.header.append(e) == null) {}
    }
    
    public void addLast(final E e) {
        checkNotNull(e);
        while (this.trailer.prepend(e) == null) {}
    }
    
    public boolean offerFirst(final E e) {
        this.addFirst(e);
        return true;
    }
    
    public boolean offerLast(final E e) {
        this.addLast(e);
        return true;
    }
    
    public E peekFirst() {
        final Node<E> n = this.header.successor();
        return (n == null) ? null : n.element;
    }
    
    public E peekLast() {
        final Node<E> n = this.trailer.predecessor();
        return (n == null) ? null : n.element;
    }
    
    public E getFirst() {
        return this.screenNullResult(this.peekFirst());
    }
    
    public E getLast() {
        return this.screenNullResult(this.peekLast());
    }
    
    public E pollFirst() {
        while (true) {
            final Node<E> n = this.header.successor();
            if (!usable(n)) {
                return null;
            }
            if (n.delete()) {
                return n.element;
            }
        }
    }
    
    public E pollLast() {
        while (true) {
            final Node<E> n = this.trailer.predecessor();
            if (!usable(n)) {
                return null;
            }
            if (n.delete()) {
                return n.element;
            }
        }
    }
    
    public E removeFirst() {
        return this.screenNullResult(this.pollFirst());
    }
    
    public E removeLast() {
        return this.screenNullResult(this.pollLast());
    }
    
    public boolean offer(final E e) {
        return this.offerLast(e);
    }
    
    public boolean add(final E e) {
        return this.offerLast(e);
    }
    
    public E poll() {
        return this.pollFirst();
    }
    
    public E remove() {
        return this.removeFirst();
    }
    
    public E peek() {
        return this.peekFirst();
    }
    
    public E element() {
        return this.getFirst();
    }
    
    public void push(final E e) {
        this.addFirst(e);
    }
    
    public E pop() {
        return this.removeFirst();
    }
    
    public boolean removeFirstOccurrence(final Object o) {
        checkNotNull(o);
    Label_0004:
        while (true) {
            Node<E> n = this.header.forward();
            while (n != null) {
                if (o.equals(n.element)) {
                    if (n.delete()) {
                        return true;
                    }
                    continue Label_0004;
                }
                else {
                    n = n.forward();
                }
            }
            return false;
        }
    }
    
    public boolean removeLastOccurrence(final Object o) {
        checkNotNull(o);
        while (true) {
            Node<E> s = this.trailer;
            while (true) {
                final Node<E> n = s.back();
                if (s.isDeleted()) {
                    break;
                }
                if (n != null && n.successor() != s) {
                    break;
                }
                if (n == null) {
                    return false;
                }
                if (o.equals(n.element)) {
                    if (n.delete()) {
                        return true;
                    }
                    break;
                }
                else {
                    s = n;
                }
            }
        }
    }
    
    public boolean contains(final Object o) {
        if (o == null) {
            return false;
        }
        for (Node<E> n = this.header.forward(); n != null; n = n.forward()) {
            if (o.equals(n.element)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEmpty() {
        return !usable(this.header.successor());
    }
    
    public int size() {
        long count = 0L;
        for (Node<E> n = this.header.forward(); n != null; n = n.forward()) {
            ++count;
        }
        return (count >= 2147483647L) ? Integer.MAX_VALUE : ((int)count);
    }
    
    public boolean remove(final Object o) {
        return this.removeFirstOccurrence(o);
    }
    
    public boolean addAll(final Collection<? extends E> c) {
        final Iterator<? extends E> it = c.iterator();
        if (!it.hasNext()) {
            return false;
        }
        do {
            this.addLast(it.next());
        } while (it.hasNext());
        return true;
    }
    
    public void clear() {
        while (this.pollFirst() != null) {}
    }
    
    public Object[] toArray() {
        return this.toArrayList().toArray();
    }
    
    public <T> T[] toArray(final T[] a) {
        return this.toArrayList().toArray(a);
    }
    
    public Iterator<E> iterator() {
        return new CLDIterator();
    }
    
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }
    
    static final class Node<E> extends AtomicReference<Node<E>>
    {
        private volatile Node<E> prev;
        final E element;
        private static final long serialVersionUID = 876323262645176354L;
        
        Node(final E element, final Node<E> next, final Node<E> prev) {
            super(next);
            this.prev = prev;
            this.element = element;
        }
        
        Node(final Node<E> next) {
            super(next);
            this.prev = this;
            this.element = null;
        }
        
        private Node<E> getNext() {
            return this.get();
        }
        
        void setNext(final Node<E> n) {
            this.set(n);
        }
        
        private boolean casNext(final Node<E> cmp, final Node<E> val) {
            return this.compareAndSet(cmp, val);
        }
        
        private Node<E> getPrev() {
            return this.prev;
        }
        
        void setPrev(final Node<E> b) {
            this.prev = b;
        }
        
        boolean isSpecial() {
            return this.element == null;
        }
        
        boolean isTrailer() {
            return this.getNext() == null;
        }
        
        boolean isHeader() {
            return this.getPrev() == null;
        }
        
        boolean isMarker() {
            return this.getPrev() == this;
        }
        
        boolean isDeleted() {
            final Node<E> f = this.getNext();
            return f != null && f.isMarker();
        }
        
        private Node<E> nextNonmarker() {
            final Node<E> f = this.getNext();
            return (f == null || !f.isMarker()) ? f : f.getNext();
        }
        
        Node<E> successor() {
            Node<E> s;
            for (Node<E> f = this.nextNonmarker(); f != null; f = s) {
                if (!f.isDeleted()) {
                    if (f.getPrev() != this && !this.isDeleted()) {
                        f.setPrev(this);
                    }
                    return f;
                }
                s = f.nextNonmarker();
                if (f == this.getNext()) {
                    this.casNext(f, s);
                }
            }
            return null;
        }
        
        private Node<E> findPredecessorOf(final Node<E> target) {
            Node<E> n = this;
            while (true) {
                final Node<E> f = n.successor();
                if (f == target) {
                    return n;
                }
                if (f == null) {
                    return null;
                }
                n = f;
            }
        }
        
        Node<E> predecessor() {
            Node<E> n = this;
            while (true) {
                final Node<E> b = n.getPrev();
                if (b == null) {
                    return n.findPredecessorOf(this);
                }
                final Node<E> s = b.getNext();
                if (s == this) {
                    return b;
                }
                if (s == null || !s.isMarker()) {
                    final Node<E> p = b.findPredecessorOf(this);
                    if (p != null) {
                        return p;
                    }
                }
                n = b;
            }
        }
        
        Node<E> forward() {
            final Node<E> f = this.successor();
            return (f == null || f.isSpecial()) ? null : f;
        }
        
        Node<E> back() {
            final Node<E> f = this.predecessor();
            return (f == null || f.isSpecial()) ? null : f;
        }
        
        Node<E> append(final E element) {
            while (true) {
                final Node<E> f = this.getNext();
                if (f == null || f.isMarker()) {
                    return null;
                }
                final Node<E> x = new Node<E>(element, f, this);
                if (this.casNext(f, x)) {
                    f.setPrev(x);
                    return x;
                }
            }
        }
        
        Node<E> prepend(final E element) {
            while (true) {
                final Node<E> b = this.predecessor();
                if (b == null) {
                    return null;
                }
                final Node<E> x = new Node<E>(element, this, b);
                if (b.casNext(this, x)) {
                    this.setPrev(x);
                    return x;
                }
            }
        }
        
        boolean delete() {
            final Node<E> b = this.getPrev();
            final Node<E> f = this.getNext();
            if (b != null && f != null && !f.isMarker() && this.casNext(f, new Node<E>(f))) {
                if (b.casNext(this, f)) {
                    f.setPrev(b);
                }
                return true;
            }
            return false;
        }
        
        Node<E> replace(final E newElement) {
            while (true) {
                final Node<E> b = this.getPrev();
                final Node<E> f = this.getNext();
                if (b == null || f == null || f.isMarker()) {
                    return null;
                }
                final Node<E> x = new Node<E>(newElement, f, b);
                if (this.casNext(f, new Node<E>(x))) {
                    b.successor();
                    x.successor();
                    return x;
                }
            }
        }
    }
    
    final class CLDIterator implements Iterator<E>
    {
        Node<E> last;
        Node<E> next;
        
        CLDIterator() {
            super();
            this.next = ConcurrentLinkedDeque.this.header.forward();
        }
        
        public boolean hasNext() {
            return this.next != null;
        }
        
        public E next() {
            final Node<E> next = this.next;
            this.last = next;
            final Node<E> l = next;
            if (l == null) {
                throw new NoSuchElementException();
            }
            this.next = this.next.forward();
            return l.element;
        }
        
        public void remove() {
            final Node<E> l = this.last;
            if (l == null) {
                throw new IllegalStateException();
            }
            while (!l.delete() && !l.isDeleted()) {}
        }
    }
}
