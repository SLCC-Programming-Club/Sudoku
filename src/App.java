import javax.swing.JFrame;
import javax.swing.UIManager;

import gui.Nav;
import gui.Root;
import gui.backend.Settings;
import gui.backend.SudokuChecker;

import java.awt.BorderLayout;

/**
 * Runner class for the Sudoku App, managing the GUI and coordinating 
 * internal logic.
 */
public class App extends JFrame {
    // GUI fields
    private Nav nav;

    // Data fields
    private Settings s;

    /**
     * Create a new App object, initializing the GUI and internal logic.
     */
    public App() {
        super("Sudoku");
        s = new Settings();
        nav = new Nav(s, false);
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
        setPreferredSize(s.getDimension());
        setSize(getPreferredSize());
        setResizable(s.getResizable());
        setLocationRelativeTo(null);

        // Set the look and feel for the app to be cross-platform.
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName()
            );
        } catch (Exception e) {
            System.out.println(
                "Error with cross platform look/feel in initSetup()."
            );
        }
    }

    /**
     * Create the Root JPanel for the Sudoku App. This will contain all other
     * GUI components.
     * 
     * Settings must be passed since, in the future, the user may be able to
     * customize the layout of the app.
     * 
     * @param settings
     * @return Root
     */
    private Root createApp() {
        Root root = new Root(s);
            root.add(nav);

        return root;
    }

    /**
     * Run the Sudoku App in a command-line interface for debugging core logic.
     */
    public static void cli() {
        Nav nav = new Nav(new Settings(), true);
        new SudokuChecker(nav.getLoadedGrid());
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