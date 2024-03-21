import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;

/**
 * The Nav class represents the Navigation bar at the top of the JFrame window.
 *
 * The Nav class is responsible for basic File IO operations. The most basic
 * functionality is the following:
 *      - creating a new, completely empty .sdku file for a user to create
 *      their own puzzle with;
 *      - open an existing .sdku file in the user's file system (a standard
 *      or default location is TBD);
 *      - save the currently open .sdku file with any changes the user has made.
 *
 * In the future, the Nav class would ideally incorporate additional elements,
 * such as the difficulty of the current problem, providing hints, displaying
 * the length of time since a puzzle was started, etc.
 */
public class Nav extends JPanel {
    private Settings s;
    private File f;
    private Cell[][] grid;

    /**
     * Create a new Nav in a command-line interface. This is intended for
     * debugging rather than practical use.
     *
     * Additionally, even for debugging, this should be used since default
     * file I/O behavior is dictated by the values in the Settings object.
     */
    public Nav() {
        super();
        System.out.println("Welcome to the Sudoku Solver!");

        Scanner in = new Scanner(System.in);
            System.out.print(
                    "Enter the name of the input file (do not " +
                    "include the file extension): "
                    );
            String inputFilename = in.nextLine();
        in.close();

        openFile(inputFilename);
    }

    /**
     * Create a new Nav object for use in the command-line. This is intended
     * for debugging rather than practical use.
     *
     * @param Settings
     */
    public Nav(Settings s, boolean cli) {
        super();
        this.s = s;
        
        if(cli) {
            System.out.println("Welcome to the Sudoku Solver!");

            Scanner in = new Scanner(System.in);
                System.out.print(
                        "Enter the name of the input file (do not " +
                        "include the file extension): "
                        );
                String inputFilename = in.nextLine();
            in.close();

            openFile(inputFilename);
        } else {
            // TODO: Create the GUI for the Nav bar.
            createGUI();
        }
    }

    /**
     * Create the GUI for the Nav bar.
     * 
     * This method is called when the Nav object is created in the App
     * class if the program is not being run in the command-line for
     * debugging purposes.
     */
    private void createGUI() {
        JComboBox<String> fileOptions = new JComboBox<String>();
            fileOptions.addItem("New");
            fileOptions.addItem("Open");
            fileOptions.addItem("Save");
            fileOptions.addItem("Exit");
            
        JLabel elapsedTime = new JLabel("Elapsed Time: 00:00:00");

        JButton solve = new JButton("Solve");

        add(fileOptions);
        add(elapsedTime);
        add(solve);
    }

    /**
     * Given the user's selection from the JComboBox, perform the appropriate
     * action.
     *
     * @param String
     */
    private void newFile() {
        int defaultOption = s.getNewFileProperties();

        // See Settings.getNewFileProperties for possible values and
        //      the expected behavior of each value.
        switch(defaultOption) {
            case 0:
            case 1:
            case 2:
            default:
        }
        // TODO: Create a new .sdku file in the default directory.
    }

    /**
     * Given an input filename, open the file and populate the grid.
     *
     * @param String
     */
    private void openFile(String filename) {
        File f = new File("resources/" + filename + ".sdku");
        if(f == null || !f.exists() || f.isDirectory() || !f.canRead()) {
            System.out.println(
                    "The file " + filename + 
                    ".sdku does not exist or cannot be read."
                );

            System.out.println("The exact path to the file is: " + 
                    f.getAbsolutePath()
                );

            return;
        }

        createGrid(f);
    }

    /**
     * Save the currently open .sdku file with any changes the user has made.
     * This needs to pull the current state of the grid from the Board JPanel.
     */
    private void saveFile() {
        // TODO: Get the current Cell[][] from the Board JPanel
        // TODO: Write to a file.
    }

    /**
     * Get the currently loaded Cell[][] grid.
     *
     * @return the grid
     */
    public Cell[][] getLoadedGrid() {
        return grid;
    }

    /**
     * Update the loaded grid with the current version with any changes the
     * user has made.
     *
     * For future auto-save functionality, this could simply be called after
     * each change the user makes on the Sudoku board.
     *
     * In the future, an optimization could be made where a different data
     * structure is used to pass only the changes the player has made within
     * the last x minutes, for eg. {row, col, value, noteValues[]}. Given
     * the size of a standard Sudoku board however, this may be an irrelevant
     * and unnecessary optimization.
     *
     * @param Cell[][]
     */
    public void updateLoadedGrid(Cell[][] grid) {
        this.grid = grid;
    }

    /**
     * To be called when a File is opened or created. This will populate the
     * Cell[][] grid with values read in from the file.
     *
     * @param File
     */
    private void createGrid(File f) {
        try (Scanner in = new Scanner(f)) {
            Cell[][] grid = new Cell[9][9];
            for(int i = 0; i < 9; i++) {
                String line = in.nextLine();
                for(int j = 0; j < 9; j++) {
                    if(line.charAt(j) == '.') {
                        grid[i][j] = new Cell(i, j, 0);
                        continue;
                    }
                    
                    int value = Character.getNumericValue(line.charAt(j));
                    grid[i][j] = new Cell(i, j, value);
                }
            }

            this.grid = grid;
        } catch (FileNotFoundException e) {
            String filename = f.getName();
            System.err.println("An error occurred while reading the file " +
                    filename + ".sdku."
                );

            e.printStackTrace();
            return;
        }
    }
}
