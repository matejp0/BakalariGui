package cz.matejprerovsky.bakalarigui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Main implements ActionListener {
static Window loginWindow, infoWindow;
static Bakal bakal;
String url, username, password;
private boolean loginSuccess=false;
    public static void main(String[] args) {
        getLogin();
    }

    public static void getLogin() {
        loginWindow = new Window("Login");
        loginWindow.login();
        loginWindow.pack();
        loginWindow.setLocationRelativeTo(null);
        if(Bakal.connected())
            loginWindow.setVisible(true);
        else {
            loginWindow.throwMessage("Žádné internetové připojení!", "Error internetového připojení", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    public static void getInfo() throws IOException {
        infoWindow = new Window("Rozvrh");
        infoWindow.userInfo(bakal);
        infoWindow.pack();
        infoWindow.setLocationRelativeTo(null);
        infoWindow.setVisible(true);

        loginWindow.dispose(); //close login window
    }
    public void initBakal() throws IOException {
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
