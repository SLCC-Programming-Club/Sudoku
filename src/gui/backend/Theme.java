package gui.backend;

import java.awt.Color;
import java.io.File;

/**
 * The Theme class represents the color scheme for the GUI components.
 * 
 * The Theme class reads a theme file and sets the colors for the GUI components.
 * 
 * The Theme class provides the following color customizations:
 *      - primaryBackground
 *      - secondaryBackground
 *      - primaryText
 *      - secondaryText
 *      - primaryHighlight
 *      - secondaryHighlight
 *      - primaryBorder
 *      - secondaryBorder
 *      - primaryButton
 *      - secondaryButton
 *      - primaryButtonHighlight
 *      - secondaryButtonHighlight
 *      - primaryButtonBorder
 *      - secondaryButtonBorder
 */
import java.awt.Color;
import java.io.File;

/**
 * The Theme class represents a theme for the GUI components.
 * It provides colors for various GUI elements.
 */
public class Theme {
    private Color primaryBackground;
    private Color secondaryBackground;
    private Color primaryText;
    private Color secondaryText;
    private Color primaryHighlight;
    private Color secondaryHighlight;
    private Color primaryBorder;
    private Color secondaryBorder;
    private Color primaryButton;
    private Color secondaryButton;
    private Color primaryButtonHighlight;
    private Color secondaryButtonHighlight;
    private Color primaryButtonBorder;
    private Color secondaryButtonBorder;

    /**
     * Constructs a new Theme object with the given theme file.
     * This object provides the colors for the GUI components.
     *
     * @param theme the theme file
     */
    public Theme(File theme) {
        this.theme = theme;
        readTheme();
    }

    /**
     * Reads the .theme file and sets the colors for the GUI components.
     * Once complete, sets the theme for the GUI components.
     */
    private void readTheme() {
        // TODO: Read the .theme file
        setTheme();
    }

    /**
     * Sets the theme for the GUI components.
     */
    private void setTheme() {
        primaryBackground = Color.decode("#4A245E");
        secondaryBackground = Color.decode("#DADFF7");
        primaryText = Color.decode("#DBABF7");
        secondaryText = Color.decode("#4A245E");
        primaryBorder = Color.decode("#A76BCA");
        secondaryBorder = Color.decode("#DBABF7");
    }

    /**
     * Gets the primary background color.
     *
     * @return the primary background color
     */
    public Color getPrimaryBackground() {
        return primaryBackground;
    }

    /**
     * Sets the primary background color.
     *
     * @param primaryBackground the primary background color
     */
    public void setPrimaryBackground(Color primaryBackground) {
        this.primaryBackground = primaryBackground;
    }

    /**
     * Gets the secondary background color.
     *
     * @return the secondary background color
     */
    public Color getSecondaryBackground() {
        return secondaryBackground;
    }

    /**
     * Sets the secondary background color.
     *
     * @param secondaryBackground the secondary background color
     */
    public void setSecondaryBackground(Color secondaryBackground) {
        this.secondaryBackground = secondaryBackground;
    }

    /**
     * Gets the primary text color.
     *
     * @return the primary text color
     */
    public Color getPrimaryText() {
        return primaryText;
    }

    /**
     * Sets the primary text color.
     *
     * @param primaryText the primary text color
     */
    public void setPrimaryText(Color primaryText) {
        this.primaryText = primaryText;
    }

    /**
     * Gets the secondary text color.
     *
     * @return the secondary text color
     */
    public Color getSecondaryText() {
        return secondaryText;
    }

    /**
     * Sets the secondary text color.
     *
     * @param secondaryText the secondary text color
     */
    public void setSecondaryText(Color secondaryText) {
        this.secondaryText = secondaryText;
    }

    /**
     * Gets the primary highlight color.
     *
     * @return the primary highlight color
     */
    public Color getPrimaryHighlight() {
        return primaryHighlight;
    }

    /**
     * Sets the primary highlight color.
     *
     * @param primaryHighlight the primary highlight color
     */
    public void setPrimaryHighlight(Color primaryHighlight) {
        this.primaryHighlight = primaryHighlight;
    }

    /**
     * Gets the secondary highlight color.
     *
     * @return the secondary highlight color
     */
    public Color getSecondaryHighlight() {
        return secondaryHighlight;
    }

    /**
     * Sets the secondary highlight color.
     *
     * @param secondaryHighlight the secondary highlight color
     */
    public void setSecondaryHighlight(Color secondaryHighlight) {
        this.secondaryHighlight = secondaryHighlight;
    }

    /**
     * Gets the primary border color.
     *
     * @return the primary border color
     */
    public Color getPrimaryBorder() {
        return primaryBorder;
    }

    /**
     * Sets the primary border color.
     *
     * @param primaryBorder the primary border color
     */
    public void setPrimaryBorder(Color primaryBorder) {
        this.primaryBorder = primaryBorder;
    }

    /**
     * Gets the secondary border color.
     *
     * @return the secondary border color
     */
    public Color getSecondaryBorder() {
        return secondaryBorder;
    }

    /**
     * Sets the secondary border color.
     *
     * @param secondaryBorder the secondary border color
     */
    public void setSecondaryBorder(Color secondaryBorder) {
        this.secondaryBorder = secondaryBorder;
    }

    /**
     * Gets the primary button color.
     *
     * @return the primary button color
     */
    public Color getPrimaryButton() {
        return primaryButton;
    }

    /**
     * Sets the primary button color.
     *
     * @param primaryButton the primary button color
     */
    public void setPrimaryButton(Color primaryButton) {
        this.primaryButton = primaryButton;
    }

    /**
     * Gets the secondary button color.
     *
     * @return the secondary button color
     */
    public Color getSecondaryButton() {
        return secondaryButton;
    }

    /**
     * Sets the secondary button color.
     *
     * @param secondaryButton the secondary button color
     */
    public void setSecondaryButton(Color secondaryButton) {
        this.secondaryButton = secondaryButton;
    }

    /**
     * Gets the primary button highlight color.
     *
     * @return the primary button highlight color
     */
    public Color getPrimaryButtonHighlight() {
        return primaryButtonHighlight;
    }

    /**
     * Sets the primary button highlight color.
     *
     * @param primaryButtonHighlight the primary button highlight color
     */
    public void setPrimaryButtonHighlight(Color primaryButtonHighlight) {
        this.primaryButtonHighlight = primaryButtonHighlight;
    }

    /**
     * Gets the secondary button highlight color.
     *
     * @return the secondary button highlight color
     */
    public Color getSecondaryButtonHighlight() {
        return secondaryButtonHighlight;
    }

    /**
     * Sets the secondary button highlight color.
     *
     * @param secondaryButtonHighlight the secondary button highlight color
     */
    public void setSecondaryButtonHighlight(Color secondaryButtonHighlight) {
        this.secondaryButtonHighlight = secondaryButtonHighlight;
    }

    /**
     * Gets the primary button border color.
     *
     * @return the primary button border color
     */
    public Color getPrimaryButtonBorder() {
        return primaryButtonBorder;
    }

    /**
     * Sets the primary button border color.
     *
     * @param primaryButtonBorder the primary button border color
     */
    public void setPrimaryButtonBorder(Color primaryButtonBorder) {
        this.primaryButtonBorder = primaryButtonBorder;
    }

    /**
     * Gets the secondary button border color.
     *
     * @return the secondary button border color
     */
    public Color getSecondaryButtonBorder() {
        return secondaryButtonBorder;
    }

    /**
     * Sets the secondary button border color.
     *
     * @param secondaryButtonBorder the secondary button border color
     */
    public void setSecondaryButtonBorder(Color secondaryButtonBorder) {
        this.secondaryButtonBorder = secondaryButtonBorder;
    }

    private File theme;
}
