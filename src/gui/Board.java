package gui;

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

    /**
     * Create a new Board object with the default styling from settings.
     * 
     * @param s
     */
    public Board(Settings s) {
        super();
        this.s = s;
        style();
    }

    /**
     * Set up the Board Panel with the appropriate styling.
     */
    private void style() {
        // TODO: Style the Board Panel.
    }
}
