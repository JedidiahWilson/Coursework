import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReplayEngine {

    public static void main(String[] args) {
        IsolaBoard board = new IsolaBoard();
        BoardVisuals visuals = new BoardVisuals();
        BoardSpace currentPlayer = BoardSpace.Player1;

        try (Scanner scanner = new Scanner(new File("game.dat"))) {
            System.out.println("Starting replay from game.dat...\n...dat's pretty cool.");
            visuals.replayBreak();
            visuals.displayBoard(board);
            System.out.println();
            pause(1);

            while (scanner.hasNextLine()) {
                String move = scanner.nextLine();
                BoardPosition currentPosition = board.findPosition(currentPlayer);
                BoardPosition newPosition = getNewPosition(currentPosition, move);

                visuals.replayBreak();

                board.movePlayer(currentPlayer, newPosition);

                visuals.displayBoard(board);
                                
                pause(1);

                // Switch players
                currentPlayer = (currentPlayer == BoardSpace.Player1) ? BoardSpace.Player2 : BoardSpace.Player1;
            }

            visuals.replayBreak();
            System.out.println("Replay finished. Was it cool?");
            BoardSpace winner = board.checkWinner();
            if (winner != BoardSpace.Available) {
                 System.out.println(winner + " wins! \nThe other one is a loser.");
            }


        } catch (FileNotFoundException e) {
            System.err.println("Error: game.dat file not found.");
        }
    }

    private static void pause(int seconds) {
        try {
            // Sleep time is in milliseconds
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            // Something is very wrong if this happens, but it was in the sample code, so I'll leave it in.
        }
    }

    private static BoardPosition getNewPosition(BoardPosition current, String move) {
        int dRow = 0;
        int dCol = 0;

        if (move.contains("N")) dRow = -1;
        if (move.contains("S")) dRow = 1;
        if (move.contains("W")) dCol = -1;
        if (move.contains("E")) dCol = 1;

        return new BoardPosition(current.getRow() + dRow, current.getColumn() + dCol);
    }
}