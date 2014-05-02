package com.thu.digi256.Utils;

import java.util.EmptyStackException;
import java.util.Vector;

/**
 * Created by SemonCat on 2014/4/8.
 */
public class CircularStack<E> extends Vector<E> {

    private int size;

    /**
     * Constructs a stack with the default size of {@code Vector}.
     */
    public CircularStack() {
        super();
    }

    public CircularStack(int size) {
        super();
        this.size = size;
    }


    /**
     * Returns whether the stack is empty or not.
     *
     * @return {@code true} if the stack is empty, {@code false} otherwise.
     */
    public boolean empty() {
        return isEmpty();
    }

    /**
     * Returns the element at the top of the stack without removing it.
     *
     * @return the element at the top of the stack.
     * @throws java.util.EmptyStackException if the stack is empty.
     * @see #pop
     */
    @SuppressWarnings("unchecked")
    public synchronized E peek() {
        try {
            return (E) elementData[elementCount - 1];
        } catch (IndexOutOfBoundsException e) {
            throw new EmptyStackException();
        }
    }

    /**
     * Returns the element at the top of the stack and removes it.
     *
     * @return the element at the top of the stack.
     * @throws java.util.EmptyStackException if the stack is empty.
     * @see #peek
     * @see #push
     */
    @SuppressWarnings("unchecked")
    public synchronized E pop() {
        if (elementCount == 0) {
            throw new EmptyStackException();
        }

        final int index = --elementCount;
        final E obj = (E) elementData[index];
        elementData[index] = null;
        modCount++;
        return obj;
    }

    /**
     * Pushes the specified object onto the top of the stack.
     *
     * @param object The object to be added on top of the stack.
     * @return the object argument.
     * @see #peek
     * @see #pop
     */
    public E push(E object) {
        while (elementCount >= size && size > 0) {
            this.remove(0);
        }

        addElement(object);
        return object;
    }

    /**
     * Returns the index of the first occurrence of the object, starting from
     * the top of the stack.
     *
     * @param o the object to be searched.
     * @return the index of the first occurrence of the object, assuming that
     * the topmost object on the stack has a distance of one.
     */
    public synchronized int search(Object o) {
        final Object[] dumpArray = elementData;
        final int size = elementCount;
        if (o != null) {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(dumpArray[i])) {
                    return size - i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (dumpArray[i] == null) {
                    return size - i;
                }
            }
        }
        return -1;
    }


}
