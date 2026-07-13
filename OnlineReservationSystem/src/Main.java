import database.DatabaseInitializer;
import ui.LoginFrame;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Main
 * ----
 * Application entry point for the Online Reservation System.
 *
 * Responsibilities:
 *  1. Initialize the SQLite database (create tables + seed sample data)
 *     before any UI is shown.
 *  2. Launch the Swing UI on the Event Dispatch Thread, starting with
 *     {@link LoginFrame}.
 *  3. Guard startup with proper exception handling so a database
 *     problem shows a friendly dialog instead of an unexplained crash.
 */
public class Main {

    public static void main(String[] args) {
        try {
            DatabaseInitializer.initialize();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to initialize the database:\n" + e.getMessage()
                            + "\n\nThe application cannot start.",
                    "Database Initialization Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "An unexpected error occurred during startup:\n" + e.getMessage(),
                    "Startup Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                new LoginFrame().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Failed to launch the application UI:\n" + e.getMessage(),
                        "Startup Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
