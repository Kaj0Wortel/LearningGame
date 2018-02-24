package learningGame;


// Own packages
import learningGame.log.Log2;

import learningGame.music.PlayMusic;

import learningGame.tools.TerminalErrorMessage;
import learningGame.tools.Slider;
import learningGame.tools.Button;
import learningGame.tools.Button2;


// Java packages
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.IOException;

import javax.sound.sampled.Clip;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class LearningGame extends JFrame {
    // The root of where the files are located.
    final public static String workingDir = System.getProperty("user.dir") + "\\learningGame\\";
    
    // The name of the application.
    final public static String appName = "application name";
    
    // The image file name for the application.
    final public static String appIconFile = workingDir + "img\\icon.png";
    
    // Return size and location after returning from full screen.
    private int oldWindowWidth;
    private int oldWindowHeight;
    private int oldWindowLocX;
    private int oldWindowLocY;
    
    // Whether the current frame is in full screen.
    private boolean fullScreen = false;
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public LearningGame() {
        super(appName);
        
        SwingUtilities.invokeLater(() -> {
            // Set image
            /*
            mainFrame.setIconImage(new ImageIcon(this.getClass().getClassLoader()
                                                     .getResource("encryption/data_encryption.png")).getImage());
                                                     */
            this.setIconImage(new ImageIcon(appIconFile).getImage());
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
            PlayMusic.setVolume(clip, 0.5f);
            PlayMusic.play(clipFileName);
            // or
            PlayMusic.setVolume(clip, 0.5f);
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
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Creates the GUI of the application.
     */
    private void createGUI() {
        this.setLayout(null);
        this.setLocation(500, 100);
        this.setSize(500, 500);
        
        try {
            Button2 button = new Button2(100, 25, 10, true, "test full screen");
            this.add(button);
            
            button.setSize(200, 50);
            button.setLocation(100, 100);
            button.addActionListener((e) -> setFullScreen(!fullScreen));
            
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
    
    /* 
     * Sets the frame to full screen or restores it to it's previous window state.
     * 
     * @param fs determines whether the frame must be set in full screen or not.
     */
    public void setFullScreen(boolean fs) {
        fullScreen = fs;
        
        //setVisible(false);
        dispose();
        
        if (fs) {
            // Remove the bar above the mainFrame
            setUndecorated(true);
            
            // Update the old size and location
            oldWindowWidth = getWidth();
            oldWindowHeight = getHeight();
            oldWindowLocX = getX();
            oldWindowLocY = getY();
            
            // Set new size and location
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
            setLocation(0, 0);
            
        } else {
            // Add the bar above the mainFrame
            setUndecorated(false);
            
            // Set the new size and location to the old size and location of the mainFrame
            setSize(oldWindowWidth, oldWindowHeight);
            setLocation(oldWindowLocX, oldWindowLocY);
        }
        
        
        setVisible(true);
    }
    
    
    /* 
     * Sets the size and location of the JFrame.
     * setSize and setLocation both call this method.
     * Any resize events go via this method.
     * 
     * @param x the new x location coordinate relative to the upper left corner of the screen.
     * @param y the new y location coordinate relative to the upper left corner of the screen.
     * @param width the new width of the frame (incl. insets).
     * @param height the new height of the frame (incl. insets).
     */
    @Override
    public void setBounds(final int x, final int y, final int width, final int height) {
        boolean resized = width != getWidth() || height != getHeight();
        
        if (resized) {
            // Do stuff
        }
        
        super.setBounds(x, y, width, height);
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Listeners
     * ----------------------------------------------------------------------------------------------------------------
     */
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
            // Do resize stuff.
            Log2.write("Window resized", Log2.INFO);
        }
    };
    
    public static void main(String[] args) {
        Log2.clear();
        new LearningGame();
    }
}