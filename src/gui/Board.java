package gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

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
    }

    /**
     * Set up the Board Panel with the appropriate styling.
     */
    private void style() {
        // TODO: Style the Board Panel.
    }

    private void createBoard() {
        // TODO: Create the board with the given grid.
    }
}
