package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.Box;
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
    private CellGUI[][] gridGUI;
    private Box[][] boxGUI = new Box[3][3];
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
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        // If a cell is already selected, deselect it and
                        // the row and column.
                        if(selected != null) {
                            int boxRow = selected.cell.getRow() / 3;
                            int boxCol = selected.cell.getCol() / 3;

                            for(int n = 0; n < 9; n++) {
                                int row = selected.cell.getRow();
                                int col = selected.cell.getCol();

                                if(
                                    (row == selected.cell.getRow() && n == selected.cell.getCol()) ||
                                    (col == selected.cell.getCol() && n == selected.cell.getRow())
                                ) continue;

                                gridGUI[n][col].select();
                                gridGUI[row][n].select();
                            }

                            for(int n = 0; n < 3; n++) {
                                for(int m = 0; m < 3; m++) {
                                    int row = boxRow * 3 + n;
                                    int col = boxCol * 3 + m;
                                    if(row == selected.cell.getRow() || col == selected.cell.getCol()) continue;

                                    gridGUI[row][col].select();
                                }
                            }
                        }

                        // Graphically highlight the row and column
                        // of the selected cell.
                        for(int n = 0; n < 9; n++) {
                            gridGUI[n][cell.cell.getCol()].select();
                            gridGUI[cell.cell.getRow()][n].select();
                        }

                        selected = cell;
                        int boxRow = selected.cell.getRow() / 3;
                        int boxCol = selected.cell.getCol() / 3;
                        for(int n = 0; n < 3; n++) {
                            for(int m = 0; m < 3; m++) {
                                int row = boxRow * 3 + n;
                                int col = boxCol * 3 + m;
                                if(row == selected.cell.getRow() || col == selected.cell.getCol()) continue;

                                if(
                                    (row == selected.cell.getRow() && n == selected.cell.getCol()) ||
                                    (col == selected.cell.getCol() && n == selected.cell.getRow())
                                ) continue;

                                gridGUI[row][col].select();
                            }
                        }
                    }
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent e) {}
                    @Override
                    public void mouseReleased(java.awt.event.MouseEvent e) {}
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {}
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {}
                
                });
                gridGUI[i][j] = cell;
                add(gridGUI[i][j]);
            }
        }
    }

    /**
     * GUI representation of a single cell in the Sudoku grid.
     */
    private class CellGUI extends JPanel {
        // GUI fields
        private JPanel internalPanel = new JPanel(new BorderLayout());
            private GridLayout noteLayout = new GridLayout(3, 3);
            private GridLayout valueLayout = new GridLayout(0, 1);
        private JLabel valueLabel;
        private Note[] notesLabels = new Note[9];
        private Theme theme;

        // Data fields
        private Cell cell;
        private boolean noteMode; // true to display notes, or display value
        private boolean selected = false; // true if the cell is selected

        /**
         * Create a new CellGUI object with the given single cell.
         * 
         * Start blank cells in notes mode if startInNotesOrValueMode is true.
         * 
         * @param cell
         * @param startInNotesOrValueMode
         */
        public CellGUI(Cell cell, boolean noteMode, Theme theme) {
            super();
            this.cell = cell;
            this.noteMode = !noteMode; // Flip for use with setNoteMode()
            this.theme = theme;
            selected = false;
            setLayout(valueLayout);
            style();

            // Populate the cell with the appropriate value or notes.
            if(cell.getValue() == 0) {
                valueLabel = new JLabel("");
                valueLabel.setForeground(theme.getPrimaryText());
                internalPanel.setLayout(noteLayout);
                generateNotes();
            } else {
                valueLabel = new JLabel(
                    Integer.toString(cell.getValue()),
                    SwingConstants.CENTER
                );
                valueLabel.setForeground(theme.getPrimaryText());
                internalPanel.setLayout(valueLayout);
                internalPanel.add(valueLabel);
            }
            add(internalPanel);
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
            // Update the GUI with the actual Cell value (unchanged if invalid)
            valueLabel.setText(Integer.toString(cell.getValue()));
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
         * 
         * If the current mode is value, switch to note mode, and vice versa.
         */
        public void setNoteMode() {
            if(cell.isInitValue()) return;

            internalPanel.removeAll();
            if(noteMode) {
                internalPanel.setLayout(valueLayout);
                internalPanel.add(valueLabel);
            } else {
                internalPanel.setLayout(noteLayout);
                generateNotes();
            }
            
            internalPanel.repaint();
            internalPanel.revalidate();
            repaint();
            revalidate();

            noteMode = !noteMode;
        }

        public void select() {
            selected = !selected;
            
            if(!selected) {
                setBackground(theme.getPrimaryBackground());
                setForeground(theme.getPrimaryText());
                internalPanel.setBackground(theme.getPrimaryBackground());
                internalPanel.setForeground(theme.getPrimaryText());
                valueLabel.setForeground(theme.getPrimaryText());
                setBorder(new LineBorder(theme.getPrimaryBorder(), 2));
            } else {
                setBackground(theme.getSecondaryBackground());
                setForeground(theme.getSecondaryText());
                internalPanel.setBackground(theme.getSecondaryBackground());
                internalPanel.setForeground(theme.getSecondaryText());
                valueLabel.setForeground(theme.getPrimaryText());
                setBorder(new LineBorder(theme.getSecondaryBorder(), 2));
            }
            
            repaint();
            revalidate();
        }

        /**
         * Style the cell with the appropriate colors from the theme.
         */
        private void style() {
            setBackground(theme.getPrimaryBackground());
            setForeground(theme.getPrimaryText());
            internalPanel.setBackground(theme.getPrimaryBackground());
            internalPanel.setForeground(theme.getPrimaryText());
            setBorder(new LineBorder(theme.getPrimaryBorder(), 2));
        }

        /**
         * Create the GUI components for the cell.
         */
        private void generateNotes() {
            int[] possibleValues = cell.getPossibleValues();
            if(possibleValues.length == 0) {
                for(int i = 0; i < 9; i++) {
                    notesLabels[i] = new Note("", theme);
                    internalPanel.add(notesLabels[i]);
                }
                return;
            }

            int index = 0;
            // Add the noted possible values to the cell.
            for(int i = 1; i <= 9 && index < possibleValues.length; i++) {
                if(possibleValues[index] == i) {
                    notesLabels[i - 1] = new Note(Integer.toString(i), theme);
                    index++;
                } else
                    notesLabels[i - 1] = new Note("", theme);
                internalPanel.add(notesLabels[i - 1]);
            }
        }
    }
}