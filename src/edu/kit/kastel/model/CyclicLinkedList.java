package edu.kit.kastel.model;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Generic model of  cyclic linked list.
 *
 * @param <T> type of the list
 * @author uiiux
 */
public class CyclicLinkedList<T> {
    private static final String LIST_EMPTY_EXCEPTION = "List is empty";
    private static final String DATA_NOT_FOUND_IN_LIST = "Data not found in List.";
    private static final String INVALID_POSITION_EXCEPTION = "Invalid position: ";
    private Node<T> head;
    private int size;

    private static final class Node<T> {
        private T data;
        private Node<T> next;

        private Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    /**
     * Represents a Cyclic Linked list.
     */
    public CyclicLinkedList() {
        head = null;
        size = 0;
    }

    /**
     * Adds an object to the list.
     *
     * @param data object to add
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> temp = head;
            while (temp.next != head) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
        newNode.next = head;
        size++;
    }

    /**
     * Gets the position of the object from the list.
     *
     * @param  data object whose position will be fetched in the list
     * @return position of the object
     */
    public int getPosition(T data) {
        Node<T> temp = head;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(temp.data, data)) {
                return i;
            }
            temp = temp.next;
        }
        return -1;
    }


    /**
     * Gets an object from the list.
     * Normalized position is there to handle negative values
     * @param position position of the object
     * @return object
     * @throws NoSuchElementException if the list is empty
     */
    public T get(int position) {
        if (head == null) {
            throw new NoSuchElementException(LIST_EMPTY_EXCEPTION);
        }
        int normalizedPosition = (position % size + size) % size;
        Node<T> temp = head;
        for (int i = 0; i < normalizedPosition; i++) {
            temp = temp.next;
        }
        return temp.data;
    }

    /**
     * Gets the data of the node that follows the first node containing the specified data.
     *
     * @param data The data to search for in the list.
     * @return The data of the next node after the node containing the specified data.
     * @throws NoSuchElementException if the specified data is not found in the list.
     */
    public T getNext(T data) {
        if (head == null) {
            throw new NoSuchElementException(LIST_EMPTY_EXCEPTION);
        }

        Node<T> current = head;
        do {
            if (Objects.equals(current.data, data)) {
                return current.next.data;
            }
            current = current.next;
        } while (current != head);

        throw new NoSuchElementException(DATA_NOT_FOUND_IN_LIST);
    }

    /**
     * Replaces an object in the list with new object.
     *
     * @param position position of the object
     * @param data     object
     * @throws IndexOutOfBoundsException throws an exception if the position is invalid
     */
    public void replace(int position, T data) {
        if (position >= size || position < 0) {
            throw new IndexOutOfBoundsException(INVALID_POSITION_EXCEPTION + position);
        }
        Node<T> temp = head;
        for (int i = 0; i < position; i++) {
            temp = temp.next;
        }
        temp.data = data;
    }

    /**
     * Gets the size of the list.
     *
     * @return size of the list
     */
    public int size() {
        return size;
    }

    /**
     * Clears the list, removing all elements.
     */
    public void clear() {
        head = null;
        size = 0;
    }


}
