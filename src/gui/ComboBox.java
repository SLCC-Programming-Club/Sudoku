package gui;

import javax.swing.JComboBox;

/**
 * A custom JComboBox with the appropriate styling for the Sudoku app.
 */
public class ComboBox<E> extends JComboBox<E> {
    
    /**
     * Create a new ComboBox with the default styling and without initial items.
     */
    public ComboBox() {
        super();
        style();
    }

    /**
     * Set up the ComboBox with the appropriate styling.
     */
    private void style() {
        // TODO: Style the ComboBox.
        System.out.println("TODO: Style the ComboBox.");
    }
}
