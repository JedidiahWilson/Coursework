
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class ModelTest {

    private Model model;

    @Before
    public void setUp() {
        model = new Model();
    }

    @Test
    public void constructor_initialState_defaultsCorrect() {
        assertEquals(40, model.timeLeft());
        assertTrue(model.storageRoomLocked());
        assertTrue(model.cabinetStuck());
        assertTrue(model.closetLocked());
        assertTrue(model.chestLocked());
        assertTrue(model.finalDoorLocked());

        assertFalse(model.hasPocketWatch());
        assertFalse(model.hasStorageRoomKey());
        assertFalse(model.hasCrowbar());
        assertFalse(model.hasClosetKey());
        assertFalse(model.hasChestKey());
        assertFalse(model.hasFinalDoorKey());
        
        assertFalse(model.livingRoomVisited());
        assertTrue(model.getState() instanceof LivingRoomState);
    }

    @Test
    public void itemLogic_grabItem_itemGainedAndTimeDecremented() {
        
        int initialTime = model.timeLeft();
        assertFalse(model.hasCrowbar());

        
        model.grabCrowbar();

        
        assertTrue(model.hasCrowbar());
        assertEquals(initialTime - 1, model.timeLeft());
    }

    @Test
    public void itemLogic_grabItemTwice_itemGainedOnlyOnceAndTimeDecrementedOnce() {
        
        model.grabCrowbar();
        int timeAfterFirstGrab = model.timeLeft();

        
        model.grabCrowbar(); // Try to grab it again

        
        assertTrue(model.hasCrowbar());
        assertEquals(timeAfterFirstGrab, model.timeLeft());
    }

    @Test
    public void lockLogic_unlock_lockOpenedAndTimeDecremented() {
        
        int initialTime = model.timeLeft();
        assertTrue(model.storageRoomLocked());

        
        model.unlockStorageRoom();

        
        assertFalse(model.storageRoomLocked());
        assertEquals(initialTime - 1, model.timeLeft());
    }

    @Test
    public void lockLogic_unlockTwice_lockOpenedOnlyOnceAndTimeDecrementedOnce() {
        
        model.unlockStorageRoom();
        int timeAfterFirstUnlock = model.timeLeft();

        
        model.unlockStorageRoom(); // Try to unlock it again

        
        assertFalse(model.storageRoomLocked());
        assertEquals(timeAfterFirstUnlock, model.timeLeft());
    }

    @Test
    public void saveAndLoadModel_stateTransfer_newStateMatchesOldState() {
        
        // Modify the original model
        model.grabCrowbar();       // time 39
        model.unlockCloset();      // time 38
        model.decrementTime();     // time 37
        model.setLivingRoomVisited(true);
        model.setState(model.getStateFromRoom(Room.KITCHEN)); // Change state
        
        int expectedTime = 37;
        String savedState = model.saveModel();

        // Create a new, default model
        Model newModel = new Model();

        
        newModel.loadModel(savedState);

        
        assertEquals(expectedTime, newModel.timeLeft());
        assertTrue(newModel.hasCrowbar());
        assertFalse(newModel.closetLocked());
        assertTrue(newModel.livingRoomVisited());
        
        // check a value that wasn't changed
        assertFalse(newModel.hasPocketWatch());
        // Check that the state (room) was loaded correctly
        assertTrue(newModel.getState() instanceof KitchenState);
    }

    @Test
    public void getStateFromRoom_roomEnum_returnsCorrectStateInstance() {
        assertTrue(model.getStateFromRoom(Room.LIVING_ROOM) instanceof LivingRoomState);
        assertTrue(model.getStateFromRoom(Room.KITCHEN) instanceof KitchenState);
        assertTrue(model.getStateFromRoom(Room.BEDROOM) instanceof BedroomState);
        assertTrue(model.getStateFromRoom(Room.BATHROOM) instanceof BathroomState);
        assertTrue(model.getStateFromRoom(Room.STORAGE_ROOM) instanceof StorageRoomState);
        assertTrue(model.getStateFromRoom(Room.BASEMENT) instanceof BasementState);
        assertTrue(model.getStateFromRoom(Room.OUTSIDE) instanceof OutsideState);
        assertTrue(model.getStateFromRoom(Room.NIGHTMARE_ROOM) instanceof NightmareRoomState);
    }

    @Test
    public void enterNightmareRoom_stateChange_setsNightmareState() {
        
        model.enterNightmareRoom();
        
        assertTrue(model.getState() instanceof NightmareRoomState);
    }

    @Test
    public void escape_stateChange_setsOutsideState() {
        model.escape();

        assertTrue(model.getState() instanceof OutsideState);
    }
}