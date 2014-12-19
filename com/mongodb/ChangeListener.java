package com.mongodb;

interface ChangeListener<T>
{
    void stateChanged(ChangeEvent<T> p0);
}
