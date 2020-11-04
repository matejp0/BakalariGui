package cz.matejprerovsky.bakalarigui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

public class Main implements ActionListener {

private static Window loginWindow, infoWindow;
private static Bakal bakal;
private String url, username, password;
private boolean loginSuccess = false;

    public static void main(String[] args) throws IOException {
        getLogin();
    }

    private static void getLogin() throws IOException {
        loginWindow = new Window("Login");
        loginWindow.login();
        loginWindow.setLocationRelativeTo(null);
        if(Bakal.connected())
            loginWindow.setVisible(true);
        else {
            loginWindow.throwMessage("Žádné internetové připojení!", "Error internetového připojení", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    private static void getInfo() throws IOException {
        infoWindow = new Window("Rozvrh");
        infoWindow.userInfo(bakal);
        infoWindow.pack();
        infoWindow.setLocationRelativeTo(null);
        infoWindow.setVisible(true);

        loginWindow.dispose(); //close login window
    }
    private void initBakal() throws IOException {
        bakal = new Bakal(url);
        loginSuccess=bakal.login(username, password, false);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(loginWindow.getLoginBtn())) {
            url = loginWindow.getUrlField().getText();
            username = loginWindow.getUsername().getText();
            password = String.valueOf(loginWindow.getPassword().getPassword());
            try { initBakal(); } catch (IOException e) { e.printStackTrace(); }

            if (loginSuccess) {
                try { getInfo(); } catch (IOException e) { e.printStackTrace(); }
                if(loginWindow.getSaveCheckBox().isSelected()) {
                    try { loginWindow.saveCsv(url, username); } catch (IOException e) {
                        loginWindow.throwMessage("Nepodařilo se uložit url a jméno", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
                loginWindow.throwMessage("Úspěšně přihlášeno", "Úspěch", JOptionPane.INFORMATION_MESSAGE);
            }
            else if (!loginSuccess) {
                if(Bakal.connected())
                    loginWindow.throwMessage("Vadné přihlašovací údaje", "Error", JOptionPane.WARNING_MESSAGE);
                else if(!Bakal.connected())
                    loginWindow.throwMessage("Žádné internetové připojení!", "Error internetového připojení", JOptionPane.ERROR_MESSAGE);
            }
        }

        else if(actionEvent.getSource().equals(infoWindow.getMarksBtn())){
            infoWindow.throwMessage("Implementaci této funkce do GUI jsem ještě nedokončil, takže nebude spuštěna.", "Vývoj", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
