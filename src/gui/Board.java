package gui;

import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.JLabel;

import gui.backend.Settings;

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
    private Cell selected;

    /**
     * Create a new Board object with the default styling from settings.
     * 
     * @param s
     */
    public Board(Settings s, Cell[][] grid) {
        super(new GridLayout(9, 9));
        this.s = s;
        this.grid = grid;
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
        removeAll();
        createBoard();
        repaint();
        revalidate();
    }

    /**
     * Set up the Board Panel with the appropriate styling.
     */
    private void style() {
        // TODO: Style the Board Panel.
    }

    private void createBoard() {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                CellGUI cell = new CellGUI(grid[i][j], s.getCellGUIStartMode());
                add(cell);
            }
        }
    }

    /**
     * GUI representation of a single cell in the Sudoku grid.
     */
    private class CellGUI extends JPanel {
        private static GridLayout noteLayout = new GridLayout(3, 3);
        private static GridLayout valueLayout = new GridLayout(1, 1);

        private Cell cell;
        private JLabel valueLabel;
        private JLabel[] notesLabels;
        private boolean notes; // true for notes, false for value
        private boolean startInNotesOrValueMode;

        /**
         * Create a new CellGUI object with the given single cell.
         * 
         * Start blank cells in notes mode if startInNotesOrValueMode is true.
         * 
         * @param cell
         * @param startInNotesOrValueMode
         */
        public CellGUI(Cell cell, boolean startInNotesOrValueMode) {
            super();
            this.cell = cell;
            this.startInNotesOrValueMode = startInNotesOrValueMode;

            // Populate the cell with the appropriate value or notes.
            if(cell.getValue() == 0) {
                generateNotes();
                
                if(startInNotesOrValueMode) {
                    setLayout(noteLayout);
                    notes = true;
                } else {
                    setLayout(valueLayout);
                    notes = false;
                    valueLabel = new JLabel("");
                    add(valueLabel);
                }
            } else {
                valueLabel = new JLabel(Integer.toString(cell.getValue()));
                add(valueLabel);
            }
        }

        /**
         * Get the value of the underlying Cell object.
         * 
         * @return int
         */
        public int getValue() {
            return cell.getValue();
        }

        /**
         * Add a possible value to the cell.
         * 
         * @param value
         */
        public void addPossibleValue(int value) {
            cell.addPossibleValue(value);
        }

        /**
         * Remove a possible value from the cell.
         * 
         * @return
         */
        public void removePossibleValue(int value) {
            cell.removePossibleValue(value);
        }

        /**
         * Get the possible values of the underlying Cell object.
         * 
         * @return int[]
         */
        public int[] getPossibleValues() {
            return cell.getPossibleValues();
        }

        /**
         * Toggle the mode of the cell between value and note, including the
         * layout of the cell.
         */
        public void setNoteMode() {
            if(notes) {
                removeAll();
                setLayout(valueLayout);
                repaint();
                revalidate();

                add(valueLabel);
            } else {
                removeAll();
                setLayout(noteLayout);
                repaint();
                revalidate();

                generateNotes();
            }
            
            notes = !notes;
        }

        /**
         * Create the GUI components for the cell.
         */
        private void generateNotes() {
            if(cell.getPossibleValues().length == 0) {
                notesLabels = new JLabel[9];
                return;
            }

            // Add the noted possible values to the cell.
            int index = 0;
            for(int i = 1; i <= 9; i++) {
                if(cell.getPossibleValues()[index] == i) {
                    notesLabels[index] = new JLabel(Integer.toString(i));
                    index++;
                } else {
                    notesLabels[index] = new JLabel("");
                }
                add(notesLabels[index]);
            }
        }
    }
}