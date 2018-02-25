package learningGame;


// Own packages
import learningGame.log.Log2;

import learningGame.music.PlayMusic;

import learningGame.tools.Button2;
import learningGame.tools.Key;
import learningGame.tools.MultiTool;
import learningGame.tools.TerminalErrorMessage;
import learningGame.tools.TimerTool;


// Java packages
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.File;
import java.io.IOException;

//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;

import javax.sound.sampled.Clip;

import javax.swing.AbstractAction;
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
    
    // The frames per second.
    final public static int FPS = 30;
    
    // Return size and location after returning from full screen.
    private int oldWindowWidth;
    private int oldWindowHeight;
    private int oldWindowLocX;
    private int oldWindowLocY;
    
    // Whether the current frame is in full screen.
    private boolean fullScreen = false;
    
    // The game timer
    private TimerTool timer;
    
    // The start screen
    private StartScreen startScreen;
    
    // Array containing all different MiniGame classes.
    final private Class<MiniGame>[] miniGames = listMiniGameClasses();
    
    // Array containing a random shuffle of all MiniGame classes.
    private Class<MiniGame>[] miniGameOrder;
    // The current MiniGame that is active according to {@code miniGameOrder}.
    private int curMiniGameNum = 0;
    
    // The current MiniGame object.
    MiniGame curMiniGame;
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public LearningGame() {
        super(appName);
        
        SwingUtilities.invokeLater(() -> {
            // Set application image
            this.setIconImage(new ImageIcon(appIconFile).getImage());
            
            createGUI();
            
            // Create and add the StartScreen
            startScreen = new StartScreen(() -> {
                remove(startScreen);
                startScreen = null;
                startMiniGames();
            });
            
            this.add(startScreen);
            
            addKeyBindings();
            addListeners();
            
            // Start timer
            timer = new TimerTool(() -> update(), 0L, 1000L/FPS);
            timer.start();
            
            
            // test
            //startMiniGames();
            
            
            //---------------------------------------------------------------------------------------------------------
            // Music examples:
            
            /** DONT EVER USE ANY METHODS FROM THE "Clip" CLASS! **/
            
            String clipFileName = workingDir + "music\\test.wav";
            Clip clip = PlayMusic.createClip(clipFileName);
            
            /* Only play a clip *//*
            PlayMusic.play(clipFileName);
            /* or *//*
            PlayMusic.play(clip);
            
            /* Play a clip with adjusted volume *//*
            PlayMusic.setVolume(clip, 0.5f);
            PlayMusic.play(clipFileName);
            /* or *//*
            PlayMusic.setVolume(clip, 0.5f);
            PlayMusic.play(clip);
            
            /* Repeats a clip 5 times *//*
            PlayMusic.loop(clip, 5);
            
            /* Repeat a clip continuously *//*
            PlayMusic.loop(clip, -1);
            /* or */
            PlayMusic.loop(clip, Clip.LOOP_CONTINUOUSLY);
            
            /* Play a(nother) clip after another clip has stopped */
            PlayMusic.addAction(clip,
                                null, null, // open/close file
                                null, () -> PlayMusic.play(clip)); // start/stop clip
            PlayMusic.play(clip);
            /**/
            //---------------------------------------------------------------------------------------------------------
            //tmp
            this.invalidate();
        });
    }
    
    /* 
     * This method is called for every frame change.
     * Should be only called by the timer.
     */
    private void update() {
        if (curMiniGame != null) {
            curMiniGame.update();
        }
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Adds the keybindings to the frame.
     */
    private void addKeyBindings() {
        // Add keybinding for full screen (F5 key)
        this.getRootPane().getInputMap().put(Key.F5.toKeyStroke(),
                                             "full screen");
        this.getRootPane().getActionMap().put("full screen", 
                                              new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFullScreen(!fullScreen);
            }
        });
    }
    
    /* 
     * Adds the listeners to the frame.
     */
    protected void addListeners() {
        this.addWindowListener(wl);
        this.addComponentListener(cl);
    }
    
    /* 
     * Creates the GUI of the application.
     */
    protected void createGUI() {
        this.setLayout(null);
        this.setLocation(500, 100);
        this.setSize(500, 500);
        /*
        try {
            Button2 tmpFullScreenButton = new Button2(100, 25, 10, true, "test full screen");
            this.add(tmpFullScreenButton);
            
            tmpFullScreenButton.setSize(200, 50);
            tmpFullScreenButton.setLocation(100, 100);
            tmpFullScreenButton.addActionListener((e) -> setFullScreen(!fullScreen));
            
            
            startButton = new Button2(100, 25, 10, true, "Start");
            this.add(startButton);
            
            startButton.setSize(200, 50);
            startButton.setLocation(350, 100);
            startButton.addActionListener((e) -> startMiniGames());
            
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        
        // Set default close operation and make the frame visible
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    
    /* 
     * Returns an array containing all classes in the folder "learningGame.miniGame".
     */
    @SuppressWarnings("unchecked") // For the cast from Object[] to Class[]
    final private Class<MiniGame>[] listMiniGameClasses() {
        // List all java classes in the package folder.
        File[] files = new File(workingDir + "miniGame\\")
            .listFiles((File dir, String name) -> {
            return name.endsWith(".java"); // Include only files that end with ".java".
        });
        
        // Create return object.
        Class<MiniGame>[] miniGames
            = (Class<MiniGame>[]) new Class[files.length];
        
        // Iterate over the files and put each class in the array
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            String className = fileName.substring(0, fileName.length() - 5); // ".java".length == 5
            
            try {
                miniGames[i] = (Class<MiniGame>) Class.forName("learningGame.miniGame." + className);
                     
            } catch (Exception e) {
                Log2.write(e);
            }
        }
        
        return miniGames;
    }
    
    /* 
     * Creates a MiniGame of a certain class, with a certain terminate action.
     * 
     * @param miniGame the class of the object that will be created.
     * @param r the action that will run when the MiniGame is finished.
     * @return a new instance of the given MiniGame class.
     */
    private MiniGame createMiniGame(Class<MiniGame> miniGame, Runnable r) {
        try {
            return miniGame.getConstructor(new Class<?>[] {this.getClass(), Runnable.class}).newInstance(this, r);
            
        } catch (Exception e) {
            Log2.write(e);
        }
        
        return null;
    }
    
    /* 
     * Begins a series of MiniGames.
     */
    @SuppressWarnings("unchecked") // For the cast from Object[] to Class[]
    private void startMiniGames() {
        miniGameOrder = (Class<MiniGame>[]) MultiTool.shuffleArray
            ((Class<MiniGame>[]) MultiTool.copyArray(miniGames));
        
        curMiniGameNum = -1; // To start MiniGame at [0]
        curMiniGame = null;  // For not fetching weird scores
        endMiniGame(); // Setup the MiniGame to be played and play it.
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
     * Sets the size and location of the frame.
     * {@code setSize(int, int)} and {@code setLocation(int, int)} both call this method.
     * Also updates the size of the children.
     * 
     * @param x the new x location coordinate relative to the upper left corner of the screen.
     * @param y the new y location coordinate relative to the upper left corner of the screen.
     * @param width the new width of the frame (incl. insets).
     * @param height the new height of the frame (incl. insets).
     */
    @Override
    public void setBounds(final int x, final int y, final int width, final int height) {
         boolean resized = width != getWidth() || height != getHeight();
         
        super.setBounds(x, y, width, height);
        if (resized) updateSizeChildren();
    }
    
    /* 
     * Updates the size of certain children.
     */
    public void updateSizeChildren() {
        Insets in = this.getInsets();
        
        if (curMiniGame != null) {
            curMiniGame.setSize(getWidth() - in.left - in.right,
                                getHeight() - in.top - in.bottom);
        }
        
        if (startScreen != null) {
            startScreen.setSize(getWidth() - in.left - in.right,
                                getHeight() - in.top - in.bottom);
        }
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Listeners and Runners
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
            updateSizeChildren();
            Log2.write("Window resized", Log2.INFO);
            
        }
    };
    
    
    /* 
     * This method is called at the and of a MiniGame.
     */
    private void endMiniGame() {
        if (curMiniGame != null) {
            Score score = curMiniGame.getScore();
            // Todo: do something with the score.
        }
        
        if (++curMiniGameNum < miniGameOrder.length) {
            // Remove old MiniGame from frame
            if (curMiniGame != null) remove(curMiniGame);
            
            // Create new MiniGame
            curMiniGame = createMiniGame(miniGameOrder[curMiniGameNum], () -> endMiniGame());
            // Add new MiniGame to frame
            add(curMiniGame);
            // Update the size of the MiniGame.
            updateSizeChildren();
            
            // Start the MiniGame
            curMiniGame.start();
            
        } else {
            System.out.println("DONE");
            // Todo: show result screen
        }
        
    }
    
    
    public static void main(String[] args) {
        Log2.clear();
        LearningGame lg = new LearningGame();
        
        while(true) {
            MultiTool.sleepThread(1000);
            lg.repaint();
        }
    }
}