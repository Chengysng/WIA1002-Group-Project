/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datastructures;

import java.util.Arrays;

/**
 * This class is used to implement a priority queue that stores parking slots
 * based on their distanceFromGate (the lowest distanceFromGate has the highest
 * priority).
 *
 * @author Yim Zi Hao
 *
 */
public class CustomMinHeap {

    /**
     * This static class is used to replace the use of Entry in this class
     * and instead use a Comparable Object with fields key and distance.
     * 
     * @author Yim Zi Hao
     */
    public static class Entry implements Comparable<Entry> {

        public String key;
        public int distance;

        public Entry(String key, int distance) {
            this.key = key;
            this.distance = distance;
        }
        
        @Override
        public int compareTo(Entry o) {
            return Integer.compare(this.distance, o.distance);
        }

    }

    private Entry[] heap;
    private int size;
    public static final int DEFAULT_CAPACITY = 11;

    public CustomMinHeap() {
        heap = new Entry[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Determines whether heap is empty.
     *
     * @return whether or not heap is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Return size of heap.
     *
     * @return size
     */
    public int size() {
        return size;
    }

    /**
     * Adds a parking slot into heap.
     *
     * @param key
     * @param distance
     */
    public void offer(String key, int distance) {
        if (size >= heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
        heap[size] = new Entry(key, distance);
        bubbleUp(size);
        size++;
    }

    /**
     * Remove and return the parking slot with the smallest distanceFromGate.
     *
     * @return root of tree / parking slot with lowest distance from gate
     */
    public Entry poll() {
        if (isEmpty()) {
            return null;
        }
        Entry min = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        sinkDown(0);
        return min;
    }

    /**
     * Peek at the best slot without removing it.
     *
     * @return heap[0] / parking slot with the lowest distanceFromGate
     */
    public Entry peek() {
        if (isEmpty()) {
            return null;
        }
        return heap[0];
    }

    public boolean remove(Entry target) {
        if (target == null || size == 0) {
            return false;
        }
        int idx = -1;
        for (int i = 0; i < size; i++) {
            if (heap[i].key.equals(target.key)) {
                idx = i;
                break;
            }
        }
        if (idx == -1) {
            return false;
        }

        // Replace target with the last element
        heap[idx] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        // Re-heapify: the replacement could be smaller than its parent (bubble up)
        // OR larger than its children (sink down). Try both — only one will move.
        if (idx < size) {
            int parent = (idx - 1) / 2;
            if (idx > 0 && heap[idx].distance < heap[parent].distance) {
                bubbleUp(idx);
            } else {
                sinkDown(idx);
            }
        }
        return true;
    }

    private void bubbleUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap[i].distance < heap[parent].distance) {
                swap(i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }

    /**
     * Check if child is smaller than current node then swap or not.
     *
     * @param i
     */
    private void sinkDown(int i) {
        while (true) {
            int smallest = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < size && heap[left].distance < heap[smallest].distance) {
                smallest = left;
            }
            if (right < size && heap[right].distance < heap[smallest].distance) {
                smallest = right;
            }
            if (smallest == i) {
                break;
            } else {
                swap(i, smallest);
                i = smallest;
            }
        }
    }

    /**
     * Swap two elements of heap
     *
     * @param i
     * @param j
     */
    private void swap(int i, int j) {
        Entry temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}
