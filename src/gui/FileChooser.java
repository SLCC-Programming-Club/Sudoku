package gui;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;

/**
 * A custom FileChooser with the appropriate styling for the Sudoku app.
 * Additionally, this FileChooser only allows .sdku files to be selected.
 */
public class FileChooser extends JFileChooser {
    private Settings s;
    
    /**
     * Create a new FileChooser with the default directory set to the user's
     * Sudoku directory and no special styling.
     * 
     * @param defaultDirectory
     */
    public FileChooser(File defaultDirectory) {
        super(defaultDirectory);
        for (File f: defaultDirectory.listFiles()) {
            ensureFileIsVisible(f);
        }

        style();
        setupSettings();
    }
    
    /**
     * Create a new FileChooser with the default directory set to the user's
     * Sudoku directory.
     * 
     * @param defaultDirectory
     * @param s
     */
    public FileChooser(File defaultDirectory, Settings s) {
        super(defaultDirectory);
        this.s = s;

        for (File f: defaultDirectory.listFiles()) {
            ensureFileIsVisible(f);
        }

        style();
        setupSettings();
    }

    /**
     * Set up the FileChooser with the appropriate styling.
     */
    private void style() {
        // TODO: Style the FileChooser.
        setFont(s.getFont());
    }

    /**
     * Set up the FileChooser with the appropriate settings.
     */
    private void setupSettings() {
        setFileHidingEnabled(false);
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        setFileFilter(new Filter());
    }

    /**
     * A custom FileFilter to only allow .sdku files to be selected.
     */
    private class Filter extends FileFilter {
        public boolean accept(File f) {
            String name = f.getName().toLowerCase();
            if(name.length() > 5) {
                return name.substring(name.length() - 5).equals(".sdku") || !f.isDirectory();
            }
            return false;
        }

        public String getDescription() {
            return "Sudoku Puzzle Files (*.sdku)";
        }
    }
}
