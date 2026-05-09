/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import datastructures.CustomMinHeap;
import datastructures.CustomMinHeap.Entry;
import models.ParkingSlot;

/**
 * This class is used to implement CustomMinHeap and give users the best parking
 * slot (lowest distanceFromGate)
 *
 * @author Yim Zi Hao
 */
public class SlotManager {

    private CustomMinHeap heap;
    private ParkingSlot[] allSlots;

    public SlotManager() {
        heap = new CustomMinHeap();
    }

    /**
     * Load parking slots at startup.
     *
     * @param parkingSlot
     */
    public void loadSlot(ParkingSlot[] parkingSlot) {
        this.allSlots = parkingSlot;
        for (ParkingSlot slot : parkingSlot) {
            heap.offer(slot.getSlotID(), slot.getDistanceFromGate());
        }
    }

    /**
     * Assign the nearest available slot to an incoming vehicle.
     *
     * @return best parking slot (lowest distanceFromGate)
     */
    public ParkingSlot assignBestSlot() {
        Entry best = heap.poll();
        if (best == null) {
            System.out.println("No available parking slots.");
            return null;
        }
        ParkingSlot min = findSlotByID(best.key);
        min.setOccupied(true);
        System.out.println("Assigned slot: " + min.getSlotID()
                + " (distance: " + min.getDistanceFromGate() + "m)");
        return min;
    }

    /**
     * Release a slot when a vehicle exits then put it back in the heap.
     *
     * @param slot
     */
    public void releaseSlot(ParkingSlot slot) {
        if (slot == null) return;
        slot.setOccupied(false);
        heap.offer(slot.getSlotID(), slot.getDistanceFromGate());
        System.out.println("Slot " + slot.getSlotID() + " is now available again.");
    }

    public void reclaimSlot(ParkingSlot slot) {
        if (slot == null) {
            return;
        }
        Entry target = new Entry(slot.getSlotID(), slot.getDistanceFromGate());
        boolean removed = heap.remove(target);
        if (removed) {
            slot.setOccupied(true);
            System.out.println("Slot " + slot.getSlotID() + " has been reclaimed (now OCCUPIED).");
        } else {
            // Slot wasn't in the heap — somebody else already took it. Mark it
            // occupied anyway so internal state stays consistent.
            slot.setOccupied(true);
            System.out.println("Slot " + slot.getSlotID() + " was not in the available heap; marked OCCUPIED.");
        }
    }

    public boolean hasAvailableSlots() {
        return !heap.isEmpty();
    }

    public int availableSlotCount() {
        return heap.size();
    }
    
    private ParkingSlot findSlotByID(String slotID) {
        for (ParkingSlot s : allSlots) {
            if (s.getSlotID().equals(slotID)) {
                return s;
            }
        }
        return null;
    }
}
