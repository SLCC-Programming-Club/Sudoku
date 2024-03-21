package gui;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;

import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.awt.FontFormatException;

/**
 * A class solely to store user settings relating to default behaviors,
 * difficulty, filepaths, and more.
 *
 * These user settings are stored in a {TBD} file located at {TBD}.
 */
public class Settings {
    private final String os;

    private int newFileProperties;
    private String defaultDirectory;
    private Font font;

    /**
     * Create a new Settings object, initializing the default settings
     * or reading in the settings from settings file if it exists.
     * 
     * While the defaultDirectory can be modified, it will not change where
     * the settings file is located. The settings file is always located in
     * either "~/.config/sudoku/" or "%APPDATA%/Local/Sudoku/", depending
     * on the user's operating system.
     */
    public Settings() {
        os = System.getProperty("os.name").toLowerCase();
        if(os.startsWith("windows"))
            defaultDirectory = System.getProperty("user.home") + "\\AppData\\Local\\Sudoku\\";
        else
            defaultDirectory = System.getProperty("user.home") + "/.config/sudoku/";

        File dir = new File(defaultDirectory);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();

            defaultSettings();
        }
        readSettings();
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
     * Set the new file properties and write them to the settings file.
     * 
     * @param newFileProperties
     */
    public void setNewFileProperties(int newFileProperties) {
        this.newFileProperties = newFileProperties;
        updateSettingsFile();
    }

    /**
     * The Default Directory is a configurable setting, where the user can
     * specify a default directory to store .sdku files. This is useful for
     * eliminating the need for user input for file operations.
     *
     * NOTE: This does not change the location that the settings file may
     * be located in. At this time, the settings file is always located in
     * either "~/.config/sudoku/" or "%APPDATA%/Local/Sudoku/", depending
     * on the user's operating system.
     * 
     * @return String
     */
    public String getDefaultDirectory() {
        return defaultDirectory;
    }

    /**
     * Set the default directory and write it to the settings file.
     * 
     * @param defaultDirectory
     */
    public void setDefaultDirectory(String defaultDirectory) {
        this.defaultDirectory = defaultDirectory;
        updateSettingsFile();
    }

    /**
     * The Font is a configurable setting, where the user can specify a
     * font to be used throughout the application.
     * 
     * Fonts are loaded from the system directory. In Windows, the font
     * is loaded from C:\Windows\Fonts\. In MacOS, the font is loaded 
     * from /Library/Fonts/. In Linux, the font is loaded from 
     * /usr/share/fonts/.
     * 
     * @return Font
     */
    public Font getFont() {
        return font;
    }

    /**
     * Set the font and write it to the settings file.
     * 
     * @param font
     */
    public void setFont(Font font) {
        this.font = font;
        updateSettingsFile();
    }

    /**
     * Open and write the settings to the settings file.
     * 
     * This method is called whenever a setting is updated.
     */
     private void updateSettingsFile() {
        // TODO: Write the settings to the settings file.
     }

    /**
     * Set the default settings for the application and write them to the
     * settings file.
     */
    private void defaultSettings() {
        newFileProperties = 0;

        // Load the font from the system directory.
        if(loadFont("HackNerdFont-Regular") == 0)
            font = new Font("Hack Nerd Font", Font.PLAIN, 12);

        // If the font does not exist, use Arial as the default font.
        else 
            font = new Font("Arial", Font.PLAIN, 12);

        updateSettingsFile();
    }

    /**
     * Read the settings from the settings file.
     * 
     * If the settings file does not exist, the default settings will be used
     * to populate the Settings file.
     */
     private void readSettings() {
        File settingsFile = new File(defaultDirectory + "/settings");
        if (!settingsFile.exists()) {
            defaultSettings();
            return;
        }

        // TODO: Read the settings from the settings file.
     }

    /**
     * Load the font from the resources folder in the user's system directory.
     * 
     * @return int 0 if successful, 1 if the file does not exist.
     */
    private int loadFont(String fontName) {
        // Load the font from the system directory.
        String fontFilepath;
        if(os.startsWith("windows"))
            fontFilepath = System.getProperty("user.home") + "\\AppData\\Local\\Microsoft\\Windows\\Fonts\\" + fontName + ".ttf";
        else if(os.startsWith("mac"))
            fontFilepath = "/Library/Fonts/" + fontName + ".ttf";
        else
            fontFilepath = "/usr/share/fonts/" + fontName + ".ttf";

        // Register the font with the GraphicsEnvironment.
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(fontFilepath)));
            return 0;

        } catch(IOException | FontFormatException e) {
            System.out.println("Filepath of font not found: " + fontFilepath + " does not exist.");
            return 1;
        }
    }
}
