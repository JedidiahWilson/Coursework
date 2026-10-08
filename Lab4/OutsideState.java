import java.util.ArrayList;

public class OutsideState implements State {
    //ending state.
    @Override
    public void enter(Model model, View view) {
        
    }

    @Override
    public ArrayList<String> getActions(Model model) {
        return new ArrayList<>();
    }

    @Override
    public State handleAction(Model model, View view, String action) {
        return this; 
    }

    @Override
    public Room getRoom() {
        return Room.OUTSIDE;
    }
}