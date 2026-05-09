/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datastructures;

import datastructures.CustomMinHeap.Entry;
import models.ParkingSlot;

/**
 * This class is used as a subclass/adapter so SlotManager continues to compile
 * unchanged.
 *
 * @author Yim Zi Hao
 */
public class SlotMinHeap {

    private CustomMinHeap heap = new CustomMinHeap();
    private ParkingSlot[] slotLookup;

    public void offer(ParkingSlot slot) {
        heap.offer(slot.getSlotID(), slot.getDistanceFromGate());
    }

    public ParkingSlot poll(ParkingSlot[] allSlots) {
        Entry min = heap.poll();
        for (ParkingSlot s : allSlots) {
            if (s.getSlotID().equals(min.key)) {
                return s;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }
}
