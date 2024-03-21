package gui;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JPanel;

import gui.backend.Settings;

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
     * Additionally, even for debugging, this should not be used since default
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
        
        // If the program is being run in the command-line, prompt the user
        // for the name of the input file.
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

        // Otherwise, create the GUI for the Nav bar.
        } else createGUI();
            
    }

    /**
     * Create the GUI for the Nav bar.
     * 
     * This method is called when the Nav object is created in the App
     * class if the program is not being run in the command-line for
     * debugging purposes.
     */
    private void createGUI() {
        JLabel elapsedTime = new JLabel("Elapsed Time: 00:00:00");
            elapsedTime.setFont(s.getFont()); // TODO: Make custom Label class.
        JButton solve = new JButton("Solve");
            solve.setFont(s.getFont()); // TODO: Make custom Button class.

        // Add the ComboBox for the file options, the elapsed time label, and
        // the solve button to the Nav Panel.
        add(createFileOptions());
        add(elapsedTime);
        add(solve);
    }

    /**
     * Create the ComboBox for the file options.
     * 
     * @return ComboBox<String>
     */
    private ComboBox<String> createFileOptions() {
        // Create a new ComboBox with the default styling.
        ComboBox<String> fileOptions = new ComboBox<>(s);
            fileOptions.addItem("New");
            fileOptions.addItem("Open");
            fileOptions.addItem("Save");
            fileOptions.addItem("Exit");

        // Add an ActionListener to the ComboBox to handle the user's selection.
        fileOptions.addActionListener(e -> {
            String selected = (String) fileOptions.getSelectedItem();
            switch(selected) {
                case "New":
                    newFile();
                    break;
                case "Open":
                    openFile();
                    break;
                case "Save":
                    saveFile();
                    break;
                case "Exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        });

        return fileOptions;
    }

    /**
     * Given the user's selection from the JComboBox, perform the appropriate
     * action.
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
     * Open an existing .sdku file in the user's file system, using a GUI.
     */
    private void openFile() {
        // No error handling is needed here, since the Settings class ensures
        // that the default directory is always a valid directory.
        File defaultDirectory = new File(s.getDefaultDirectory());

        // Custom FileChooser class for style and to ensure the user can only
        // select .sdku files.
        FileChooser fc = new FileChooser(defaultDirectory, s);
        int result = fc.showOpenDialog(this);

        // If the user selects a file, open it.
        if(result == FileChooser.APPROVE_OPTION) {
            f = fc.getSelectedFile();
            createGrid(f);
        }
    }

    /**
     * Given an input filename, open the file and populate the grid.
     * 
     * This implementation is for use only in the command-line interface. 
     *
     * @param String
     */
    private void openFile(String filename) {
        File f = new File("resources/" + filename + ".sdku");
        // If the file does not exist, print an error message and return.
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

        // Otherwise, open the file and populate the grid.
        System.out.println("Opening the file " + filename + ".sdku.");
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
            // Create a new 9x9 grid of Cells.
            Cell[][] grid = new Cell[9][9];
            
            // Read in the file line by line.
            for(int i = 0; i < 9; i++) {

                // Read in the line and create a new Cell for each character.
                String line = in.nextLine();
                for(int j = 0; j < 9; j++) {

                    // If the character is a period, the cell is empty.
                    if(line.charAt(j) == '.') {
                        grid[i][j] = new Cell(i, j, 0);
                        continue;
                    }
                    
                    // Otherwise, the character is a digit.
                    int value = Character.getNumericValue(line.charAt(j));
                    grid[i][j] = new Cell(i, j, value);
                }
            }

            this.grid = grid;

        // If the file is not found, print an error message and return.
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
