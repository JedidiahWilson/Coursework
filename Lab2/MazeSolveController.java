import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// connects model and view
public class MazeSolveController {

    private MazeSolveModel model;
    private final MazeSolveView view;

   
    public MazeSolveController() {
        this.view = new MazeSolveView();
    }

    
    public void run() {
        File mazeFile = view.getMazeFile();
        if (mazeFile == null) {
            System.out.println("No file selected. Exiting.");
            return;
        }

        try (Scanner scanner = new Scanner(mazeFile)) {
            if (!scanner.hasNextLine()) {
                System.err.println("Error: Maze file is empty.");
                return;
            }
            int width = Integer.parseInt(scanner.nextLine());

            if (!scanner.hasNextLine()) {
                System.err.println("Error: Maze file is missing height.");
                return;
            }
            int height = Integer.parseInt(scanner.nextLine());
            char[][] mazeLayout = new char[height][width];
            
            // this bit adds whitespace at the end of a row if there insn't enough
            // and trims off whitespace if there's toomuch. 
            for (int i = 0; i < height; i++) {
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String paddedLine = String.format("%-" + width + "s", line);
                    mazeLayout[i] = paddedLine.substring(0, width).toCharArray();
                } else {
                    System.err.println("Error: Too short. you probably coppied the maze wrong.");
                    for (int j = i; j < height; j++) {
                        mazeLayout[j] = " ".repeat(width).toCharArray();
                    }
                    break;
                }
            }

            this.model = new MazeSolveModel(mazeLayout, width, height);
            
            List<String> results = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                //should be robust enough to handle a few formatting errors. I'll probably make some in testing
                try (Scanner lineScanner = new Scanner(line.replace(',', ' '))) {
                    if (lineScanner.hasNextInt()) {
                        int startRow = lineScanner.nextInt();
                        if (!lineScanner.hasNextInt()) continue;
                        int startCol = lineScanner.nextInt();
                        if (!lineScanner.hasNextInt()) continue;
                        int endRow = lineScanner.nextInt();
                        if (!lineScanner.hasNextInt()) continue;
                        int endCol = lineScanner.nextInt();

                        String path = model.findPath(startRow, startCol, endRow, endCol);
                        results.add(path);
                    }
                }
            }
            
            writeResults(mazeFile, results);

        } catch (FileNotFoundException e) {
            System.err.println("Error: Maze file not found... better luck next time.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error: Incorrect formatting. The file needs to begin with maze width, then height, and format after.");
            e.printStackTrace();
        }
    }

    
    private void writeResults(File inputFile, List<String> results) {
        String outputFileName = inputFile.getAbsolutePath() + ".out";
        try (PrintWriter writer = new PrintWriter(outputFileName)) {
            for (String result : results) {
                writer.println(result);
            }
            view.showCompletionMessage("Maze solving complete. Yes, that was fast. Output written to:\n" + outputFileName);
        } catch (FileNotFoundException e) {
            System.err.println("Error: Could not write to output file. This had better never come up");
            e.printStackTrace();
        }
    }

    
    public static void main(String[] args) {
        MazeSolveController controller = new MazeSolveController();
        controller.run();
    }
}

