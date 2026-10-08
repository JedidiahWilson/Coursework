import java.util.ArrayList;

public class StorageRoomState implements State {
    @Override
    public void enter(Model model, View view) {
        view.Print("You squeeze into the cramped storage room. Cobwebs brush against your face.");
        if (!model.hasCrowbar()) {
            view.Print("A crowbar leans againt the wall. Perhaps it might open that stubborn cabinet?");
        }
    }

    @Override
    public ArrayList<String> getActions(Model model) {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("Enter Kitchen");

        if (!model.hasCrowbar()) {
            actions.add("Pick up Crowbar");
        }
        return actions;
    }

    @Override
    public State handleAction(Model model, View view, String action) {
        switch (action) {
            case "Enter Kitchen":
                model.decrementTime();
                return model.getStateFromRoom(Room.KITCHEN);
            case "Pick up Crowbar":
                model.grabCrowbar();
                view.Print("You wrap your hand around the heavy, cold steel of the crowbar.");
                return this;
            default:
                view.Print("You can't do that.");
                return this;
        }
    }

    @Override
    public Room getRoom() {
        return Room.STORAGE_ROOM;
    }
}