package hospitalmanagement;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class AppointmentFrame extends JFrame {

    JComboBox<String> cmbPatient, cmbDoctor, cmbStatus;
    JTextField txtDate;
    JTable table;
    DefaultTableModel model;

    public AppointmentFrame() {
        setTitle("Appointments");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitle = new JLabel("Appointment Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(0, 10, 900, 30);
        add(lblTitle);

        // Form
        JLabel l1 = new JLabel("Patient:");
        l1.setBounds(20, 55, 100, 25);
        add(l1);
        cmbPatient = new JComboBox<>();
        cmbPatient.setBounds(130, 55, 180, 25);
        add(cmbPatient);

        JLabel l2 = new JLabel("Doctor:");
        l2.setBounds(20, 90, 100, 25);
        add(l2);
        cmbDoctor = new JComboBox<>();
        cmbDoctor.setBounds(130, 90, 180, 25);
        add(cmbDoctor);

        JLabel l3 = new JLabel("Date (YYYY-MM-DD):");
        l3.setBounds(20, 125, 150, 25);
        add(l3);
        txtDate = new JTextField();
        txtDate.setBounds(175, 125, 130, 25);
        add(txtDate);

        JLabel l4 = new JLabel("Status:");
        l4.setBounds(20, 160, 100, 25);
        add(l4);
        cmbStatus = new JComboBox<>(new String[]{"Pending", "Done", "Cancelled"});
        cmbStatus.setBounds(130, 160, 180, 25);
        add(cmbStatus);

        // Buttons
        JButton btnAdd = new JButton("Book Appointment");
        btnAdd.setBounds(20, 210, 160, 30);
        btnAdd.setBackground(new Color(255, 165, 0));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Arial", Font.BOLD, 12));
        add(btnAdd);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(195, 210, 100, 30);
        btnDelete.setBackground(new Color(220, 80, 80));
        btnDelete.setForeground(Color.WHITE);
        add(btnDelete);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(20, 520, 100, 30);
        btnBack.setBackground(new Color(70, 130, 180));
        btnBack.setForeground(Color.WHITE);
        add(btnBack);

        // Table
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Patient");
        model.addColumn("Doctor");
        model.addColumn("Date");
        model.addColumn("Status");

        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(380, 50, 500, 480);
        add(sp);

        // Load dropdowns
        loadPatients();
        loadDoctors();
        loadAppointments();

        // Actions
        btnAdd.addActionListener(e -> bookAppointment());
        btnDelete.addActionListener(e -> deleteAppointment());
        btnBack.addActionListener(e -> {
            new DashboardFrame();
            dispose();
        });

        setVisible(true);
    }

    void loadPatients() {
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT patient_id, name FROM patients");
            while (rs.next()) {
                cmbPatient.addItem(rs.getInt("patient_id") + " - " + rs.getString("name"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    void loadDoctors() {
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT doctor_id, name FROM doctors");
            while (rs.next()) {
                cmbDoctor.addItem(rs.getInt("doctor_id") + " - " + rs.getString("name"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    void loadAppointments() {
        model.setRowCount(0);
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT a.app_id, p.name, d.name, a.app_date, a.status " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id"
            );
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5)
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    void bookAppointment() {
        if (cmbPatient.getItemCount() == 0 || cmbDoctor.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Pehle Patient aur Doctor add karo!");
            return;
        }

        String patientItem = cmbPatient.getSelectedItem().toString();
        String doctorItem = cmbDoctor.getSelectedItem().toString();
        int patientId = Integer.parseInt(patientItem.split(" - ")[0]);
        int doctorId = Integer.parseInt(doctorItem.split(" - ")[0]);
        String date = txtDate.getText();
        String status = cmbStatus.getSelectedItem().toString();

        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Date daalo! (YYYY-MM-DD format mein)");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO appointments (patient_id, doctor_id, app_date, status) VALUES (?,?,?,?)"
            );
            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setString(3, date);
            ps.setString(4, status);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Appointment book ho gayi!");
            loadAppointments();
            txtDate.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    void deleteAppointment() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pehle table mein appointment select karo!");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM appointments WHERE app_id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Appointment delete ho gayi!");
            loadAppointments();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
