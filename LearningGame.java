package learningGame;


// Own packages
import learningGame.log.Log2;

import learningGame.music.PlayMusic;

import learningGame.tools.TerminalErrorMessage;
import learningGame.tools.Slider;
import learningGame.tools.Button;
import learningGame.tools.Button2;


// Java packages
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class LearningGame extends JFrame {
    final public static String workingDir = System.getProperty("user.dir") + "\\learningGame\\";
    /* 
     * The String appName contains the name of our final application.
     */
    final public static String appName = "application name";
    
    public LearningGame() {
        super(appName);
        
        SwingUtilities.invokeLater(() -> {
            createGUI();
            PlayMusic.play(workingDir + "music\\test.wav");
        });
    }
    
    private void createGUI() {
        this.setLayout(null);
        this.setLocation(500, 100);
        this.setSize(500, 500);
        
        try {
            Button2 sl = new Button2(100, 25, 5, true, "test");
            this.add(sl);
            
            sl.setSize(200, 50);
            sl.setLocation(100, 100);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Add listeners
        this.addWindowListener(wl);
        this.addComponentListener(cl);
        
        // Set default close operation and make the frame visible
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    
    
    
    WindowListener wl = new WindowAdapter() {
        // Called when the user attempts to close the window
        @Override
        public void windowClosing(WindowEvent e) {
            // Add closing operation stuff.
            Log2.write(" === Closing application === ", Log2.INFO);
        }
    };
    
    ComponentListener cl = new ComponentAdapter() {
        // Called when the windows was resized.
        @Override
        public void componentResized(ComponentEvent e) {
            // Add stuff
            Log2.write("Window resized", Log2.INFO);
            repaint();
        }
    };
    
    public static void main(String[] args) {
        Log2.clear();
        new LearningGame();
    }
}