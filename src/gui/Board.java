package gui;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.Box;

import gui.backend.Cell;
import gui.backend.Settings;
import gui.backend.SudokuChecker;

/**
 * The Board class represents the Sudoku board in the GUI.
 * 
 * It contains all other related GUI components, and is the main JPanel for
 * the board. Accessible information from the Board includes:
 *      - the current state of the board, including filled values and notes;
 *      - the current difficulty;
 *      - the current time since the puzzle was started;
 *      - the current hints available; and
 *      - the current number of mistakes.
 */
public class Board extends JPanel {
    private Settings s;
    private Cell[][] grid;
    private CellGUI[][] gridGUI;
    private SudokuChecker sc;
    private CellGUI selected;

    /**
     * Create a new Board object with the default styling from settings.
     * 
     * @param s
     */
    public Board(Settings s, Cell[][] grid, SudokuChecker sc) {
        super(new GridLayout(9, 9));
        this.s = s;
        this.grid = grid;
        this.sc = sc;
        style();
        createBoard();
    }

    private KeyListener createKeyListener() {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char key = e.getKeyChar();
                if(!Character.isDigit(key)) return;
                if(selected.cell.isInitValue()) return;

                if(e.getKeyCode() == e.VK_BACK_SPACE) {
                    if(selected.isInNotesMode()) {
                        System.out.println("Remove all notes.");
                        return;
                    } else {
                        selected.setValue(0);
                        return;
                    }
                }

                if(!selected.isInNotesMode())
                    selected.setValue(Character.getNumericValue(key));
            }

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        };
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
     * Select the given cell and highlight all cells in the same row, column,
     * and box.
     * 
     * Intended to be used in conjunction with a MouseListener.
     * 
     * @param cell
     */
    private void select(Cell cell) {
        for(int n = 0; n < 9; n++) {
            int row = cell.getRow();
            int col = cell.getCol();
            boolean colInSameBox = 
                (gridGUI[n][col].cell.getBox() == 
                cell.getBox());

            boolean rowInSameBox = 
                (gridGUI[row][n].cell.getBox() ==
                cell.getBox());

            if(colInSameBox && !rowInSameBox) {
                gridGUI[row][n].select();
                continue;
            } else if(!colInSameBox && rowInSameBox) {
                gridGUI[n][col].select();
                continue;
            } else if(colInSameBox && rowInSameBox) continue;

            gridGUI[n][col].select();
            gridGUI[row][n].select();
        }

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
     * Create the Board Panel with the appropriate cells.
     */
    private void createBoard() {
        if(s.getAutoFillNotes()) grid = sc.getPossibleValues(grid);
        gridGUI = new CellGUI[9][9];

        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                CellGUI cell = new CellGUI(
                    grid[i][j],
                    s.getCellGUIStartMode(),
                    s.getTheme()
                );
                cell.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) { 
                        // Left click
                        if(e.getButton() == 1) {
                            if(selected != null) select(selected.cell);
                            
                            // Deselect the cell if it is already selected.
                            if(selected == cell) {
                                selected = null;
                                return;
                            }

                            select(cell.cell);
                            selected = cell;
                            selected.requestFocusInWindow();
                            selected.addKeyListener(createKeyListener());
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
                
                });

                gridGUI[i][j] = cell;
                add(gridGUI[i][j]);
            }
        }
    }
}