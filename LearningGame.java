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

import javax.sound.sampled.Clip;

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
            
            // Music examples:
            
            /** DONT EVER USE ANY METHODS FROM THE "Clip" CLASS! **/
            
            String clipFileName = workingDir + "music\\test.wav";
            
            Clip clip = PlayMusic.createClip(clipFileName);
            /* Only play a clip *//*
            PlayMusic.play(clipFileName);
            // or
            PlayMusic.play(clip);
            
            /* Play a clip with adjusted volume *//*
            PlayMusic.setVolume(clip, 0.9f);
            PlayMusic.play(clipFileName);
            // or
            PlayMusic.setVolume(clip, 0.9f);
            PlayMusic.play(clip);
            
            /* Repeats a clip 5 times *//*
            PlayMusic.loop(clip, 5);
            
            /* Repeat a clip continuously *//*
            PlayMusic.loop(clip, -1);
            // or
            PlayMusic.loop(clip, Clip.LOOP_CONTINUOUSLY);
            
            /* Play a(nother) clip after another clip has stopped */
            PlayMusic.addAction(clip,
                                null, null, // open/close file
                                null, () -> PlayMusic.play(clip)); // start/stop clip
            PlayMusic.play(clip);
            /**/
                                
            
        });
    }
    
    /* 
     * Creates the GUI of the application.
     */
    private void createGUI() {
        this.setLayout(null);
        this.setLocation(500, 100);
        this.setSize(500, 500);
        
        try {
            Button2 sl = new Button2(100, 25, 10, true, "test");
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
    
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        boolean resized = width != getWidth() || height != getHeight();
        
        if (resized) {
            // Do stuff
        }
    }
    
    
    WindowListener wl = new WindowAdapter() {
        // Called when the user attempts to close the window
        @Override
        public void windowClosing(WindowEvent e) {
            // Do closing operation stuff.
            Log2.write(" === Closing application === ", Log2.INFO);
        }
    };
    
    ComponentListener cl = new ComponentAdapter() {
        // Called when the windows was resized.
        @Override
        public void componentResized(ComponentEvent e) {
            // Do stuff
            Log2.write("Window resized", Log2.INFO);
            repaint();
        }
    };
    
    public static void main(String[] args) {
        Log2.clear();
        new LearningGame();
    }
}