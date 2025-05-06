import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}

class MainFrame extends JFrame {
    private JComboBox<String> originCombo;
    private JComboBox<String> destinationCombo;
    private JComboBox<String> dayCombo, monthCombo, yearCombo;
    private JButton searchButton;
    private JPanel resultPanel;

    public MainFrame() {
        setTitle("Flight Booking System");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
        setVisible(true);
    }

    private void createUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(230, 240, 255));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(200, 220, 240));

        originCombo = new JComboBox<>(new String[]{"Delhi", "Mumbai", "Chennai", "Kolkata", "Bangalore", "Hyderabad", "Dehradun", "Ahemdabad", "Bhubaneswar", "Goa"});
        destinationCombo = new JComboBox<>(new String[]{"Kolkata", "Bangalore", "Hyderabad", "Delhi", "Mumbai", "Chennai", "Dehradun", "Ahemdabad", "Bhubaneswar", "Goa"});

        // Date selection combo boxes
        dayCombo = new JComboBox<>();
        for (int i = 1; i <= 31; i++) dayCombo.addItem(String.format("%02d", i));
        monthCombo = new JComboBox<>(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"});
        yearCombo = new JComboBox<>(new String[]{"2025", "2026"});

        searchButton = new JButton("Search Flights");

        topPanel.add(new JLabel("From:"));
        topPanel.add(originCombo);
        topPanel.add(new JLabel("To:"));
        topPanel.add(destinationCombo);
        topPanel.add(new JLabel("Date:"));
        topPanel.add(dayCombo);
        topPanel.add(monthCombo);
        topPanel.add(yearCombo);
        topPanel.add(searchButton);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(new Color(245, 248, 255));
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        searchButton.addActionListener(e -> showDummyFlights());

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
    }

    private void showDummyFlights() {
        String from = (String) originCombo.getSelectedItem();
        String to = (String) destinationCombo.getSelectedItem();
        String selectedDate = dayCombo.getSelectedItem() + "-" + monthCombo.getSelectedItem() + "-" + yearCombo.getSelectedItem();

        resultPanel.removeAll();
        resultPanel.revalidate();
        resultPanel.repaint();

        if (from.equals(to)) {
            JLabel header = new JLabel("No flights available from and to the same city.");
            header.setFont(new Font("Arial", Font.BOLD, 16));
            header.setForeground(Color.RED);
            header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            resultPanel.add(header);
            return;
        }

        JLabel header = new JLabel("Available Flights from " + from + " to " + to + " on " + selectedDate);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultPanel.add(header);

        Random rand = new Random();
        String[] airlines = {"IndiGo", "Air India", "SpiceJet", "Vistara", "Go First"};
        String[] flightCodes = {"6E", "AI", "SG", "UK", "G8"};

        for (int i = 0; i < 3; i++) {
            int airlineIndex = rand.nextInt(airlines.length);
            String airline = airlines[airlineIndex];
            String flightNumber = flightCodes[airlineIndex] + (100 + rand.nextInt(900));

            int depHour = 6 + rand.nextInt(12);
            int depMin = rand.nextBoolean() ? 0 : 30;
            String depTime = String.format("%02d:%02d %s", (depHour > 12 ? depHour - 12 : depHour), depMin, (depHour >= 12 ? "PM" : "AM"));

            int duration = 1 + rand.nextInt(3);
            String arrTime = String.format("%02d:%02d %s", ((depHour + duration) > 12 ? (depHour + duration - 12) : (depHour + duration)), depMin, ((depHour + duration) >= 12 ? "PM" : "AM"));

            int basePrice = 3000 + rand.nextInt(2000);

            JPanel flightCard = new JPanel(new BorderLayout(10, 10));
            flightCard.setBackground(new Color(255, 255, 255));
            flightCard.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            flightCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

            JTextArea info = new JTextArea(String.format(
                    "%d. %s (%s)\nDeparture: %s | Arrival: %s | Duration: %d hr | Price: Rs. %d",
                    i + 1, airline, flightNumber, depTime, arrTime, duration, basePrice));
            info.setFont(new Font("SansSerif", Font.PLAIN, 13));
            info.setEditable(false);
            info.setBackground(null);
            info.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JButton bookBtn = new JButton("Book Ticket");
            int finalPrice = basePrice;
            bookBtn.addActionListener(e -> new SeatSelectionDialog(this, finalPrice));

            flightCard.add(info, BorderLayout.CENTER);
            flightCard.add(bookBtn, BorderLayout.EAST);

            resultPanel.add(Box.createVerticalStrut(10));
            resultPanel.add(flightCard);
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }
}

class SeatSelectionDialog extends JDialog {
    private JCheckBox[] seatBoxes;
    private JButton payButton;

    public SeatSelectionDialog(JFrame parent, int pricePerSeat) {
        super(parent, "Select Seats", true);
        setSize(500, 400);
        setLayout(new BorderLayout());

        JPanel seatPanel = new JPanel();
        seatPanel.setLayout(new BoxLayout(seatPanel, BoxLayout.Y_AXIS));
        seatPanel.setBackground(new Color(245, 255, 245));

        seatBoxes = new JCheckBox[24];
        int seatNumber = 1;

        for (int row = 0; row < 4; row++) {
            JPanel rowPanel = new JPanel(new GridLayout(1, 6, 10, 5));
            rowPanel.setBackground(new Color(245, 255, 245));

            for (int col = 0; col < 6; col++) {
                JCheckBox seat = new JCheckBox("Seat " + seatNumber++);
                seatBoxes[seatNumber - 2] = seat;
                rowPanel.add(seat);
            }

            seatPanel.add(Box.createVerticalStrut(5));
            seatPanel.add(rowPanel);
        }

        payButton = new JButton("Proceed to Payment");
        payButton.addActionListener(e -> processPayment(pricePerSeat));

        add(seatPanel, BorderLayout.CENTER);
        add(payButton, BorderLayout.SOUTH);



        getContentPane().setBackground(new Color(230, 255, 230));
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void processPayment(int pricePerSeat) {
        int selectedCount = 0;
        for (JCheckBox box : seatBoxes) {
            if (box.isSelected()) selectedCount++;
        }

        if (selectedCount == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one seat.");
            return;
        }

        double total = selectedCount * pricePerSeat;
        int result = JOptionPane.showConfirmDialog(this, "Total: Rs. " + total + "\nConfirm Payment?", "Payment", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Payment successful! Booking confirmed.");
            dispose();
        }
    }
}
