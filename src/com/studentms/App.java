package com.studentms;

import com.studentms.ui.LoginFrame;
import com.studentms.util.Theme;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.applyLaf();
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}
