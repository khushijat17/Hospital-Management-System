package hospitalmanagement;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class DoctorFrame extends JFrame {

    JTextField txtName, txtSpec, txtPhone, txtEmail;
    JTable table;
    DefaultTableModel model;

    public DoctorFrame() {
        setTitle("Manage Doctors");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitle = new JLabel("Doctor Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(0, 10, 900, 30);
        add(lblTitle);

        // Form
        addLabel("Name:", 20, 55);
        txtName = addField(130, 55);

        addLabel("Specialization:", 20, 90);
        txtSpec = addField(130, 90);

        addLabel("Phone:", 20, 125);
        txtPhone = addField(130, 125);

        addLabel("Email:", 20, 160);
        txtEmail = addField(130, 160);

        // Buttons
        JButton btnAdd = new JButton("Add Doctor");
        btnAdd.setBounds(20, 210, 120, 30);
        btnAdd.setBackground(new Color(60, 179, 113));
        btnAdd.setForeground(Color.WHITE);
        add(btnAdd);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(155, 210, 100, 30);
        btnDelete.setBackground(new Color(220, 80, 80));
        btnDelete.setForeground(Color.WHITE);
        add(btnDelete);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(265, 210, 100, 30);
        btnClear.setBackground(new Color(128, 128, 128));
        btnClear.setForeground(Color.WHITE);
        add(btnClear);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(20, 520, 100, 30);
        btnBack.setBackground(new Color(70, 130, 180));
        btnBack.setForeground(Color.WHITE);
        add(btnBack);

        // Table
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Specialization");
        model.addColumn("Phone");
        model.addColumn("Email");

        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(380, 50, 500, 480);
        add(sp);

        loadDoctors();

        btnAdd.addActionListener(e -> addDoctor());
        btnDelete.addActionListener(e -> deleteDoctor());
        btnClear.addActionListener(e -> clearFields());
        btnBack.addActionListener(e -> {
            new DashboardFrame();
            dispose();
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.getSelectedRow();
                txtName.setText(model.getValueAt(row, 1).toString());
                txtSpec.setText(model.getValueAt(row, 2).toString());
                txtPhone.setText(model.getValueAt(row, 3).toString());
                txtEmail.setText(model.getValueAt(row, 4).toString());
            }
        });

        setVisible(true);
    }

    JLabel addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 110, 25);
        add(lbl);
        return lbl;
    }

    JTextField addField(int x, int y) {
        JTextField txt = new JTextField();
        txt.setBounds(x, y, 150, 25);
        add(txt);
        return txt;
    }

    void loadDoctors() {
        model.setRowCount(0);
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM doctors");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("doctor_id"),
                    rs.getString("name"),
                    rs.getString("specialization"),
                    rs.getString("phone"),
                    rs.getString("email")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    void addDoctor() {
        String name = txtName.getText();
        String spec = txtSpec.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();

        if (name.isEmpty() || spec.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name aur Specialization zaroori hai!");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO doctors (name, specialization, phone, email) VALUES (?,?,?,?)"
            );
            ps.setString(1, name);
            ps.setString(2, spec);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Doctor add ho gaya!");
            loadDoctors();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    void deleteDoctor() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pehle table mein doctor select karo!");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM doctors WHERE doctor_id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Doctor delete ho gaya!");
            loadDoctors();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    void clearFields() {
        txtName.setText("");
        txtSpec.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
    }
}