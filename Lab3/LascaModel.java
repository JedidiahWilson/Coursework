import java.util.Stack;


public class LascaModel {

    private final int BOARD_SIZE = 7;
    
    private Stack<Color>[][] board;
    private Color currentPlayer;

   
    public LascaModel() {
        
        board = new Stack[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new Stack<>();
            }
        }
        setupInitialBoard();
        currentPlayer = Color.BLACK; // black first in this version
    }

    
    public void setupInitialBoard() {
        // Clear the board first
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j].clear();
            }
        }

        // Place Red pieces
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    board[row][col].push(Color.RED);
                }
            }
        }

        // Place Black pieces
        for (int row = 4; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    board[row][col].push(Color.BLACK);
                }
            }
        }
        currentPlayer = Color.BLACK;
    }
    
    public Stack<Color>[][] getBoard() {
        return board;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    
    public void switchPlayer() {
        currentPlayer = currentPlayer.opponent();
    }

   
    public Color getPieceOnTop(int row, int col) {
        if (isOutOfBounds(row, col) || board[row][col].isEmpty()) {
            return null;
        }
        return board[row][col].peek();
    }

   
    
    public Stack<Color> getStackContents(int row, int col) {
        if (isOutOfBounds(row, col)) {
            return new Stack<>();
        }
        // return a copy 
        return (Stack<Color>) board[row][col].clone();
    }
    
    
    public void executeMove(int startRow, int startCol, int endRow, int endCol) {
        // Both simple moves and jumps move the entire stack.
        Stack<Color> movingStack = board[startRow][startCol];
        board[startRow][startCol] = new Stack<>(); // Empty the starting square

        // Check if the move is a jump (2 spaces)
        if (Math.abs(startRow - endRow) == 2) {
            // only capture the top piece from the jumped stack
            int jumpedRow = (startRow + endRow) / 2;
            int jumpedCol = (startCol + endCol) / 2;
            
            // only pop the top piece from the jumped stack
            if (!board[jumpedRow][jumpedCol].isEmpty()) {
                Color capturedPiece = board[jumpedRow][jumpedCol].pop(); // Pop only the top piece
                movingStack.add(0, capturedPiece); // Add that single piece to the bottom
            }
            // The rest of the jumped stack (if any) is left behind.
            
            board[endRow][endCol] = movingStack;
        } else {
            //he entire stack moves to the new square.
            board[endRow][endCol] = movingStack;
        }
    }

    
    public boolean isMoveValid(int startRow, int startCol, int endRow, int endCol) {
        if (isOutOfBounds(startRow, startCol) || isOutOfBounds(endRow, endCol)) return false;
        
        Color pieceColor = getPieceOnTop(startRow, startCol);
        if (pieceColor != currentPlayer) return false; // Not the player's piece

        int rowDiff = Math.abs(startRow - endRow);
        int colDiff = Math.abs(startCol - endCol);
        
        if (rowDiff != colDiff) return false; // Not a diagonal move. cheater

        // Standard move (1 space)
        if (rowDiff == 1) {
            return board[endRow][endCol].isEmpty(); // Can only move to an empty square
        }
        
        // Jump move (2 spaces)
        if (rowDiff == 2) {
            if (!board[endRow][endCol].isEmpty()) return false; // Can only land on empty square
            
            int jumpedRow = (startRow + endRow) / 2;
            int jumpedCol = (startCol + endCol) / 2;
            Color jumpedPieceColor = getPieceOnTop(jumpedRow, jumpedCol);
            
            // Must jump over an opponent's piece
            return jumpedPieceColor != null && jumpedPieceColor == currentPlayer.opponent();
        }
        
        return false; // Not a valid move distance. vheater
    }
    
    
    public boolean isGameOver() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (getPieceOnTop(r, c) == currentPlayer) {
                    // Check all possible diagonal moves (1 and 2 spaces)
                    for (int dr = -2; dr <= 2; dr++) {
                        for (int dc = -2; dc <= 2; dc++) {
                            if (dr == 0 || dc == 0) continue; // Skip non-diagonal
                            if (isMoveValid(r, c, r + dr, c + dc)) {
                                return false; // Found at least one valid move
                            }
                        }
                    }
                }
            }
        }
        return true; // No valid moves found for any piece. that's all she wrote
    }

    private boolean isOutOfBounds(int row, int col) {
        return row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE;
    }
}