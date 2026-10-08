import java.util.ArrayList;

public class BasementState implements State {
    @Override
    public void enter(Model model, View view) {
        view.Print("You descend into the basement. The darkness here feels... heavy.");
        view.Print("A heavy chest sits in the middle of the dusty floor, its silver lock reflecting no light.");
    }

    @Override
    public ArrayList<String> getActions(Model model) {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("Enter Living Room");

        if (model.hasChestKey() && model.chestLocked()) {
            actions.add("Unlock Chest");
        }
        if (!model.hasFinalDoorKey() && !model.chestLocked()) {
            actions.add("Pick up Golden Key");
        }
        return actions;
    }

    @Override
    public State handleAction(Model model, View view, String action) {
        switch (action) {
            case "Enter Living Room":
                model.decrementTime();
                return model.getStateFromRoom(Room.LIVING_ROOM);
            case "Unlock Chest":
                model.unlockChest();
                view.Print("The heavy chest unlocks with a satisfying clunk.");
                view.Print("Inside is a large, golden key. This must be the key to the front door!");
                return this;
            case "Pick up Golden Key":
                model.grabFinalDoorKey();
                view.Print("You take the golden key. Hurry and escape, before time runs out!");
                return this;
            default:
                view.Print("You can't do that.");
                return this;
        }
    }

    @Override
    public Room getRoom() {
        return Room.BASEMENT;
    }
}