package gui;

import javax.swing.JPanel;

import gui.backend.Settings;
import gui.backend.Theme;

/**
 * The Root class represents the root JPanel for the Sudoku app.
 * 
 * It contains all other GUI components, and is the main JPanel for the app.
 * To create a new Root object, a Settings object must be passed in for
 * styling purposes.
 */
public class Root extends JPanel {
    private Theme theme;
    
    /**
     * Create a new Root object with the default styling from settings.
     * 
     * @param s
     */
    public Root(Settings settings) {
        super();
        this.theme = settings.getTheme();
        style();
    }

    /**
     * Set up the Root Panel with the appropriate styling.
     */
    private void style() {
        setBackground(theme.getPrimaryBackground());
        setForeground(theme.getPrimaryText());
    }
}
