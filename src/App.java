import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Runner class for the Sudoku App, managing the GUI and coordinating internal logic.
 */
public class App extends JFrame {
    private Settings settings;

    public App() {
        super("Sudoku");
        settings = new Settings();
        initSetup();
    }

    /**
     * Basic JFrame setup for the GUI.
     */
    private void initSetup() {
        // TODO: These are just some default values, these should use the values from Settings.
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(750, 550));
        setSize(getPreferredSize());
        setResizable(false);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error with cross platform look/feel in frameSetup().");
        }
    }

    private JPanel createApp() {
        // TODO: Create the root JPanel holding all other GUI components.

        return null;
    }

    public static void cli() {
        Nav nav = new Nav(new Settings());
        SudokuChecker check = new SudokuChecker(nav.getLoadedGrid());
    }

    public static void main(String[] args) {
        if(args.length == 0)
            new App().setVisible(true);
        else if(args[0].equals("-c") || args[0].equals("--cli"))
            cli();
        else
            new App().setVisible(true);
    }
}
