import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import gui.Settings;

import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Runner class for the Sudoku App, managing the GUI and coordinating 
 * internal logic.
 */
public class App extends JFrame {
    // GUI fields
    private Nav nav;

    // Data fields
    private Settings settings;
    private SudokuChecker checker;

    /**
     * Create a new App object, initializing the GUI and internal logic.
     */
    public App() {
        super("Sudoku");
        settings = new Settings();
        nav = new Nav(settings, false);
        initSetup();
        add(createApp());
    }

    /**
     * Basic JFrame settings and setup for the GUI.
     */
    private void initSetup() {
        // TODO: These are just some default values, these should use the 
        //       values from Settings.
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(750, 550));
        setSize(getPreferredSize());
        setResizable(false);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName()
            );
        } catch (Exception e) {
            System.out.println(
                "Error with cross platform look/feel in frameSetup()."
            );
        }
    }

    /**
     * Create the root JPanel holding all other GUI components.
     * 
     * @return JPanel
     */
    private JPanel createApp() {
        // TODO: Create the root JPanel holding all other GUI components.
        JPanel root = new JPanel();
            root.add(nav);
        return root;
    }

    /**
     * Run the Sudoku App in a command-line interface for debugging core logic.
     */
    public static void cli() {
        Nav nav = new Nav(new Settings(), true);
        SudokuChecker check = new SudokuChecker(nav.getLoadedGrid());
    }

    /**
     * Main method for the Sudoku App.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if(args.length == 0)
            new App().setVisible(true);
        else if(args[0].equals("-c") ||
                args[0].equals("--cli"))
            cli();
        else
            new App().setVisible(true);
    }
}