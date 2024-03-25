package gui;

// GUI imports
import javax.swing.JPanel;
import java.awt.GridLayout;

// Event & action imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Processing & backend imports
import gui.backend.Cell;
import gui.backend.Settings;
import gui.backend.SudokuChecker;

/**
 * The Board class represents the Sudoku board in the GUI.
 * 
 * It contains all other related GUI components, and is the main JPanel for
 * the board. Accessible information from the Board includes:
 *      - the current state of the board, including filled values and notes;
 * 
 * TODO: Future features:
 *      - the current difficulty;
 *      - the current time since the puzzle was started;
 *      - the current hints available; and
 *      - the current number of mistakes.
 */
public class Board extends JPanel {
    private Settings s;
    private Cell[][] grid;
    private Cell[][] solvedGrid;
    private CellGUI[][] gridGUI;
    private SudokuChecker sc;
    private CellGUI selected;

    /**
     * Create a new Board object.
     * 
     * Board requires a Settings object for styling, sizing, and for different
     * toggleable features.
     * 
     * A 2D Cell array is required to abstract the data away from the Board,
     * and the GUI in general.
     * 
     * @param s
     * @param grid
     */
    public Board(Settings s, Cell[][] grid) {
        super(new GridLayout(9, 9));
        this.s = s;
        this.grid = grid;
        solvedGrid = new SudokuChecker(Cell.copyGrid(grid)).getSolution();

        style();
        createBoard();
    }

    /**
     * Create a new Board object.
     * 
     * Board requires a Settings object for styling, sizing, and for different
     * toggleable features.
     * 
     * A 2D Cell array is required to abstract the data away from the Board,
     * and the GUI in general.
     * 
     * A SudokuChecker is required if Settings.getAutoCheckValues() is true.
     * 
     * @param s
     * @param grid
     * @param sc
     */
    public Board(Settings s, Cell[][] grid, SudokuChecker sc) {
        super(new GridLayout(9, 9));
        this.s = s;
        this.grid = grid;
        this.sc = sc;
        solvedGrid = new SudokuChecker(Cell.copyGrid(grid)).getSolution();

        style();
        createBoard();
    }

    /**
     * Get the grid of the Board.
     * 
     * @return
     */
    public Cell[][] getGrid() {
        return grid;
    }

    /**
     * Set the grid of the Board to the given grid.
     * 
     * Intended to be used when loading a new puzzle.
     * 
     * @param grid
     */
    public void setGrid(Cell[][] grid) {
        this.grid = grid;
        gridGUI = null;
        selected = null;

        removeAll();
        createBoard();
        repaint();
        revalidate();
    }

    /**
     * Set up the Board Panel with the appropriate styling.
     */
    private void style() {
        setBackground(s.getTheme().getPrimaryBackground());
        setForeground(s.getTheme().getPrimaryText());
    }

    /**
     * Create the Board Panel with the appropriate cells.
     * 
     * Every CellGUI object is created with the appropriate Cell object and
     * Settings object. The CellGUI objects are then added to the Board Panel.
     * 
     * Each CellGUI object is also given a MouseListener to handle user input,
     * and a KeyListener to handle keyboard input if/when the cell is selected.
     */
    private void createBoard() {
        if(s.getAutoFillNotes()) grid = sc.getPossibleValues(grid);
        gridGUI = new CellGUI[9][9];

        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                // Create a new CellGUI object with the appropriate Cell object.
                CellGUI cell = new CellGUI(grid[i][j], s);

                // Add a MouseListener to the cell.
                cell.addMouseListener(createMouseListener(cell));

                // Add the CellGUI object to the Board Panel.
                gridGUI[i][j] = cell;
                add(gridGUI[i][j]);
            }
        }
    }

    /**
     * Select the given cell and highlight all cells in the same row, column,
     * and box.
     * 
     * Intended to be used in conjunction with a MouseListener.
     * 
     * @param cell
     */
    private void select(Cell cell) {
        // Select the CellGUI objects in the same row and column, but not in
        // the same box. 
        for(int n = 0; n < 9; n++) {
            int row = cell.getRow();
            int col = cell.getCol();
            boolean colInSameBox = 
                (gridGUI[n][col].cell.getBox() == 
                cell.getBox());

            boolean rowInSameBox = 
                (gridGUI[row][n].cell.getBox() ==
                cell.getBox());

            // Handling edge cases where the column or row are in the box.
            if(colInSameBox && !rowInSameBox) {
                gridGUI[row][n].select();
                continue;
            } else if(!colInSameBox && rowInSameBox) {
                gridGUI[n][col].select();
                continue;
                
            // Skip the cell if it is in the same box.
            } else if(colInSameBox && rowInSameBox) continue;

            gridGUI[n][col].select();
            gridGUI[row][n].select();
        }

        // Select every cell in the box.
        int boxRow = cell.getRow() / 3;
        int boxCol = cell.getCol() / 3;
        for(int n = 0; n < 3; n++) {
            for(int m = 0; m < 3; m++) {
                int row = boxRow * 3 + n;
                int col = boxCol * 3 + m;
                
                // Skip the user-selected cell for clearer highlighting.
                if(row == cell.getRow() && col == cell.getCol()) continue;

                gridGUI[row][col].select();
            }
        }
    }

    /**
     * Create a MouseListener for CellGUI objects.
     * 
     * This MouseListener listens for mouse clicks, and selects the cell
     * if it is clicked. If the cell is already selected, it is deselected.
     * 
     * Additionally, adds hover highlighting for the cell.
     * 
     * mouseClicked() is implemented for cell selection functionality.
     * mouseEntered() and mouseExited() are implemented for hover functionality.
     * Other methods are not implemented.
     * 
     * @param cell
     * @return MouseListener
     */
    private MouseListener createMouseListener(CellGUI cell) {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Left click
                if(e.getButton() == 1) {
                    // Deselect the already selected cell.
                    if(selected != null) select(selected.cell);
                    
                    // Deselect the cell if it is already selected.
                    if(selected == cell) {
                        selected = null;
                        return;
                    }

                    // Select the cell and add a KeyListener for input.
                    select(cell.cell);
                    selected = cell;
                    selected.requestFocusInWindow();
                    selected.addKeyListener(createKeyListener());

                // Right click
                } else if(e.getButton() == 3) {
                    cell.setNoteMode();
                    return;
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {
                cell.select();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                cell.select();
            }
        };
    }

    /**
     * Create a KeyListener for CellGUI objects.
     * 
     * This KeyListener listens for key releases, and updates the
     * selected cell with additional possible values or a new value.
     * 
     * Only keyReleased is implemented, as keyTyped does not allow for
     * certain features regarding key codes.
     * 
     * @return KeyListener
     */
    private KeyListener createKeyListener() {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                // If a CellGUI is not selected, do nothing.
                if(selected == null) return;

                char key = e.getKeyChar();
                Cell c = selected.cell;

                // If the Cell is an initial value read from the starting file
                // then skip it.
                if(c.isInitValue()) return;
                
                // Backspace support for removing values.
                if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    selected.removeValue();
                    selected.repaint();
                    selected.revalidate();
                    return;
                }

                // Only allow digits to be entered.
                if(!Character.isDigit(key)) return;

                // Enter the value from the keyboard.
                if(!selected.isInNotesMode()) {

                    // Set the value of the cell and check if it is correct.
                    if(s.getAutoCheckValues()) {
                        int row = c.getRow();
                        int col = c.getCol();
                        int expected = solvedGrid[row][col].getValue();
                        selected.setValue(Character.getNumericValue(key), expected);

                    // Set the value of the cell.
                    } else selected.setValue(Character.getNumericValue(key));

                // Add the possible value to the cell.
                } else selected.addPossibleValue(Character.getNumericValue(key));

                selected.repaint();
                selected.revalidate();
            }
        };
    }
}