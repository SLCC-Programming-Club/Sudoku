package gui;

// GUI imports
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

// Processing & backend imports
import gui.backend.Cell;
import gui.backend.Settings;
import gui.backend.Theme;

/**
 * GUI representation of a single cell in the Sudoku grid.
 */
class CellGUI extends JPanel {
    // GUI fields
    private GridLayout noteLayout = new GridLayout(3, 3);
    private GridLayout valueLayout = new GridLayout(0, 1);
    private JPanel internalPanel = new JPanel(valueLayout);
        private JLabel valueLabel;
        private Label[] notesLabels = new Label[9];

    // GUI data fields read from a Settings object.
    private Dimension size;
    private Theme theme;
    private Font font;

    // Data fields
    Cell cell;
    private boolean noteMode; // true to display notes, or display value
    private boolean selected = false; // true if the cell is selected
    private boolean incorrect = false; // true if the cell has an invalid value

    /**
     * Create a new CellGUI object with the given single cell.
     * 
     * Start blank cells in notes mode if startInNotesOrValueMode is true.
     * 
     * @param cell
     * @param startInNotesOrValueMode
     */
    public CellGUI(Cell cell, Settings s) {
        super();
        this.cell = cell;
        
        // Set the initial state of the cell based off the settings.
        noteMode = s.getCellGUIStartMode();
        theme = s.getTheme();
        font = s.getFont();
        selected = false;
        size = s.getCellDimensions();

        // Set the layout and size of the cell.
        setFocusable(true);
        setLayout(valueLayout);
        valueLabel = new Label("", theme, font);
        setSize(size);
        internalPanel.setSize(size);
        defaultStyle();

        // Populate the cell with the appropriate value or notes.
        if(cell.getValue() == 0) {
            internalPanel.setLayout(noteLayout);
            generateNotes(s.getAutoFillNotes());
        } else {
            internalPanel.setLayout(valueLayout);
            valueLabel.setText(Integer.toString(cell.getValue()));
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
        if(!selected) return;

        cell.setValue(value);
        // Update the GUI with the actual Cell value (unchanged if invalid)
        valueLabel.setText(Integer.toString(cell.getValue()));
        refresh();
    }

    /**
     * Set the value of the underlying Cell object.
     * 
     * This implementation allows for incorrect values to be highlighted.
     * 
     * @param value
     */
    public void setValue(int value, int expected) {
        if(!selected) return;

        cell.setValue(value);
        valueLabel.setText(Integer.toString(cell.getValue()));

        if(value != expected) {
            incorrect = true;
            errorStyle();
        } else refresh();
    }

    /**
     * Remove the value of the underlying Cell object.
     */
    public void removeValue() {
        if(!selected) return;

        cell.setValue(0);
        if(!cell.isInitValue())
            valueLabel.setText("");

        incorrect = false;
        highlightedStyle();
    }

    /**
     * Add a possible value to the cell.
     * 
     * @param value
     */
    public void addPossibleValue(int value) {
        if(!selected) return;

        cell.addPossibleValue(value);
        internalPanel.removeAll();
        internalPanel.setLayout(noteLayout);
        noteMode = true;
        generateNotes(true);
        highlightedStyle();
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
     * Get the status of the cell in notes mode.
     * 
     * @return
     */
    public boolean isInNotesMode() {
        return noteMode;
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
            noteMode = true;
            generateNotes(true);
        }
        
        refresh();

        noteMode = !noteMode;
    }

    /**
     * Select the cell, and style it for error, highlight, or default.
     */
    public void select() {
        selected = !selected;
        if(incorrect) {
            errorStyle();
            return;
        }

        if(!selected)
            defaultStyle();

        else highlightedStyle();
    }

    /**
     * Style the CellGUI with the primary colors from the Theme.
     * 
     * @see Theme.java
     */
    private void defaultStyle() {
        setBackground(theme.getPrimaryBackground());
        setForeground(theme.getPrimaryText());
        internalPanel.setBackground(theme.getPrimaryBackground());
        internalPanel.setForeground(theme.getPrimaryText());
        valueLabel.setForeground(theme.getPrimaryText());
        setBorder(new LineBorder(theme.getPrimaryBorder(), 2));

        for(int i = 0; i < 9; i++) {
            if(notesLabels[i] != null) {
                notesLabels[i].setBackground(theme.getPrimaryBackground());
                notesLabels[i].setForeground(theme.getPrimaryText());
            }
        }

        refresh();
    }

    /**
     * Style the CellGUI with the secondary colors from the Theme.
     * 
     * @see Theme.java
     */
    private void highlightedStyle() {
        setBackground(theme.getSecondaryBackground());
        setForeground(theme.getSecondaryText());
        internalPanel.setBackground(theme.getSecondaryBackground());
        internalPanel.setForeground(theme.getSecondaryText());
        valueLabel.setForeground(theme.getSecondaryText());
        setBorder(new LineBorder(theme.getSecondaryBorder(), 2));

        for(int i = 0; i < 9; i++) {
            if(notesLabels[i] != null) {
                notesLabels[i].setBackground(theme.getSecondaryBackground());
                notesLabels[i].setForeground(theme.getSecondaryText());
            }
        }
        
        refresh();
    }

    /**
     * Style the CellGUI with the error colors from the Theme.
     * 
     * @see Theme.java
     */
    private void errorStyle() {
        setBackground(theme.getErrorBackground());
        setForeground(theme.getErrorText());
        internalPanel.setBackground(theme.getErrorBackground());
        internalPanel.setForeground(theme.getErrorText());
        valueLabel.setForeground(theme.getErrorText());
        setBorder(new LineBorder(theme.getErrorBorder(), 2));

        for(int i = 0; i < 9; i++) {
            if(notesLabels[i] != null) {
                notesLabels[i].setBackground(theme.getErrorBackground());
                notesLabels[i].setForeground(theme.getErrorText());
            }
        }

        refresh();
    }

    /**
     * Helper method to repaint and revalidate the internal panel and CellGUI.
     */
    private void refresh() {
        internalPanel.repaint();
        internalPanel.revalidate();
        repaint();
        revalidate();
    }

    /**
     * Create the GUI components for the cell's notes.
     * 
     * If autoFill is true, then the CellGUI will automatically switch to
     * noteMode.
     * 
     * @param autoFill
     */
    private void generateNotes(boolean autoFill) {
        int[] possibleValues = cell.getPossibleValues();
        
        // Prepare the internalPanel for noteMode.
        if(autoFill) {
            internalPanel.removeAll();
            noteMode = true;
        }

        // Handle cases where there are no possible values stored.
        if(possibleValues.length == 0) {
            for(int i = 0; i < 9; i++) {
                notesLabels[i] = new Label("", theme, font);
                
                if(autoFill) internalPanel.add(notesLabels[i]);
            }
            return;
        }

        int index = 0;
        // Add the noted possible values to the cell.
        for(int i = 1; i <= 9 && index < possibleValues.length; i++) {
            // Add the possible value if it exists.
            if(possibleValues[index] == i) {
                notesLabels[i - 1] = new Label(Integer.toString(i), theme, font);
                index++;
            
            // Otherwise, add a blank value.
            } else
                notesLabels[i - 1] = new Label("", theme, font);

            if(autoFill) internalPanel.add(notesLabels[i - 1]);
        }
    }
}