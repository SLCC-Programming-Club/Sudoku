package gui;

import java.awt.Font;

import javax.swing.JComboBox;

import gui.backend.Settings;
import gui.backend.Theme;

/**
 * A custom JComboBox with the appropriate styling for the Sudoku app.
 */
public class ComboBox<E> extends JComboBox<E> {
    private Theme theme;
    private Font font;
    
    /**
     * Create a new ComboBox with the default styling and without initial items.
     */
    public ComboBox() {
        super();
    }

    public ComboBox(Settings s) {
        super();
        theme = s.getTheme();
        font = s.getFont();
        style();
    }

    /**
     * Set up the ComboBox with the appropriate styling.
     */
    private void style() {
        setFont(font);
        setBackground(theme.getPrimaryBackground());
        setForeground(theme.getPrimaryText());
    }
}
