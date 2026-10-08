import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MazeSolveModel {

    private final char[][] maze;
    private final int width;
    private final int height;
    private boolean[][] visited;

    public MazeSolveModel(char[][] maze, int width, int height) {
        this.maze = maze;
        this.width = width;
        this.height = height;
    }

    public String findPath(int startRow, int startCol, int endRow, int endCol) {
        this.visited = new boolean[height][width];
        List<Character> path = new ArrayList<>();
        if (solve(startRow, startCol, endRow, endCol, path)) {
            // handy dandy bit to reverse the directions. useful if you want the map to work
            Collections.reverse(path);
            StringBuilder pathString = new StringBuilder();
            for (Character direction : path) {
                pathString.append(direction);
            }
            return pathString.toString();
        }
        return "Impossible"; // no-win maze. A rotten thing to do. 
    }

    private boolean solve(int r, int c, int endRow, int endCol, List<Character> path) {
        // base case for bounderies, walls, and visitied places
        if (r < 0 || r >= height || c < 0 || c >= width || visited[r][c] || maze[r][c] == '#') {
            return false;
        }

        visited[r][c] = true;

        // base case for being at the end
        if (r == endRow && c == endCol) {
            return true;
        }

        // check north
        if (solve(r - 1, c, endRow, endCol, path)) {
            path.add('N');
            return true;
        }
        // check south
        if (solve(r + 1, c, endRow, endCol, path)) {
            path.add('S');
            return true;
        }
        // check east
        if (solve(r, c + 1, endRow, endCol, path)) {
            path.add('E');
            return true;
        }
        // check west
        if (solve(r, c - 1, endRow, endCol, path)) {
            path.add('W');
            return true;
        }
        
        // backtrack
        visited[r][c] = false;
        return false;
    }
}

