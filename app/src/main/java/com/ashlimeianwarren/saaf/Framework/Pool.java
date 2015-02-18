package com.ashlimeianwarren.saaf.Framework;

import java.util.ArrayList;
import java.util.List;

/**
 * Pools can reduce the amount of memory usage as well as
 * lightening the load on the system garbage collector.
 *
 * This is done by having a standard set group of objects
 * and manipulating them which is faster than creating a
 * new one.
 *
 * @param <T> The object to be pooled.
 */
public class Pool<T>
{
    /**
     * Set the object to be pooled.
     *
     * @param <T> The object to be pooled.
     */
    public interface PoolObjectFactory<T>
    {
        public T createObject();
    }

    private final List<T> freeObjects;
    private final PoolObjectFactory<T> factory;
    private final int maxSize;

    /**
     * Constructor for the pool class.
     *
     * @param factory Pool object factory.
     * @param maxSize The maximum number of objects in the pool.
     */
    public Pool(PoolObjectFactory<T> factory, int maxSize)
    {
        this.factory = factory;
        this.maxSize = maxSize;
        this.freeObjects = new ArrayList<T>(maxSize);
    }

    /**
     * Only create a new object if the free object pool is empty.
     *
     * @return The object to be used.
     */
    public T newObject()
    {
        T object = null;

        if (freeObjects.size() == 0)
        {
            object = factory.createObject();
        }
        else
        {
            object = freeObjects.remove(freeObjects.size() - 1);
        }

        return object;
    }

    /**
     * Return an object to the free pool if there is space.
     *
     * @param object The object to be returned to the pool.
     */
    public void free(T object)
    {
        if (freeObjects.size() < maxSize)
        {
            freeObjects.add(object);
        }
    }
}
