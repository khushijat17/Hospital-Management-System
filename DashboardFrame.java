package hospitalmanagement;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Hospital Management System - Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        JLabel lblTitle = new JLabel("Hospital Management System", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBounds(0, 20, 600, 35);
        add(lblTitle);

        JLabel lblSub = new JLabel("Select an option to continue", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSub.setBounds(0, 55, 600, 25);
        add(lblSub);

        JButton btnPatient = new JButton("Manage Patients");
        btnPatient.setBounds(80, 120, 180, 60);
        btnPatient.setBackground(new Color(70, 130, 180));
        btnPatient.setForeground(Color.WHITE);
        btnPatient.setFont(new Font("Arial", Font.BOLD, 13));
        add(btnPatient);

        JButton btnDoctor = new JButton("Manage Doctors");
        btnDoctor.setBounds(340, 120, 180, 60);
        btnDoctor.setBackground(new Color(60, 179, 113));
        btnDoctor.setForeground(Color.WHITE);
        btnDoctor.setFont(new Font("Arial", Font.BOLD, 13));
        add(btnDoctor);

        JButton btnAppointment = new JButton("Appointments");
        btnAppointment.setBounds(80, 220, 180, 60);
        btnAppointment.setBackground(new Color(255, 165, 0));
        btnAppointment.setForeground(Color.WHITE);
        btnAppointment.setFont(new Font("Arial", Font.BOLD, 13));
        add(btnAppointment);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(340, 220, 180, 60);
        btnLogout.setBackground(new Color(220, 80, 80));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 13));
        add(btnLogout);

        // Button Actions
        btnPatient.addActionListener(e -> {
            new PatientFrame();
            dispose();
        });

       btnDoctor.addActionListener(e -> {
    new DoctorFrame();
    dispose();
});

        btnAppointment.addActionListener(e -> {
    new AppointmentFrame();
    dispose();
});

        btnLogout.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }
}