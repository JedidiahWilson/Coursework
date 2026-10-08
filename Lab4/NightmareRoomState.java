import java.util.ArrayList;
// mostly exists as a game over state, so kind of empty other  than game over text
public class NightmareRoomState implements State {
    @Override
    public void enter(Model model, View view) {    
         view.endGame(); 
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
        return Room.NIGHTMARE_ROOM;
    }
}