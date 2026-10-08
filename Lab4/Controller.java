import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Controller {

    
    private void saveGame(Model model, View view) {
        try (PrintWriter out = new PrintWriter("nightmare_save.txt")) {
            out.println(model.saveModel());
            view.Print("Your nightmare has been... remembered.");
        } catch (FileNotFoundException e) {
            view.Print("Failed to save the nightmare.");
        }
    }

    private void loadGame(Model model, View view) {
        try (Scanner in = new Scanner(new File("nightmare_save.txt"))) {
            if (in.hasNextLine()) {
                model.loadModel(in.nextLine());
                view.Print("You return to a familiar nightmare...");
                // After loading, tell the new state to print its description
                model.getState().enter(model, view);
            } else {
                view.Print("No nightmare to return to.");
            }
        } catch (FileNotFoundException e) {
            view.Print("No nightmare to return to.");
        }
    }

    public static void main(String[] args) {
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller();
        Scanner input = new Scanner(System.in);

        view.Print("Welcome to your nightmare.");
        view.nowPrint("0: ");
        view.Print("New Game");
        view.nowPrint("1: ");
        view.Print("Load Game"); 

        int startChoice = -1;
        while (startChoice < 0 || startChoice > 1) {
            if (!input.hasNextInt()) {
                view.Print("Please enter 0 or 1.");
                input.next();
                continue;
            }
            startChoice = input.nextInt();
            if (startChoice < 0 || startChoice > 1) {
                view.Print("Please enter 0 or 1.");
            }
        }

        if (startChoice == 1) {
            controller.loadGame(model, view);
        } else {
            // New game, so enter the starting state
            model.getState().enter(model, view);
        }

        // Main game loop
        
        while (model.getState().getRoom() != Room.OUTSIDE && model.getState().getRoom() != Room.NIGHTMARE_ROOM) {
            
            State currentState = model.getState();
            
            // Get the list of actions from the current state
            ArrayList<String> commands = currentState.getActions(model);
            commands.add(0, "Save Game"); 

            // Display the menu
            for (int i = 0; i < commands.size(); i++) {
                view.nowPrint(i + ": ");
                view.Print(commands.get(i));
            }

            if (model.hasPocketWatch()) {
                view.Print("The pocket watch ticks down to " + model.timeLeft());
            }

            int choice;
            // Get and validate user input
            if (!input.hasNextInt()) {
                view.Print("Didn't catch that. Please enter a number.");
                input.next();
                continue;
            }
            choice = input.nextInt();

            if (choice < 0 || choice >= commands.size()) {
                view.Print("That's not a valid option.");
                continue;
            }

            String action = commands.get(choice);

            
            if (action.equals("Save Game")) {
                controller.saveGame(model, view);
                continue; // Don't advance the state
            }
            
            
            State nextState = currentState.handleAction(model, view, action);
            model.setState(nextState);

            if (nextState != currentState) {
                nextState.enter(model, view);
            }
            // check for game over
            if (model.timeLeft() <= 0 && model.getState().getRoom() != Room.NIGHTMARE_ROOM) {
                model.enterNightmareRoom();
                model.getState().enter(model, view); 
            }
        }
        
        input.close();
    }
}