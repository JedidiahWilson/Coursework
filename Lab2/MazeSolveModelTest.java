import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MazeSolveModelTest {

    private MazeSolveModel model;

    @Test
    public void testFindPath_simplePathExists() {
        char[][] maze = {
            {' ', ' ', ' '},
            {'#', ' ', '#'},
            {' ', ' ', ' '}
        };
        model = new MazeSolveModel(maze, 3, 3);
        String result = model.findPath(0, 0, 2, 2);
        assertEquals("ESSE", result, "Should find a simple path in the maze.");
    }

    @Test
    public void testFindPath_noPathExists() {
        char[][] maze = {
            {' ', ' ', ' '},
            {'#', '#', '#'},
            {' ', ' ', ' '}
        };
        model = new MazeSolveModel(maze, 3, 3);
        String result = model.findPath(0, 0, 2, 2);
        assertEquals("Impossible", result, "Should return 'Impossible' when no path exists.");
    }


    @Test
    public void testFindPath_startIsEnd() {
        char[][] maze = {
            {' ', ' '},
            {' ', ' '}
        };
        model = new MazeSolveModel(maze, 2, 2);
        String result = model.findPath(0, 0, 0, 0);
        assertEquals("", result, "Should return an empty string if the start and end points are the same.");
    }

    @Test
    public void testFindPath_outOfBoundsStart_negative() {
        char[][] maze = {
            {' ', ' '},
            {' ', ' '}
        };
        model = new MazeSolveModel(maze, 2, 2);
        String result = model.findPath(-1, 0, 1, 1);
        assertEquals("Impossible", result, "Should be 'Impossible' if some smart aleck starts it out of bounds.");
    }

    @Test
    public void testFindPath_outOfBoundsStart_positive() {
        char[][] maze = {
            {' ', ' '},
            {' ', ' '}
        };
        model = new MazeSolveModel(maze, 2, 2);
        String result = model.findPath(0, 2, 1, 1);
        assertEquals("Impossible", result, "Should be 'Impossible' if someone tries to break my code with an out of bounds start.");
    }

    @Test
    public void testFindPath_outOfBoundsEnd() {
        char[][] maze = {
            {' ', ' '},
            {' ', ' '}
        };
        model = new MazeSolveModel(maze, 2, 2);
        // The algorithm still explores and will find no path to the invalid end.
        String result = model.findPath(0, 0, 2, 0);
        assertEquals("Impossible", result, "Should be 'Impossible' if end is out of bounds.");
    }

    @Test
    public void testFindPath_startInWall() {
        char[][] maze = {
            {'#', ' '},
            {' ', ' '}
        };
        model = new MazeSolveModel(maze, 2, 2);
        String result = model.findPath(0, 0, 1, 1);
        assertEquals("Impossible", result, "Should be 'Impossible' if start is inside a wall. Phasing out of walls is hard.");
    }

    @Test
    public void testFindPath_endInWall() {
        char[][] maze = {
            {' ', ' '},
            {' ', '#'}
        };
        model = new MazeSolveModel(maze, 2, 2);
        String result = model.findPath(0, 0, 1, 1);
        assertEquals("Impossible", result, "Should be 'Impossible' if end is inside a wall. Running into walls hurts.");
    }

    @Test
    public void testFindPath_complexMazeWithPath() {
        char[][] maze = {
            {' ', '#', ' ', '#', ' '},
            {' ', '#', ' ', '#', ' '},
            {' ', ' ', ' ', '#', ' '},
            {'#', '#', ' ', ' ', ' '},
            {' ', ' ', ' ', '#', ' '}
        };
        model = new MazeSolveModel(maze, 5, 5);
        String result = model.findPath(0, 0, 4, 4);
        assertEquals("SSEESEES", result, "Should find the correct path in a complex maze.");
    }
    
    @Test
    public void testFindPath_longWindingPath() {
        char[][] maze = {
                {' ', ' ', ' ', '#', ' '},
                {'#', '#', ' ', '#', ' '},
                {' ', ' ', ' ', '#', ' '},
                {' ', '#', '#', '#', ' '},
                {' ', ' ', ' ', ' ', ' '}
        };
        model = new MazeSolveModel(maze, 5, 5);
        String result = model.findPath(0, 0, 4, 0);
        assertEquals("EESSWWSS", result, "Should find a winding path.");
    }
}
