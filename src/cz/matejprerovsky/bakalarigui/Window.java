package cz.matejprerovsky.bakalarigui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Window extends JFrame{
    private JButton loginBtn, marksBtn;
    private JTextField urlField, username;
    private JPasswordField password;
    private final GridBagConstraints g;
    private final Container pane;

    public JTextField getUrlField() { return urlField; }
    public JTextField getUsername() { return username; }
    public JPasswordField getPassword() { return password; }
    public JButton getLoginBtn() { return loginBtn; }
    public JButton getMarksBtn() { return marksBtn; }

    Window(String additionalTitle) {
        pane = this.getContentPane();
        pane.setLayout(new GridBagLayout());
        g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        this.setTitle("Bakaláři" + " – " + additionalTitle);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("baky.png"));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
    }
    /*
    private void getMarks(Bakal bakal) throws IOException {
        Window marksWindow = new Window("Rozvrh", bakal);
        marksWindow.marks(bakal);
        marksWindow.pack();
        marksWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        marksWindow.setLocationRelativeTo(null);
        marksWindow.setResizable(false);
        marksWindow.setVisible(true);
    }
    private void marks(Bakal bakal) throws IOException {
        JTextArea textArea = new JTextArea(bakal.getMarks());
        g.gridx = g.gridy = 0;
        pane.add(textArea, g);
    }

     */
    public void userInfo(Bakal bakal) throws IOException {
        JLabel userInfoLabel = new JLabel(bakal.getUserInfo());
            g.gridx=0; g.gridy=0; g.gridwidth=2;
            pane.add(userInfoLabel, g);
        String[] hours = new String[13];
        Arrays.fill(hours, "");
        JTable timetableTable = new JTable(bakal.getTimetable(date()[0], date()[1], date()[2]), hours);
            g.gridx=0; g.gridy=1; g.gridwidth=2;
            timetableTable.setCellSelectionEnabled(false);
            timetableTable.setEnabled(false);
            timetableTable.setRowHeight(50);
            JTableUtilities.setCellsAlignment(timetableTable, SwingConstants.CENTER);
            pane.add(timetableTable, g);

        marksBtn = new JButton("Známky");
            g.gridx=1; g.gridy=2; g.gridwidth=1;
            pane.add(marksBtn, g);
            marksBtn.addActionListener(new Main());
    }
    public void login(){
        urlField = new JTextField("https://",30);
            g.gridx=0; g.gridy=0; g.gridwidth=3; g.fill = GridBagConstraints.HORIZONTAL;
            pane.add(urlField, g);
        username = new JTextField("Username", 12);
            g.gridx=0; g.gridy=1; g.gridwidth=1;
            pane.add(username, g);
        password = new JPasswordField("Password", 12);
            g.gridx=1; g.gridy=1; g.gridwidth=1;
            pane.add(password, g);
        loginBtn = new JButton("Login");
            g.gridx=2; g.gridy=1; g.gridwidth=1;
            pane.add(loginBtn, g);
            loginBtn.addActionListener(new Main());
    }

    public void throwMessage(String error, String title, int type){
        JOptionPane.showMessageDialog(this, error, title, type);
    }

    public int[] date(){
        int[] output = new int[3];
        Date date = new Date();
        SimpleDateFormat formatter;
        //day
        formatter = new SimpleDateFormat("dd");
        output[0] = Integer.parseInt(formatter.format(date));

        //month
        formatter = new SimpleDateFormat("MM");
        output[1] = Integer.parseInt(formatter.format(date));

        //year
        formatter = new SimpleDateFormat("yyyy");
        output[2] = Integer.parseInt(formatter.format(date));

        return output;
    }
}
