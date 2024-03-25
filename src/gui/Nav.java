package gui;

// File IO imports
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

// GUI imports
import javax.swing.JPanel;

// Processing & backend imports
import gui.backend.Cell;
import gui.backend.Settings;
import gui.backend.SudokuChecker;

// Unused imports
//import javax.swing.JLabel;
//import javax.swing.JButton;

/**
 * The Nav class represents the Navigation bar at the top of the JFrame window.
 *
 * The Nav class is responsible for basic File IO operations. The most basic
 * functionality is the following:
 *      - open an existing .sdku file in the user's file system (a standard
 *      or default location is TBD);
 *
 * NOTE: Needed features
 *      - default open state from Settings
 *      - creating a new, completely empty .sdku file for a user to create
 *      their own puzzle with;
 *      - save the currently open .sdku file with any changes the user has made;
 * 
 * NOTE: Future features
 *      - auto-save
 *      - display elapsed time;
 *      - open a settings GUI interface;
 *      - display the number of mistakes made;
 *      - display how many hints have been used;
 *      - button to globally enable noteMode in CellGUI objects contained in
 *          the Board; and
 *      - toggleable number pad instead of using keyboard inputs.
 */
public class Nav extends JPanel {
    @SuppressWarnings("unused")
    private SudokuChecker sc;

    private Settings s;

    private File f;
    private Board b;
    private Cell[][] grid;

    /**
     * Create a new Nav in a command-line interface. This is intended for
     * debugging rather than practical use.
     *
     * Additionally, even for debugging, this should not be used since default
     * file I/O behavior is dictated by the values in the Settings object.
     * 
     * @Deprecated
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
        } else {
            createGUI();
            
            // TODO: Need to implement the default file options in Settings.
            switch(s.getDefaultOpenState()) {
                case 0:
                    openFile("input");
                    break;
                case 1:
                    openFile("medium");
                    break;
                case 2:
                    openFile("hard");
                    break;
                default:
                    System.out.println("Invalid default file option.");
            }
        }
    }

    /**
     * Set the Board object for the Nav bar.
     * 
     * This is necessary for the Nav bar to be able to interact with the
     * Board object and update the grid with file I/O operations. This
     * shouldn't be necessary to change once the program is running.
     * 
     * @param b
     */
    public void setBoard(Board b) {
        this.b = b;
    }

    /**
     * Set the SudokuChecker object for the Nav bar.
     * 
     * This is necessary for the Nav bar to be able to interact with the
     * SudokuChecker object for the solve button.
     * 
     * In the future, the SudokuChecker object is also used for hints.
     * 
     * This shouldn't be necessary to change once the program is running.
     */
    public void setChecker(SudokuChecker sc) {
        this.sc = sc;
    }

    /**
     * Style the Nav with the primary colors from Theme.
     */
    private void style() {
        setBackground(s.getTheme().getPrimaryBackground());
        setForeground(s.getTheme().getPrimaryText());
    }

    /**
     * Create the GUI for the Nav bar.
     * 
     * This method is called when the Nav object is created in the App
     * class and if the program is not being run in the command-line for
     * debugging purposes.
     */
    private void createGUI() {
        style();
        
        /*
        // TODO: Make custom Label class.
        JLabel elapsedTime = new JLabel("Elapsed Time: 00:00:00");
            elapsedTime.setFont(s.getFont()); 

        // TODO: Make custom Button class.
        JButton solve = new JButton("Solve");
            solve.setFont(s.getFont()); 
        */

        // Add the ComboBox for the file options, the elapsed time label, and
        // the solve button to the Nav Panel.
        add(createFileOptions());
        //add(elapsedTime);
        //add(solve);
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
            fileOptions.addItem("Save As");
            fileOptions.addItem("Exit");

        fileOptions.addActionListener(e -> {
            int selected = fileOptions.getSelectedIndex();
            switch(selected) {
                case 0:
                    newFile();
                    break;
                case 1:
                    openFile();
                    break;
                case 2:
                    saveFile(false);
                    break;
                case 3:
                    saveFile(true);
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        });
        return fileOptions;
    }

    /**
     * Given the user's selection from the ComboBox, perform the appropriate
     * action.
     * 
     * See Settings.getNewFileProperties() for more information on the expected
     * actions.
     * 
     * @see Settings.java
     */
    private void newFile() {
        int defaultOption = s.getNewFileProperties();
        
        switch(defaultOption) {
            case 0:
                f = new File("new.sdku");
                try {
                    f.createNewFile();
                    
                    // Create a new, blank .sdku file.
                    for(int i = 0; i < 9; i++)
                        for(int j = 0; j < 9; j++)
                            grid[i][j] = new Cell(i, j, 0);

                    // Update the Board with the new, blank .sdku puzzle.
                    b.setGrid(grid);

                    // Save the file if auto-save is on.
                    if(s.getAutoSave()) saveFile(true);
                } catch (IOException e1) {
                    System.err.println("Unable to create new file " + f.getName());
                    // TODO: Add a dialog box GUI to notify.
                    
                    // Open the default, pre-downloaded default.sdku file.
                    openFile(s.getDefaultDirectory() + "default");
                }
            case 1:
            case 2:
            default:
        }
    }

    /**
     * Open an existing .sdku file in the user's file system, selected using
     * a GUI.
     * 
     * Create the Grid and update the Board with the loaded Grid.
     */
    private void openFile() {
        // No error handling is needed here, since the Settings class ensures
        // that the default directory is always a valid directory.
        File defaultDirectory = new File(s.getDefaultDirectory());

        // Custom FileChooser class for style and to ensure the user can only
        // select .sdku files.
        FileChooser fc = new FileChooser(defaultDirectory);
        int result = fc.showOpenDialog(this);

        // If the user selects a file, open it.
        if(result == FileChooser.APPROVE_OPTION) {
            f = fc.getSelectedFile();
            createGrid(f);
        }
        b.setGrid(grid);
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

            grid = new Cell[9][9];
            for(int i = 0; i < 9; i++)
                for(int j = 0; j < 9; j++)
                    grid[i][j] = new Cell(i, j, 0);

            return;
        }

        // Otherwise, open the file and populate the grid.
        createGrid(f);
    }

    /**
     * Save the currently open .sdku file with any changes the user has made.
     * This needs to pull the current state of the grid from the Board JPanel.
     * 
     * If called from 'Save' option, then saveAs should be false.
     * If called from 'Save As' option, then saveAs should be true.
     * 
     * 'Save' will simply overwrite the existing file that was opened,
     * while 'Save As' while prompt the user to select or create a file.
     * 
     * 'Save As' functionality is the default behavior for new .sdku files
     * created. Therefore, if the filename is 'new.sdku', then saveAs will
     * be made true.
     * 
     * @param boolean
     */
    private void saveFile(boolean saveAs) {
        if(f.getName().equals("new.sdku"))
            saveAs = true;
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
     * If the user has enabled auto-save in Settings, then save the file.
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
        if(s.getAutoSave()) saveFile(true);
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
            newFile();
            return;
        }
    }
}
