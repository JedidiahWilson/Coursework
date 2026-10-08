import java.util.ArrayList;

public class BedroomState implements State {
    @Override
    public void enter(Model model, View view) {
        view.Print("You creep into the bedroom. Dark blood drips slowly from the ceiling.");
        if (model.closetLocked()) {
            view.Print("The door to the closet is shut, held tight by a steel lock.");
        } else {
            view.Print("The closet door hangs open.");
        }
    }

    @Override
    public ArrayList<String> getActions(Model model) {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("Enter Living Room");
        actions.add("Enter Bathroom");

        if (model.hasClosetKey() && model.closetLocked()) {
            actions.add("Unlock Closet");
        }
        if (!model.hasChestKey() && !model.closetLocked()) {
            actions.add("Pick up Silver Key");
        }
        return actions;
    }

    @Override
    public State handleAction(Model model, View view, String action) {
        switch (action) {
            case "Enter Living Room":
                model.decrementTime();
                return model.getStateFromRoom(Room.LIVING_ROOM);
            case "Enter Bathroom":
                model.decrementTime();
                return model.getStateFromRoom(Room.BATHROOM);
            case "Unlock Closet":
                model.unlockCloset();
                view.Print("Click. The closet door unlocks with a rusty groan. A silver key lies inside.");
                return this;
            case "Pick up Silver Key":
                model.grabChestKey();
                view.Print("You take the silver key. Even in the light, it doesn't shine.");
                return this;
            default:
                view.Print("You can't do that.");
                return this;
        }
    }

    @Override
    public Room getRoom() {
        return Room.BEDROOM;
    }
}