package gui;

import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;

import gui.backend.Settings;
import gui.backend.SudokuChecker;
import gui.backend.Theme;

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
    private SudokuChecker sc;
    private Cell selected;

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
        setBackground(s.getTheme().getPrimaryBackground());
        setForeground(s.getTheme().getPrimaryText());
    }

    /**
     * Create the Board Panel with the appropriate cells.
     */
    private void createBoard() {
        if(s.getAutoFillNotes()) grid = sc.getPossibleValues(grid);

        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                CellGUI cell = new CellGUI(grid[i][j], s.getCellGUIStartMode() || s.getAutoFillNotes(), s.getTheme());
                add(cell);
            }
        }
    }

    /**
     * GUI representation of a single cell in the Sudoku grid.
     */
    private class CellGUI extends JPanel {
        private GridLayout noteLayout = new GridLayout(3, 3);
        private GridLayout valueLayout = new GridLayout(1, 1);
        private Theme theme;

        private Cell cell;
        private JLabel valueLabel;
        private Note[] notesLabels = new Note[9];
        private boolean notes; // true for notes, false for value
        private boolean cellGUIStartMode;

        /**
         * Create a new CellGUI object with the given single cell.
         * 
         * Start blank cells in notes mode if startInNotesOrValueMode is true.
         * 
         * @param cell
         * @param startInNotesOrValueMode
         */
        public CellGUI(Cell cell, boolean notes, Theme theme) {
            super();
            this.cell = cell;
            this.notes = notes;
            this.theme = theme;
            style();

            // Populate the cell with the appropriate value or notes.
            if(cell.getValue() == 0) {
                notes = !notes;
                valueLabel = new JLabel("");
                setNoteMode();
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
         * Set the value of the underlying Cell object.
         * 
         * @param value
         */
        public void setValue(int value) {
            cell.setValue(value);
            valueLabel.setText(Integer.toString(value));
        }

        /**
         * Set the possible values of the underlying Cell object.
         * 
         * @param value
         */
        public void setPossibleValues(int[] values) {
            cell.setPossibleValues(values);
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
            removeAll();
            if(notes) {
                setLayout(valueLayout);

                add(valueLabel);
            } else {
                setLayout(noteLayout);

                generateNotes();
                for(int i = 0; i < 9; i++)
                    add(notesLabels[i]);
            }
            
            repaint();
            revalidate();
            notes = !notes;
        }

        /**
         * Style the cell with the appropriate colors from the theme.
         */
        private void style() {
            setBackground(theme.getPrimaryBackground());
            setForeground(theme.getPrimaryText());
            setBorder(new LineBorder(theme.getPrimaryBorder(), 2));
        }

        /**
         * Create the GUI components for the cell.
         */
        private void generateNotes() {
            if(cell.getPossibleValues().length == 0) {
                for(int i = 0; i < 9; i++) {
                    notesLabels[i] = new Note("", theme);
                    add(notesLabels[i]);
                }
                return;
            }


            // Add the noted possible values to the cell.
            for(int i = 1; i <= 9; i++) {
                if(cell.getPossibleValues()[i - 1] == i)
                    notesLabels[i - 1] = new Note(Integer.toString(i), theme);
                else
                    notesLabels[i - 1] = new Note("", theme);

                add(notesLabels[i - 1]);
            }
        }
    }
}