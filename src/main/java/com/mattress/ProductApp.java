package com.mattress;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class ProductApp extends JFrame {

    private static final String DATA_FILE = "data.txt";
    private static final String STARS = "********************************";

    private final JTextField txtCustomer = new JTextField(20);
    private final JComboBox<String> cmbProduct = new JComboBox<>(new String[]{
        "Cozy Nights Monarch", "Restonic Barbados", "Sealy Phuket"});
    private final JTextField txtPrice = new JTextField(20);
    private final JButton btnProcess = new JButton("PROCESS");

    public ProductApp() {
        super("Product App");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buildMenu();
        buildForm();
        pack();
        setSize(500, 300);
        setLocationRelativeTo(null);
    }

    private void buildMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem processItem = new JMenuItem("Process");
        processItem.addActionListener(e -> processSale());
        JMenuItem clearItem = new JMenuItem("Clear");
        clearItem.addActionListener(e -> clearForm());
        toolsMenu.add(processItem);
        toolsMenu.add(clearItem);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        setJMenuBar(menuBar);
    }

    private void buildForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0; gc.gridy = 0; gc.weightx = 0;
        panel.add(new JLabel("Customer Name:"), gc);
        gc.gridx = 1; gc.weightx = 1;
        panel.add(txtCustomer, gc);

        gc.gridx = 0; gc.gridy = 1; gc.weightx = 0;
        panel.add(new JLabel("Select Product:"), gc);
        gc.gridx = 1; gc.weightx = 1;
        panel.add(cmbProduct, gc);

        gc.gridx = 0; gc.gridy = 2; gc.weightx = 0;
        panel.add(new JLabel("Enter Price:"), gc);
        gc.gridx = 1; gc.weightx = 1;
        panel.add(txtPrice, gc);

        gc.gridx = 1; gc.gridy = 3;
        panel.add(btnProcess, gc);

        btnProcess.addActionListener(e -> processSale());
        add(panel);
    }

    /** Shared by the PROCESS button and the Tools > Process menu item. */
    private void processSale() {
        String customer = txtCustomer.getText().trim();
        String product = (String) cmbProduct.getSelectedItem();

        if (customer.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the customer name.",
                    "Missing information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(txtPrice.getText().trim());
            if (price <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price greater than 0.",
                    "Invalid price", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProductCalculations calc = new ProductCalculations(price);
        double vat = calc.GetVat();
        double commission = calc.GetCommission();
        double total = calc.GetTotal();

        String details = "Customer Name: " + customer
                + "\nProduct: " + product
                + "\nVAT: R " + vat
                + "\nCOMMISSION: R " + commission
                + "\nTOTAL: R " + total;

        JOptionPane.showMessageDialog(this, details, "Message", JOptionPane.INFORMATION_MESSAGE);
        saveToFile(details);
    }

    /** Appends the sale to data.txt in the DATA LOG format. */
    private void saveToFile(String details) {
        try (PrintWriter out = new PrintWriter(new FileWriter(DATA_FILE, true))) {
            out.println("DATA LOG");
            out.println(STARS);
            out.println(details);
            out.println(STARS);
            out.println();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not save to " + DATA_FILE + ": " + ex.getMessage(),
                    "File error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtCustomer.setText("");
        txtPrice.setText("");
        cmbProduct.setSelectedIndex(0);
        txtCustomer.requestFocus();
    }
}
