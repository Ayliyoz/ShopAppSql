package SwingNecessities;

import javax.swing.*;

public class App implements Runnable {

    private AppPanel appPanel;
    private AppFrame appFrame;
    private final int FPS = 120;
    private final int width = 1200;
    private final int height = 800;
    private Thread mainAppThread;
    public App(){
        appPanel = new AppPanel(width, height);
        appFrame = new AppFrame(appPanel, width, height);

        appFrame.setSize(width,height);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setVisible(true);
        appFrame.setResizable(false);
        appFrame.pack();
        appFrame.setLocationRelativeTo(null);

        appPanel.requestFocus();
        mainAppThread = new Thread(this);
        mainAppThread.start();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000/FPS;
        long lastFrame = System.nanoTime();
        while(true){
            if(System.nanoTime() - lastFrame >= timePerFrame){
                appPanel.repaint();
                lastFrame = System.nanoTime();
            }
        }
    }
}
