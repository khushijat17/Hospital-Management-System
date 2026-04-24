package hospitalmanagement;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class PatientFrame extends JFrame {

    JTextField txtName, txtAge, txtPhone, txtDisease, txtAddress;
    JComboBox<String> cmbGender;
    JTable table;
    DefaultTableModel model;

    public PatientFrame() {
        setTitle("Manage Patients");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Title
        JLabel lblTitle = new JLabel("Patient Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(0, 10, 900, 30);
        add(lblTitle);

        // Form Fields
        addLabel("Name:", 20, 55);
        txtName = addField(100, 55);

        addLabel("Age:", 20, 90);
        txtAge = addField(100, 90);

        addLabel("Gender:", 20, 125);
        cmbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        cmbGender.setBounds(100, 125, 150, 25);
        add(cmbGender);

        addLabel("Phone:", 20, 160);
        txtPhone = addField(100, 160);

        addLabel("Disease:", 20, 195);
        txtDisease = addField(100, 195);

        addLabel("Address:", 20, 230);
        txtAddress = addField(100, 230);

        // Buttons
        JButton btnAdd = new JButton("Add Patient");
        btnAdd.setBounds(20, 275, 120, 30);
        btnAdd.setBackground(new Color(70, 130, 180));
        btnAdd.setForeground(Color.WHITE);
        add(btnAdd);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(155, 275, 100, 30);
        btnDelete.setBackground(new Color(220, 80, 80));
        btnDelete.setForeground(Color.WHITE);
        add(btnDelete);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(265, 275, 100, 30);
        btnClear.setBackground(new Color(128, 128, 128));
        btnClear.setForeground(Color.WHITE);
        add(btnClear);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(20, 520, 100, 30);
        btnBack.setBackground(new Color(60, 179, 113));
        btnBack.setForeground(Color.WHITE);
        add(btnBack);

        // Table
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Age");
        model.addColumn("Gender");
        model.addColumn("Phone");
        model.addColumn("Disease");

        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(380, 50, 500, 480);
        add(sp);

        // Load Data
        loadPatients();

        // Button Actions
        btnAdd.addActionListener(e -> addPatient());
        btnDelete.addActionListener(e -> deletePatient());
        btnClear.addActionListener(e -> clearFields());
        btnBack.addActionListener(e -> {
            new DashboardFrame();
            dispose();
        });

        // Table click se form fill ho
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.getSelectedRow();
                txtName.setText(model.getValueAt(row, 1).toString());
                txtAge.setText(model.getValueAt(row, 2).toString());
                txtPhone.setText(model.getValueAt(row, 4).toString());
                txtDisease.setText(model.getValueAt(row, 5).toString());
            }
        });

        setVisible(true);
    }

    JLabel addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 80, 25);
        add(lbl);
        return lbl;
    }

    JTextField addField(int x, int y) {
        JTextField txt = new JTextField();
        txt.setBounds(x, y, 150, 25);
        add(txt);
        return txt;
    }

    void loadPatients() {
        model.setRowCount(0);
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM patients");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("patient_id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getString("phone"),
                    rs.getString("disease")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    void addPatient() {
        String name = txtName.getText();
        String age = txtAge.getText();
        String gender = cmbGender.getSelectedItem().toString();
        String phone = txtPhone.getText();
        String disease = txtDisease.getText();
        String address = txtAddress.getText();

        if (name.isEmpty() || age.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name aur Age zaroori hai!");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO patients (name, age, gender, phone, disease, address, admit_date) VALUES (?,?,?,?,?,?, CURDATE())"
            );
            ps.setString(1, name);
            ps.setInt(2, Integer.parseInt(age));
            ps.setString(3, gender);
            ps.setString(4, phone);
            ps.setString(5, disease);
            ps.setString(6, address);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient add ho gaya!");
            loadPatients();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    void deletePatient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pehle table mein patient select karo!");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM patients WHERE patient_id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient delete ho gaya!");
            loadPatients();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    void clearFields() {
        txtName.setText("");
        txtAge.setText("");
        txtPhone.setText("");
        txtDisease.setText("");
        txtAddress.setText("");
    }
}