package cz.matejprerovsky.bakalarigui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Main implements ActionListener {

private static Window loginWindow, infoWindow, marksWindow;
private static Bakal bakal;
private boolean loginSuccess = false;

    public static void main(String[] args) {
        getLogin();
    }

    private static void getLogin() {
        loginWindow = new Window("Login");
        if(!Bakal.connected()) {
            loginWindow.throwMessage("Žádné internetové připojení!", "Error internetového připojení", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        loginWindow.login();
    }
    private static void getMarks() throws IOException {
        marksWindow = new Window("Známky");
        marksWindow.marks(bakal);
    }
    private static void getInfo() throws IOException {
        infoWindow = new Window("Rozvrh");
        infoWindow.userInfo(bakal);
        loginWindow.dispose(); //close the login window
    }
    private void initBakal(String url, String username, char[] password) throws IOException {
        bakal = new Bakal(url);
        loginSuccess=bakal.login(username, String.valueOf(password), false);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(loginWindow.getLoginBtn())) {
            String url, username;
            char[] password;
            url = loginWindow.getUrlField().getText();
            username = loginWindow.getUsername().getText();
            password = loginWindow.getPassword().getPassword();
            try { initBakal(url, username,password); } catch (IOException e) { e.printStackTrace(); }

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
            infoWindow.throwMessage("Implementaci této funkce do GUI jsem ještě nedokončil, takže ještě není hotová.", "Ve vývoj", JOptionPane.INFORMATION_MESSAGE);
            try { getMarks(); } catch (IOException e) { e.printStackTrace(); }
        }
    }
}
