import java.util.Scanner;
import java.io.File;

/**
 * A class solely to store user settings relating to default behaviors,
 * difficulty, filepaths, and more.
 *
 * These user settings are stored in a {TBD} file located at {TBD}.
 */
public class Settings {
    private int newFileProperties;
    private String defaultDirectory;

    /**
     * Create a new Settings object, initializing the default settings
     * or reading in the settings from a file if it exists.
     */
    public Settings() {
        // TODO: Read in a file from the master default directory and populate
        // settings, effectively toggles. If the file does not exist,
        // populate it with the following values (possibly in .json).
        newFileProperties = 0;
        defaultDirectory = "~/AppData/Local/sudoku/";
    }

    /**
     * New File Properties is a configurable setting, where the following
     * values are allowable:
     *      0 - Load the last opened .sdku file, default behavior
     *      1 - Create a new, blank .sdku file
     *      2 - Create a new, random .sdku file
     *
     * @return int
     */
    public int getNewFileProperties() {
        return newFileProperties;
    }

    /**
     * The Default Directory is a configurable setting, where the user can
     * specify a default directory to store .sdku files. This is useful for
     * eliminating the need for user input for file operations.
     *
     * By default, the value will be "~/.config/sudoku/" on MacOS and Linux.
     * On Windows, the value will be "~/AppData/Local/sudoku/".
     *
     * NOTE: This does not change the location that the settings file may
     * be located in. This must be located at {TBD}.
     *
     * @return String
     */
    public String getDefaultDirectory() {
        return defaultDirectory;
    }
}
