package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import gui.backend.Cell;
import gui.backend.Settings;
import gui.backend.Theme;

/**
 * GUI representation of a single cell in the Sudoku grid.
 */
class CellGUI extends JPanel {
    // GUI fields
    private JPanel internalPanel = new JPanel(new BorderLayout());
        private GridLayout noteLayout = new GridLayout(3, 3);
        private GridLayout valueLayout = new GridLayout(0, 1);
        private Dimension size;
    private JLabel valueLabel;
    private Note[] notesLabels = new Note[9];
    private Theme theme;
    private Font font;
    private boolean incorrect;

    // Data fields
    Cell cell;
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
    public CellGUI(Cell cell, Settings s) {
        super();
        this.cell = cell;
        
        // Set the initial state of the cell based off the settings.
        noteMode = s.getCellGUIStartMode();
        theme = s.getTheme();
        font = s.getFont();
        selected = false;
        size = s.getCellDimensions();

        setFocusable(true);
        setLayout(valueLayout);
        valueLabel = new Note("", theme, font);
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
     * Get the size of the cell.
     */
    public Dimension getSize() {
        return size;
    }

    /**
     * Set the size of the cell.
     */
    public void setSize(Dimension size) {
        this.size = size;
        super.setSize(size);
        internalPanel.setSize(size);
        valueLabel.setSize(size);
        for(int i = 0; i < 9; i++)
            if(notesLabels[i] != null) notesLabels[i].setSize(size);
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
        internalPanel.repaint();
        internalPanel.revalidate();
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
        }

        internalPanel.repaint();
        internalPanel.revalidate();
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

        internalPanel.repaint();
        internalPanel.revalidate();
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
        
        internalPanel.repaint();
        internalPanel.revalidate();
        repaint();
        revalidate();

        noteMode = !noteMode;
    }

    /**
     * Select the cell, and style it for error, highlight, or default.
     */
    public void select() {
        selected = !selected;
        if(incorrect) {
            errorStyle();
            repaint();
            revalidate();
            return;
        }

        if(!selected)
            defaultStyle();

        else highlightedStyle();
        
        repaint();
        revalidate();
    }

    /**
     * Style the cell with the appropriate colors from the theme.
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
    }

    /**
     * Style the cell with the appropriate colors from the theme.
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
    }

    /**
     * Style the cell with the appropriate colors from the theme.
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
    }

    /**
     * Create the GUI components for the cell's notes.
     * 
     * @param autoFill
     */
    private void generateNotes(boolean autoFill) {
        int[] possibleValues = cell.getPossibleValues();
        if(possibleValues.length == 0) {
            for(int i = 0; i < 9; i++) {
                notesLabels[i] = new Note("", theme, font);
                if(autoFill) internalPanel.add(notesLabels[i]);
            }
            return;
        }

        int index = 0;
        // Add the noted possible values to the cell.
        for(int i = 1; i <= 9 && index < possibleValues.length; i++) {
            if(possibleValues[index] == i) {
                notesLabels[i - 1] = new Note(Integer.toString(i), theme, font);
                index++;
            } else
                notesLabels[i - 1] = new Note("", theme, font);
            if(autoFill) internalPanel.add(notesLabels[i - 1]);
        }
    }
}