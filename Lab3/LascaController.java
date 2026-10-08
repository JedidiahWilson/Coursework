import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class LascaController {

    private LascaModel model;
    private LascaView view;

    // Controller state for handling moves
    private boolean pieceSelected = false;
    private int startRow, startCol;
    private LascaView.SquarePanel selectedPanel = null;

    public LascaController() {
        model = new LascaModel();
        view = new LascaView(model);

        addListeners();
        
        view.refreshView();
        view.setVisible(true);
    }

    
    private void addListeners() {
        // listen for clicks on every square
        for (LascaView.SquarePanel[] row : view.getSquarePanels()) {
            for (LascaView.SquarePanel panel : row) {
                panel.addMouseListener(new SquareClickListener());
            }
        }

        // listen to the reset button
        view.getResetButton().addActionListener(new ResetButtonListener());
    }

    
    private class SquareClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (model.isGameOver()) return; // no need to acknowledge clicks if game is over

            LascaView.SquarePanel clickedPanel = (LascaView.SquarePanel) e.getSource();
            int row = clickedPanel.getRow();
            int col = clickedPanel.getCol();

            if (!pieceSelected) {
                // This is the first click (selecting a piece to move)
                if (model.getPieceOnTop(row, col) == model.getCurrentPlayer()) {
                    pieceSelected = true;
                    startRow = row;
                    startCol = col;
                    selectedPanel = clickedPanel;
                    selectedPanel.highlight(true);
                    view.setStatus(model.getCurrentPlayer() + ": Select destination square.");
                } else {
                    view.setStatus("Invalid: Not your piece. " + model.getCurrentPlayer() + "'s turn.");
                }
            } else {
                // This is the second click (selecting a destination)
                if (model.isMoveValid(startRow, startCol, row, col)) {
                    // Valid move
                    model.executeMove(startRow, startCol, row, col);
                    model.switchPlayer();
                    view.refreshView(); 
                } else {
                    view.setStatus("Invalid move. " + model.getCurrentPlayer() + "'s turn.");
                }
                
                // reset selection state
                pieceSelected = false;
                if (selectedPanel != null) {
                    selectedPanel.highlight(false);
                    selectedPanel = null;
                }
            }
        }
    }

    
    private class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.setupInitialBoard();
            pieceSelected = false;
            if (selectedPanel != null) {
                selectedPanel.highlight(false);
                selectedPanel = null;
            }
            view.refreshView();
        }
    }

  
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LascaController();
            }
        });
    }
}
