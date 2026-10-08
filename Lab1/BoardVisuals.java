public class BoardVisuals {
    public void replayBreak(){
        System.out.println();
        System.out.println("----------------");
        System.out.println();
    }
    public void displayBoard(IsolaBoard board) {
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                BoardSpace space = board.get(row, col);
                switch (space) {
                    case Player1:
                        System.out.print('1');
                        break;
                    case Player2:
                        System.out.print('2');
                        break;
                    case Missing:
                        System.out.print(' ');
                        break;
                    case Available:
                        System.out.print('-');
                        break;
                }
            }
            System.out.println();
        }
    }
}