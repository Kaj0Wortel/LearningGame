
package learningGame;


// Own packages
import learningGame.log.Log2;

import learningGame.music.PlayMusic;

import learningGame.tools.Button2;
import learningGame.tools.Key;
import learningGame.tools.KeyDetector;
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

import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.Clip;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class LearningGame extends JFrame {
    /* 
     * 
     * TMP
     * 
     */
    String langQ = "Italian";
    String langA = "English";
    
    
    // The root of where the files are located.
    final public static String workingDir = System.getProperty("user.dir") + "\\learningGame\\";
    final public static String wordFile = workingDir + "data\\words.csv";
    final public static String miniGameDir = workingDir + "miniGame\\";
    final public static String imgSpriteDir = workingDir + "img\\sprites\\";
    final public static String imgWordDir = workingDir + "img\\word_images\\";
    final public static long TIME_OUT = 5000;
    static {
        Log2.clear();
    }
    
    // The name of the application.
    final public static String appName = "application name";
    
    // The image file name for the application.
    final public static String appIconFile = workingDir + "img\\icon.png";
    
    // The frames per second.
    final public static int FPS = 30;
    
    // The KeyDetector used to detect the key presses between updates.
    final private KeyDetector kd = new KeyDetector();
    
    // The random object use for this instance
    private static Random random = new Random();
    
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
    final private static Word[] words = MultiTool.listToArray
        (Word.createWordList(wordFile, miniGameDir, imgWordDir), Word.class);
    
    // Array containing the order in which the words are asked.
    private Word[] wordOrder;
    
    // The current Word that is active according to {@code miniGameOrder}.
    private int curWordNum = 0;
    
    // The Word an MiniGame that are currently active.
    Word curWord;
    MiniGameHandler curMiniGameHandler;
    
    // The score of the current session.
    private Score totalScore = new Score();
    
    
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
            timer = new TimerTool(0L, 1000L/FPS, () -> update());
            timer.start();
            
            
            //---------------------------------------------------------------------------------------------------------
            // Music examples:
            
            /** DONT EVER USE ANY METHODS FROM THE "Clip" CLASS! **/
            /*
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
            /* or *//*
            PlayMusic.loop(clip, Clip.LOOP_CONTINUOUSLY);
            
            /* Play a(nother) clip after another clip has stopped *//*
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
        if (curMiniGameHandler != null) {
            curMiniGameHandler.update();
        }
    }
    
    /* 
     * @return the miniGameHandler that is currently active.
     */
    protected MiniGameHandler getMiniGameHandler() {
        return curMiniGameHandler;
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
                                             "full_screen");
        this.getRootPane().getActionMap().put("full_screen", 
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
        this.getRootPane().addKeyListener(kd);
    }
    
    /* 
     * Creates the GUI of the application.
     */
    protected void createGUI() {
        this.setLayout(null);
        this.setLocation(500, 100);
        this.setSize(800, 800);
        
        // Set default close operation and make the frame visible
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    
    /* 
     * @return the words array.
     */
    public static Word[] getWords() {
        return words;
    }
    
    /* 
     * @param exclude list containing all the words that are not allowed to be returned.
     * @return a random word in the words list, but not occuring in exclude.
     *     Returns null iff {@code
     *         {\forall int i; words.has(i); 
     *             {\exists int j; exclude.has(j); words.get(i).equals(exclude.get(j))}
     *         }
     *     }
     */
    public static Word getRandomWord(ArrayList<Word> exclude) {
        if (exclude == null) return words[random.nextInt(words.length)];
        
        ArrayList<Word> allowedWords = new ArrayList<Word>();
        
        for (Word word : words) {
            if (!exclude.contains(word)) {
                allowedWords.add(word);
            }
        }
        
        if (allowedWords.size() == 0) return null;
        return allowedWords.get(random.nextInt(allowedWords.size()));
    }
    
    /* 
     * Begins a series of MiniGames.
     */
    @SuppressWarnings("unchecked") // For the cast from Object[] to Class[]
    private void startMiniGames() {
        wordOrder = (Word[]) MultiTool.shuffleArray
            ((Word[]) 
                 MultiTool.copyArray(words));
        /*
        miniGameOrder = (Class<MiniGame>[]) MultiTool.shuffleArray
            ((Class<MiniGame>[]) MultiTool.copyArray(miniGames));*/
        
        curWordNum = -1; // To start MiniGame at [0]
        curWord = null;  // For not fetching weird scores
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
         boolean resized = (width != getWidth() || height != getHeight());
         
        super.setBounds(x, y, width, height);
        if (resized) updateSizeChildren();
    }
    
    /* 
     * Updates the size of certain children.
     */
    public void updateSizeChildren() {
        Insets in = this.getInsets();
        
        if (curMiniGameHandler != null) {
            curMiniGameHandler.setSize(getWidth() - in.left - in.right,
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
            
        }
    };
    
    /* 
     * This method is called at the and of a MiniGame.
     */
    private void endMiniGame() {
        if (curMiniGameHandler != null) {
            totalScore.add(curMiniGameHandler.getScore());
            curMiniGameHandler = null;
        }
        
        // Select a new word from the list.
        Word word = null;
        while (word == null && ++curWordNum < wordOrder.length) {
            word = wordOrder[curWordNum];
            
            if (!word.hasMiniGame()) {
                Log2.write("The defined word \"" + word.toString() + " has no MiniGame!", Log2.WARNING);
                word = null;
            }
        }
        
        if (word != null) {
            // Create a new MiniGameHandler
            curMiniGameHandler = new MiniGameHandler(this, word, langQ, langA, () -> endMiniGame(), TIME_OUT);
            
            // Add keydetector
            curMiniGameHandler.useKeyDetector(kd);
            
            // Update the size of the MiniGame.
            updateSizeChildren();
            
            // Start
            Log2.write("Started MiniGameHandler of word: " + word.toString(), Log2.INFO);
            curMiniGameHandler.begin();
            
        } else {
            Log2.write("Finished word list!");
            System.out.println("Finished word list!");
            ScoreScreen ss = new ScoreScreen(totalScore);
            // Todo: show final result screen
        }
        
    }
    
    
    public static void main(String[] args) {
        LearningGame lg = new LearningGame();
    }
    
}