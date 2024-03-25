package gui;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import gui.backend.Theme;

/**
 * The Note class represents a single note of a cell in the Sudoku board.
 * 
 * This is the GUI abstraction for the underlying Cell object, and is used to
 * display and manage the note for a cell.
 */
public class Note extends JLabel {
    private Theme t;
    private Font f;
    
    /**
     * Create a new Note object with the given text and theme.
     * 
     * @param text
     * @param t
     */
    public Note(String text, Theme t, Font f) {
        super(text, SwingConstants.CENTER);
        this.t = t;
        this.f = f;
        style();
    }

    /**
     * Style the Note component with the current theme.
     */
    private void style() {
        setBackground(t.getPrimaryBackground());
        setForeground(t.getPrimaryText());
        setFont(f);
    }
}
