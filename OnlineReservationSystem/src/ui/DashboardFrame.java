package ui;

import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * DashboardFrame
 * --------------
 * The main menu shown after a successful login. Provides navigation
 * to Reserve Ticket, View Reservation, Cancel Reservation, Logout,
 * and Exit — matching the "Dashboard" module in the spec.
 */
public class DashboardFrame extends JFrame {

    private final User loggedInUser;

    public DashboardFrame(User loggedInUser) {
        super("Online Reservation System - Dashboard");
        this.loggedInUser = loggedInUser;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 380);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUser.getUsername() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        JButton reserveButton = new JButton("Reserve Ticket");
        JButton viewButton = new JButton("View Reservation");
        JButton cancelButton = new JButton("Cancel Reservation");
        JButton logoutButton = new JButton("Logout");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(reserveButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);

        reserveButton.addActionListener(e -> {
            new ReservationFrame(this).setVisible(true);
            setVisible(false);
        });

        viewButton.addActionListener(e -> {
            new ViewReservationFrame(this).setVisible(true);
            setVisible(false);
        });

        cancelButton.addActionListener(e -> {
            new CancelReservationFrame(this).setVisible(true);
            setVisible(false);
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
