import org.junit.Before;
import org.junit.Test;
import java.util.Stack;
import static org.junit.Assert.*;


public class LascaModelTest {

    private LascaModel model;

    
    @Before // very handy time saver. use for future unit tests.
    public void setUp() {
        model = new LascaModel();
    }

    
    @Test
    public void testConstructor_InitialBoardSetup_CorrectPieces() {
        // red and black in the right places
        assertEquals(Color.RED, model.getPieceOnTop(0, 0));
        assertEquals(Color.BLACK, model.getPieceOnTop(6, 0));
        assertEquals(Color.RED, model.getPieceOnTop(1, 1));
        assertEquals(Color.BLACK, model.getPieceOnTop(5, 1));
        
        // empty squares are empty
        assertNull(model.getPieceOnTop(0, 1)); // White square
        assertNull(model.getPieceOnTop(3, 3)); // Middle square

        // no stacks greater than one at start. that would be cheating!
        assertEquals(1, model.getStackContents(0, 0).size());
        
        // black goes first. gamecenter rules.
        assertEquals(Color.BLACK, model.getCurrentPlayer());
    }

    
    @Test
    public void testGetPieceOnTop_OutOfBounds_ReturnsNull() {
        assertNull(model.getPieceOnTop(-1, 0));
        assertNull(model.getPieceOnTop(7, 0));
        assertNull(model.getPieceOnTop(0, -1));
        assertNull(model.getPieceOnTop(0, 7));
    }

    
    @Test
    public void testGetStackContents_EmptySquare_ReturnsEmptyStack() {
        Stack<Color> stack = model.getStackContents(3, 3);
        assertNotNull(stack);
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testSwitchPlayer_FromBlackToRedAndBack_PlayerIsCorrect() {
        model.switchPlayer();
        assertEquals(Color.RED, model.getCurrentPlayer());
        model.switchPlayer();
        assertEquals(Color.BLACK, model.getCurrentPlayer());
    }

    /**
     * Tests a valid 1-space simple move for the starting player.
     * (Corrected to use valid 'dark' squares)
     */
    @Test
    public void testIsMoveValid_ValidSimpleMove_ReturnsTrue() {
        // A valid forward-diagonal move for the starting black player
        // (4,0) and (3,1) are both valid 'dark' squares
        assertTrue(model.isMoveValid(4, 0, 3, 1));
    }
    
    
    @Test
    public void testIsMoveValid_ValidJump_ReturnsTrue() {
        for(int r=0; r<7; r++) for(int c=0; c<7; c++) model.getBoard()[r][c].clear(); // start by clearing board
        // manually place pieces for the jump
        model.getBoard()[3][1].push(Color.RED);
        model.getBoard()[4][0].push(Color.BLACK);
        // (4,0), (3,1), and (2,2) are all valid 'dark' squares
        assertTrue(model.isMoveValid(4, 0, 2, 2));
    }

    
    @Test
    public void testIsMoveValid_MoveToOccupiedSquare_ReturnsFalse() {
        assertFalse(model.isMoveValid(6, 1, 5, 2));
    }

    
    @Test
    public void testIsMoveValid_MoveWrongPlayersPiece_ReturnsFalse() {
        // black goes first. red cant move black's piece. no cheating.
        assertFalse(model.isMoveValid(2, 1, 3, 2));
    }

    
    @Test
    public void testIsMoveValid_JumpOverOwnPiece_ReturnsFalse() {
        for(int r=0; r<7; r++) for(int c=0; c<7; c++) model.getBoard()[r][c].clear();
        model.getBoard()[3][1].push(Color.BLACK);
        model.getBoard()[4][0].push(Color.BLACK);
        // no jumping your own piece.
        assertFalse(model.isMoveValid(4, 0, 2, 2));
    }
    
    
    @Test
    public void testIsMoveValid_JumpOverEmptySquare_ReturnsFalse() {
        // no jumping over a void, please
        assertFalse(model.isMoveValid(4, 0, 2, 2));
    }

    
    @Test
    public void testIsMoveValid_NonDiagonalMove_ReturnsFalse() {
        //diagonals only, please
        assertFalse(model.isMoveValid(4, 0, 4, 1));
        assertFalse(model.isMoveValid(4, 0, 3, 0));
    }

    
    @Test
    public void testExecuteMove_SimpleMove_EntireStackMoves() {
        for(int r=0; r<7; r++) for(int c=0; c<7; c++) model.getBoard()[r][c].clear();
        // Manually create a stack: BLACK on top of RED at (4,0)
        model.getBoard()[4][0].clear(); // clear the single BLACK
        model.getBoard()[4][0].push(Color.RED);
        model.getBoard()[4][0].push(Color.BLACK);
        
        assertEquals(2, model.getStackContents(4, 0).size());

        // Execute simple move from (4,0) to (3,1)
        model.executeMove(4, 0, 3, 1);

        assertTrue(model.getBoard()[4][0].isEmpty());
        
        Stack<Color> endStack = model.getStackContents(3, 1);
        assertEquals(2, endStack.size());
        assertEquals(Color.BLACK, endStack.pop());
        assertEquals(Color.RED, endStack.pop());
    }
    
   
    @Test
    public void testExecuteMove_Jump_CapturesOnlyTopPieceOfStack() { // that's a mouthful
    
        // Set up the board for the test:
        // Moving piece: A single BLACK at (4,0)
        // Jumped stack: RED on top of RED at (3,1)
        model.getBoard()[3][1].push(Color.RED); // (3,1) is normally empty, add one R
        model.getBoard()[3][1].push(Color.RED); // Now (3,1) has R-R
        model.getBoard()[2][2].clear(); // clear the destination square
        
        assertEquals("Jumped stack size should be 2 before move", 2, model.getStackContents(3, 1).size());

        // Execute jump from (4,0) to (2,2)
        assertTrue(model.isMoveValid(4, 0, 2, 2));
        model.executeMove(4, 0, 2, 2);

        // Check starting square
        assertTrue("Starting square (4,0) should be empty", model.getBoard()[4][0].isEmpty());
        
        // Check jumped square
        Stack<Color> jumpedStack = model.getStackContents(3, 1);
        assertEquals("Jumped square (3,1) should now have size 1", 1, jumpedStack.size());
        assertEquals("Remaining piece on jumped square should be RED", Color.RED, jumpedStack.peek());
        
        // Check ending square
        Stack<Color> endStack = model.getStackContents(2, 2);
        assertEquals("Ending square (2,2) should have stack size 2", 2, endStack.size());
        assertEquals("Top of end stack should be BLACK", Color.BLACK, endStack.pop());
        assertEquals("Bottom of end stack should be captured RED", Color.RED, endStack.pop());
    }

    /**
     * KEY TEST: Tests that a moving stack correctly captures a single piece.
     * (Corrected to use valid 'dark' squares)
     */
    @Test
    public void testExecuteMove_StackJumps_CapturesSinglePiece() {
        // Set up the board for the test:
        // Moving stack: BLACK on top of RED at (4,0)
        model.getBoard()[4][0].clear();
        model.getBoard()[4][0].push(Color.RED);
        model.getBoard()[4][0].push(Color.BLACK);
        // Jumped piece: A single RED at (3,1)
        model.getBoard()[3][1].push(Color.RED);

        // Execute jump from (4,0) to (2,2)
        model.executeMove(4, 0, 2, 2);

        // Check jumped square
        assertTrue("Jumped square (3,1) should be empty", model.getBoard()[3][1].isEmpty());

        // Check ending square
        Stack<Color> endStack = model.getStackContents(2, 2);
        assertEquals("Ending square (2,2) should have stack size 3", 3, endStack.size());
        assertEquals("Top of end stack should be BLACK", Color.BLACK, endStack.pop());
        assertEquals("Middle of end stack should be RED", Color.RED, endStack.pop());
        assertEquals("Bottom of end stack should be captured RED", Color.RED, endStack.pop());
    }

    /**
     * Tests that the game is not over at the start.
     */
    @Test
    public void testIsGameOver_InitialBoard_ReturnsFalse() {
        assertFalse("Game should not be over at the start", model.isGameOver());
    }
    
    /**
     * Tests that the game is over when a player is completely blocked.
     * This is the CORRECTED test.
     */
    @Test
    public void testIsGameOver_PlayerIsBlocked_ReturnsTrue() {
        // Clear the board except for one black piece at (0,0)
        for(int r=0; r<7; r++) for(int c=0; c<7; c++) model.getBoard()[r][c].clear();
        
        model.getBoard()[0][0].push(Color.BLACK); // Trapped piece
        model.getBoard()[1][1].push(Color.RED); // Block simple move
        model.getBoard()[2][2].push(Color.RED); // Block jump move
        
        // It's BLACK's turn, and they are *truly* blocked.
        assertTrue("Game should be over when player is blocked by pieces at (1,1) and (2,2)", model.isGameOver());
    }

    /**
     * NEW TEST: Tests that a piece is NOT trapped if it can jump,
     * even if its simple moves are blocked.
     */
    @Test
    public void testIsGameOver_PlayerCanJump_ReturnsFalse() {
        // Clear the board except for one black piece at (0,0)
        for(int r=0; r<7; r++) for(int c=0; c<7; c++) model.getBoard()[r][c].clear();
        model.getBoard()[0][0].push(Color.BLACK); // Piece at (0,0)
        model.getBoard()[1][1].push(Color.RED); // Opponent piece to be jumped
        // Landing square (2,2) is empty
        
        // It's BLACK's turn. Simple move to (1,1) is blocked, but jump to (2,2) is possible.
        assertFalse("Game should NOT be over when player can jump", model.isGameOver());
    }

    /**
     * Tests that the game is over when a player has no pieces left.
     */
    @Test
    public void testIsGameOver_PlayerHasNoPieces_ReturnsTrue() {
        // Remove all black pieces
        for(int r=0; r<7; r++) {
            for(int c=0; c<7; c++) {
                if(model.getPieceOnTop(r,c) == Color.BLACK) {
                    model.getBoard()[r][c].clear();
                }
            }
        }
        assertTrue("Game should be over when player has no pieces", model.isGameOver());
    }
}

