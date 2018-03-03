
package learningGame;


// Own packages
import learningGame.LearningGame;
import learningGame.Score;
import learningGame.log.Log2;
import learningGame.tools.ImageTools;
import learningGame.tools.Key;
import learningGame.tools.KeyDetector;


// Java packages
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JLayeredPane;


abstract public class MiniGame extends JLayeredPane implements MouseMotionListener, MouseListener {
    // The action that is executed after the minigame has ended.
    final private Runnable r;
    
    // The KeyDetector used to detect the key presses between updates.
    final private KeyDetector kd = new KeyDetector();
    
    // Instance of the parent.
    final protected LearningGame lg;
    
    // The working directory
    final protected String workingDir = LearningGame.workingDir;
    
    // Denotes whether the application has started.
    private boolean started = false;
    // Deonotes whether the application has ended.
    private boolean stopped = false;
    
    // The background image that is shown.
    protected BufferedImage background;
    protected BufferedImage backgroundResized;
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public MiniGame(LearningGame lg, Runnable r) {
        super();
        this.setLayout(null);
        this.lg = lg;
        this.r = r;
    }
    
    /* 
     * Adds all nessesary listeners.
     */
    final private void addListeners() {
        this.addKeyListener(kd);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }
    
    /* 
     * Updates the frame for the minigame.
     */
    final public void update() {
        if (started) {
            kd.update();
            update(kd.getKeysPressed(), System.currentTimeMillis());
        }
    }
    
    /* 
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
     * Starts the current minigame.
     */
    final public void start() {
        if (!started && !stopped) {
            createGUI();
            addListeners();
            resized(getWidth(), getHeight());
            recreateBackground();
            requestFocus();
            started = true;
        }
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
            recreateBackground();
        }
    }
    
    /* 
     * Recreates the background image.
     */
    protected void recreateBackground() {
        if (background != null) {
            // Copy and resize the background image.
            backgroundResized = ImageTools.toBufferedImage
                (ImageTools.imageDeepCopy
                     (background)
                     .getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST));
        }
    }
    
    /* 
     * This method draws the background.
     * Override this method to paint something else then the background.
     */
    protected void drawBackground(Graphics g) {
        if (backgroundResized != null) g.drawImage(backgroundResized, 0, 0, null);
    }
    
    /* 
     * This method is called when the panel is repainted.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Abstract functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * This method is called to create the GUI of the application.
     */
    abstract protected void createGUI();
    
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
     * This method returns the score
     * 
     * Note: perhaps let it return a Score object.
     */
    abstract public Score getScore(); // todo: determine score object
    
    /* 
     * This method is called when the MiniGame is resized.
     * 
     * @param width the new width of the MiniGame.
     * @param height the new height of the MiniGame.
     */
    abstract public void resized(int width, int height);
    
    
    // tmp
    public static void main(String[] args) {
        Log2.clear();
        LearningGame lg = new LearningGame();
    }
    
}