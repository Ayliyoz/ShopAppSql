package SwingNecessities;

import javax.swing.*;

public class AppFrame extends JFrame {

    public AppFrame(AppPanel appPanel, int width, int height){
        appPanel.setSize(width, height);
        this.add(appPanel);
        appPanel.setVisible(true);
        appPanel.setFocusable(true);
    }
}
