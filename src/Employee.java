import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Employee extends JFrame {
    public Employee() {
        super("Employee Management System");
        JButton addButton = new JButton("Add");
        JButton viewButton = new JButton("View");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton chartButton = new JButton("Chart");

        JPanel Addpanel = new JPanel();
        JPanel Viewpanel = new JPanel();
        JPanel Updatepanel = new JPanel();
        JPanel Deletepanel = new JPanel();
        JPanel Chartpanel = new JPanel();

        Addpanel.add(addButton);
        Viewpanel.add(viewButton);
        Updatepanel.add(updateButton);
        Deletepanel.add(deleteButton);
        Chartpanel.add(chartButton);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(Addpanel);
        getContentPane().add(Viewpanel);
        getContentPane().add(Updatepanel);
        getContentPane().add(Deletepanel);
        getContentPane().add(Chartpanel);
        Addpanel.setBackground(new Color(200, 200, 121));
        Viewpanel.setBackground(new Color(200, 200, 121));
        Updatepanel.setBackground(new Color(200, 200, 121));
        Deletepanel.setBackground(new Color(200, 200, 121));
        Chartpanel.setBackground(new Color(200, 200, 121));

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddWindow();
            }
        });
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewWindow();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UpdateWindow();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DeleteWindow();
            }
        });
        chartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChartWindow();
            }
        });
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLocation(526, 24);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    //Add Window
            private void AddWindow() {
                JFrame addFrame = new JFrame("Add Employee");
                addFrame.setSize(300, 300);
                addFrame.setLocationRelativeTo(null);
                addFrame.setLocation(32, 340);
                addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JPanel addPanel = new JPanel(new GridLayout(0, 1));
                addPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
                addPanel.setBackground(new Color(200, 200, 240));
                addFrame.add(addPanel);

                JLabel empIdLabel = new JLabel("Employee ID:");
                JTextField empIdTextField = new JTextField(20);

                JLabel empNameLabel = new JLabel("Employee Name:");
                JTextField empNameTextField = new JTextField(20);

                JLabel salaryLabel = new JLabel("Salary:");
                JTextField salaryTextField = new JTextField(20);

                addPanel.add(empIdLabel);
                addPanel.add(empIdTextField);
                addPanel.add(empNameLabel);
                addPanel.add(empNameTextField);
                addPanel.add(salaryLabel);
                addPanel.add(salaryTextField);

                JButton submitButton = new JButton("Submit");
                JButton backButton = new JButton("Back");

                submitButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        String empid = empIdTextField.getText();
                        String name = empNameTextField.getText();
                        String salary = salaryTextField.getText();

                        if (empid.isEmpty() || !empid.matches("[0-9]+")) {
                            JOptionPane.showMessageDialog(null, empid.isEmpty() ? "Please enter Employee ID." : "Please enter a valid numeric Employee ID.");
                            return;
                        }

                        if (name.isEmpty() || !name.matches("[a-zA-Z ]+")) {
                            JOptionPane.showMessageDialog(null, name.isEmpty() ? "Please enter Employee Name." : "Please enter a valid Employee Name.");
                            return;
                        }

                        if (salary.isEmpty() || !salary.matches("[0-9]+")) {
                            JOptionPane.showMessageDialog(null, salary.isEmpty() ? "Please enter Salary." : "Please enter a valid Salary.");
                            return;
                        }

                        try {
                            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                            String url = "jdbc:mysql://localhost:3306/employee_data";
                            Connection con = DriverManager.getConnection(url, "root", "abc123");
                            String checkSql = "SELECT * FROM data WHERE Emp_Id=?";
                            PreparedStatement checkPst = con.prepareStatement(checkSql);
                            checkPst.setString(1, empid);
                            ResultSet resultSet = checkPst.executeQuery();

                            if (resultSet.next()) {

                                JOptionPane.showMessageDialog(null, "Employee ID already exists.");
                                return;
                            }
                            String insertSql = "INSERT INTO data (Emp_Id, Emp_Name, Salary) VALUES (?, ?, ?)";
                            PreparedStatement pst = con.prepareStatement(insertSql);
                            pst.setString(1, empid);
                            pst.setString(2, name);
                            pst.setString(3, salary);
                            pst.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Data stored successfully");
                            empIdTextField.setText("");
                            empNameTextField.setText("");
                            salaryTextField.setText("");

                            empIdTextField.requestFocus();
                            con.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                backButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addFrame.dispose();
                    }
                });

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.setBackground(new Color(200, 200, 240));
                buttonPanel.add(submitButton);
                buttonPanel.add(backButton);
                addPanel.add(buttonPanel);
                addFrame.setVisible(true);
            }

    //View Window
            private void ViewWindow() {
                JFrame newFrame = new JFrame("Employee Data");
                newFrame.setSize(300, 300);
                newFrame.setLocationRelativeTo(null);
                newFrame.setLocation(369, 340);

                try {
                    DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                    String url = "jdbc:mysql://localhost:3306/employee_data";
                    Connection con = DriverManager.getConnection(url, "root", "abc123");
                    String sql = "SELECT * FROM data";
                    try (PreparedStatement pst = con.prepareStatement(sql);
                         ResultSet rs = pst.executeQuery()) {
                        DefaultTableModel model = new DefaultTableModel();
                        JTable table = new JTable(model);
                        model.addColumn("ID");
                        model.addColumn("Name");
                        model.addColumn("Salary");

                        while (rs.next()) {
                            Object[] row = new Object[3];
                            for (int i = 1; i <= 3; i++) {
                                row[i - 1] = rs.getObject(i);
                            }
                            model.addRow(row);
                        }
                        JScrollPane scrollPane = new JScrollPane(table);
                        newFrame.add(scrollPane, BorderLayout.CENTER);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                newFrame.setVisible(true);
            }

    //Update window
            private void UpdateWindow() {
                JFrame updateFrame = new JFrame("Update Employee");
                updateFrame.setSize(300, 300);
                updateFrame.setLocationRelativeTo(null);
                updateFrame.setLocation(705, 340);
                updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JPanel updatePanel = new JPanel(new GridLayout(0, 1));
                updatePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
                updatePanel.setBackground(new Color(255, 228, 196));
                updateFrame.add(updatePanel);

                JLabel empIdLabel = new JLabel("Employee ID:");
                JTextField empIdTextField = new JTextField(20);

                JLabel empNameLabel = new JLabel("New Employee Name:");
                JTextField empNameTextField = new JTextField(20);

                JLabel salaryLabel = new JLabel("New Salary:");
                JTextField salaryTextField = new JTextField(20);

                updatePanel.add(empIdLabel);
                updatePanel.add(empIdTextField);
                updatePanel.add(empNameLabel);
                updatePanel.add(empNameTextField);
                updatePanel.add(salaryLabel);
                updatePanel.add(salaryTextField);

                JButton updateButton = new JButton("Update");
                JButton backButton = new JButton("Back");
                ;
                updateButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String empId = empIdTextField.getText();
                        String newName = empNameTextField.getText();
                        String newSalary = salaryTextField.getText();

                        if (empId.isEmpty() || !empId.matches("[0-9]+")) {
                            JOptionPane.showMessageDialog(null, empId.isEmpty() ? "Please enter Employee ID." : "Please enter a valid numeric Employee ID.");
                            return;
                        }

                        if (newName.isEmpty() || !newName.matches("[a-zA-Z ]+")) {
                            JOptionPane.showMessageDialog(null, newName.isEmpty() ? "Please enter Employee Name." : "Please enter a valid Employee Name.");
                            return;
                        }

                        if (newSalary.isEmpty() || !newSalary.matches("[0-9]+")) {
                            JOptionPane.showMessageDialog(null, newSalary.isEmpty() ? "Please enter Salary." : "Please enter a valid Salary.");
                            return;
                        }

                        try {
                            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                            String url = "jdbc:mysql://localhost:3306/employee_data";
                            Connection con = DriverManager.getConnection(url, "root", "abc123");

                            String checkSql = "SELECT * FROM data WHERE Emp_Id=?";
                            PreparedStatement checkPst = con.prepareStatement(checkSql);
                            checkPst.setString(1, empId);
                            ResultSet resultSet = checkPst.executeQuery();

                            if (!resultSet.next()) {

                                JOptionPane.showMessageDialog(null, "Employee with ID " + empId + " not found.");
                                return;
                            }

                            String updateSql = "UPDATE data SET Emp_Name=?, Salary=? WHERE Emp_Id=?";
                            PreparedStatement updatePst = con.prepareStatement(updateSql);
                            updatePst.setString(1, newName);
                            updatePst.setString(2, newSalary);
                            updatePst.setString(3, empId);
                            int affectedRows = updatePst.executeUpdate();

                            if (affectedRows > 0) {
                                JOptionPane.showMessageDialog(null, "Employee information updated successfully");
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to update employee information");
                            }

                            empIdTextField.setText("");
                            empNameTextField.setText("");
                            salaryTextField.setText("");

                            con.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });


                backButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        updateFrame.dispose();
                    }
                });
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.setBackground(new Color(255, 228, 196));
                buttonPanel.add(updateButton);
                buttonPanel.add(backButton);
                updatePanel.add(buttonPanel);
                updateFrame.setVisible(true);
            }
    private boolean isValidEmployeeId(String empId) {
        try {
            Integer.parseInt(empId);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isValidSalary(String salary) {
        try {
            double parsedSalary = Double.parseDouble(salary);
            return parsedSalary >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //Delete window
            private void DeleteWindow() {
                JFrame deleteFrame = new JFrame("Delete Employee");
                deleteFrame.setSize(300, 170);
                deleteFrame.setLocationRelativeTo(null);
                deleteFrame.setLocation(1042, 340);
                deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JPanel deletePanel = new JPanel(new GridLayout(0, 1));
                deletePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
                deletePanel.setBackground(new Color(200, 200, 240));
                deleteFrame.add(deletePanel);

                JLabel empIdLabel = new JLabel("Employee ID:");
                JTextField empIdTextField = new JTextField(20);
                deletePanel.add(empIdLabel);
                deletePanel.add(empIdTextField);
                JButton deleteButton = new JButton("Delete");
                JButton backButton = new JButton("Back");

                deleteButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String empId = empIdTextField.getText();

                        if (empId.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter Employee ID.");
                            return;
                        }

                        if (!empId.matches("[0-9]+")) {
                            JOptionPane.showMessageDialog(null, "Invalid ID.");
                            return;
                        }

                        try {
                            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                            String url = "jdbc:mysql://localhost:3306/employee_data";
                            Connection con = DriverManager.getConnection(url, "root", "abc123");
                            String checkSql = "SELECT * FROM data WHERE Emp_Id=?";
                            PreparedStatement checkPst = con.prepareStatement(checkSql);
                            checkPst.setString(1, empId);
                            ResultSet resultSet = checkPst.executeQuery();

                            if (!resultSet.next()) {

                                JOptionPane.showMessageDialog(null, "Employee with ID " + empId + " not found.");
                                return;
                            }

                            String deleteSql = "DELETE FROM data WHERE Emp_Id=?";
                            PreparedStatement deletePst = con.prepareStatement(deleteSql);
                            deletePst.setString(1, empId);
                            int affectedRows = deletePst.executeUpdate();

                            if (affectedRows > 0) {
                                JOptionPane.showMessageDialog(null, "Employee deleted successfully");
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to delete employee");
                            }
                            empIdTextField.setText("");
                            con.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                backButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        deleteFrame.dispose();
                    }
                });
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.setBackground(new Color(200, 200, 240));
                buttonPanel.add(deleteButton);
                buttonPanel.add(backButton);
                deletePanel.add(buttonPanel);
                deleteFrame.setVisible(true);
            }

            //Chart Window
            private void ChartWindow() {
                JFrame chartFrame = new JFrame("Top 5 Employees");
                chartFrame.setSize(300, 300);
                chartFrame.setLocationRelativeTo(null);
                chartFrame.setLocation(1042, 24);
                chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                chartFrame.getContentPane().setBackground(new Color(255, 228, 112));
                try {
                    DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                    String url = "jdbc:mysql://localhost:3306/employee_data";
                    Connection con = DriverManager.getConnection(url, "root", "abc123");

                    String sql = "SELECT * FROM data ORDER BY Salary DESC LIMIT 5";
                    PreparedStatement pst = con.prepareStatement(sql);
                    ResultSet resultSet = pst.executeQuery();

                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    Object[][] data = new Object[5][columnCount];
                    int row = 0;
                    while (resultSet.next()) {
                        for (int col = 1; col <= columnCount; col++) {
                            data[row][col - 1] = resultSet.getObject(col);
                        }
                        row++;
                    }
                    Object[] columnNames = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        columnNames[i - 1] = metaData.getColumnName(i);
                    }
                    JTable table = new JTable(data, columnNames);
                    JScrollPane scrollPane = new JScrollPane(table);
                    chartFrame.add(scrollPane);
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                chartFrame.setVisible(true);
            }
    public static void main(String[] args) {
        new Employee();
    }
}