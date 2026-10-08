import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class RunIsola {

    public static void main(String[] args) {
        IsolaBoard board = new IsolaBoard();
        BoardVisuals visuals = new BoardVisuals();
        Scanner scanner = new Scanner(System.in);
        BoardSpace currentPlayer = BoardSpace.Player1;

        try (PrintWriter writer = new PrintWriter(new FileWriter("game.dat"))) {
            while (board.checkWinner() == BoardSpace.Available) {
                visuals.displayBoard(board);
                System.out.println();
                System.out.println(currentPlayer + "'s turn.");

                BoardPosition currentPosition = board.findPosition(currentPlayer);
                BoardPosition newPosition = null;
                boolean moveIsValid = false;

                while (!moveIsValid) {
                    System.out.print("Enter your move (N, NE, E, SE, S, SW, W, NW): ");
                    String move = scanner.next().toUpperCase();

                    newPosition = getNewPosition(currentPosition, move);

                    if (isValidMove(board, currentPosition, newPosition)) {
                        board.movePlayer(currentPlayer, newPosition);
                        writer.println(move);
                        moveIsValid = true;
                    } else {
                        System.out.println("Invalid move. Please try again.");
                    }
                }

                
                currentPlayer = (currentPlayer == BoardSpace.Player1) ? BoardSpace.Player2 : BoardSpace.Player1;
            }

            System.out.println("\nGame Over!");
            visuals.displayBoard(board);
            BoardSpace winner = board.checkWinner();
            System.out.println(winner + " wins! \nThe other one is a loser.");

        } catch (IOException e) {
            System.err.println("What the heck did you do? There was an error writing to game.dat: " + e.getMessage());
        } finally {
            scanner.close();
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

    private static boolean isValidMove(IsolaBoard board, BoardPosition current, BoardPosition newPos) {
        if (newPos == null) return false;

        int newRow = newPos.getRow();
        int newCol = newPos.getColumn();

        // No flying away off the board. Not vrey sportsmanlike.
        if (newRow < 0 || newRow >= board.getHeight() || newCol < 0 || newCol >= board.getWidth()) {
            return false;
        }

        // No hovering over the abyss or phasing into other people. Very rude.
        if (board.get(newRow, newCol) != BoardSpace.Available) {
            return false;
        }

        // No teleporting, please
        int rowDiff = Math.abs(current.getRow() - newRow);
        int colDiff = Math.abs(current.getColumn() - newCol);
        if (rowDiff > 1 || colDiff > 1) {
            return false;
        }
        
        // Don't just stand still. Get some exercise. Lzy thing.
        if (rowDiff == 0 && colDiff == 0) {
            return false;
        }

        return true;
    }
}