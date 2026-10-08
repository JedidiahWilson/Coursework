import java.awt.*;
import java.util.Stack;
import javax.swing.*;
import javax.swing.border.Border;

// Based on a checkers board
public class LascaView extends JFrame {

    private final int BOARD_SIZE = 7;
    private SquarePanel[][] squarePanels;
    private JLabel statusLabel;
    private JButton resetButton;

    private LascaModel model;

    public LascaView(LascaModel model) {
        this.model = model;
        setTitle("Lasca Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Friendly message at the top
        statusLabel = new JLabel("Welcome to Lasca! BLACK's turn.", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(statusLabel, BorderLayout.NORTH);

        //draws the board
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        squarePanels = new SquarePanel[BOARD_SIZE][BOARD_SIZE];
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                squarePanels[r][c] = new SquarePanel(r, c);
                // Set alternating colors for the board squares
                if ((r + c) % 2 == 0) {
                    squarePanels[r][c].setBackground(new java.awt.Color(210, 180, 140)); // Tan
                } else {
                    squarePanels[r][c].setBackground(new java.awt.Color(139, 69, 19)); // Brown
                }
                boardPanel.add(squarePanels[r][c]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Reset Button (Bottom)
        resetButton = new JButton("Reset Game");
        add(resetButton, BorderLayout.SOUTH);

        pack();
        setSize(500, 550); // too small?? 
        setLocationRelativeTo(null); // center the window
    }

    // refreshes the view... very creatively named
    public void refreshView() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                squarePanels[r][c].updateStack(model.getStackContents(r, c));
            }
        }
        statusLabel.setText(model.getCurrentPlayer() + "'s turn.");
        
        if (model.isGameOver()) {
            statusLabel.setText("Game Over! " + model.getCurrentPlayer().opponent() + " wins!");
        }
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }
    
    public SquarePanel[][] getSquarePanels() {
        return squarePanels;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    // easier than the alternative
    public class SquarePanel extends JPanel {
        private int row;
        private int col;
        private Stack<Color> stack;
        private final int PIECE_HEIGHT = 10;
        private final int PIECE_OVERLAP = 4;

        public SquarePanel(int row, int col) {
            this.row = row;
            this.col = col;
            this.stack = new Stack<>();
            setPreferredSize(new Dimension(60, 60));
        }

        public void updateStack(Stack<Color> stack) {
            this.stack = stack;
            repaint(); // Trigger a redraw
        }
        
        public int getRow() { return row; }
        public int getCol() { return col; }

        public void highlight(boolean selected) {
            Border border = selected ? BorderFactory.createLineBorder(java.awt.Color.CYAN, 3) : null;
            setBorder(border);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (stack.isEmpty()) {
                return; // Nothing to draw
            }
            
            
            Object[] pieces = stack.toArray();
            int y = getHeight() - PIECE_HEIGHT + -15; // 0 would be way too low, too high and stack reaches to the next square

            for (Object piece : pieces) {
                Color color = (Color) piece;
                
                
                if (color == Color.RED) {
                    g.setColor(java.awt.Color.RED.darker());
                } else {
                    g.setColor(java.awt.Color.BLACK);
                }
                
                
                g.fillOval(10, y, getWidth() - 20, PIECE_HEIGHT * 2); // maybe make this look more 3d?
                
                // Draw a border to distinguish pieces. makes two pieces of the same color look 3d, so maybe use to solve above problem?
                g.setColor(java.awt.Color.GRAY);
                g.drawOval(10, y, getWidth() - 20, PIECE_HEIGHT * 2);

                y -= (PIECE_HEIGHT - PIECE_OVERLAP); // move up for the next piece
            }
        }
    }
}
