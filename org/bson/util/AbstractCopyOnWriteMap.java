package org.bson.util;

import java.util.concurrent.*;
import java.io.*;
import org.bson.util.annotations.*;
import java.util.concurrent.locks.*;
import java.util.*;

@ThreadSafe
abstract class AbstractCopyOnWriteMap<K, V, M extends Map<K, V>> implements ConcurrentMap<K, V>, Serializable
{
    private static final long serialVersionUID = 4508989182041753878L;
    @GuardedBy("lock")
    private volatile M delegate;
    private final transient Lock lock;
    private final View<K, V> view;
    
    protected AbstractCopyOnWriteMap(final N map, final View.Type viewType) {
        super();
        this.lock = new ReentrantLock();
        this.delegate = Assertions.notNull("delegate", this.copy((Map)Assertions.notNull("map", (N)map)));
        this.view = Assertions.notNull("viewType", viewType).get((AbstractCopyOnWriteMap<K, V, Map>)this);
    }
    
    @GuardedBy("lock")
    abstract <N extends Map<? extends K, ? extends V>> M copy(final N p0);
    
    public final void clear() {
        this.lock.lock();
        try {
            this.set(this.copy(Collections.emptyMap()));
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public final V remove(final Object key) {
        this.lock.lock();
        try {
            if (!this.delegate.containsKey(key)) {
                return null;
            }
            final M map = this.copy();
            try {
                return ((Map<K, V>)map).remove(key);
            }
            finally {
                this.set(map);
            }
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public boolean remove(final Object key, final Object value) {
        this.lock.lock();
        try {
            if (this.delegate.containsKey(key) && this.equals(value, ((Map<K, Object>)this.delegate).get(key))) {
                final M map = this.copy();
                map.remove(key);
                this.set(map);
                return true;
            }
            return false;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public boolean replace(final K key, final V oldValue, final V newValue) {
        this.lock.lock();
        try {
            if (!this.delegate.containsKey(key) || !this.equals(oldValue, ((Map<K, Object>)this.delegate).get(key))) {
                return false;
            }
            final M map = this.copy();
            map.put(key, newValue);
            this.set(map);
            return true;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public V replace(final K key, final V value) {
        this.lock.lock();
        try {
            if (!this.delegate.containsKey(key)) {
                return null;
            }
            final M map = this.copy();
            try {
                return map.put(key, value);
            }
            finally {
                this.set(map);
            }
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public final V put(final K key, final V value) {
        this.lock.lock();
        try {
            final M map = this.copy();
            try {
                return map.put(key, value);
            }
            finally {
                this.set(map);
            }
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public V putIfAbsent(final K key, final V value) {
        this.lock.lock();
        try {
            if (!this.delegate.containsKey(key)) {
                final M map = this.copy();
                try {
                    return map.put(key, value);
                }
                finally {
                    this.set(map);
                }
            }
            return ((Map<K, V>)this.delegate).get(key);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public final void putAll(final Map<? extends K, ? extends V> t) {
        this.lock.lock();
        try {
            final M map = this.copy();
            map.putAll(t);
            this.set(map);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    protected M copy() {
        this.lock.lock();
        try {
            return this.copy(this.delegate);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @GuardedBy("lock")
    protected void set(final M map) {
        this.delegate = map;
    }
    
    public final Set<Map.Entry<K, V>> entrySet() {
        return this.view.entrySet();
    }
    
    public final Set<K> keySet() {
        return this.view.keySet();
    }
    
    public final Collection<V> values() {
        return this.view.values();
    }
    
    public final boolean containsKey(final Object key) {
        return this.delegate.containsKey(key);
    }
    
    public final boolean containsValue(final Object value) {
        return this.delegate.containsValue(value);
    }
    
    public final V get(final Object key) {
        return ((Map<K, V>)this.delegate).get(key);
    }
    
    public final boolean isEmpty() {
        return this.delegate.isEmpty();
    }
    
    public final int size() {
        return this.delegate.size();
    }
    
    public final boolean equals(final Object o) {
        return this.delegate.equals(o);
    }
    
    public final int hashCode() {
        return this.delegate.hashCode();
    }
    
    protected final M getDelegate() {
        return this.delegate;
    }
    
    public String toString() {
        return this.delegate.toString();
    }
    
    private boolean equals(final Object o1, final Object o2) {
        if (o1 == null) {
            return o2 == null;
        }
        return o1.equals(o2);
    }
    
    private class KeySet extends CollectionView<K> implements Set<K>
    {
        Collection<K> getDelegate() {
            return (Collection<K>)AbstractCopyOnWriteMap.this.delegate.keySet();
        }
        
        public void clear() {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                final M map = AbstractCopyOnWriteMap.this.copy();
                ((Map<K, V>)map).keySet().clear();
                AbstractCopyOnWriteMap.this.set(map);
            }
            finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }
        
        public boolean remove(final Object o) {
            return AbstractCopyOnWriteMap.this.remove(o) != null;
        }
        
        public boolean removeAll(final Collection<?> c) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                final M map = AbstractCopyOnWriteMap.this.copy();
                try {
                    return ((Map<K, V>)map).keySet().removeAll(c);
                }
                finally {
                    AbstractCopyOnWriteMap.this.set(map);
                }
            }
            finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }
        
        public boolean retainAll(final Collection<?> c) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                final M map = AbstractCopyOnWriteMap.this.copy();
                try {
                    return ((Map<K, V>)map).keySet().retainAll(c);
                }
                finally {
                    AbstractCopyOnWriteMap.this.set(map);
                }
            }
            finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }
    }
    
    private final class Values extends CollectionView<V>
    {
        Collection<V> getDelegate() {
            return ((Map<K, V>)AbstractCopyOnWriteMap.this.delegate).values();
        }
        
        public void clear() {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                final M map = AbstractCopyOnWriteMap.this.copy();
                ((Map<K, V>)map).values().clear();
                AbstractCopyOnWriteMap.this.set(map);
            }
            finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }
        
        public boolean remove(final Object o) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                if (!this.contains(o)) {
                    return false;
                }
                final M map = AbstractCopyOnWriteMap.this.copy();
                try {
                    return ((Map<K, V>)map).values().remove(o);
                }
                finally {
                    AbstractCopyOnWriteMap.this.set(map);
                }
            }
            finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }
        
        public boolean removeAll(final Collection<?> c) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                final M map = AbstractCopyOnWriteMap.this.copy();
                try {
                    return ((Map<K, V>)map).values().removeAll(c);
                }
                finally {
                    AbstractCopyOnWriteMap.this.set(map);
                }
            }
            finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }
        
        public boolean retainAll(final Collection<?> c) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                final M map = AbstractCopyOnWriteMap.this.copy();
                try {
                    return ((Map<K, V>)map).values().retainAll(c);
                }
                finally {
                    AbstractCopyOnWriteMap.this.set(map);
                }
            }
            finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }
    }
    
    private class EntrySet extends CollectionView<Map.Entry<K, V>> implements Set<Map.Entry<K, V>>
    {
        Collection<Map.Entry<K, V>> getDelegate() {
            return (Collection<Map.Entry<K, V>>)AbstractCopyOnWriteMap.this.delegate.entrySet();
        }
        
        public void clear() {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                final M map = AbstractCopyOnWriteMap.this.copy();
                map.entrySet().clear();
                AbstractCopyOnWriteMap.this.set(map);
            }
            finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }
        
        public boolean remove(final Object o) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                if (!this.contains(o)) {
                    return false;
                }
                final M map = AbstractCopyOnWriteMap.this.copy();
                try {
                    return map.entrySet().remove(o);
                }
                finally {
                    AbstractCopyOnWriteMap.this.set(map);
                }
            }
            finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }
        
        public boolean removeAll(final Collection<?> c) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                final M map = AbstractCopyOnWriteMap.this.copy();
                try {
                    return map.entrySet().removeAll(c);
                }
                finally {
                    AbstractCopyOnWriteMap.this.set(map);
                }
            }
            finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }
        
        public boolean retainAll(final Collection<?> c) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                final M map = AbstractCopyOnWriteMap.this.copy();
                try {
                    return map.entrySet().retainAll(c);
                }
                finally {
                    AbstractCopyOnWriteMap.this.set(map);
                }
            }
            finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }
    }
    
    private static class UnmodifiableIterator<T> implements Iterator<T>
    {
        private final Iterator<T> delegate;
        
        public UnmodifiableIterator(final Iterator<T> delegate) {
            super();
            this.delegate = delegate;
        }
        
        public boolean hasNext() {
            return this.delegate.hasNext();
        }
        
        public T next() {
            return this.delegate.next();
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    protected abstract static class CollectionView<E> implements Collection<E>
    {
        abstract Collection<E> getDelegate();
        
        public final boolean contains(final Object o) {
            return this.getDelegate().contains(o);
        }
        
        public final boolean containsAll(final Collection<?> c) {
            return this.getDelegate().containsAll(c);
        }
        
        public final Iterator<E> iterator() {
            return new UnmodifiableIterator<E>(this.getDelegate().iterator());
        }
        
        public final boolean isEmpty() {
            return this.getDelegate().isEmpty();
        }
        
        public final int size() {
            return this.getDelegate().size();
        }
        
        public final Object[] toArray() {
            return this.getDelegate().toArray();
        }
        
        public final <T> T[] toArray(final T[] a) {
            return this.getDelegate().toArray(a);
        }
        
        public int hashCode() {
            return this.getDelegate().hashCode();
        }
        
        public boolean equals(final Object obj) {
            return this.getDelegate().equals(obj);
        }
        
        public String toString() {
            return this.getDelegate().toString();
        }
        
        public final boolean add(final E o) {
            throw new UnsupportedOperationException();
        }
        
        public final boolean addAll(final Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public abstract static class View<K, V>
    {
        abstract Set<K> keySet();
        
        abstract Set<Map.Entry<K, V>> entrySet();
        
        abstract Collection<V> values();
        
        public enum Type
        {
            STABLE {
                 <K, V, M extends Map<K, V>> View<K, V> get(final AbstractCopyOnWriteMap<K, V, M> host) {
                    return (View<K, V>)host.new Immutable();
                }
            }, 
            LIVE {
                 <K, V, M extends Map<K, V>> View<K, V> get(final AbstractCopyOnWriteMap<K, V, M> host) {
                    return (View<K, V>)host.new Mutable();
                }
            };
            
            abstract <K, V, M extends Map<K, V>> View<K, V> get(final AbstractCopyOnWriteMap<K, V, M> p0);
        }
    }
    
    final class Immutable extends View<K, V> implements Serializable
    {
        private static final long serialVersionUID = -4158727180429303818L;
        
        public Set<K> keySet() {
            return Collections.unmodifiableSet(((Map<? extends K, V>)AbstractCopyOnWriteMap.this.delegate).keySet());
        }
        
        public Set<Map.Entry<K, V>> entrySet() {
            return Collections.unmodifiableSet((Set<? extends Map.Entry<K, V>>)AbstractCopyOnWriteMap.this.delegate.entrySet());
        }
        
        public Collection<V> values() {
            return Collections.unmodifiableCollection(((Map<K, ? extends V>)AbstractCopyOnWriteMap.this.delegate).values());
        }
    }
    
    final class Mutable extends View<K, V> implements Serializable
    {
        private static final long serialVersionUID = 1624520291194797634L;
        private final transient KeySet keySet;
        private final transient EntrySet entrySet;
        private final transient Values values;
        
        Mutable() {
            super();
            this.keySet = new KeySet();
            this.entrySet = new EntrySet();
            this.values = new Values();
        }
        
        public Set<K> keySet() {
            return this.keySet;
        }
        
        public Set<Map.Entry<K, V>> entrySet() {
            return this.entrySet;
        }
        
        public Collection<V> values() {
            return this.values;
        }
    }
}
