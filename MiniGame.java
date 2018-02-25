
package learningGame;


// Own packages
import learningGame.Score;
import learningGame.tools.Key;
import learningGame.tools.KeyDetector;


// Java packages
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;


public abstract class MiniGame extends JPanel implements MouseMotionListener, MouseListener {
    // The action that is executed after the minigame has ended.
    final private Runnable r;
    final private KeyDetector kd = new KeyDetector();
    final protected LearningGame lg;
    
    private boolean started = false;
    private boolean stopped = false;
    
    public MiniGame(LearningGame lg, Runnable r) {
        super(null);
        this.lg = lg;
        this.r = r;
        createGUI();
        addListeners();
    }
    /* 
     * Adds all nessesary listeners.
     */
    final private void addListeners() {
        lg.addKeyListener(kd);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }
    
    /* 
     * Updates the frame for the minigame.
     */
    final public void update() {
        if (started) {
            kd.update();
            update(kd.getKeysPressed());
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
    
    /* 
     * Starts the current minigame.
     */
    public void start() {
        if (!started) {
            kd.clear();
            started = true;
            addListeners();
        }
    }
    
    /* 
     * This method is called when the mini game is finished.
     */
    final public void finish() {
        if (!stopped) {
            started = false;
            stopped = true;
            cleanUp();
            r.run();
        }
    }
    
    /* 
     * This method is called when the MiniGame is interrupted in it's normal behaviour.
     * Calling this method prevents the finish method from being called.
     */
    final public void stop() {
        if (!stopped) {
            started = false;
            stopped = true;
            cleanUp();
        }
    }
    
    
    /* 
     * This method is called to create the GUI of the application.
     * Only called in the constructor.
     */
    abstract protected void createGUI();
    
    /* 
     * The update method. Put all time based stuff in here.
     */
    abstract protected void update(Key[] keys);
    
    /* 
     * This method is always called when the MiniGame is about to shut down.
     */
    abstract protected void cleanUp();
    
    /* 
     * This method returns the score
     * Note: perhaps let it return a Score object.
     */
    abstract public Score getScore(); // todo: determine score object
}