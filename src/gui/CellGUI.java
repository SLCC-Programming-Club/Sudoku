package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import gui.backend.Cell;
import gui.backend.Theme;

/**
 * GUI representation of a single cell in the Sudoku grid.
 */
class CellGUI extends JPanel {
    // GUI fields
    private JPanel internalPanel = new JPanel(new BorderLayout());
        private GridLayout noteLayout = new GridLayout(3, 3);
        private GridLayout valueLayout = new GridLayout(0, 1);
    private JLabel valueLabel;
    private Note[] notesLabels = new Note[9];
    private Theme theme;

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
    public CellGUI(Cell cell, boolean noteMode, Theme theme) {
        super();
        this.cell = cell;
        this.noteMode = !noteMode; // Flip for use with setNoteMode()
        this.theme = theme;
        selected = false;
        setFocusable(true);
        setLayout(valueLayout);
        valueLabel = new JLabel("", SwingConstants.CENTER);
        style();

        // Populate the cell with the appropriate value or notes.
        if(cell.getValue() == 0) {
            internalPanel.setLayout(noteLayout);
            generateNotes();
        } else {
            valueLabel.setText(Integer.toString(cell.getValue()));
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
        internalPanel.repaint();
        internalPanel.revalidate();
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
        setNoteMode();
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
            style();
        } else {
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