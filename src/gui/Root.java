package gui;

import java.awt.LayoutManager;
import javax.swing.JPanel;

import gui.backend.Settings;

/**
 * The Root class represents the root JPanel for the Sudoku app.
 * 
 * It contains all other GUI components, and is the main JPanel for the app.
 * To create a new Root object, a Settings object must be passed in for
 * styling purposes.
 */
public class Root extends JPanel {
    private Settings s;
    
    /**
     * Create a new Root object with the default styling from settings.
     * 
     * @param s
     */
    public Root(Settings s) {
        super();

        this.s = s;
        style();
    }

    /**
     * Set up the Root Panel with the appropriate styling.
     */
    private void style() {
        // TODO: Style the Root Panel.
    }
}
