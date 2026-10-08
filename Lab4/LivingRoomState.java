import java.util.ArrayList;

public class LivingRoomState implements State {

    @Override
    public void enter(Model model, View view) {
        if (!model.livingRoomVisited()) {
            // intro, complete with drama
            view.Print("Your mind feels foggy as you slowly come to.");
            view.delay(700);
            view.dramaticPrint("You're in your home...", 600, " but something feels wrong.");
            view.delay(700);
            view.Print("Welcome to your nightmare.");
            view.delay(700);
            view.dramaticPrint("If you want to wake up, find your way out...", 600, " before time runs out.");
            view.delay(700);
            view.dramaticPrint("You're in your living room...", 600, " sort of.");
            view.delay(600);
            view.Print("The front door looms high, secured with a heavy golden lock.");
            view.delay(600);
            view.Print("Doors around you lead to other rooms in the house.");
            view.delay(600);
            view.Print("A golden pocket watch sits on a dusty table.");
            view.delay(800);
            view.Print("Good luck...");
            view.delay(700);
            model.setLivingRoomVisited(true);
        } else {
            // standard text without intro
            view.Print("You return to the living room. The large wooden door and dull golden lock stand resolute as ever.");
        }
    }

    @Override
    public ArrayList<String> getActions(Model model) {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("Enter Bedroom");
        actions.add("Enter Kitchen");
        actions.add("Enter Basement");

        if (!model.hasPocketWatch()) {
            actions.add("Pick up Pocket Watch");
        }
        if (model.hasFinalDoorKey() && model.finalDoorLocked()) {
            actions.add("Unlock the Front Door");
        }
        if (!model.finalDoorLocked()) {
            actions.add("Escape the Nightmare");
        }
        return actions;
    }

    @Override
    public State handleAction(Model model, View view, String action) {
        switch (action) {
            case "Enter Bedroom":
                model.decrementTime();
                return model.getStateFromRoom(Room.BEDROOM);
            case "Enter Kitchen":
                model.decrementTime();
                return model.getStateFromRoom(Room.KITCHEN);
            case "Enter Basement":
                model.decrementTime();
                return model.getStateFromRoom(Room.BASEMENT);
            case "Pick up Pocket Watch":
                model.grabPocketWatch();
                view.Print("You pick up the pocket watch. With a loud \"tick\", the sole hand reads " + model.timeLeft() + ".");
                return this; // Stay in the same state
            case "Unlock the Front Door":
                model.unlockFinalDoor();
                view.Print("The golden key turns. The front door is unlocked! Run!");
                return this;
            case "Escape the Nightmare":
                view.Print("You burst through the front door into the cool night air! You've escaped!");
                view.Print("The nightmare shatters around you and you awaken with a jolt inside your own bedroom");
                model.escape(); // Model handles setting terminal state
                return model.getState();
            default:
                view.Print("You can't do that.");
                return this;
        }
    }

    @Override
    public Room getRoom() {
        return Room.LIVING_ROOM;
    }
}