import java.util.ArrayList;

public class BathroomState implements State {
    @Override
    public void enter(Model model, View view) {
        view.Print("You step into the cold bathroom. The tiles are cracked and stained.");
        if (!model.hasStorageRoomKey()) {
            view.Print("Something catches your eye in the cracked mirror...");
        }
    }

    @Override
    public ArrayList<String> getActions(Model model) {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("Enter Bedroom");

        if (!model.hasStorageRoomKey()) {
            actions.add("Look in Mirror");
        }
        return actions;
    }

    @Override
    public State handleAction(Model model, View view, String action) {
        switch (action) {
            case "Enter Bedroom":
                model.decrementTime();
                return model.getStateFromRoom(Room.BEDROOM);
            case "Look in Mirror":
                model.lookInMirror();
                view.Print("You look in the cracked mirror. Your eyes are wider than you remember.");
                view.Print("A brass key hangs from a twine string around your neck. That wasn't there before...");
                return this;
            default:
                view.Print("You can't do that.");
                return this;
        }
    }

    @Override
    public Room getRoom() {
        return Room.BATHROOM;
    }
}