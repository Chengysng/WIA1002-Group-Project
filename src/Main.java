import java.util.Scanner;
import models.Vehicle;
import managers.*;

/**
 * Combine everyone's code
 * Not completely done
 * @author XU ZIMAO
 */
public class Main {
    public static void main(String[] args) {
        // Initialize managers from all members
        RecordManager recordManager = new RecordManager();         // Member 2
        SlotManager slotManager = new SlotManager();               // Member 4
        SearchManager searchManager = new SearchManager();         // Member 5
        
        // Member 3 now connects Member 2 and 4 for the Undo system
        GateManager gateManager = new GateManager(slotManager, recordManager);
        
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Smart Parking Management System (Sprint 1) ===");

        while (running) {
            // Display Menu
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Process Entry");
            System.out.println("2. Find Vehicle");
            System.out.println("3. Undo Last Action");
            System.out.println("4. Show All Records");
            System.out.println("5. Exit");
            System.out.print("Select an option [1-5]: ");

            try {
                // Prevent crashes from non-integer input using try/catch
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        System.out.print("Enter License Plate: ");
                        String plate = scanner.nextLine();
                        System.out.print("Enter Owner Name: ");
                        String name = scanner.nextLine();
                        
                        // 1. Create the Vehicle object
                        Vehicle newVehicle = new Vehicle(plate, name, System.currentTimeMillis());
                        
                        // 2. Add to waiting queue (Member 3 logic)
                        gateManager.addVehicleToQueue(newVehicle);
                        
                        // 3. Process the entry (Member 3 triggers Member 2 & 4)
                        Vehicle processed = gateManager.processNextArrival();
                        
                        // 4. Update the Search System (Member 5 logic)
                        if (processed != null) {
                            searchManager.addVehicleRecord(processed);
                            System.out.println("Vehicle " + plate + " has been registered and parked.");
                        }
                        break;
                    case 2:
                        System.out.print("Enter License Plate to Search: ");
                        String searchPlate = scanner.nextLine();
                        Vehicle found = searchManager.findVehicle(searchPlate);
                        if (found != null) {
                            System.out.println("Vehicle Found:" + found.getOwnerName() + "(Entry Time:" + found.getEntryTime() + ")");
                        } else {
                            System.out.println("Error: Vehicle not found.");
                        }
                        break;

                    case 3:
                        // This triggers the stack-based reversal logic from Member 3 [cite: 50, 81]
                        gateManager.undoLastAction();
                        
                        // Move the print statement BEFORE the break so it actually displays
                        System.out.println("Undo operation executed."); 
                        break;

                    case 4:
                        System.out.println("Displaying all parking records...");
                        // Call Member 2's RecordManager to show the linked list data
                        recordManager.displayAllRecords(); 
                        
                        break;

                    case 5:
                        running = false;
                        System.out.println("System shutting down. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid option! Please enter a number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                // Handling non-numeric input[cite: 1, 2]
                System.out.println("Input Error: Please enter a valid menu number.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred:" + e.getMessage());
            }
        }
        scanner.close();
    }
}
