package com.mongodb;

import java.util.*;

abstract class IndexMap
{
    static IndexMap create() {
        return new RangeBased();
    }
    
    static IndexMap create(final int startIndex, final int count) {
        return new RangeBased(startIndex, count);
    }
    
    abstract IndexMap add(final int p0, final int p1);
    
    abstract int map(final int p0);
    
    private static class HashBased extends IndexMap
    {
        private final Map<Integer, Integer> indexMap;
        
        public HashBased(final int startIndex, final int count) {
            super();
            this.indexMap = new HashMap<Integer, Integer>();
            for (int i = startIndex; i <= count; ++i) {
                this.indexMap.put(i - startIndex, i);
            }
        }
        
        public IndexMap add(final int index, final int originalIndex) {
            this.indexMap.put(index, originalIndex);
            return this;
        }
        
        public int map(final int index) {
            final Integer originalIndex = this.indexMap.get(index);
            if (originalIndex == null) {
                throw new MongoInternalException("no mapping found for index " + index);
            }
            return originalIndex;
        }
    }
    
    private static class RangeBased extends IndexMap
    {
        private int startIndex;
        private int count;
        
        public RangeBased() {
            super();
        }
        
        public RangeBased(final int startIndex, final int count) {
            super();
            this.startIndex = startIndex;
            this.count = count;
        }
        
        public IndexMap add(final int index, final int originalIndex) {
            if (this.count == 0) {
                this.startIndex = originalIndex;
                this.count = 1;
                return this;
            }
            if (originalIndex == this.startIndex + this.count) {
                ++this.count;
                return this;
            }
            final IndexMap hashBasedMap = new HashBased(this.startIndex, this.count);
            hashBasedMap.add(index, originalIndex);
            return hashBasedMap;
        }
        
        public int map(final int index) {
            if (index >= this.count) {
                throw new MongoInternalException("index should not be greater than count");
            }
            return this.startIndex + index;
        }
    }
}
