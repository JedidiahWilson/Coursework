import java.util.Scanner;

public class Model {
    
    private State currentState; 
    
    
    private boolean hasPocketWatch = false;
    private boolean hasStorageRoomKey = false;
    private boolean hasCrowbar = false;
    private boolean hasClosetKey = false;
    private boolean hasChestKey = false;
    private boolean hasFinalDoorKey = false;
    private boolean storageRoomLocked = true;
    private boolean cabinetStuck = true;
    private boolean closetLocked = true;
    private boolean chestLocked = true;
    private boolean finalDoorLocked = true;
    private int timeLeft = 40;

    // to manage printing the intro text only once
    private boolean livingRoomVisited = false;

    public Model() {
        // start in the living room
        this.currentState = new LivingRoomState();
    }

    public State getState() {
        return currentState;
    }

    public void setState(State newState) {
        this.currentState = newState;
    }

    
    public State getStateFromRoom(Room room) {
        switch (room) {
            case LIVING_ROOM:
                return new LivingRoomState();
            case STORAGE_ROOM:
                return new StorageRoomState();
            case BEDROOM:
                return new BedroomState();
            case KITCHEN:
                return new KitchenState();
            case BATHROOM:
                return new BathroomState();
            case BASEMENT:
                return new BasementState();
            case OUTSIDE:
                return new OutsideState();
            case NIGHTMARE_ROOM:
                return new NightmareRoomState();
            default:
                return new LivingRoomState(); // Failsafe
        }
    }

    public String saveModel() {
        String status = "";
        
        status += currentState.getRoom().ordinal();
        status += " " + hasPocketWatch;
        status += " " + hasStorageRoomKey;
        status += " " + hasCrowbar;
        status += " " + hasClosetKey;
        status += " " + hasChestKey;
        status += " " + hasFinalDoorKey;
        status += " " + storageRoomLocked;
        status += " " + cabinetStuck;
        status += " " + closetLocked;
        status += " " + chestLocked;
        status += " " + finalDoorLocked;
        status += " " + timeLeft;
        
        status += " " + livingRoomVisited;
        return status;
    }

    public void loadModel(String savedStatus) {
        Scanner scanner = new Scanner(savedStatus);

        
        int roomOrdinal = scanner.nextInt();
        this.currentState = getStateFromRoom(Room.values()[roomOrdinal]);

        hasPocketWatch = scanner.nextBoolean();
        hasStorageRoomKey = scanner.nextBoolean();
        hasCrowbar = scanner.nextBoolean();
        hasClosetKey = scanner.nextBoolean();
        hasChestKey = scanner.nextBoolean();
        hasFinalDoorKey = scanner.nextBoolean();
        storageRoomLocked = scanner.nextBoolean();
        cabinetStuck = scanner.nextBoolean();
        closetLocked = scanner.nextBoolean();
        chestLocked = scanner.nextBoolean();
        finalDoorLocked = scanner.nextBoolean();
        timeLeft = scanner.nextInt();
        
        
        if (scanner.hasNextBoolean()) {
            livingRoomVisited = scanner.nextBoolean();
        } else {
            livingRoomVisited = true; 
        }
    }

    

    public boolean hasPocketWatch() { return hasPocketWatch; }
    public void grabPocketWatch() {
        if (!hasPocketWatch) {
            hasPocketWatch = true;
            timeLeft--;
        }
    }

    public boolean hasStorageRoomKey() { return hasStorageRoomKey; }
    public void lookInMirror() {
        if (!hasStorageRoomKey) {
            hasStorageRoomKey = true;
            timeLeft--;
        }
    }

    public boolean hasCrowbar() { return hasCrowbar; }
    public void grabCrowbar() {
        if (!hasCrowbar) {
            hasCrowbar = true;
            timeLeft--;
        }
    }

    public boolean hasClosetKey() { return hasClosetKey; }
    public void grabClosetKey() {
        if (!hasClosetKey) {
            hasClosetKey = true;
            timeLeft--;
        }
    }

    public boolean hasChestKey() { return hasChestKey; }
    public void grabChestKey() {
        if (!hasChestKey) {
            hasChestKey = true;
            timeLeft--;
        }
    }

    public boolean hasFinalDoorKey() { return hasFinalDoorKey; }
    public void grabFinalDoorKey() {
        if (!hasFinalDoorKey) {
            hasFinalDoorKey = true;
            timeLeft--;
        }
    }

    public boolean storageRoomLocked() { return storageRoomLocked; }
    public void unlockStorageRoom() {
        if (storageRoomLocked) {
            storageRoomLocked = false;
            timeLeft--;
        }
    }

    public boolean cabinetStuck() { return cabinetStuck; }
    public void openCabinet() {
        if (cabinetStuck) {
            cabinetStuck = false;
            timeLeft--;
        }
    }

    public boolean closetLocked() { return closetLocked; }
    public void unlockCloset() {
        if (closetLocked) {
            closetLocked = false;
            timeLeft--;
        }
    }

    public boolean chestLocked() { return chestLocked; }
    public void unlockChest() {
        if (chestLocked) {
            chestLocked = false;
            timeLeft--;
        }
    }

    public boolean finalDoorLocked() { return finalDoorLocked; }
    public void unlockFinalDoor() {
        if (finalDoorLocked) {
            finalDoorLocked = false;
            timeLeft--;
        }
    }

    public int timeLeft() { return timeLeft; }
    public void decrementTime() { timeLeft--; }

    public boolean livingRoomVisited() { return livingRoomVisited; }
    public void setLivingRoomVisited(boolean visited) { this.livingRoomVisited = visited; }

    

    public void enterNightmareRoom() {
        this.currentState = new NightmareRoomState();
    }
    
    public void escape() {
        this.currentState = new OutsideState();
    }
}