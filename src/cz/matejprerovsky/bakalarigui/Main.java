package cz.matejprerovsky.bakalarigui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.prefs.BackingStoreException;

public class Main implements ActionListener {
static Window loginWindow, infoWindow;
static Bakal bakal;
String url, username, password;
private boolean loginSuccess=false;
private boolean connected;
    public static void main(String[] args) {
        getLogin();
    }

    public static void getLogin() {
        loginWindow = new Window("Login");
        loginWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginWindow.login();
        loginWindow.pack();
        loginWindow.setLocationRelativeTo(null);
        loginWindow.setResizable(false);
        loginWindow.setVisible(true);
    }
    public static void getInfo() throws IOException, BackingStoreException {
        infoWindow = new Window("Rozvrh");
        infoWindow.userInfo(bakal);
        infoWindow.pack();
        infoWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        infoWindow.setLocationRelativeTo(null);
        infoWindow.setResizable(false);
        infoWindow.setVisible(true);

        loginWindow.dispose();
    }
    public void initBakal() throws IOException {
        bakal = new Bakal(url);
        connected = bakal.connected();
        if(connected)
            loginSuccess=bakal.login(username, password, false);


    }
    //fssdgsd
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(loginWindow.getLoginBtn())) {
            url = loginWindow.getUrlField().getText();
            username = loginWindow.getUsername().getText();
            password = String.valueOf(loginWindow.getPassword().getPassword());

            try { initBakal(); } catch (IOException e) { e.printStackTrace(); }

            if(loginSuccess) {
                try { getInfo(); } catch (IOException e) { e.printStackTrace(); } catch (BackingStoreException e) { e.printStackTrace(); }
                loginWindow.throwMessage("Úspěšně přihlášeno", "Úspěch", JOptionPane.INFORMATION_MESSAGE);
            }

            else if((!loginSuccess) && connected)
                loginWindow.throwMessage("Vadné přihlašovací údaje", "Error", JOptionPane.WARNING_MESSAGE);

            else if(!connected)
                loginWindow.throwMessage("Žádné internetové připojení!", "Error internetového připojení", JOptionPane.ERROR_MESSAGE);
        }
        else if(actionEvent.getSource().equals(infoWindow.getMarksBtn())){
            infoWindow.throwMessage("Implementaci této funkce do GUI jsem ještě nedokončil, takže nebude spuštěna.", "Vývoj", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
