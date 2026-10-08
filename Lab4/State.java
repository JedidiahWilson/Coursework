import java.util.ArrayList;

// state interface inherited by the room state classes
public interface State {
    
    void enter(Model model, View view);

    
    ArrayList<String> getActions(Model model);

    
    State handleAction(Model model, View view, String action);

    
    Room getRoom();
}