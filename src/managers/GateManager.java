package managers;

import datastructures.CustomQueue;
import datastructures.CustomStack;
import models.Vehicle; 
import models.ParkingSlot;
import java.util.EmptyStackException;

public class GateManager {
    private CustomQueue<Vehicle> waitingQueue;
    private CustomStack<Action> historyStack;
    
    // 引入其他成员的系统
    private SlotManager slotManager;     // Member 4: 负责车位
    private RecordManager recordManager; // Member 2: 负责记录账本

    // 内部类，用于完整记录一次操作的所有上下文
    private static class Action {
        String type; // "ENTER" or "EXIT"
        Vehicle vehicle;
        ParkingSlot slot;

        public Action(String type, Vehicle vehicle, ParkingSlot slot) {
            this.type = type;
            this.vehicle = vehicle;
            this.slot = slot;
        }
    }

    // 构造函数：现在需要同时接收 SlotManager 和 RecordManager
    public GateManager(SlotManager slotManager, RecordManager recordManager) {
        this.waitingQueue = new CustomQueue<>();
        this.historyStack = new CustomStack<>();
        this.slotManager = slotManager;
        this.recordManager = recordManager;
    }

    // 1. 车辆到达，加入等待队列
    public void addVehicleToQueue(Vehicle v) {
        waitingQueue.enqueue(v);
        System.out.println("Vehicle[" + v.getLicensePlate() + "] has entered the waiting queue.");
    }

    // 2. 核心联动：处理驶入、分配车位、并记录在案
    public Vehicle processNextArrival() {
        if (waitingQueue.isEmpty()) {
            System.out.println("No vehicles in the waiting queue.");
            return null;
        }
        
        // 检查是否有空车位 (Member 4)
        if (!slotManager.hasAvailableSlots()) {
            System.out.println("Parking is full! Vehicle must wait.");
            return null;
        }

        Vehicle nextVehicle = waitingQueue.dequeue();
        
        // 分配车位 (Member 4)
        ParkingSlot assignedSlot = slotManager.assignBestSlot();
        
        // 记录到链表账本 (Member 2)
        recordManager.addRecord(nextVehicle);
        
        // 记录到历史栈，准备随时 Undo
        historyStack.push(new Action("ENTER", nextVehicle, assignedSlot));
        System.out.println("Processed arrival for: Vehicle[" + nextVehicle.getLicensePlate() + "]");
        
        return nextVehicle;
    }

    // 3. 模拟车辆离开：释放车位并删除记录
    public void processExit(Vehicle v, ParkingSlot slotToRelease) {
        // 释放车位 (Member 4)
        slotManager.releaseSlot(slotToRelease);
        
        // 从链表账本中删除 (Member 2)
        recordManager.removeRecord(v.getLicensePlate());
        
        // 记录离开历史
        historyStack.push(new Action("EXIT", v, slotToRelease));
        System.out.println("Processed exit for: Vehicle[" + v.getLicensePlate() + "]");
    }

    // 4. 终极功能：撤销上一次操作（完美联动）
    public void undoLastAction() {
        try {
            Action lastAction = historyStack.pop();
            
            if (lastAction.type.equals("ENTER")) {
                // 【撤销进入】：说明这辆车不该进
                System.out.println("Undoing ENTER action for: Vehicle[" + lastAction.vehicle.getLicensePlate() + "]");
                
                // 1. 把车位还回去 (Member 4)
                if (lastAction.slot != null) {
                    slotManager.releaseSlot(lastAction.slot);
                    System.out.println("Undo: Reverted slot assignment.");
                }
                
                // 2. 把记录从账本里抹除 (Member 2)
                recordManager.removeRecord(lastAction.vehicle.getLicensePlate());
                System.out.println("Undo: Removed record from linked list.");
                
            } else if (lastAction.type.equals("EXIT")) {
                // 【撤销离开】：说明这辆车不该走
                System.out.println("Undoing EXIT action for: Vehicle[" + lastAction.vehicle.getLicensePlate() + "]");
                
                // 1. 重新在账本里加上这辆车 (Member 2)
                recordManager.addRecord(lastAction.vehicle);
                System.out.println("Undo: Re-added record to linked list.");
                
              
            }
        } catch (EmptyStackException e) {
            System.out.println("No actions to undo! Stack is empty.");
        }
    }
}
   