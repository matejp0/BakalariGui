package cz.matejprerovsky.bakalarigui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Window extends JFrame {
    private String[] dataArray;

    private JButton loginBtn;
    private JButton marksBtn;
    private JTextField urlField, username;
    private JPasswordField password;
    private final GridBagConstraints g;
    private final Container pane;
    private JCheckBox saveCheckBox;

    public JTextField getUrlField() { return urlField; }
    public JTextField getUsername() { return username; }
    public JPasswordField getPassword() { return password; }
    public JButton getLoginBtn() { return loginBtn; }
    public JButton getMarksBtn() { return marksBtn; }
    public JCheckBox getSaveCheckBox() { return saveCheckBox; }

    Window(String additionalTitle) {
        pane = this.getContentPane();
        pane.setLayout(new GridBagLayout());
        g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        this.setTitle("Bakaláři" + " – " + additionalTitle);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("baky.png"));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }
    //-----CSV-------------------------------------
    public boolean loadCsv(){
        dataArray = new String[2];
        try (BufferedReader br = new BufferedReader(new FileReader("addressAndUsername.txt"))) {
            String s;
            while ((s = br.readLine()) != null) {
                dataArray = s.split(";");
            }
            return true;
        }
        catch(IOException e){
            dataArray[0]="https://";
            dataArray[1]="username";
            return false;
        }
    }///
    public void saveCsv(String url, String username) throws IOException{
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("addressAndUsername.txt"))) {
            String[] values = {url, username};
            String line = String.join(";", values);
            bw.append(line);
            bw.append("\n");
            bw.flush();
        }
    }
    //--------------------------------------
    public void login() {
        loadCsv();
        urlField = new JTextField(dataArray[0], 30);
        g.gridx=0; g.gridy=0; g.gridwidth=3; g.fill = GridBagConstraints.HORIZONTAL;
        pane.add(urlField, g);
        username = new JTextField(dataArray[1], 12);
        g.gridx=0; g.gridy=1; g.gridwidth=1;
        pane.add(username, g);
        password = new JPasswordField("Password", 12);
        g.gridx=1; g.gridy=1; g.gridwidth=1;
        pane.add(password, g);
        loginBtn = new JButton("Login");
        g.gridx=2; g.gridy=1; g.gridwidth=1;
        pane.add(loginBtn, g);
        loginBtn.addActionListener(new Main());
        saveCheckBox = new JCheckBox("Uložit jméno a url", false);
        g.gridx=0;g.gridy=2;g.gridwidth=1;
        pane.add(saveCheckBox, g);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
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

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void marks(Bakal bakal) throws IOException {
        JTextArea textArea = new JTextArea(bakal.getMarks());
        g.gridx = g.gridy = 0;
        pane.add(textArea, g);

        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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
