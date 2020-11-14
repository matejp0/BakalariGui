package cz.matejprerovsky.bakalarigui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Main implements ActionListener {

    private static Window loginWindow, infoWindow;
    private static Bakal bakal;

    final private static String notConnectedText = "Žádné internetové připojení!";
    final private static String wrongLoginText = "Vadné přihlašovací údaje.";
    final private static String errorText = "Error";

    public static void main(String[] args) {
        getLogin();
    }

    private static void getLogin() {
        loginWindow = new Window("Login");
        loginWindow.login();
    }

    private static void getInfo(){
        infoWindow = new Window(bakal.getUserInfo());
        infoWindow.userInfo(bakal);
        loginWindow.dispose(); //dispose the login window
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource()==loginWindow.getLoginBtn())
        {
            String url, username;
            char[] password;
            url = loginWindow.getUrlField().getText();
            username = loginWindow.getUsername().getText();
            password = loginWindow.getPassword().getPassword();
            bakal = new Bakal(url);

            //-----login-----------------------------------------------------
            if (bakal.login(username, String.valueOf(password), false)) {
                getInfo();
                loginWindow.throwMessage("Úspěšně přihlášeno", "Úspěch", JOptionPane.INFORMATION_MESSAGE);
                if(loginWindow.getSaveCheckBox().isSelected())
                    loginWindow.saveCsv(url, username);
            }
        }
    }

    public static void wrongLoginMessage(){ loginWindow.throwMessage(wrongLoginText, wrongLoginText, JOptionPane.WARNING_MESSAGE); }
    public static void wrongAddressOrNoInternetConnectionMessage(){
        loginWindow.throwMessage("Buď máte špatně URL adresu, nebo nejste připojeni k internetu", "Buď máte špatně URL adresu, nebo nejste připojeni k internetu", JOptionPane.ERROR_MESSAGE);
    }
    public static void errorMessage(){ loginWindow.throwMessage(errorText, errorText, JOptionPane.ERROR_MESSAGE); }
}