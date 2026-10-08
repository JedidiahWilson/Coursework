import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;



public class StateTest {

    private Model model;
    private View view; 

    @Before
    public void setUp() {
        model = new Model();
        view = new View(); // needs to exist, but output wont matter for these tests
    }

    

    @Test
    public void livingRoomState_getActions_initial_correctActions() {
        
        LivingRoomState state = new LivingRoomState();
        
        
        ArrayList<String> actions = state.getActions(model);

        
        assertTrue(actions.contains("Enter Bedroom"));
        assertTrue(actions.contains("Enter Kitchen"));
        assertTrue(actions.contains("Enter Basement"));
        assertTrue(actions.contains("Pick up Pocket Watch"));
        assertFalse(actions.contains("Unlock the Front Door"));
        assertFalse(actions.contains("Escape the Nightmare"));
    }

    @Test
    public void livingRoomState_getActions_withFinalKey_showsUnlock() {
        
        LivingRoomState state = new LivingRoomState();
        model.grabFinalDoorKey(); // Get the key

        
        ArrayList<String> actions = state.getActions(model);

        
        assertTrue(actions.contains("Unlock the Front Door"));
    }

    @Test
    public void livingRoomState_getActions_doorUnlocked_showsEscape() {
        
        LivingRoomState state = new LivingRoomState();
        model.grabFinalDoorKey();
        model.unlockFinalDoor(); // Unlock the door

        
        ArrayList<String> actions = state.getActions(model);

        
        assertFalse(actions.contains("Unlock the Front Door"));
        assertTrue(actions.contains("Escape the Nightmare"));
    }

    @Test
    public void livingRoomState_handleAction_enterKitchen_returnsKitchenState() {
        
        LivingRoomState state = new LivingRoomState();
        int initialTime = model.timeLeft();

        
        State nextState = state.handleAction(model, view, "Enter Kitchen");

        
        assertTrue(nextState instanceof KitchenState);
        assertEquals(initialTime - 1, model.timeLeft());
    }

    @Test
    public void livingRoomState_handleAction_pickUpWatch_modelUpdatedAndStateStays() {
        
        LivingRoomState state = new LivingRoomState();
        
        
        State nextState = state.handleAction(model, view, "Pick up Pocket Watch");

        
        assertTrue(model.hasPocketWatch());
        assertTrue(nextState instanceof LivingRoomState);
    }

    // --- KitchenState Tests ---

    @Test
    public void kitchenState_getActions_initial_correctActions() {
        
        KitchenState state = new KitchenState();

        
        ArrayList<String> actions = state.getActions(model);

        
        assertTrue(actions.contains("Enter Living Room"));
        assertFalse(actions.contains("Unlock Storage Room"));
        assertFalse(actions.contains("Enter Storage Room"));
        assertFalse(actions.contains("Pry Open Cabinet with Crowbar"));
        assertFalse(actions.contains("Pick up Steel Key"));
    }

    @Test
    public void kitchenState_getActions_withKeyAndCrowbarAndOpenCabinet_showsAll() {
        
        KitchenState state = new KitchenState();
        model.lookInMirror();    // Get storage key
        model.grabCrowbar();     // Get crowbar
        model.openCabinet();     // Open cabinet

        
        ArrayList<String> actions = state.getActions(model);
        
        
        assertTrue(actions.contains("Unlock Storage Room"));
        assertFalse(actions.contains("Enter Storage Room"));
        assertFalse(actions.contains("Pry Open Cabinet with Crowbar"));
        assertTrue(actions.contains("Pick up Steel Key"));
    }

    @Test
    public void kitchenState_handleAction_pryCabinet_modelUpdated() {
        
        KitchenState state = new KitchenState();
        
        
        State nextState = state.handleAction(model, view, "Pry Open Cabinet with Crowbar");

        
        assertFalse(model.cabinetStuck());
        assertTrue(nextState instanceof KitchenState); // Stays in same state
    }

    // --- BathroomState Tests ---
    
    @Test
    public void bathroomState_getActions_initial_showsLookInMirror() {
        
        BathroomState state = new BathroomState();
        
        
        ArrayList<String> actions = state.getActions(model);

        
        assertTrue(actions.contains("Enter Bedroom"));
        assertTrue(actions.contains("Look in Mirror"));
    }

    @Test
    public void bathroomState_getActions_afterLook_hidesLookInMirror() {
        
        BathroomState state = new BathroomState();
        model.lookInMirror(); // Already looked

        
        ArrayList<String> actions = state.getActions(model);

        
        assertFalse(actions.contains("Look in Mirror"));
    }

    @Test
    public void bathroomState_handleAction_lookInMirror_modelUpdated() {
        
        BathroomState state = new BathroomState();
        
        
        state.handleAction(model, view, "Look in Mirror");

        
        assertTrue(model.hasStorageRoomKey());
    }

   

    @Test
    public void fullGamePath_winCondition_completesWithTimeLeft() {
        // Full-scale, start to finish game test
        // assumes perfect runthrough

        
        State currentState = model.getState(); // Starts in LivingRoom
        int expectedTimeLeft;

        
        currentState = currentState.handleAction(model, view, "Pick up Pocket Watch");      // time: 39
        currentState = currentState.handleAction(model, view, "Enter Bedroom");         // time: 38
        currentState = currentState.handleAction(model, view, "Enter Bathroom");        // time: 37
        currentState = currentState.handleAction(model, view, "Look in Mirror");        // time: 36 (Get Storage Key)
        currentState = currentState.handleAction(model, view, "Enter Bedroom");         // time: 35
        currentState = currentState.handleAction(model, view, "Enter Living Room");     // time: 34
        currentState = currentState.handleAction(model, view, "Enter Kitchen");         // time: 33
        currentState = currentState.handleAction(model, view, "Unlock Storage Room");   // time: 32
        currentState = currentState.handleAction(model, view, "Enter Storage Room");    // time: 31
        currentState = currentState.handleAction(model, view, "Pick up Crowbar");       // time: 30
        currentState = currentState.handleAction(model, view, "Enter Kitchen");         // time: 29
        currentState = currentState.handleAction(model, view, "Pry Open Cabinet with Crowbar"); // time: 28
        currentState = currentState.handleAction(model, view, "Pick up Steel Key");     // time: 27 (Get Closet Key)
        currentState = currentState.handleAction(model, view, "Enter Living Room");     // time: 26
        currentState = currentState.handleAction(model, view, "Enter Bedroom");         // time: 25
        currentState = currentState.handleAction(model, view, "Unlock Closet");         // time: 24
        currentState = currentState.handleAction(model, view, "Pick up Silver Key");    // time: 23 (Get Chest Key)
        currentState = currentState.handleAction(model, view, "Enter Living Room");     // time: 22
        currentState = currentState.handleAction(model, view, "Enter Basement");        // time: 21
        currentState = currentState.handleAction(model, view, "Unlock Chest");          // time: 20
        currentState = currentState.handleAction(model, view, "Pick up Golden Key");    // time: 19 (Get Final Key)
        currentState = currentState.handleAction(model, view, "Enter Living Room");     // time: 18
        currentState = currentState.handleAction(model, view, "Unlock the Front Door"); // time: 17
        
        expectedTimeLeft = 17;
        
        currentState = currentState.handleAction(model, view, "Escape the Nightmare");  // time: 17 (no change)

        
        assertTrue(currentState instanceof OutsideState);
        assertEquals(expectedTimeLeft, model.timeLeft());
        assertEquals(Room.OUTSIDE, model.getState().getRoom());
    }
}