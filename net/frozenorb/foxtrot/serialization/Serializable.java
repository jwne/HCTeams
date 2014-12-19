package net.frozenorb.foxtrot.serialization;

public interface Serializable<T>
{
    T serialize();
    
    void deserialize(T p0);
}
