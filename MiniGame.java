
package learningGame;


// Own packages
import learningGame.LearningGame;
import learningGame.Score;

import learningGame.font.FontLoader;

import learningGame.log.Log2;

import learningGame.tools.ImageTools;
import learningGame.tools.Key;
import learningGame.tools.KeyDetector;


// Java packages
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JLayeredPane;


abstract public class MiniGame extends JLayeredPane implements MouseMotionListener, MouseListener {
    // The working directory
    final protected String workingDir = LearningGame.workingDir;
    
    // The action that is executed after the minigame has ended.
    final private Runnable r;
    
    // The KeyDetector used to detect the key presses between updates.
    private KeyDetector kd;
    
    // Instance of the parent.
    final protected LearningGame lg;
    
    // The time stamp from when the start() method was invoked.
    protected long startTimeStamp;
    
    // The time to complete the miniGame
    final protected long timeOut;
    
    // The total time left to complete the MiniGame.
    protected Long timeLeft;
    
    // Denotes whether the application has started.
    private boolean started = false;
    // Deonotes whether the application has ended.
    private boolean stopped = false;
    
    // The font used to display the text.
    final private static Font textFont = FontLoader.getLocalFont("Cooper Black\\Cooper Black Regular.ttf");
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public MiniGame(LearningGame lg, Runnable r, long timeOut) {
        super();
        this.setLayout(null);
        this.lg = lg;
        this.r = r;
        this.timeOut = timeOut;
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * MouseMotionListener overrides.
     * These are supposed to be overridden by the child child class.
     * They are here for easy access.
     */
    @Override
    public void mouseDragged(MouseEvent e) { }
    @Override
    public void mouseMoved(MouseEvent e) { }
    
    /* 
     * MouseListener overrides.
     * These are supposed to be overridden by the child child class.
     * They are here for easy access.
     */
    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
    @Override
    public void mousePressed(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) { }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * @return whether the current MiniGame was started.
     */
    final public boolean isStarted() {
        return started;
    }
    
    /* 
     * @return whether the current MiniGame is running.
     */
    final public boolean isRunning() {
        return started && !stopped;
    }
    
    /* 
     * @return whether the current MiniGame was stopped.
     */
    final public boolean isStopped() {
        return stopped;
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Other functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Adds all nessesary listeners.
     */
    final private void addListeners() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }
    
    /* 
     * Starts the current minigame.
     */
    final public void start() {
        if (!started && !stopped) {
            createGUI();
            addListeners();
            resized(getWidth(), getHeight());
            startMiniGame();
            timeLeft = timeOut;
            startTimeStamp = System.currentTimeMillis();
            started = true;
        }
    }
    
    /* 
     * Updates the frame for the minigame.
     */
    final public void update() {
        if (started) {
            long time = System.currentTimeMillis();
            timeLeft = timeOut - (time - startTimeStamp);
            
            if (kd != null) {
                kd.update();
                update(kd.getKeysPressed(), time);
                
            } else {
                update(new Key[0], System.currentTimeMillis());
            }
        }
        
        repaint();
    }
    
    /* 
     * This method is called when the mini game is finished.
     * Calling this method prevents future calls to both the finish() and stop() method.
     */
    final public void finish() {
        if (!stopped) {
            stopped = true;
            cleanUp();
            if (r != null) r.run();
        }
    }
    
    /* 
     * This method is called when the MiniGame is interrupted in it's normal behaviour.
     * Calling this method prevents future calls to both the finish() and stop() method.
     * Also ignores the final action that is executed when the MiniGame ends from running.
     */
    final public void stop() {
        if (!stopped) {
            stopped = true;
            cleanUp();
        }
    } 
    /* 
     * This method is called when the MiniGame is resized/relocated.
     */
    @Override
    final public void setBounds(int x, int y, int width, int height) {
        boolean resized = (width != getWidth() && getWidth() != 0) ||
                           (height != getHeight() && getHeight() != 0);
        
        super.setBounds(x, y, width, height);
        
        if (resized) {
            resized(width, height);
        }
    }
    
    /* 
     * Uses the given KeyDetector as key input.
     */
    final public void useKeyDetector(KeyDetector kd) {
        this.kd = kd;
    }
    
    /* 
     * This method is called when the panel is repainted.
     */
    @Override
    final protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Convert graphics object
        Graphics2D g2d = (Graphics2D) g;
        
        // Retrieve the current g2d transformation.
        AffineTransform g2dTrans = g2d.getTransform();
        
        // Draw background
        drawBackground(g, getBackgroundImage());
        
        // Restore the g2d transformation.
        g2d.setTransform(g2dTrans);
        
        if (timeLeft != null) {
            String text;
            if (timeLeft > 0) {
                text = timeLeft.toString();
                
            } else {
                text = "Time's Up !";
            }
            // todo: center text
            // todo: add bar for text
            // todo: change size of the font
            g2d.setFont(textFont.deriveFont(50F));
            g2d.drawString(text, 300, 50);
        }
    }
    
    /* 
     * This method draws the background.
     * Override this method to paint something else then the background.
     */
    protected void drawBackground(Graphics g, BufferedImage background) {
        if (background != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.scale(getWidth()  / background.getWidth(),
                      getHeight() / background.getHeight());
            g.drawImage(background, 0, 0, null);
        }
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Abstract functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * This method is invoked to create the GUI of the application.
     */
    abstract protected void createGUI();
    
    /* 
     * This method is invoked when the minigame is started.
     */
    abstract protected void startMiniGame();
    
    /* 
     * The update method. Put all time based stuff in here.
     * 
     * @param keys the keys that were pressed since the previous update.
     * @param timeStamp the start of the update cycle.
     */
    abstract protected void update(Key[] keys, long timeStamp);
    
    /* 
     * This method is always called when the MiniGame is about to shut down.
     */
    abstract protected void cleanUp();
    
    /* 
     * This method returns the score.
     */
    abstract public Score getScore(); // todo: determine score object
    
    /* 
     * This method is called when the MiniGame is resized.
     * 
     * @param width the new width of the MiniGame.
     * @param height the new height of the MiniGame.
     */
    abstract protected void resized(int width, int height);
    
    /*
     * @return the background image.
     */
    abstract protected BufferedImage getBackgroundImage();
    
    // tmp
    public static void main(String[] args) {
        LearningGame lg = new LearningGame();
    }
    
}