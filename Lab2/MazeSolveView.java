import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;


public class MazeSolveView {

   
    public File getMazeFile() {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Select a Maze File");
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
    
    // looks nicer than printing to a console, even if it is a little windows 8
    public void showCompletionMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Maze Solver", JOptionPane.INFORMATION_MESSAGE);
    }
}
